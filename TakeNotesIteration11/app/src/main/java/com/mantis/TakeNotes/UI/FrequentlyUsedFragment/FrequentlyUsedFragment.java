package com.mantis.TakeNotes.UI.FrequentlyUsedFragment;

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
import com.mantis.TakeNotes.UI.MenuManagers.EditMenuManager.EditMenuManager;
import com.mantis.TakeNotes.UI.MenuManagers.ViewMenuManager.ViewMenuManager;
import com.mantis.TakeNotes.Utils.ToastProvider;
import com.mantis.TakeNotes.data.source.local.Note;
import com.mantis.TakeNotes.databinding.FragmentFrequentlyUsedBinding;
import com.mantis.TakeNotes.Adapters.NotesAdapter;
import com.mantis.TakeNotes.Dialogs.NoteActionDialog;
import com.mantis.TakeNotes.UI.AddNoteFragment.AddNoteFragment;
import com.mantis.TakeNotes.Utils.Logger;
import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.Models.NotesViewModelFactory;
import com.mantis.TakeNotes.data.source.DefaultNoteRepository;
import com.mantis.TakeNotes.data.source.NoteRepository;
import com.mantis.TakeNotes.Utils.MenuConfigurator;
import com.mantis.TakeNotes.R;

import java.util.ArrayList;
import java.util.List;

public class FrequentlyUsedFragment extends Fragment {

    private static final boolean SHOW_ARCHIVE_OPTION = true;
    private FragmentFrequentlyUsedBinding binding;
    private NotesAdapter notesAdapter;
    private NotesViewModel notesViewModel;
    private ViewMenuManager frequentlyUsedFragmentViewMenuManager;
    private EditMenuManager frequentlyUsedFragmentEditMenuManager;
    private List<Note> frequentFragmentNotes = new ArrayList<>();

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
        binding = FragmentFrequentlyUsedBinding.inflate( inflater, container, false );
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
        NoteRepository noteRepository = DefaultNoteRepository.getRepository(
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
                .frequentlyUsedFragmentAppBarLayout.toolbar, controller, appBarConfiguration );
        NavigationUI.setupWithNavController( navigationView, controller );
        binding.frequentlyUsedFragmentContent.frequentlyUsedFragmentAppBarLayout
                .toolbar.inflateMenu( R.menu.minimal_options_menu);
        MenuConfigurator.configureMenu( binding.frequentlyUsedFragmentContent
                .frequentlyUsedFragmentAppBarLayout.toolbar.getMenu() );
        configureSearchMenuItem();
    }

    private void configureSearchMenuItem() {
        MenuItem searchMenuItem = binding.frequentlyUsedFragmentContent.
                frequentlyUsedFragmentAppBarLayout.toolbar.getMenu().
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
        binding.frequentlyUsedFragmentContent.editOptionToolbar.inflateMenu( R.menu.bottom_toolbar_menu );
    }

    private void setupViewMenuManager() {
        frequentlyUsedFragmentViewMenuManager = new FrequentlyUsedFragmentViewMenuManager( this, notesViewModel,
                binding );
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
            }

            @Override
            public void onNoteNotesSizeChange(int newNotesSize) {
                displayNoteCount( newNotesSize );
            }
        } );
    }

    private void navigateToAddNoteFragment( View view, int viewHolderPosition ) {
        FrequentlyUsedFragmentDirections.ActionNavFrequentlyUsedToNavAddNote action =
                FrequentlyUsedFragmentDirections
                        .actionNavFrequentlyUsedToNavAddNote(
                                notesAdapter.getData().get( viewHolderPosition ).getId(),
                                AddNoteFragment.FREQUENT_FRAGMENT );
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
                notesViewModel.deleteFrequentFragmentNote( noteSelectedId );
            }

            @Override
            public void onArchiveSelected() {
                notesViewModel.archiveFrequentFragmentNote( noteSelectedId );
            }

            @Override
            public void onUnarchiveSelected() {
                // Not possible here..
            }

            @Override
            public void onShareSelected() {
                ToastProvider.showToast( getContext(), "Not yet implemented" );
            }

            @Override
            public void onSendFeedbackSelected() {
                ToastProvider.showToast( getContext(), "Not yet implemented" );
            }
        } );
    }

    private void showMenu( boolean recyclerViewIsEmpty ) {
        Menu menu = binding.frequentlyUsedFragmentContent.frequentlyUsedFragmentAppBarLayout
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
            binding.frequentlyUsedFragmentContent.frequentlyUsedFragmentAppBarLayout.noteCountTextView
                    .setVisibility( View.GONE );
        else {
            String displayString = noteCount > 1 ? getString( R.string.note_count, noteCount ) :
                    getString( R.string.single_note_count, noteCount );
            binding.frequentlyUsedFragmentContent.frequentlyUsedFragmentAppBarLayout.
                    noteCountTextView.setVisibility( View.VISIBLE );
            binding.frequentlyUsedFragmentContent.frequentlyUsedFragmentAppBarLayout.
                    noteCountTextView.setText( displayString );
        }
    }

    private void setupEditMenuManager() {
        frequentlyUsedFragmentEditMenuManager = new FrequentlyUsedFragmentEditMenuManager(
                this, notesAdapter, notesViewModel, binding );
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
        binding.frequentlyUsedFragmentContent.frequentlyUsedFragmentAppBarLayout.mainTitle.setText( getString( R.string.frequently_used_fragment ) );
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
            binding.frequentlyUsedFragmentContent.frequentlyUsedFragmentAppBarLayout
                    .allCheckBox.setChecked( false );
            return;
        }
        NavController controller = NavHostFragment.findNavController( this );
        controller.navigateUp();
    }
}