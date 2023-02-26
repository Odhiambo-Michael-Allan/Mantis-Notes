package com.mantis.takenotes.UI.SearchFragment;


import android.view.View;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;
import com.mantis.takenotes.Adapters.NotesAdapter;
import com.mantis.takenotes.Adapters.RecentSearchesAdapter;

import com.mantis.takenotes.Models.NotesViewModel;
import com.mantis.takenotes.UI.AddNoteFragment.AddNoteFragment;
import com.mantis.takenotes.Utils.DateProvider;

import com.mantis.takenotes.Utils.Logger;
import com.mantis.takenotes.Utils.ToastProvider;
import com.mantis.takenotes.data.source.local.Note;
import com.mantis.takenotes.data.source.local.Query;

import com.mantis.takenotes.databinding.TakeNotesFragmentSearchBinding;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchFragmentMenuManager {

    private Fragment owner;
    private NotesViewModel notesViewModel;
    private TakeNotesFragmentSearchBinding binding;
    private NotesAdapter notesAdapter;
    private RecentSearchesAdapter recentSearchesAdapter;
    private List<Query> searchHistory = new ArrayList<>();
    private RecyclerView.LayoutManager currentLayoutManager;
    private boolean resultsAreBeingDisplayed = false;

    public SearchFragmentMenuManager( Fragment owner, NotesViewModel notesViewModel,
                                      TakeNotesFragmentSearchBinding binding ) {
        this.owner = owner;
        this.notesViewModel = notesViewModel;
        this.binding = binding;
        observeSearchBar();
        setupRecentSearchesAdapter();
        attachListenerToClearSearchHistoryButton();
    }

    public void updateAdapter( NotesAdapter notesAdapter ) {
        Logger.log( "Updating Search Fragment Menu Manager Adapter" );
        this.notesAdapter = notesAdapter;
        attachNotesAdapterListener();
        currentLayoutManager = binding.recyclerview.getLayoutManager();
    }

    private void attachNotesAdapterListener() {
        notesAdapter.addListener( new NotesAdapter.NoteAdapterListener() {
            @Override
            public void onViewHolderClicked( View view, int viewHolderPosition) {
                Query currentQuery = new Query( binding.searchView.getQuery().toString(),
                        new Date() );
                if ( !recentSearchesAdapter.contains( currentQuery ) )
                    currentQuery.add( notesViewModel.getNoteRepository() );
                navigateToAddNoteFragment( view, viewHolderPosition );
            }

            @Override
            public void onViewHolderLongClicked( int viewHolderPosition ) {
                // Do Nothing..
            }

            @Override
            public void onRecyclerViewEmpty( boolean isEmpty ) {
                // Do Nothing..
            }

            @Override
            public void onNoteNotesSizeChange(int newNotesSize) {
                // Do Nothing..
            }
        } );
    }

    private void navigateToAddNoteFragment( View view, int viewHolderPosition ) {
        SearchFragmentDirections.ActionNavSearchToNavAddNote action =
                SearchFragmentDirections.actionNavSearchToNavAddNote(
                        notesAdapter.getData().get( viewHolderPosition ).getId(),
                        AddNoteFragment.SEARCH_FRAGMENT );
        Navigation.findNavController( view ).navigate( action );
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
                displaySearchResultsMatching( newText );
                return true;
            }
        } );
    }

    private void displaySearchResultsMatching( String query ) {
        List<Note> searchResults = getSearchResults( query );
        if ( searchResults.size() > 0 )
            displaySearchResults( searchResults, query );
        else if ( !query.equals( "" ) )
            displayNoResultsFoundView();
        else
            displayRecentSearchesView();
    }

    private List<Note> getSearchResults( String queryString ) {
        return notesViewModel.searchNotesMatching( queryString );
    }

    private void displaySearchResults( List<Note> searchResults, String query ) {
        binding.recyclerview.setLayoutManager( currentLayoutManager );
        binding.recyclerview.setAdapter( notesAdapter );
        notesAdapter.setData( searchResults );
        notifyAdapterToHighlightViewHoldersWithResultsMatching( query );
        binding.recyclerview.setVisibility( View.VISIBLE );
        binding.clearSearchHistoryButton.setVisibility( View.GONE );
        binding.noSearchResultsFound.setVisibility( View.GONE );
    }

    private void attachListenerToClearSearchHistoryButton() {
        binding.clearSearchHistoryButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                ToastProvider.showToast( owner.getContext(), "Not yet implemented" );
            }
        } );
    }
    // ===============================================================================================================


    // ----------------------- SearchBar Status Behavior -----------------------

    private void observeFocusChange() {
        binding.searchView.setOnQueryTextFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange( View view, boolean isFocused ) {
                if ( !isFocused )
                    return;
                handleFocusGained( binding.searchView.getQuery().toString() );
            }
        } );
    }

    private void handleFocusGained( String textInSearchBar ) {
        if ( textInSearchBar.equals( "" ) )
            displayRecentSearchesView();
        else if ( getSearchResults( textInSearchBar ).size() <= 0 )
            displayNoResultsFoundView();
        else
            displaySearchResultsMatching( textInSearchBar );
    }

    private void displayRecentSearchesView() {
        if ( searchHistory.size() > 0 )
            displayRecentSearches();
        else
            displayNoRecentSearchesView();
    }

    private void displayRecentSearches() {
        binding.recyclerview.setLayoutManager( new LinearLayoutManager( owner.getContext() ) );
        binding.recyclerview.setAdapter( recentSearchesAdapter );
        recentSearchesAdapter.setData( searchHistory );
        binding.recyclerview.setVisibility( View.VISIBLE );
        binding.layoutEmpty.setVisibility( View.GONE );
        binding.noSearchResultsFound.setVisibility( View.GONE );
        binding.clearSearchHistoryButton.setVisibility( View.VISIBLE );
    }

    private void displayNoRecentSearchesView() {
        binding.layoutEmpty.setVisibility( View.VISIBLE );
        binding.recyclerview.setVisibility( View.GONE );
        binding.noSearchResultsFound.setVisibility( View.GONE );
        binding.clearSearchHistoryButton.setVisibility( View.GONE );
    }

    private void notifyAdapterToHighlightViewHoldersWithResultsMatching( String query ) {
        notesAdapter.notifyViewHoldersToHighlightTextMatching( query );
    }

    private void displayNoResultsFoundView() {
        binding.noSearchResultsFound.setVisibility( View.VISIBLE );
        binding.layoutEmpty.setVisibility( View.GONE );
        binding.recyclerview.setVisibility( View.GONE );
    }

    private void setupRecentSearchesAdapter() {
        recentSearchesAdapter = new RecentSearchesAdapter( this.owner, this.notesViewModel );
        recentSearchesAdapter.addListener( new RecentSearchesAdapter.RecentSearchesAdapterListener() {
            @Override
            public void onRecentQueryClick( Query query ) {
                displaySearchResultsMatching( query.getDescription() );
            }

            @Override
            public void onSearchHistoryChange( List<Query> searchHistory ) {
                Logger.log( "SEARCH HISTORY SIZE: " + searchHistory.size() );
                SearchFragmentMenuManager.this.searchHistory = searchHistory;
                binding.searchView.requestFocus();
            }
        } );
    }

}