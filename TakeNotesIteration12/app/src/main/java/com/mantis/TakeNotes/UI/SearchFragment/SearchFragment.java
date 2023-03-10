package com.mantis.TakeNotes.UI.SearchFragment;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mantis.TakeNotes.Adapters.NotesAdapter;
import com.mantis.TakeNotes.Adapters.RecentSearchesAdapter;
import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.Models.NotesViewModelFactory;
import com.mantis.TakeNotes.Utils.DateProvider;
import com.mantis.TakeNotes.Utils.Logger;
import com.mantis.TakeNotes.Utils.RecyclerViewConfigurator;
import com.mantis.TakeNotes.data.source.DefaultNoteRepository;
import com.mantis.TakeNotes.data.source.NoteRepository;
import com.mantis.TakeNotes.data.source.local.Note;
import com.mantis.TakeNotes.data.source.local.Query;
import com.mantis.TakeNotes.databinding.FragmentSearchBinding;

import com.mantis.TakeNotes.R;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private NotesViewModel notesViewModel;
    private NotesAdapter notesAdapter;
    private SearchFragmentMenuManager searchFragmentMenuManager;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance( String param1, String param2 ) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate( inflater, container, false );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ) {
        setupNotesViewModel();
        setupAppBar();
    }

    private void setupNotesViewModel() {
        createNotesViewModel();
        observeLayoutState();
    }

    private void createNotesViewModel() {
        NoteRepository noteRepository = DefaultNoteRepository.getRepository(
                this.getActivity().getApplication() );
        NotesViewModelFactory factory = new NotesViewModelFactory( noteRepository );
        notesViewModel = new ViewModelProvider( requireActivity(), factory ).get(
                NotesViewModel.class );
    }

    private void observeLayoutState() {
        notesViewModel.getLayoutTypeConfig().observe( getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged( Integer integer ) {
                configureRecyclerView( integer );
                // We setup the search fragment manager here because it depends on the notes
                // adapter and at this point we are sure the notes adapter is available..
                setupSearchFragmentMenuManager();
                searchFragmentMenuManager.updateAdapter( notesAdapter );
            }
        } );
    }

    private void configureRecyclerView( int layoutType ) {
        RecyclerView recyclerView = binding.recyclerview;
        createAppropriateLayoutType( layoutType, recyclerView );
        configureRecyclerViewComponents( recyclerView );
    }

    private void createAppropriateLayoutType( int layoutType, RecyclerView recyclerView ) {
        if ( layoutType == NotesViewModel.LAYOUT_STATE_SIMPLE_LIST )
            createSimpleListLayout( recyclerView );
        else if ( layoutType == NotesViewModel.LAYOUT_STATE_GRID )
            createGridLayout( recyclerView );
        else
            createListLayout( recyclerView );
    }

    private void createSimpleListLayout( RecyclerView recyclerView ) {
        RecyclerViewConfigurator.configureSimpleListRecyclerView( recyclerView, getContext() );
    }

    private void createGridLayout( RecyclerView recyclerView ) {
        RecyclerViewConfigurator.configureGridLayoutRecyclerView( recyclerView, getContext() );
    }

    private void createListLayout( RecyclerView recyclerView ) {
        RecyclerViewConfigurator.configureListRecyclerView( recyclerView, getContext() );
    }

    private void configureRecyclerViewComponents( RecyclerView recyclerView ) {
        binding.textEmpty.setText( getString( R.string.no_recent_searches ) );
        notesAdapter = ( NotesAdapter ) recyclerView.getAdapter();
        notesAdapter.setEmptyView( binding.layoutEmpty );
        notesAdapter.setNotesViewModel( notesViewModel );
    }

    private void setupAppBar() {
        binding.mainTitle.setText( getString( R.string.search ) );
        binding.searchView.requestFocus();
    }

    private void setupSearchFragmentMenuManager() {
        searchFragmentMenuManager = new SearchFragmentMenuManager( this, notesViewModel,
                binding );
    }

}