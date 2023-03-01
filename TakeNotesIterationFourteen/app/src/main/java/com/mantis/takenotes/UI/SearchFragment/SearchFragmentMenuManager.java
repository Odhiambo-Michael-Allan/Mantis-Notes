package com.mantis.takenotes.UI.SearchFragment;


import android.view.View;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;
import com.mantis.takenotes.Adapters.NotesAdapter;
import com.mantis.takenotes.Adapters.RecentSearchesAdapter;

import com.mantis.takenotes.Models.NotesViewModel;
import com.mantis.takenotes.UI.AddNoteFragment.AddNoteFragment;

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
    private SearchResultsMediator searchResultsMediator;

    public SearchFragmentMenuManager( Fragment owner, NotesViewModel notesViewModel,
                                      TakeNotesFragmentSearchBinding binding ) {
        this.owner = owner;
        this.notesViewModel = notesViewModel;
        this.binding = binding;
        setupRecentSearchesAdapter();
    }

    public void updateAdapter( NotesAdapter notesAdapter ) {
        this.notesAdapter = notesAdapter;
        attachNotesAdapterListener();
        currentLayoutManager = binding.recyclerview.getLayoutManager();
        setupSearchResultsMediator( notesAdapter );
        searchResultsMediator.setCurrentLayoutManager( currentLayoutManager );
    }

    private void setupSearchResultsMediator( NotesAdapter notesAdapter ) {
        if ( searchResultsMediator == null ) {
            setupRecentSearchesAdapter();
            searchResultsMediator = new SearchResultsMediator( owner, binding,
                    notesViewModel, notesAdapter, recentSearchesAdapter );
        }
        searchResultsMediator.updateAdapter( notesAdapter );
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
                        notesAdapter.getData().get( viewHolderPosition ).getId() );
        Navigation.findNavController( view ).navigate( action );
    }


    private void setupRecentSearchesAdapter() {
        recentSearchesAdapter = new RecentSearchesAdapter( this.owner, this.notesViewModel );
        recentSearchesAdapter.addListener( new RecentSearchesAdapter.RecentSearchesAdapterListener() {
            @Override
            public void onRecentQueryClick( Query query ) {
                searchResultsMediator.displayResultsMatching( query.getDescription() );
            }

            @Override
            public void onSearchHistoryChange( List<Query> searchHistory ) {
            }
        } );
    }

}