package org.lastresponders.uberassignment.asynctask;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.GridView;

import org.lastresponders.uberassignment.MainActivity;
import org.lastresponders.uberassignment.data.model.ImageSearchResult;
import org.lastresponders.uberassignment.service.ISearchService;
import org.lastresponders.uberassignment.service.SearchService;
import org.lastresponders.uberassignment.view.ImageAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sjan on 1/15/2015.
 */
public class SearchImageAsyncTask extends AsyncTask<String, Void,  List <ImageSearchResult>> {
    public static SearchImageAsyncTask task;

    public static Map <String, List<ImageSearchResult>> searchMap = new HashMap<String, List<ImageSearchResult>> ();

    public static Integer DEFAULT_SIZE = 32;

    private ISearchService searchService = SearchService.getInstance();
    private GridView gridView;
    private String searchString;
    private AbsListView.OnScrollListener endlessScrollListener = null;

    public SearchImageAsyncTask (GridView gridView) {
        this.gridView = gridView;
    }

    public SearchImageAsyncTask(GridView gridView, AbsListView.OnScrollListener endlessScrollListener) {
        this.gridView = gridView;
        this.endlessScrollListener = endlessScrollListener;
    }

    @Override protected  List <ImageSearchResult> doInBackground(String ... params) {
        if (params == null || params.length<1) {
            Log.e("SearchImageAsyncTask", "null or empty search string");
            return null;
        } else {
            Log.d("SearchImageAsyncTask", "search for " + params[0]);
            searchString = params[0];
        }

        //get list to keep track of all search results
        List <ImageSearchResult>searchList = searchMap.get(searchString);
        if(searchList == null ) {
            searchList = new ArrayList<ImageSearchResult>();
            searchMap.put(searchString, searchList);
        }
        Integer startIndex = searchList.size();

        Log.d("SearchImageAsyncTask" , "search for " + searchString + " start at " + startIndex);
        List <ImageSearchResult>ret =  searchService.imageSearch(searchString, DEFAULT_SIZE, startIndex);

        if(startIndex == searchList.size()) {
           searchList.addAll(startIndex, ret);
        }

        return ret;
    }

    @Override protected void onPostExecute( List <ImageSearchResult> result) {
       Log.d("SearchImageAsyncTask" , "s:" + searchString + " backing size " +  ((ImageAdapter)gridView.getAdapter()).getBackingArray().getSize(searchString) );
        if(endlessScrollListener!=null) {
            Log.d("SearchImageAsyncTask" , "attaching scroll listner");
            gridView.setOnScrollListener(this.endlessScrollListener);
        }

        //if backing array is empty. kick off inital img download task
        if (((ImageAdapter) gridView.getAdapter()).getBackingArray().getSize(searchString) == 0) {
            //new AsyncImageLoadAsyncTask(gridView).execute(searchString);
        }
    }
}