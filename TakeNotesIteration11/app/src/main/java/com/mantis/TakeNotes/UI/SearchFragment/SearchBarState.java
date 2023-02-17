package com.mantis.TakeNotes.UI.SearchFragment;

import com.mantis.TakeNotes.data.source.local.Note;
import com.mantis.TakeNotes.data.source.local.Query;

import java.util.List;

public interface SearchBarState {
    void searchResultsAvailable( List<Note> searchResults );
    void recentSearchesAvailable( List<Query> recentSearches );
    void searchBarFocusChange( boolean isFocused );
}
