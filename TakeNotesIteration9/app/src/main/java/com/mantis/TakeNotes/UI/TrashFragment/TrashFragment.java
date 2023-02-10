package com.mantis.TakeNotes.UI.TrashFragment;

import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.mantis.TakeNotes.Adapters.NotesAdapter;
import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.Models.NotesViewModelFactory;
import com.mantis.TakeNotes.UI.AddNoteFragment.AddNoteFragment;
import com.mantis.TakeNotes.data.source.DefaultNoteRepository;
import com.mantis.TakeNotes.data.source.NoteRepository;
import com.mantis.TakeNotes.data.source.local.Note;
import com.mantis.TakeNotes.R;
import com.mantis.TakeNotes.Utils.RecyclerViewConfigurator;
import com.mantis.TakeNotes.databinding.FragmentTrashBinding;

import java.util.ArrayList;
import java.util.List;

public class TrashFragment extends Fragment {


    private FragmentTrashBinding binding;
    private NotesAdapter adapter;
    private NotesViewModel notesViewModel;

    public TrashFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment TrashFragment.
     */
    public static TrashFragment newInstance( String param1, String param2 ) {
        TrashFragment fragment = new TrashFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState ) {
        binding = FragmentTrashBinding.inflate( inflater, container, false );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );
        showTrashFragmentBanner();
        setupNotesViewModel();
        setupToolbar();
        configureRecyclerView( NotesViewModel.LAYOUT_STATE_SIMPLE_LIST );
    }

    private void showTrashFragmentBanner() {
        binding.trashFragmentContent.trashFragmentAppBarLayout.trashFragmentBanner
                .setVisibility( View.VISIBLE );
    }

    private void setupNotesViewModel() {
        NoteRepository noteRepository = DefaultNoteRepository.getRepository(
                this.getActivity().getApplication() );
        NotesViewModelFactory factory = new NotesViewModelFactory( noteRepository );
        notesViewModel = new ViewModelProvider( requireActivity(), factory ).get(
                NotesViewModel.class );
        observeLayoutState();
        observeTrashedNotes();
    }

    private void observeLayoutState() {
        notesViewModel.getLayoutTypeConfig().observe(getViewLifecycleOwner(),
                new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        configureRecyclerView( integer );
                    }
                } );
    }

    private void configureRecyclerView( int layoutType ) {
        RecyclerView recyclerView = binding.trashFragmentContent.trashFragmentNotesRecyclerView
                .recyclerview;
        NotesAdapter oldAdapter = ( NotesAdapter ) recyclerView.getAdapter();
        createAppropriateLayoutType( layoutType, recyclerView );
        configureRecyclerViewComponents( recyclerView );
        reloadAdapterData( oldAdapter );
        configureListenerOnNotesAdapter();
    }

    private void createAppropriateLayoutType( int layoutType, RecyclerView recyclerView ) {
        if ( layoutType == NotesViewModel.LAYOUT_STATE_SIMPLE_LIST)
            createSimpleListLayout( recyclerView );
        else if ( layoutType == NotesViewModel.LAYOUT_STATE_GRID)
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
        binding.trashFragmentContent.textEmpty.setText( R.string.no_notes );
        adapter = ( NotesAdapter ) recyclerView.getAdapter();
        adapter.setEmptyView( binding.trashFragmentContent.layoutEmpty );
        adapter.setNotesViewModel( notesViewModel );
    }

    private void reloadAdapterData( NotesAdapter oldAdapter ) {
        Menu menu = binding.trashFragmentContent.trashFragmentAppBarLayout.
                toolbar.getMenu();
        if ( oldAdapter == null )
            adapter.setData( new ArrayList<>() );
        else
            adapter.setData( oldAdapter.getData() );
    }

    private void configureListenerOnNotesAdapter() {
        adapter.addListener( new NotesAdapter.NoteAdapterListener() {
            @Override
            public void onViewHolderClicked( View view, int viewHolderPosition ) {
                TrashFragmentDirections.ActionNavTrashToNavAddNote action =
                        TrashFragmentDirections
                                .actionNavTrashToNavAddNote( viewHolderPosition,
                                        AddNoteFragment.TRASH_FRAGMENT );
                Navigation.findNavController( view ).navigate( action );
            }

            @Override
            public void onViewHolderLongClicked( int viewHolderPosition ) {
                Toast.makeText( getContext(), "Not yet implemented",
                        Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onRecyclerViewEmpty( boolean isEmpty ) {

            }
        } );
    }

    private void observeTrashedNotes() {
        notesViewModel.getTrashFragmentNotesList().observe( getViewLifecycleOwner(),
                new Observer<List<Note>>() {
                    @Override
                    public void onChanged( List<Note> notes ) {
                        adapter.setData( notes );
                    }
                } );
    }

    private void setupToolbar() {
        NavController navController = NavHostFragment.findNavController( this );
        DrawerLayout drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder( R.id.nav_home, R.id.nav_frequently_used, R.id.nav_trash )
                .setOpenableLayout( drawerLayout )
                .build();
        NavigationUI.setupWithNavController( binding.trashFragmentContent.
                        trashFragmentAppBarLayout.toolbar,
                navController, appBarConfiguration );
        NavigationUI.setupWithNavController( navigationView, navController );
        binding.trashFragmentContent.trashFragmentAppBarLayout
                .toolbar.inflateMenu( R.menu.trash_options_menu );
        configureMenu();
    }

    private void configureMenu() {
        MenuItem editMenuItem = binding.trashFragmentContent.trashFragmentAppBarLayout
                .toolbar.getMenu()
                .findItem( R.id.edit_option );
        editMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText( getContext(), "Not yet implemented", Toast.LENGTH_SHORT )
                        .show();
                return true;
            }
        } );
        MenuItem emptyMenuItem = binding.trashFragmentContent.trashFragmentAppBarLayout
                .toolbar.getMenu()
                .findItem( R.id.empty_option );
        emptyMenuItem.setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick( MenuItem item ) {
                notesViewModel.emptyTrashList();
                return true;
            }
        } );
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.navView.getMenu().findItem( R.id.nav_trash ).setChecked( true );
        binding.trashFragmentContent.trashFragmentAppBarLayout.mainTitle
                .setText( getString( R.string.trash ) );
    }
}