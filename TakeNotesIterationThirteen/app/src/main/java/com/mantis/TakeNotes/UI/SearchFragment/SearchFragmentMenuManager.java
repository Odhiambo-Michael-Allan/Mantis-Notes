package com.mantis.TakeNotes.UI.SearchFragment;


import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mantis.TakeNotes.Adapters.NotesAdapter;
import com.mantis.TakeNotes.Adapters.RecentSearchesAdapter;
import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.UI.AddNoteFragment.AddNoteFragment;
import com.mantis.TakeNotes.UI.HomeFragment.HomeFragmentDirections;
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
    private NotesAdapter notesAdapter;
    private RecentSearchesAdapter recentSearchesAdapter = new RecentSearchesAdapter();
    private List<Query> searchHistory = new ArrayList<>();
    private RecyclerView.LayoutManager currentLayoutManager;
    private boolean resultsAreBeingDisplayed = false;

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
        currentLayoutManager = binding.recyclerview.getLayoutManager();
    }

    private void attachNotesAdapterListener() {
        notesAdapter.addListener( new NotesAdapter.NoteAdapterListener() {
            @Override
            public void onViewHolderClicked( View view, int viewHolderPosition) {
                Query currentQuery = new Query( binding.searchView.getQuery().toString(),
                        DateProvider.getCurrentDate() );
                Logger.log( "QUERY DESCRIPTION BEING ADDED: " + currentQuery.getDescription() );
                notesViewModel.addQuery( currentQuery );
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

    private void observeSearchHistory() {
        notesViewModel.getSearchHistory().observe( this.owner.getViewLifecycleOwner(),
                new Observer<List<Query>>() {
                    @Override
                    public void onChanged( List<Query> searchHistory ) {
                        Logger.log( "SEARCH HISTORY SIZE: " + searchHistory.size() );
                        SearchFragmentMenuManager.this.searchHistory = searchHistory;
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
                resultsAreBeingDisplayed = false;
                handleFocusGained( newText );
                return true;
            }
        } );
    }


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
        if ( searchHistory.size() > 0 ) {
            Logger.log( "SEARCH HISTORY SIZE: " + searchHistory.size() + " DISPLAYING RECENT SEARCHES VIEW" );
            displayRecentSearches();
        }
        else
            displayNoRecentSearchesView();
    }

    private void displayRecentSearches() {
        binding.recyclerview.setLayoutManager( new LinearLayoutManager( owner.getContext() ) );
        binding.notesRecyclerViewContainer.setVisibility( View.VISIBLE );
        binding.recyclerview.setAdapter( recentSearchesAdapter );
        binding.recyclerview.setLayoutManager( new LinearLayoutManager( owner.getContext() ) );
        recentSearchesAdapter.setData( searchHistory );
        binding.layoutEmpty.setVisibility( View.GONE );
        binding.noSearchResultsFound.setVisibility( View.GONE );
        binding.clearSearchHistoryTextview.setVisibility( View.VISIBLE );
    }

    private void displayNoRecentSearchesView() {
        binding.layoutEmpty.setVisibility( View.VISIBLE );
        binding.notesRecyclerViewContainer.setVisibility( View.GONE );
        binding.noSearchResultsFound.setVisibility( View.GONE );

    }

    private List<Note> getSearchResults( String queryString ) {
        return notesViewModel.searchNotesMatching( queryString );
    }


    private void displaySearchResultsMatching( String query ) {
        Logger.log( "Displaying search results matching: " + query );
        List<Note> searchResults = getSearchResults( query );
        if ( resultsAreBeingDisplayed )
            return;  // Results are already being displayed so do nothing..
        if ( searchResults.size() > 0 ) {
            binding.recyclerview.setLayoutManager( currentLayoutManager );
            binding.recyclerview.setAdapter( notesAdapter );
            notesAdapter.setData( searchResults );
            notifyAdapterToHighlightViewHoldersWithResultsMatching( query );
            binding.notesRecyclerViewContainer.setVisibility( View.VISIBLE );
            binding.clearSearchHistoryTextview.setVisibility( View.GONE );
            binding.noSearchResultsFound.setVisibility( View.GONE );
            resultsAreBeingDisplayed = true;
        }
    }

    private void notifyAdapterToHighlightViewHoldersWithResultsMatching( String query ) {
        notesAdapter.notifyViewHoldersToHighlightTextMatching( query );
    }

    private void displayNoResultsFoundView() {
        binding.noSearchResultsFound.setVisibility( View.VISIBLE );
        binding.layoutEmpty.setVisibility( View.GONE );
        binding.notesRecyclerViewContainer.setVisibility( View.GONE );
    }

}