package com.mantis.takenotes.UI.HomeFragment;

import android.content.Context;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.fragment.app.Fragment;
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

import com.google.android.material.navigation.NavigationView;
import com.mantis.takenotes.Adapters.NotesAdapter;
import com.mantis.takenotes.Commands.ArchiveCommand;

import com.mantis.takenotes.Commands.DeleteCommand;
import com.mantis.takenotes.Commands.SendFeedbackCommand;
import com.mantis.takenotes.Commands.ShareCommand;

import com.mantis.takenotes.Dialogs.NoteActionDialog;
import com.mantis.takenotes.UI.AddNoteFragment.AddNoteFragment;
import com.mantis.takenotes.UI.MenuManagers.EditMenuManager.EditMenuManager;

import com.mantis.takenotes.UI.MenuManagers.SortMenuManager.SortMenuManager;
import com.mantis.takenotes.UI.MenuManagers.ViewMenuManager.ViewMenuManager;
import com.mantis.takenotes.Models.NotesViewModel;

import com.mantis.takenotes.Models.NotesViewModelFactory;
import com.mantis.takenotes.Utils.Logger;
import com.mantis.takenotes.data.source.NoteRepositoryImpl;

import com.mantis.takenotes.data.source.NoteRepository;
import com.mantis.takenotes.R;
import com.mantis.takenotes.Utils.MenuConfigurator;

