package org.lastresponders.uberassignment.service;

import org.lastresponders.uberassignment.data.model.ImageSearchResult;


import java.util.List;

/**
 * Created by sjan on 1/15/2015.
 */
public interface  ISearchService {

    public List<ImageSearchResult> imageSearch(String searchTerm);
    public List<ImageSearchResult> imageSearch(String searchTerm, Integer count);
    public List<ImageSearchResult> imageSearch(String searchTerm, Integer count, Integer startIndex );
    public List<ImageSearchResult> searchResults(String searchString);

    public void clear();
}
