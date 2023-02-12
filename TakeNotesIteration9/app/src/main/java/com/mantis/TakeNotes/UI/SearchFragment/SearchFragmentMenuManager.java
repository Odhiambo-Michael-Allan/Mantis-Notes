package com.mantis.TakeNotes.UI.SearchFragment;


import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.mantis.TakeNotes.Adapters.NotesAdapter;
import com.mantis.TakeNotes.Adapters.RecentSearchesAdapter;
import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.Utils.DateProvider;
import com.mantis.TakeNotes.Utils.Logger;
import com.mantis.TakeNotes.data.source.local.Note;
import com.mantis.TakeNotes.data.source.local.Query;
import com.mantis.TakeNotes.databinding.FragmentSearchBinding;

import java.util.ArrayList;
import java.util.List;

public class SearchFragmentMenuManager {

    private Fragment owner;
    private NotesViewModel notesViewModel;
    private FragmentSearchBinding binding;
    private Query currentQuery;
    private NotesAdapter notesAdapter;
    private RecentSearchesAdapter recentSearchesAdapter = new RecentSearchesAdapter();
    private List<Query> searchHistory = new ArrayList<>();

    public SearchFragmentMenuManager( Fragment owner, NotesViewModel notesViewModel,
                                      FragmentSearchBinding binding ) {
        this.owner = owner;
        this.notesViewModel = notesViewModel;
        this.binding = binding;
        observeSearchHistory();
        observeSearchBar();
    }

    public void updateAdapter( NotesAdapter notesAdapter ) {
        this.notesAdapter = notesAdapter;
        attachNotesAdapterListener();
    }

    private void attachNotesAdapterListener() {
        notesAdapter.addListener( new NotesAdapter.NoteAdapterListener() {
            @Override
            public void onViewHolderClicked( View view, int viewHolderPosition) {
                Logger.log( "QUERY DESCRIPTION BEING ADDED: " + currentQuery.getDescription() );
                notesViewModel.addQuery( currentQuery );
            }

            @Override
            public void onViewHolderLongClicked( int viewHolderPosition ) {
                // Do Nothing..
            }

            @Override
            public void onRecyclerViewEmpty( boolean isEmpty ) {
                // Do Nothing..
            }
        } );
    }

    private void observeSearchHistory() {
        notesViewModel.getSearchHistory().observe( this.owner.getViewLifecycleOwner(),
                new Observer<List<Query>>() {
                    @Override
                    public void onChanged( List<Query> searchHistory ) {
                        Logger.log( "NEW SEARCH HISTORY SIZE: " + searchHistory.size() );
                        SearchFragmentMenuManager.this.searchHistory = searchHistory;
                        recentSearchesAdapter.setData( searchHistory );
                    }
                } );
    }

    private void observeSearchBar() {
        observeTextChange();
        observeFocusChange();
    }

    private void observeTextChange() {
        binding.searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit( String query ) {
                return true;
            }

            @Override
            public boolean onQueryTextChange( String newText ) {
                if ( newText.equals( "" ) && recentSearchesAdapter.hasItems() ) {
                    displayRecentSearchesView();
                    return true;
                }
                if ( newText.equals( "" ) ) {
                    displayNoRecentSearchesView();
                    return true;
                }
                // At this point, a query is available..
                currentQuery = new Query( newText, DateProvider.getCurrentDate() );
                displaySearchResultsMatching( newText );
                return true;
            }
        } );
    }

    private void displaySearchResultsMatching( String query ) {
        List<Note> searchResults = notesViewModel.searchNotesMatching( query );
        if ( searchResults.size() > 0 ) {
            binding.recyclerview.setAdapter( notesAdapter );
            notesAdapter.setData( searchResults );
            binding.recyclerview.setVisibility( View.VISIBLE );
        }
        else
            displayNoResultsFoundView();
    }

    private void observeFocusChange() {
        binding.searchView.setOnQueryTextFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange( View view, boolean isFocused ) {
                if ( !isFocused )
                    handleFocusLost();
            }
        } );
    }

    private void handleFocusLost() {
        if ( searchHistory.size() > 0 )
            displayRecentSearchesView();
        else
            displayNoRecentSearchesView();
    }

    private void displayRecentSearchesView() {
        binding.recyclerview.setAdapter( recentSearchesAdapter );
        recentSearchesAdapter.setData( searchHistory );
        binding.layoutEmpty.setVisibility( View.GONE );
        binding.clearSearchHistoryTextview.setVisibility( View.VISIBLE );
    }

    private void displayNoRecentSearchesView() {
        binding.layoutEmpty.setVisibility( View.VISIBLE );
        binding.recyclerview.setVisibility( View.GONE );
        binding.noSearchResultsFound.setVisibility( View.GONE );
    }

    private void displayNoResultsFoundView() {
        binding.noSearchResultsFound.setVisibility( View.VISIBLE );
        binding.recyclerview.setVisibility( View.GONE );
        binding.layoutEmpty.setVisibility( View.GONE );
    }
}
