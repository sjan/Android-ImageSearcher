package org.lastresponders.uberassignment.service;

import android.util.Log;

import org.lastresponders.uberassignment.data.MessageFactory;
import org.lastresponders.uberassignment.data.access.GoogleImageSearchAccess;
import org.lastresponders.uberassignment.data.access.GoogleImageSearchClient;
import org.lastresponders.uberassignment.data.model.ImageSearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sjan on 1/15/2015.
 */
public class SearchService implements ISearchService{
    private GoogleImageSearchAccess searchAccess;
    private GoogleImageSearchClient client;
    private MessageFactory messageFactory;
    private Map<String,List<ImageSearchResult>> searchResultCache;

    private static SearchService singleton;
    public static int DEFAULTLOAD = 8;

    private SearchService() {
        searchAccess = new GoogleImageSearchAccess();
        messageFactory = new MessageFactory();
        searchAccess.setMessageFactory(messageFactory);
        client = new GoogleImageSearchClient();
        searchAccess.setClient(client);
        searchResultCache = new ConcurrentHashMap<String,List<ImageSearchResult>>();
    }

    public static synchronized  SearchService getInstance() {
        if(singleton == null ) {
           singleton = new SearchService();
        }
        return singleton;
    }

    @Override
    public List<ImageSearchResult> imageSearch(String searchTerm) {
        return this.imageSearch(searchTerm, DEFAULTLOAD, 0);
    }

    @Override
    public List<ImageSearchResult> imageSearch(String searchTerm, Integer count) {
        return this.imageSearch(searchTerm, count, 0);
    }

    @Override
    public List<ImageSearchResult> imageSearch(String searchTerm, Integer count,Integer startIndex) {
        List<ImageSearchResult> ret = searchAccess.searchImage(searchTerm, count, startIndex);
        List<ImageSearchResult> list = searchResultCache.get(searchTerm);
        if( list  == null) {
            list = new ArrayList<ImageSearchResult>();
            searchResultCache.put(searchTerm,list);
        }
        list.addAll(ret);
        Log.d("SearchService", "imageSearch " + searchTerm + " " + searchResultCache.get(searchTerm).size()+  " elements " + searchResultCache.size());
        return ret;
    }

    @Override
    public List<ImageSearchResult> searchResults(String searchString) {
        Log.d("SearchService", "searchResult " + searchString + " " + searchResultCache.size());
        List<ImageSearchResult> list = searchResultCache.get(searchString);
        return list;
    }

    @Override
    public void clear() {
        searchResultCache.clear();
    }
}
