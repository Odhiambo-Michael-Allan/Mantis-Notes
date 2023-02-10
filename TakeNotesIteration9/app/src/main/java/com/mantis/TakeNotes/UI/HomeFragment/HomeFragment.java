package com.mantis.TakeNotes.UI.HomeFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.mantis.TakeNotes.Adapters.NotesAdapter;
import com.mantis.TakeNotes.Dialogs.NoteActionDialog;
import com.mantis.TakeNotes.Dialogs.SortOptionDialog;
import com.mantis.TakeNotes.UI.AddNoteFragment.AddNoteFragment;
import com.mantis.TakeNotes.UI.MenuManagers.EditMenuManager.EditMenuManager;
import com.mantis.TakeNotes.Utils.Logger;
import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.Models.NotesViewModelFactory;
import com.mantis.TakeNotes.data.source.DefaultNoteRepository;
import com.mantis.TakeNotes.data.source.NoteRepository;
import com.mantis.TakeNotes.data.source.local.Note;
import com.mantis.TakeNotes.R;
import com.mantis.TakeNotes.Utils.MenuConfigurator;
import com.mantis.TakeNotes.Utils.RecyclerViewConfigurator;
import com.mantis.TakeNotes.databinding.FragmentHomeBinding;


import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final boolean SHOW_ARCHIVE_OPTION = true;
    private FragmentHomeBinding binding;
    private NotesViewModel notesViewModel;
    private NotesAdapter notesAdapter;
    private EditMenuManager editMenuManager;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance( String param1, String param2 ) {
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
        setupNotesViewModel();
        setupToolbar();
        configureRecyclerView( notesViewModel.getCurrentLayoutTypeConfig() );
        setupFloatingActionButton();
        setupEditOptionsToolbar();
        setupEditMenuManager();
    }

    private void setupNotesViewModel() {
        createNotesViewModel();
        observeLayoutState();
        observeNotes();
        observeSortingConfiguration();
        observeEditStatus();
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
                MenuConfigurator.checkSelectedLayoutType( integer, binding.homeFragmentContent.appBarLayout.toolbar.getMenu() );
            }
        } );
    }

    private void configureRecyclerView( int layoutType ) {
        RecyclerView recyclerView = binding.homeFragmentContent.notesRecyclerView.recyclerview;
        NotesAdapter oldAdapter = ( NotesAdapter ) recyclerView.getAdapter();
        createAppropriateLayoutType( layoutType, recyclerView );
        configureRecyclerViewComponents( recyclerView );
        reloadAdapterData( oldAdapter );
        configureListenerOnNotesAdapter();
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
                navigationToAddNoteFragment( view, viewHolderPosition );
            }

            @Override
            public void onViewHolderLongClicked( int viewHolderPosition ) {
                showNoteActionDialog( viewHolderPosition );
            }

            @Override
            public void onRecyclerViewEmpty( boolean isEmpty ) {
                if ( isEmpty )
                    hideMenu();
                else
                    showMenu();
            }
        } );
    }

    private void navigationToAddNoteFragment( View view, int viewHolderPosition ) {
        HomeFragmentDirections.ActionNavHomeToNavAddNote action =
                HomeFragmentDirections.actionNavHomeToNavAddNote(
                        notesAdapter.getData().get( viewHolderPosition ).getId(),
                        AddNoteFragment.HOME_FRAGMENT );
        Navigation.findNavController( view ).navigate( action );
    }

    private void showNoteActionDialog( int viewHolderPosition ) {
        int noteSelectedId = notesAdapter.getData().get( viewHolderPosition ).getId();
        NoteActionDialog noteActionDialog = NoteActionDialog.newInstance( SHOW_ARCHIVE_OPTION );
        addNoteActionDialogListener( noteActionDialog, noteSelectedId );
        noteActionDialog.show( ((AppCompatActivity) getContext()).getSupportFragmentManager(),
                "note-actions" );
    }

    private void addNoteActionDialogListener( NoteActionDialog noteActionDialog, int noteSelectedId ) {
        noteActionDialog.addListener( new NoteActionDialog.NoteActionDialogListener() {
            @Override
            public void deleteSelected() {
                notesViewModel.deleteHomeFragmentNote( noteSelectedId );
            }

            @Override
            public void onArchiveSelected() {
                notesViewModel.archiveHomeFragmentNote( noteSelectedId );
            }

            @Override
            public void onUnarchiveSelected() {
                // Not possible here..
            }
        } );
    }

    private void observeNotes() {
        notesViewModel.getHomeFragmentNotesList().
                observe( getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged( List<Note> notes ) {
                notesAdapter.setData( notes );
            }
        } );
    }

    private void observeSortingConfiguration() {
        observeAscendingOption();
        observeSortingStrategyConfigOption();
    }

    private void observeAscendingOption() {
        notesViewModel.getAscendingConfigOption()
                .observe( getViewLifecycleOwner(), new Observer<Integer>() {
                    @Override
                    public void onChanged( Integer integer ) {
                        Logger.log( "ASCENDING CHANGED TO " + integer + ". REQUESTING ADAPTER TO SORT DATA" );
                        notesAdapter.sortData();
                    }
                } );
    }

    private void observeSortingStrategyConfigOption() {
        notesViewModel.getSortingStrategyConfigOption()
                .observe( getViewLifecycleOwner(), new Observer<Integer>() {
                    @Override
                    public void onChanged( Integer integer ) {
                        notesAdapter.sortData();
                    }
                } );
    }

    private void hideMenu() {
        Menu menu = binding.homeFragmentContent.appBarLayout.toolbar.getMenu();
        if ( menu == null )
            return;
        for ( int i = 0; i < menu.size(); i++ )
            menu.getItem( i ).setVisible( false );
    }

    private void showMenu() {
        Menu menu = binding.homeFragmentContent.appBarLayout.toolbar.getMenu();
        if ( menu == null )
            return;
        for ( int i = 0; i < menu.size(); i++ )
            menu.getItem( i ).setVisible( true );
    }


    private void setupToolbar() {
        NavController controller = NavHostFragment.findNavController( this );
        DrawerLayout drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder( R.id.nav_home, R.id.nav_frequently_used, R.id.nav_trash, R.id.nav_archive )
                .setOpenableLayout( drawerLayout )
                .build();
        NavigationUI.setupWithNavController( binding.homeFragmentContent.appBarLayout.toolbar,
                controller, appBarConfiguration );
        NavigationUI.setupWithNavController( navigationView, controller );
        binding.homeFragmentContent.appBarLayout.toolbar.inflateMenu(
                R.menu.home_fragment_options_menu );
        MenuConfigurator.configureMenu( binding.homeFragmentContent.appBarLayout.toolbar.getMenu() );
        configureSortMenuItem();
    }

    private void setupEditMenuManager() {
        editMenuManager = new HomeFragmentEditMenuManager( notesAdapter, notesViewModel, binding );
    }

    private void observeEditStatus() {
        notesViewModel.getObservableEditStatus().observe( getViewLifecycleOwner(),
                new Observer<Boolean>() {
                    @Override
                    public void onChanged( Boolean isEditing ) {
                        editMenuManager.updateAdapter( HomeFragment.this.notesAdapter );
                        editMenuManager.editStatusChanged( isEditing );
                        attachAllNotesCheckedObserver( isEditing );
                    }
                } );
    }

    private void attachAllNotesCheckedObserver( boolean isEditing ) {
        if ( !isEditing )
            return;
        LiveData<Boolean> allNotesAreChecked = this.notesAdapter.getAllNotesCheckedStatus();
        allNotesAreChecked.removeObservers( getViewLifecycleOwner() );
        allNotesAreChecked.observe( getViewLifecycleOwner(),
                new Observer<Boolean>() {
                    @Override
                    public void onChanged( Boolean checked ) {
                        editMenuManager.allNotesChecked( checked );
                    }
                } );
    }

    private void configureSortMenuItem() {
        MenuItem sortMenuItem = binding.homeFragmentContent
                .appBarLayout.toolbar.getMenu().findItem( R.id.sort_option );
        sortMenuItem.setOnMenuItemClickListener(
                new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick( MenuItem item ) {
                        SortOptionDialog sortOptionDialog = SortOptionDialog.newInstance(
                                notesViewModel.getCurrentSortingStrategyConfigOption(),
                                notesViewModel.getCurrentAscendingConfigOption() );
                        sortOptionDialog.addListener( new SortOptionDialog.SortOptionDialogListener() {
                            @Override
                            public void onTitleSelected() {
                                notesViewModel.updateSortingStrategyConfig( NotesViewModel.TITLE );
                            }

                            @Override
                            public void onDateCreatedSelected() {
                                notesViewModel.updateSortingStrategyConfig( NotesViewModel.DATE_CREATED );
                            }

                            @Override
                            public void onDateModifiedSelected() {
                                notesViewModel.updateSortingStrategyConfig( NotesViewModel.DATE_MODIFIED );
                            }

                            @Override
                            public void onAscendingSelected() {
                                notesViewModel.updateAscendingConfig( NotesViewModel.ASCENDING );
                            }

                            @Override
                            public void onDescendingSelected() {
                                notesViewModel.updateAscendingConfig( NotesViewModel.DESCENDING );
                            }
                        } );
                        sortOptionDialog.show( ( (AppCompatActivity) getContext() )
                                .getSupportFragmentManager(), "sort-options" );
                        return true;
                    }
                } );
    }

    private void setupEditOptionsToolbar() {
        binding.homeFragmentContent.editOptionToolbar.inflateMenu( R.menu.bottom_toolbar_menu );
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
                                -1, AddNoteFragment.HOME_FRAGMENT );
                Navigation.findNavController( v ).navigate( action );
            }
        } );
    }

    @Override
    public void onAttach( @NonNull Context context ) {
        super.onAttach( context );
        OnBackPressedCallback callback = new OnBackPressedCallback( true ) {
            @Override
            public void handleOnBackPressed() {
                handleBackNavigation();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback( this, callback );
    }

    private void handleBackNavigation() {
        if ( notesViewModel.getObservableEditStatus().getValue() ) {
            notesViewModel.doneEditing();
            binding.homeFragmentContent.appBarLayout.allCheckBox.setChecked( false );
            return;
        }
        requireActivity().finish();
    }
}