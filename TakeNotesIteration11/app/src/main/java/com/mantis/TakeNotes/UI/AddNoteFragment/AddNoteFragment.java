package com.mantis.TakeNotes.UI.AddNoteFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.Models.NotesViewModelFactory;
import com.mantis.TakeNotes.R;
import com.mantis.TakeNotes.Utils.DateProvider;
import com.mantis.TakeNotes.Utils.Logger;
import com.mantis.TakeNotes.data.source.DefaultNoteRepository;
import com.mantis.TakeNotes.data.source.NoteRepository;
import com.mantis.TakeNotes.data.source.local.Note;
import com.mantis.TakeNotes.databinding.FragmentAddNoteBinding;

import java.util.Date;

public class AddNoteFragment extends Fragment {

    public static final int HOME_FRAGMENT = 1;
    public static final int FREQUENT_FRAGMENT = 2;
    public static final int ARCHIVE_FRAGMENT = 3;
    public static final int TRASH_FRAGMENT = 4;
    public static final int SEARCH_FRAGMENT = 5;
    public static final String TIME_REMAINING = "Time Remaining";

    private FragmentAddNoteBinding binding;
    private NotesViewModel notesViewModel;
    private NavController controller;
    private boolean isEditing;
    private int noteId;
    private int fragmentNavigatedFrom;
    private Note noteSelected;
    private boolean fragmentIsAlive = true;



    public AddNoteFragment() {
        // Required empty public constructor
    }

    public static AddNoteFragment newInstance() {
        AddNoteFragment fragment = new AddNoteFragment();
        Bundle args = new Bundle();
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        AddNoteFragmentArgs args = AddNoteFragmentArgs.fromBundle( getArguments() );
        noteId = args.getNoteID();
        fragmentNavigatedFrom = args.getFragmentType();
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        binding = FragmentAddNoteBinding.inflate( inflater, container, false );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ) {

        binding.editOptionToolbar.inflateMenu( R.menu.bottom_toolbar_menu );
        setupNotesViewModel();
        setupToolbar();
        showTrashFragmentComponents();
    }


    private void setupNotesViewModel() {
        NoteRepository noteRepository = DefaultNoteRepository.getRepository(
                this.getActivity().getApplication() );
        NotesViewModelFactory factory = new NotesViewModelFactory( noteRepository );
        notesViewModel = new ViewModelProvider( requireActivity(), factory ).get(
                NotesViewModel.class );
    }

    private void setupToolbar() {
        controller = NavHostFragment.findNavController( this );
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder( controller.getGraph() ).build();
        NavigationUI.setupWithNavController( binding.toolbar, controller, appBarConfiguration );
        binding.toolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                handleBackNavigation();
            }
        } );
        binding.toolbar.setTitle( "" );
    }

    private void showTrashFragmentComponents() {
        if ( !( fragmentNavigatedFrom == TRASH_FRAGMENT ) )
            return;
        showAppropriateViews();
    }

    private void showAppropriateViews() {
        binding.noteTitle.setVisibility( View.GONE );
        binding.addNoteEditText.setVisibility( View.GONE );
        binding.trashNoteTitle.setVisibility( View.VISIBLE );
        binding.trashNoteTextView.setVisibility( View.VISIBLE );
        binding.trashFragmentBottomBanner.setVisibility( View.VISIBLE );
        binding.trashContentBottomDivider.setVisibility( View.VISIBLE );
        binding.editOptionToolbar.setVisibility( View.GONE );
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

    @Override
    public void onStart() {
        super.onStart();
        if ( noteId == -1 )
            return;
        noteSelected = notesViewModel.getNoteWithId( noteId );
        populateViews( noteSelected );
    }

    // --------------------------------- Behavioral Details --------------------------------------

    private void handleBackNavigation() {
        if ( !isEditing )
            addNoteToModel();
        else
            editNote();
        isEditing = false;
        controller.navigateUp();
    }

    private void editNote() {
        Note placeHolder = new Note( binding.noteTitle.getText().toString(),
                binding.addNoteEditText.getText().toString(),
                DateProvider.getCurrentDate(), new Date() );
        if ( fragmentNavigatedFrom == HOME_FRAGMENT )
            notesViewModel.editHomeFragmentNote( noteId, placeHolder );
        else if ( fragmentNavigatedFrom == FREQUENT_FRAGMENT )
            notesViewModel.editFrequentFragmentNote( noteId, placeHolder );
        else
            notesViewModel.editArchiveFragmentNote( noteId, placeHolder );
    }

    private void addNoteToModel() {
        String noteTitle = binding.noteTitle.getText().toString();
        String noteDescription = binding.addNoteEditText.getText().toString();
        Note note = new Note( noteTitle, noteDescription, DateProvider.getCurrentDate(),
                new Date() );
        notesViewModel.addNote( note );  // View model will perform the necessary checks..
    }

    private void populateViews( Note note ) {
        if ( fragmentNavigatedFrom == TRASH_FRAGMENT )
            populateTrashFragmentViews( note );
        else
            populateOtherFragmentViews( note );
    }

    private void populateTrashFragmentViews( Note note ) {
        binding.trashNoteTitle.setText( note.getTitle() );
        binding.trashNoteTextView.setText( note.getDescription() );
    }


    private void populateOtherFragmentViews( Note note ) {
        isEditing = true;
        binding.noteTitle.setText( note.getTitle() );
        binding.addNoteEditText.setText( note.getDescription() );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
