package org.lastresponders.uberassignment.asynctask;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.lastresponders.uberassignment.MainActivity;
import org.lastresponders.uberassignment.data.model.ImageSearchResult;
import org.lastresponders.uberassignment.service.ISearchService;
import org.lastresponders.uberassignment.service.SearchService;
import org.lastresponders.uberassignment.view.ImageAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by sjan on 1/16/2015.
 */
public class AsyncImageLoadAsyncTask extends AsyncTask<String, Void, List <ImageSearchResult>> {
    public static AsyncImageLoadAsyncTask task;
    public static int TIMEOUT_SEC = 30;
    public static double loadFactor = .7;

    private ISearchService searchService = SearchService.getInstance();
    private GridView imageTable;
    private String searchString;
    public static  Integer PAGESIZE = 16;

    private  static  int pendingDownload=0;
    private static Date lastRefresh;



    public static synchronized int getPendingDownload() {
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(new Date());
        cal.add(Calendar.SECOND, -TIMEOUT_SEC);
        cal.getTime();

        if(lastRefresh!=null && pendingDownload > 0 && cal.getTime().after(lastRefresh)) {
               pendingDownload = 0;
        }

        return pendingDownload;
    }

    public static synchronized void decrmentPendingDownload() {
        lastRefresh = Calendar.getInstance().getTime();
        pendingDownload--;
    }

    public static synchronized void incrementPendingDownload() {
        pendingDownload++;
    }

    public AsyncImageLoadAsyncTask(GridView gridView) {
        this.imageTable = gridView;
        task =this;
    }

    public static Status getTaskState() {
        if (task == null) {
            return Status.FINISHED;
        }
        return task.getStatus();
    }

    @Override
    protected List<ImageSearchResult> doInBackground(String... params) {
        List <ImageSearchResult> loadList = new ArrayList<>();
        List<ImageSearchResult> totalList = null;
        if (params == null || params.length<1) {
            Log.e("AsyncImageLoadAsyncTask", "null or empty search string");
            return null;
        } else {
            Log.d("AsyncImageLoadAsyncTask", "loading images for " + params[0]);
            searchString = params[0];
            totalList = searchService.searchResults(searchString);
        }

         if(totalList == null) {
            Log.e("AsyncImageLoadAsyncTask", "searchresults empty");
            return null;
        }

        ListIterator <ImageSearchResult>  iterator = totalList.listIterator();
        while(iterator.hasNext() && loadList.size()<PAGESIZE) {
            ImageSearchResult imageSearch = iterator.next();
            if(!((ImageAdapter) imageTable.getAdapter()).getBackingArray().containsUrl(imageSearch.getTbUrl())) { //omit url's i've already on screen
                loadList.add(imageSearch);
            }
        }


        int backingarraysize = ((ImageAdapter) imageTable.getAdapter()).getBackingArray().getSize(searchString);

        //check if need to load more search results
        if( ((int) (loadFactor*totalList.size())) <  (backingarraysize + loadList.size() ) ) {
            Log.d("AsyncImageLoadAsyncTask","backingArray:" + backingarraysize + " load more " +   (backingarraysize + loadList.size() ) +"/"+ ((int) (totalList.size()) + " exceeds lf:" + loadFactor) );
            new SearchImageAsyncTask(imageTable).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, searchString);
        }

        return loadList;
    }

    @Override
    protected void onPostExecute(List<ImageSearchResult> loadList) {
        //LOAD URLS
       ImageLoader imageLoader = ImageLoader.getInstance();

        if(loadList == null) {
            return;
        }

       ListIterator<ImageSearchResult> iterator = loadList.listIterator();
        while(iterator.hasNext()) {
            ImageSearchResult result = iterator.next();
            String url = result.getTbUrl();
            imageLoader.loadImage(url, new SimpleImageLoadingListener(searchString) );
        }
    }

    public class SimpleImageLoadingListener implements ImageLoadingListener {
        private String searchString;

        public SimpleImageLoadingListener(String s) {
            this.searchString = s;
        }

        @Override
        public void onLoadingStarted(String s, View view) {
            AsyncImageLoadAsyncTask.incrementPendingDownload();
            Log.d("AsyncImageLoadAsyncTask","onLoadingStarted "  + s + " " + pendingDownload);

        }

        @Override
        public void onLoadingFailed(String s, View view, FailReason failReason) {
            AsyncImageLoadAsyncTask.decrmentPendingDownload();
            Log.d("AsyncImageLoadAsyncTask","onLoadingFailed "  + s + " " + pendingDownload);
        }

        @Override
        public void onLoadingComplete(String url, View view, Bitmap bitmap) {
            AsyncImageLoadAsyncTask.decrmentPendingDownload();
            ((ImageAdapter) imageTable.getAdapter()).getBackingArray().addImage(searchString, bitmap, url );
            ((ImageAdapter) imageTable.getAdapter()).notifyDataSetChanged();
            Log.d("AsyncImageLoadAsyncTask","onLoadingComplete "  + url + " " + pendingDownload);

        }

        @Override
        public void onLoadingCancelled(String s, View view) {
            AsyncImageLoadAsyncTask.decrmentPendingDownload();
            Log.d("AsyncImageLoadAsyncTask","onLoadingCancelled "  + s + " " + pendingDownload);
        }
    }

}