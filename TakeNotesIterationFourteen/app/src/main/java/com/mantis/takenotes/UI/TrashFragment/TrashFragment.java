package com.mantis.takenotes.UI.TrashFragment;

import android.content.Context;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;

import androidx.annotation.NonNull;
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
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;

import com.mantis.takenotes.Adapters.NotesAdapter;
import com.mantis.takenotes.Commands.EmptyTrashCommand;
import com.mantis.takenotes.Models.NotesViewModel;

import com.mantis.takenotes.Models.NotesViewModelFactory;
import com.mantis.takenotes.UI.AddNoteFragment.AddNoteFragment;
import com.mantis.takenotes.UI.MenuManagers.EditMenuManager.EditMenuManager;

import com.mantis.takenotes.UI.MenuManagers.ViewMenuManager.ViewMenuManager;
import com.mantis.takenotes.data.source.NoteRepositoryImpl;
import com.mantis.takenotes.data.source.NoteRepository;

import com.mantis.takenotes.data.source.local.Note;
import com.mantis.takenotes.R;
import com.mantis.takenotes.databinding.TakeNotesFragmentTrashBinding;

import java.util.ArrayList;
import java.util.List;

public class TrashFragment extends Fragment {

    private TakeNotesFragmentTrashBinding binding;
    private NotesAdapter notesAdapter;
    private NotesViewModel notesViewModel;
    private ViewMenuManager trashFragmentViewMenuManager;
    private EditMenuManager editMenuManager;
    private List<Note> trashNotesList = new ArrayList<>();
    private boolean editingIsInProgress = false;

    public TrashFragment() {
        // Required empty public constructor
    }

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
        binding = TakeNotesFragmentTrashBinding.inflate( inflater, container, false );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );
        setupNotesViewModel();
        setupMainToolbar();
        setupEditingToolbar();
        showTrashFragmentBanner();
        setupViewMenuManager();
        setupEditMenuManager();
        observeNotes();
    }

    private void setupViewMenuManager() {
        trashFragmentViewMenuManager = new TrashFragmentViewMenuManager( this, notesViewModel,
                binding );
        trashFragmentViewMenuManager.addListener( new ViewMenuManager.ViewMenuManagerListener() {
            @Override
            public void onAdapterChange( NotesAdapter newNotesAdapter ) {
                notesAdapter = newNotesAdapter;
                notesAdapter.setData( trashNotesList );
                configureListenerOnNotesAdapter();
            }
        } );
    }

    private void setupEditMenuManager() {
        editMenuManager = new TrashFragmentEditMenuManager( this, notesAdapter,
                notesViewModel, binding );
        editMenuManager.addListener( new EditMenuManager.EditMenuManagerListener() {
            @Override
            public void editStatusChange( boolean editing ) {
                editingIsInProgress = editing;
            }
        } );
    }

    private void setupEditingToolbar() {
        binding.trashFragmentContent.editOptionToolbar.inflateMenu( R.menu.take_notes_trash_edit_options_menu );
    }

    private void showTrashFragmentBanner() {
        binding.trashFragmentContent.trashFragmentAppBarLayout.trashFragmentBanner
                .setVisibility( View.VISIBLE );
    }

    private void setupNotesViewModel() {
        NoteRepository noteRepository = NoteRepositoryImpl.getRepository(
                this.getActivity().getApplication() );
        NotesViewModelFactory factory = new NotesViewModelFactory( noteRepository );
        notesViewModel = new ViewModelProvider( requireActivity(), factory ).get(
                NotesViewModel.class );
    }

    private void configureListenerOnNotesAdapter() {
        notesAdapter.addListener(new NotesAdapter.NoteAdapterListener() {
            @Override
            public void onViewHolderClicked( View view, int viewHolderPosition ) {
                navigationToAddNoteFragment( view, viewHolderPosition );
            }

            @Override
            public void onViewHolderLongClicked( int viewHolderPosition ) {
                Toast.makeText( getContext(), "Not yet implemented",
                        Toast.LENGTH_SHORT ).show();
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

    private void showMenu( boolean recyclerViewIsEmpty ) {
        Menu menu = binding.trashFragmentContent.trashFragmentAppBarLayout
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
            binding.trashFragmentContent.trashFragmentAppBarLayout.noteCountTextView
                    .setVisibility( View.GONE );
        else {
            String displayString = noteCount > 1 ? getString( R.string.note_count, noteCount ) :
                    getString( R.string.single_note_count, noteCount );
            binding.trashFragmentContent.trashFragmentAppBarLayout.noteCountTextView
                    .setVisibility( View.VISIBLE );
            binding.trashFragmentContent.trashFragmentAppBarLayout.noteCountTextView.setText(
                    displayString );
        }
    }

    private void observeNotes() {
        notesViewModel.getTrashFragmentNotesList().observe( getViewLifecycleOwner(),
                new Observer<List<Note>>() {
                    @Override
                    public void onChanged( List<Note> notes ) {
                        trashNotesList = notes;
                        notesAdapter.setData( trashNotesList );
                    }
                } );
    }

    private void setupMainToolbar() {
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
                .toolbar.inflateMenu( R.menu.take_notes_trash_options_menu );
        configureMenu();
    }

    private void configureMenu() {
        configureEditMenu();
        configureEmptyMenu();
    }

    private void configureEditMenu() {
        MenuItem editMenuItem = binding.trashFragmentContent.trashFragmentAppBarLayout
                .toolbar.getMenu()
                .findItem( R.id.edit_option );
        editMenuItem.setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick( MenuItem item ) {
                editingIsInProgress = true;
                editMenuManager.updateEditingStatus( true );
                return true;
            }
        } );
    }

    private void configureEmptyMenu() {
        MenuItem emptyMenuItem = binding.trashFragmentContent.trashFragmentAppBarLayout
                .toolbar.getMenu()
                .findItem( R.id.empty_option );
        emptyMenuItem.setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick( MenuItem item ) {
                new EmptyTrashCommand( getContext(), notesViewModel, notesAdapter ).execute();
                return true;
            }
        } );
    }

    private void navigationToAddNoteFragment( View view, int viewHolderPosition ) {
        TrashFragmentDirections.ActionNavTrashToNavAddNote action =
                TrashFragmentDirections.actionNavTrashToNavAddNote(
                        notesAdapter.getData().get( viewHolderPosition ).getId() );
        Navigation.findNavController( view ).navigate( action );
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.navView.getMenu().findItem( R.id.nav_trash ).setChecked( true );
        binding.trashFragmentContent.trashFragmentAppBarLayout.mainTitle
                .setText( getString( R.string.trash ) );
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