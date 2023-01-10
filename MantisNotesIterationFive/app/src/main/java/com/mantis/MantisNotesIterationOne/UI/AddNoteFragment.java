package com.mantis.MantisNotesIterationOne.UI;

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

import com.mantis.MantisNotesIterationOne.Logger;
import com.mantis.MantisNotesIterationOne.Models.DateProvider;
import com.mantis.MantisNotesIterationOne.Models.Note;
import com.mantis.MantisNotesIterationOne.Models.NotesViewModel;
import com.mantis.MantisNotesIterationOne.databinding.FragmentAddNoteBinding;

import java.util.Date;

public class AddNoteFragment extends Fragment {

    public static final int HOME_FRAGMENT = 1;
    public static final int FREQUENT_FRAGMENT = 2;
    public static final int ARCHIVE_FRAGMENT = 3;
    public static final int TRASH_FRAGMENT = 4;

    private FragmentAddNoteBinding binding;
    private NotesViewModel notesViewModel;
    private NavController controller;
    private boolean isEditing;
    private int position;
    private int fragmentNavigatedFrom;

    public AddNoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment AddNoteFragment.
     */
    public static AddNoteFragment newInstance() {
        AddNoteFragment fragment = new AddNoteFragment();
        Bundle args = new Bundle();
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddNoteBinding.inflate( inflater, container, false );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ) {
        setupToolbar();
        setupNotesViewModel();
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

    private void setupNotesViewModel() {
        notesViewModel = new ViewModelProvider( requireActivity() ).get( NotesViewModel.class );
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
        if ( !isEditing )
            addNoteToModel();
        else
            editNote();
        isEditing = false;
        controller.navigateUp();
    }

    private void editNote() {
        Note note = new Note( binding.noteTitle.getText().toString(),
                    binding.addNoteEditText.getText().toString(),
                    DateProvider.getCurrentDate(), new Date() );
        if ( fragmentNavigatedFrom == HOME_FRAGMENT )
            notesViewModel.editNoteInNotesList( note, position );
        else if ( fragmentNavigatedFrom == FREQUENT_FRAGMENT )
            notesViewModel.editNoteInFrequentsList( note, position );
        else
            notesViewModel.editNoteInArchivesList( note, position );
    }

    private void addNoteToModel() {
        String noteTitle = binding.noteTitle.getText().toString();
        String noteDescription = binding.addNoteEditText.getText().toString();
        Note note = new Note( noteTitle, noteDescription, DateProvider.getCurrentDate(),
                new Date() );
        notesViewModel.addNote( note );  // View model will perform the necessary checks..
    }

    @Override
    public void onStart() {
        super.onStart();
        AddNoteFragmentArgs args = AddNoteFragmentArgs.fromBundle( getArguments() );
        position = args.getNotePosition();
        fragmentNavigatedFrom = args.getFragmentType();
        if ( position == -1 )
            return;
        Note noteSelected;
        if ( fragmentNavigatedFrom == HOME_FRAGMENT )
            noteSelected = notesViewModel.getNoteAt( position );
        else if ( fragmentNavigatedFrom == FREQUENT_FRAGMENT )
            noteSelected = notesViewModel.getFrequentedNoteAt( position );
        else if ( fragmentNavigatedFrom == TRASH_FRAGMENT )
            noteSelected = notesViewModel.getDeletedNoteAt( position );
        else
            noteSelected = notesViewModel.getArchivedNoteAt( position );
        populateViews( noteSelected );
    }

    private void populateViews( Note note ) {
        isEditing = true;
        binding.noteTitle.setText( note.getTitle() );
        binding.addNoteEditText.setText( note.getDescription() );
    }
}