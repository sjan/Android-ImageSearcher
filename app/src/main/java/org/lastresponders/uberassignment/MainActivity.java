package org.lastresponders.uberassignment;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.lastresponders.uberassignment.asynctask.AsyncImageLoadAsyncTask;
import org.lastresponders.uberassignment.asynctask.SearchImageAsyncTask;
import org.lastresponders.uberassignment.data.AdapterBackingImageArray;
import org.lastresponders.uberassignment.service.SearchService;
import org.lastresponders.uberassignment.view.ImageAdapter;
import org.lastresponders.uberassignment.view.SimpleSuggestionProvider;
import org.lastresponders.uberassignment.view.SquareImageView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ActionBarActivity {
    private GridView gridView;
    private SearchView searchView;
    private ImageAdapter imageAdapter;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            //switching context,
            String searchString = intent.getStringExtra(SearchManager.QUERY);

            //search for term
            if (searchString != null) {
                imageAdapter.setSearchContext(searchString); //swap backing array
                imageAdapter.notifyDataSetChanged(); //update ui
                Integer size = imageAdapter.getBackingArray().getSize(searchString);

                if(size == 0) { //new search
                    if(imageAdapter.getBackingArray().getTotalUrlCount() > ImageAdapter.OBJECT_THRESHOLD) {
                        Log.d("MainActivity","clearing cache at " +imageAdapter.getBackingArray().getTotalUrlCount() );
                        imageAdapter.getBackingArray().clear();
                        SearchService.getInstance().clear();
                    }

                    gridView.setOnScrollListener(null);
                    SearchImageAsyncTask task = new SearchImageAsyncTask(gridView, new EndlessScrollListener( searchString));
                    task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, searchString);
                }

                //attach new scroll listener for new search
                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                        SimpleSuggestionProvider.AUTHORITY, SimpleSuggestionProvider.MODE);
                suggestions.saveRecentQuery(searchString, null);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_image);

        //TODO:move this
        initImageLoader(getApplicationContext());

        //initialize searchview
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView = (SearchView) findViewById(R.id.searchViewBox);
        searchView.setSearchableInfo(searchableInfo);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);

        //initialize gridView
        gridView = (GridView) findViewById(R.id.gridView);
        AdapterBackingImageArray array = new AdapterBackingImageArray();
        imageAdapter = new ImageAdapter(this, array);
        gridView.setAdapter(imageAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //initialize image loader
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
    }


    public class EndlessScrollListener implements AbsListView.OnScrollListener {
        private int visibleThreshold = 3;
        private boolean loading = false;
        private  String searchString;

        public EndlessScrollListener(String s) {
            this.searchString = s;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            Log.d("EndlessScrollListener:onScroll", loading + " " + firstVisibleItem + " " + visibleItemCount + " " + totalItemCount + " " +AsyncImageLoadAsyncTask.getPendingDownload() + " " + AsyncImageLoadAsyncTask.getTaskState());

            //check to see if i'm still loading images
            if (loading && AsyncImageLoadAsyncTask.getPendingDownload() <= 0) {
                loading = false;
            }

            //check to see if i need to load
            if (!loading &&
                    AsyncImageLoadAsyncTask.getPendingDownload() <= 0 && //wait for pending downloads to resolved
                    ((firstVisibleItem + visibleItemCount + visibleThreshold) >= totalItemCount) && //wait until reach bottom of the zcreen
                     AsyncImageLoadAsyncTask.getTaskState() == AsyncTask.Status.FINISHED) { //wait until Existing AsynTask is complete
                loading = true;
                Log.d("EndlessScrollListener","Spawn AsyncImageLoadAsyncTask s:" + searchString);
                new AsyncImageLoadAsyncTask(gridView).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, searchString);
             }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }
    }
}
