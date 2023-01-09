package com.mantis.MantisNotesIterationOne.UI;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.mantis.MantisNotesIterationOne.Adapters.NotesAdapter;
import com.mantis.MantisNotesIterationOne.Dialogs.SortOptionDialog;
import com.mantis.MantisNotesIterationOne.Logger;
import com.mantis.MantisNotesIterationOne.Models.Note;
import com.mantis.MantisNotesIterationOne.Models.NotesViewModel;
import com.mantis.MantisNotesIterationOne.R;
import com.mantis.MantisNotesIterationOne.Utils.MenuConfigurator;
import com.mantis.MantisNotesIterationOne.Utils.RecyclerViewConfigurator;
import com.mantis.MantisNotesIterationOne.databinding.FragmentHomeBinding;


import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private NotesViewModel notesViewModel;
    private NotesAdapter notesAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate( inflater, container, false );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ) {
        Logger.log( "View created" );
        setupNotesViewModel();
        setupToolbar();
        configureRecyclerView( notesViewModel.getCurrentLayoutType() );
        setupFloatingActionButton();
    }

    private void setupNotesViewModel() {
        notesViewModel = new ViewModelProvider( requireActivity() ).get( NotesViewModel.class );
        observeLayoutState();
        observeNotes();
    }

    private void observeLayoutState() {
        notesViewModel.getLayoutState().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged( Integer integer ) {
                Logger.log( "SETTING VIEW STATE" );
                configureRecyclerView( integer );
                MenuConfigurator.checkSelectedLayoutType( integer, binding.homeFragmentContent.appBarLayout.toolbar.getMenu() );
            }
        } );
    }

    private void configureRecyclerView( int layoutType ) {
        RecyclerView recyclerView = binding.homeFragmentContent.notesRecyclerView.recyclerview;
        NotesAdapter oldAdapter = ( NotesAdapter ) recyclerView.getAdapter();
        Logger.log( String.format( "Old Adapter: %s", oldAdapter ) );
        createAppropriateLayoutType( layoutType, recyclerView );
        configureRecyclerViewComponents( recyclerView );
        reloadAdapterData( oldAdapter );
        configureListenerOnNotesAdapter();
    }


    private void createAppropriateLayoutType( int layoutType, RecyclerView recyclerView ) {
        if ( layoutType == NotesViewModel.VIEW_STATE_SIMPLE_LIST )
            createSimpleListLayout( recyclerView );
        else if ( layoutType == NotesViewModel.VIEW_STATE_GRID )
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
        binding.homeFragmentContent.textEmpty.setText( Html.fromHtml( getString( R.string.text_empty_message ) ) );
        notesAdapter = ( NotesAdapter ) recyclerView.getAdapter();
        notesAdapter.setEmptyView( binding.homeFragmentContent.layoutEmpty );
        notesAdapter.setNotesViewModel( notesViewModel );
    }

    private void reloadAdapterData( NotesAdapter oldAdapter ) {
        if ( oldAdapter == null )
            notesAdapter.setData( new ArrayList<>() );
        else
            notesAdapter.setData( oldAdapter.getData() );
    }

    private void configureListenerOnNotesAdapter() {
        notesAdapter.addListener( new NotesAdapter.NoteAdapterListener() {
            @Override
            public void onViewHolderClicked( View view, int viewHolderPosition ) {
                HomeFragmentDirections.ActionNavHomeToNavAddNote action =
                        HomeFragmentDirections.actionNavHomeToNavAddNote(
                                viewHolderPosition );
                Navigation.findNavController( view ).navigate( action );
            }
        } );
    }


    private void observeNotes() {
        notesViewModel.getNotes().observe( getViewLifecycleOwner(), new Observer<ArrayList<Note>>() {
            @Override
            public void onChanged( ArrayList<Note> notes ) {
                Logger.log( "SETTING NOTES LIST" );
                notesAdapter.setData( notes );
            }
        } );
    }


    private void setupToolbar() {
        NavController controller = NavHostFragment.findNavController( this );
        DrawerLayout drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder( R.id.nav_home, R.id.nav_frequently_used, R.id.nav_trash )
                .setOpenableLayout( drawerLayout )
                .build();
        NavigationUI.setupWithNavController( binding.homeFragmentContent.appBarLayout.toolbar,
                controller, appBarConfiguration );
        NavigationUI.setupWithNavController( navigationView, controller );
        binding.homeFragmentContent.appBarLayout.toolbar.inflateMenu( R.menu.options_menu );
        MenuConfigurator.configureMenu( binding.homeFragmentContent.appBarLayout.toolbar.getMenu() );
        configureSortMenuItem();
    }

    private void configureSortMenuItem() {
        MenuItem sortMenuItem = binding.homeFragmentContent
                .appBarLayout.toolbar.getMenu().findItem( R.id.sort_option );
        sortMenuItem.setOnMenuItemClickListener(
                new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick( MenuItem item ) {
                        SortOptionDialog sortOptionDialog = SortOptionDialog.newInstance(
                                notesViewModel.getCurrentSortingStrategy(), notesViewModel.getAscendingOrDescending() );
                        sortOptionDialog.addListener( new SortOptionDialog.SortOptionDialogListener() {
                            @Override
                            public void onTitleSelected() {
                                notesViewModel.setSortingStrategy( NotesViewModel.TITLE );
                            }

                            @Override
                            public void onDateCreatedSelected() {
                                notesViewModel.setSortingStrategy( NotesViewModel.DATE_CREATED );
                            }

                            @Override
                            public void onDateModifiedSelected() {
                                notesViewModel.setSortingStrategy( NotesViewModel.DATE_MODIFIED );
                            }

                            @Override
                            public void onAscendingSelected() {
                                notesViewModel.setAscendingOrDescending( NotesViewModel.ASCENDING );
                            }

                            @Override
                            public void onDescendingSelected() {
                                notesViewModel.setAscendingOrDescending( NotesViewModel.DESCENDING );

                            }
                        } );
                        sortOptionDialog.show( ( (AppCompatActivity) getContext() )
                                .getSupportFragmentManager(), "sort-options" );
                        return true;
                    }
                } );
    }



    @Override
    public void onResume() {
        super.onResume();
        binding.navView.getMenu().findItem( R.id.nav_home ).setChecked( true );
    }


    private void setupFloatingActionButton() {
        binding.homeFragmentContent.fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                HomeFragmentDirections.ActionNavHomeToNavAddNote action =
                        HomeFragmentDirections.actionNavHomeToNavAddNote(
                                -1 );
                Navigation.findNavController( v ).navigate( action );
            }
        } );
    }
}