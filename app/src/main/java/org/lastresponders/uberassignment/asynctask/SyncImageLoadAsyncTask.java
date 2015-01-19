package org.lastresponders.uberassignment.asynctask;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.lastresponders.uberassignment.MainActivity;
import org.lastresponders.uberassignment.data.model.ImageSearchResult;
import org.lastresponders.uberassignment.service.ISearchService;
import org.lastresponders.uberassignment.service.SearchService;
import org.lastresponders.uberassignment.view.ImageAdapter;

import java.util.List;
import java.util.ListIterator;

/**
 * Created by sjan on 1/16/2015.
 */
public class SyncImageLoadAsyncTask extends AsyncTask<String, Void, List <ImageSearchResult>> {
    private ISearchService searchService = SearchService.getInstance();
    private GridView imageTable;
    private String searchString;
    public static  Integer PAGESIZE = 15;

    public SyncImageLoadAsyncTask(GridView gridView) {
        this.imageTable = gridView;
    }

    @Override
    protected List<ImageSearchResult> doInBackground(String... params) {
        if (params == null || params.length<1) {
            Log.e("SyncImageLoadAsyncTask", "null or empty search string");
            return null;
        } else {
            Log.d("SyncImageLoadAsyncTask", "loading images for " + params[0]);
            searchString = params[0];
        }
        ImageLoader imageLoader = ImageLoader.getInstance();
        List<ImageSearchResult> list = searchService.searchResults(searchString);

        //LOAD URLS
        ListIterator<ImageSearchResult> iterator = list.listIterator();
        while(iterator.hasNext() && iterator.nextIndex()<PAGESIZE) {
            ImageSearchResult result = iterator.next();
            String url = result.getTbUrl();
            Bitmap imageBitmap= imageLoader.loadImageSync(url);
            ((ImageAdapter) imageTable.getAdapter()).getBackingArray().addImage(searchString, imageBitmap, url );
        }

        return list;
    }

    @Override
    protected void onPostExecute(List<ImageSearchResult> list) {
        Log.d("SyncImageLoadAsyncTask", "onPostExecute  " );
        ((ImageAdapter) imageTable.getAdapter()).notifyDataSetChanged();

        //add scroll listener here

    }
}