import com.mantis.takenotes.data.source.local.Note;
import com.mantis.takenotes.databinding.TakeNotesFragmentHomeBinding;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final boolean SHOW_ARCHIVE_OPTION = true;
    private TakeNotesFragmentHomeBinding binding;
    private NotesViewModel notesViewModel;
    private NotesAdapter notesAdapter;
    private EditMenuManager editMenuManager;
    private SortMenuManager sortMenuManager;
    private ViewMenuManager viewMenuManager;
    private List<Note> homeFragmentNotes = new ArrayList<>();
    private boolean editingIsInProgress = false;

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
        binding = TakeNotesFragmentHomeBinding.inflate( inflater, container, false );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ) {
        setupNotesViewModel();
        setupMainToolbar();
        setupFloatingActionButton();
        setupEditOptionsToolbar();
        setupViewMenuManager();
        setupEditMenuManager();
        setupSortMenuManager();
        observeNotes();
    }

    private void setupNotesViewModel() {
        NoteRepository noteRepository = NoteRepositoryImpl.getRepository(
                this.getActivity().getApplication() );
        NotesViewModelFactory factory = new NotesViewModelFactory( noteRepository );
        notesViewModel = new ViewModelProvider( requireActivity(), factory ).get(
                NotesViewModel.class );
    }

    private void setupMainToolbar() {
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
                R.menu.take_notes_home_fragment_options_menu);
        MenuConfigurator.configureMenu( binding.homeFragmentContent.appBarLayout.toolbar.getMenu() );
        configureSearchMenuItem();
        configureEditMenuItem();
    }

    private void configureEditMenuItem() {
        MenuItem editMenuItem = binding.homeFragmentContent.appBarLayout.toolbar.getMenu()
                .findItem( R.id.edit_option );
        editMenuItem.setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick( @NonNull MenuItem menuItem ) {
                editingIsInProgress = true;
                editMenuManager.updateEditingStatus( true );
                return true;
            }
        } );
    }

    private void configureSearchMenuItem() {
        MenuItem searchMenuItem = binding.homeFragmentContent.appBarLayout.toolbar.getMenu().
                findItem( R.id.nav_search );
        searchMenuItem.setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick( @NonNull MenuItem menuItem ) {
                navigateToSearchFragment();
                return true;
            }
        } );
    }

    private void navigateToSearchFragment() {
        NavHostFragment.findNavController( this ).navigate(
                HomeFragmentDirections.actionNavHomeToNavSearch() );
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

    private void setupEditOptionsToolbar() {
        binding.homeFragmentContent.editOptionToolbar.inflateMenu( R.menu.take_notes_bottom_toolbar_menu);
    }

    private void setupViewMenuManager() {
        viewMenuManager = new HomeFragmentViewMenuManager( this, notesViewModel,
                binding );
        viewMenuManager.addListener( new ViewMenuManager.ViewMenuManagerListener() {
            @Override
            public void onAdapterChange( NotesAdapter newNotesAdapter ) {
                notesAdapter = newNotesAdapter;
                notesAdapter.setData( homeFragmentNotes );
                configureListenerOnNotesAdapter();
            }
        } );
    }

    private void configureListenerOnNotesAdapter() {
        notesAdapter.addListener( new NotesAdapter.NoteAdapterListener() {
            @Override
            public void onViewHolderClicked(View view, int viewHolderPosition ) {
                navigateToAddNoteFragment( view, viewHolderPosition );
            }

            @Override
            public void onViewHolderLongClicked( int viewHolderPosition ) {
                showNoteActionDialog( viewHolderPosition );
            }

            @Override
            public void onRecyclerViewEmpty( boolean isEmpty ) {
                showMenu( isEmpty );
            }

            @Override
            public void onNoteNotesSizeChange( int newNotesSize ) {
                displayNoteCount( newNotesSize );

            }
        } );
    }

    private void navigateToAddNoteFragment( View view, int viewHolderPosition ) {
        HomeFragmentDirections.ActionNavHomeToNavAddNote action =
                HomeFragmentDirections.actionNavHomeToNavAddNote(
                        notesAdapter.getData().get( viewHolderPosition ).getId() );
        Navigation.findNavController( view ).navigate( action );
    }

    private void showNoteActionDialog( int viewHolderPosition ) {
        int noteSelectedId = notesAdapter.getData().get( viewHolderPosition ).getId();
        NoteActionDialog noteActionDialog = NoteActionDialog.newInstance( SHOW_ARCHIVE_OPTION );
        addNoteActionDialogListener( noteActionDialog, noteSelectedId );
        noteActionDialog.show( ( ( AppCompatActivity ) getContext()).getSupportFragmentManager(),
                "note-actions" );
    }

    private void addNoteActionDialogListener( NoteActionDialog noteActionDialog, int noteSelectedId ) {
        noteActionDialog.addListener( new NoteActionDialog.NoteActionDialogListener() {

            Note note = notesViewModel.getNoteWithId( noteSelectedId );

            @Override
            public void deleteSelected() {
                new DeleteCommand( getContext(), Arrays.asList( new Note[] { note } ),
                        notesViewModel, R.string.delete_single_note ).execute();
            }

            @Override
            public void onArchiveSelected() {
                new ArchiveCommand( getContext(), Arrays.asList( new Note[] { note } ),
                        notesViewModel, true ).execute();
            }

            @Override
            public void onUnarchiveSelected() {
                // Not possible here..
            }

            @Override
            public void onShareSelected() {
                new ShareCommand( getContext(), Arrays.asList( new Note[] { note } ),
                        notesViewModel ).execute();
            }

            @Override
            public void onSendFeedbackSelected() {
                new SendFeedbackCommand( getContext() ).execute();
            }
        } );
    }

    private void showMenu( boolean recyclerViewIsEmpty ) {
        Menu menu = binding.homeFragmentContent.appBarLayout.toolbar.getMenu();
        if ( menu == null )
            return;
        for ( int i = 0; i < menu.size(); i++ )
            if ( recyclerViewIsEmpty )
                menu.getItem( i ).setVisible( false );
            else
                menu.getItem( i ).setVisible( true );
    }

    private void displayNoteCount( int noteCount ) {
        if ( noteCount <= 0 )
            binding.homeFragmentContent.appBarLayout.noteCountTextView
                    .setVisibility( View.GONE );
        else {
            String displayString = noteCount > 1 ? getString( R.string.note_count, noteCount ) :
                    getString( R.string.single_note_count, noteCount );
            binding.homeFragmentContent.appBarLayout.noteCountTextView.setVisibility( View.VISIBLE );
            binding.homeFragmentContent.appBarLayout.noteCountTextView.setText( displayString );
        }
    }

    private void setupEditMenuManager() {
        editMenuManager = new HomeFragmentEditMenuManager( this,
                notesAdapter, notesViewModel, binding );
        editMenuManager.addListener( new EditMenuManager.EditMenuManagerListener() {
            @Override
            public void editStatusChange( boolean editing ) {
                editingIsInProgress = editing;
            }
        } );
    }

    private void setupSortMenuManager() {
        sortMenuManager = new HomeFragmentSortMenuManager( this, notesViewModel, notesAdapter,
                binding );
    }

    private void observeNotes() {
        notesViewModel.getHomeFragmentNotesList().
                observe( getViewLifecycleOwner(), new Observer<List<Note>>() {
                    @Override
                    public void onChanged( List<Note> notes ) {
                        Logger.log( "SETTING ADAPTER DATA. DATA SIZE: " + notes.size() );
                        homeFragmentNotes = notes;
                        notesAdapter.setData( notes );
                    }
                } );
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.navView.getMenu().findItem( R.id.nav_home ).setChecked( true );
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
        if ( editingIsInProgress ) {
            editingIsInProgress = false;
            editMenuManager.updateEditingStatus( false );
            return;
        }
        requireActivity().finish();
    }
}