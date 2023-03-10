package com.mantis.TakeNotes.UI.TrashFragment;

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
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.mantis.TakeNotes.Adapters.NotesAdapter;
import com.mantis.TakeNotes.Commands.EmptyTrashCommand;
import com.mantis.TakeNotes.Dialogs.ConfirmationDialog;
import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.Models.NotesViewModelFactory;
import com.mantis.TakeNotes.UI.AddNoteFragment.AddNoteFragment;
import com.mantis.TakeNotes.UI.MenuManagers.EditMenuManager.EditMenuManager;
import com.mantis.TakeNotes.UI.MenuManagers.ViewMenuManager.ViewMenuManager;
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
    private NotesAdapter notesAdapter;
    private NotesViewModel notesViewModel;
    private ViewMenuManager trashFragmentViewMenuManager;
    private EditMenuManager trashFragmentEditMenuManager;
    private List<Note> trashNotesList = new ArrayList<>();

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
        binding = FragmentTrashBinding.inflate( inflater, container, false );
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
        trashFragmentEditMenuManager = new TrashFragmentEditMenuManager( this, notesAdapter,
                notesViewModel, binding );
    }

    private void setupEditingToolbar() {
        binding.trashFragmentContent.editOptionToolbar.inflateMenu( R.menu.trash_edit_options_menu );
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
                .toolbar.inflateMenu( R.menu.trash_options_menu );
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
                notesViewModel.editMenuSelected();
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
                        notesAdapter.getData().get( viewHolderPosition ).getId(),
                        AddNoteFragment.TRASH_FRAGMENT );
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
        if ( notesViewModel.getObservableEditStatus().getValue() ) {
            notesViewModel.doneEditing();
            binding.trashFragmentContent.trashFragmentAppBarLayout
                    .allCheckBox.setChecked( false );
            return;
        }
        NavController controller = NavHostFragment.findNavController( this );
        controller.navigateUp();
    }
}