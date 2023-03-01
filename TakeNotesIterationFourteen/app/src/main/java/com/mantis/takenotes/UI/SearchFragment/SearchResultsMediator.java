package com.mantis.takenotes.UI.SearchFragment;

import android.text.Layout;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mantis.takenotes.Adapters.NotesAdapter;
import com.mantis.takenotes.Adapters.RecentSearchesAdapter;
import com.mantis.takenotes.Models.NotesViewModel;
import com.mantis.takenotes.Utils.Logger;
import com.mantis.takenotes.data.source.local.Note;
import com.mantis.takenotes.data.source.local.Query;
import com.mantis.takenotes.databinding.TakeNotesFragmentSearchBinding;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsMediator {

    private Fragment owner;
    private TakeNotesFragmentSearchBinding binding;
    private NotesViewModel notesViewModel;
    private NotesAdapter notesAdapter;
    private List<Query> searchHistory = new ArrayList<>();
    private RecentSearchesAdapter recentSearchesAdapter;
    private RecyclerView.LayoutManager currentLayoutManager;

    public SearchResultsMediator( Fragment owner,
                                  TakeNotesFragmentSearchBinding binding,
                                  NotesViewModel notesViewModel,
                                  NotesAdapter notesAdapter,
                                  RecentSearchesAdapter recentSearchesAdapter ) {
        this.owner = owner;
        this.binding = binding;
        this.notesViewModel = notesViewModel;
        this.notesAdapter = notesAdapter;
        this.recentSearchesAdapter = recentSearchesAdapter;
        attachListenerToSearchView();
        observeSearchHistory();
    }

    public void setCurrentLayoutManager( RecyclerView.LayoutManager currentLayoutManager ) {
        this.currentLayoutManager = currentLayoutManager;
    }

    private void observeSearchHistory() {
        notesViewModel.getSearchHistory().observe( owner.getViewLifecycleOwner(),
                new Observer<List<Query>>() {
                    @Override
                    public void onChanged( List<Query> queries ) {
                        searchHistory = queries;
                        displaySearchHistory();
                    }
                } );
    }

    private void attachListenerToSearchView() {
        binding.searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit( String query ) {
                return false;
            }

            @Override
            public boolean onQueryTextChange( String newText ) {
                if ( !newText.equals( "" ) )
                    displayResultsMatching( newText );
                else
                    displaySearchHistory();
                return true;
            }
        } );
    }

    public void displayResultsMatching( String query ) {
        List<Note> searchResultsList = notesViewModel.getNotesMatching( query );
        binding.recyclerview.setLayoutManager( currentLayoutManager );
        if ( searchResultsList.size() < 1 )
            displayNoResultsFound();
        else
            displayNotesFound( searchResultsList, query );
    }

    private void displayNoResultsFound() {
        binding.recyclerview.setVisibility( View.GONE );
        binding.layoutEmpty.setVisibility( View.GONE );
        binding.clearSearchHistoryButton.setVisibility( View.GONE );
        binding.noSearchResultsFound.setVisibility( View.VISIBLE );
    }

    private void displayNotesFound( List<Note> notesFound, String query ) {
        binding.layoutEmpty.setVisibility( View.GONE );
        binding.clearSearchHistoryButton.setVisibility( View.GONE );
        binding.noSearchResultsFound.setVisibility( View.GONE );

        binding.recyclerview.setAdapter( notesAdapter );
        notesAdapter.setData( notesFound );
        notesAdapter.notifyViewHoldersToHighlightTextMatching( query );
        binding.recyclerview.setVisibility( View.VISIBLE );
    }

    public void updateAdapter( NotesAdapter notesAdapter ) {
        this.notesAdapter = notesAdapter;
    }

    private void displaySearchHistory() {
        if ( searchHistory.isEmpty() )
            displayNoRecentSearches();
        else
            displayHistory();
    }

    private void displayNoRecentSearches() {
        binding.layoutEmpty.setVisibility( View.VISIBLE );
        binding.recyclerview.setVisibility( View.GONE );
        binding.clearSearchHistoryButton.setVisibility( View.GONE );
        binding.noSearchResultsFound.setVisibility( View.GONE );
    }

    private void displayHistory() {
        binding.layoutEmpty.setVisibility( View.GONE );
        binding.noSearchResultsFound.setVisibility( View.GONE );

        binding.clearSearchHistoryButton.setVisibility( View.VISIBLE );
        binding.recyclerview.setVisibility( View.VISIBLE );
        binding.recyclerview.setLayoutManager( new LinearLayoutManager( owner.getContext() ) );
        binding.recyclerview.setAdapter( recentSearchesAdapter );
        recentSearchesAdapter.setData( searchHistory );
    }

}
