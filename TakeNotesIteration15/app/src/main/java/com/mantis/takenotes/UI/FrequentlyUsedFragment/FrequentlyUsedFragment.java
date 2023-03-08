package com.mantis.takenotes.UI.FrequentlyUsedFragment;

import android.content.Context;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import com.google.android.material.navigation.NavigationView;
import com.mantis.takenotes.Commands.ArchiveCommand;

import com.mantis.takenotes.Commands.DeleteCommand;
import com.mantis.takenotes.Commands.SendFeedbackCommand;
import com.mantis.takenotes.Commands.ShareCommand;

import com.mantis.takenotes.UI.MenuManagers.EditMenuManager.EditMenuManager;
import com.mantis.takenotes.UI.MenuManagers.ViewMenuManager.ViewMenuManager;

import com.mantis.takenotes.Repository.Note;
import com.mantis.takenotes.databinding.TakeNotesFragmentFrequentlyUsedBinding;
import com.mantis.takenotes.Adapters.NotesAdapter;

import com.mantis.takenotes.Dialogs.NoteActionDialog;
import com.mantis.takenotes.Utils.Logger;

import com.mantis.takenotes.Models.NotesViewModel;
import com.mantis.takenotes.Models.NotesViewModelFactory;
import com.mantis.takenotes.Repository.NoteRepositoryImpl;

import com.mantis.takenotes.Repository.NoteRepository;
import com.mantis.takenotes.Utils.MenuConfigurator;
import com.mantis.takenotes.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FrequentlyUsedFragment extends Fragment {

    private static final boolean SHOW_ARCHIVE_OPTION = true;
    private TakeNotesFragmentFrequentlyUsedBinding binding;
    private NotesAdapter notesAdapter;
    private NotesViewModel notesViewModel;
    private ViewMenuManager frequentlyUsedFragmentViewMenuManager;
    private EditMenuManager editMenuManager;
    private List<Note> frequentFragmentNotes = new ArrayList<>();
    private boolean editingIsInProgress = false;

    public FrequentlyUsedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static FrequentlyUsedFragment newInstance( String param1, String param2 ) {
        FrequentlyUsedFragment fragment = new FrequentlyUsedFragment();
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
        // Inflate the layout for this fragment
        binding = TakeNotesFragmentFrequentlyUsedBinding.inflate( inflater, container, false );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );
        setupNotesViewModel();
        setupMainToolbar();
        setupEditOptionsToolbar();
        setupViewMenuManager();
        setupEditMenuManager();
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
        NavigationUI.setupWithNavController( binding.frequentlyUsedFragmentContent
                .appBarLayout.toolbar, controller, appBarConfiguration );
        NavigationUI.setupWithNavController( navigationView, controller );
        binding.frequentlyUsedFragmentContent.appBarLayout
                .toolbar.inflateMenu( R.menu.take_notes_minimal_options_menu);
        MenuConfigurator.configureMenu( binding.frequentlyUsedFragmentContent
                .appBarLayout.toolbar.getMenu() );
        configureSearchMenuItem();
        configureEditMenuItem();
    }

    private void configureEditMenuItem() {
        MenuItem editMenuItem = binding.frequentlyUsedFragmentContent.appBarLayout.toolbar.getMenu()
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
        MenuItem searchMenuItem = binding.frequentlyUsedFragmentContent.
                appBarLayout.toolbar.getMenu().
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
                FrequentlyUsedFragmentDirections.actionNavFrequentlyUsedToNavSearch() );
    }

    private void setupEditOptionsToolbar() {
        binding.frequentlyUsedFragmentContent.editOptionToolbar.inflateMenu( R.menu.take_notes_bottom_toolbar_menu );
    }

    private void setupViewMenuManager() {
        frequentlyUsedFragmentViewMenuManager = new FrequentlyUsedFragmentViewMenuManager(
                this, notesViewModel, binding );
        frequentlyUsedFragmentViewMenuManager.addListener( new ViewMenuManager.ViewMenuManagerListener() {
            @Override
            public void onAdapterChange( NotesAdapter newNotesAdapter ) {
                notesAdapter = newNotesAdapter;
                notesAdapter.setData( frequentFragmentNotes );
                configureListenerOnNotesAdapter();
            }
        } );
    }

    private void configureListenerOnNotesAdapter() {
        notesAdapter.addListener(new NotesAdapter.NoteAdapterListener() {
            @Override
            public void onViewHolderClicked( View view, int viewHolderPosition ) {
                navigateToAddNoteFragment( view, viewHolderPosition );

            }

            @Override
            public void onViewHolderLongClicked( int viewHolderPosition ) {
                showNoteActionDialog( viewHolderPosition );
            }

            @Override
            public void onRecyclerViewEmpty( boolean isEmpty ) {
                showMenu( isEmpty );
                showEmptyView( isEmpty );
            }

            @Override
            public void onNoteNotesSizeChange(int newNotesSize) {
                displayNoteCount( newNotesSize );
            }
        } );
    }

    private void showEmptyView( boolean show ) {
        if ( show )
            binding.frequentlyUsedFragmentContent.layoutEmpty.setVisibility( View.VISIBLE );
        else
            binding.frequentlyUsedFragmentContent.layoutEmpty.setVisibility( View.GONE );
    }

    private void navigateToAddNoteFragment( View view, int viewHolderPosition ) {
        FrequentlyUsedFragmentDirections.ActionNavFrequentlyUsedToNavAddNote action =
                FrequentlyUsedFragmentDirections
                        .actionNavFrequentlyUsedToNavAddNote(
                                notesAdapter.getData().get( viewHolderPosition ).getId() );
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
        Menu menu = binding.frequentlyUsedFragmentContent.appBarLayout
                .toolbar.getMenu();
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
            binding.frequentlyUsedFragmentContent.appBarLayout.noteCountTextView
                    .setVisibility( View.GONE );
        else {
            String displayString = noteCount > 1 ? getString( R.string.note_count, noteCount ) :
                    getString( R.string.single_note_count, noteCount );
            binding.frequentlyUsedFragmentContent.appBarLayout.
                    noteCountTextView.setVisibility( View.VISIBLE );
            binding.frequentlyUsedFragmentContent.appBarLayout.
                    noteCountTextView.setText( displayString );
        }
    }

    private void setupEditMenuManager() {
        editMenuManager = new FrequentlyUsedFragmentEditMenuManager(
                this, notesAdapter, notesViewModel, binding );
        editMenuManager.addListener( new EditMenuManager.EditMenuManagerListener() {
            @Override
            public void editStatusChange(boolean editing) {
                editingIsInProgress = editing;
            }
        } );
    }

    private void observeNotes() {
        notesViewModel.getFrequentFragmentNotesList().
                observe( getViewLifecycleOwner(), new Observer<List<Note>>() {
                    @Override
                    public void onChanged( List<Note> notes ) {
                        Logger.log( "SETTING ADAPTER DATA. DATA SIZE: " + notes.size() );
                        frequentFragmentNotes = notes;
                        notesAdapter.setData( frequentFragmentNotes );
                    }
                } );
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.navView.getMenu().findItem( R.id.nav_frequently_used ).setChecked( true );
        binding.frequentlyUsedFragmentContent.appBarLayout.mainTitle.setText( getString(
                R.string.frequent_fragment ) );
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
        NavController controller = NavHostFragment.findNavController( this );
        controller.navigateUp();
    }
}