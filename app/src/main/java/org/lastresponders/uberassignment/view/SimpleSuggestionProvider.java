package org.lastresponders.uberassignment.view;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by sjan on 1/17/2015.
 */
public class SimpleSuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "org.lastresponders.uberassignment.view.SimpleSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SimpleSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}