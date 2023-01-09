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
import android.widget.TextView;

import com.mantis.MantisNotesIterationOne.Logger;
import com.mantis.MantisNotesIterationOne.Models.DateProvider;
import com.mantis.MantisNotesIterationOne.Models.Note;
import com.mantis.MantisNotesIterationOne.Models.NotesViewModel;
import com.mantis.MantisNotesIterationOne.databinding.FragmentAddNoteBinding;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddNoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNoteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddNoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNoteFragment newInstance(String param1, String param2) {
        AddNoteFragment fragment = new AddNoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        else if (  binding.noteTitle.getText().toString().equals( "" ) &&
                binding.addNoteEditText.getText().toString().equals( "" ) ) {
            Logger.log( "EDITED NOTE IS BEING DELETED" );
            deleteNoteFromModel();
        }
        else {
            editNote();
        }
        isEditing = false;
        controller.navigateUp();
    }

    private void deleteNoteFromModel() {
        if ( fragmentNavigatedFrom == HOME_FRAGMENT )
            notesViewModel.deleteNoteFromNotesList( position );
        else if ( fragmentNavigatedFrom == FREQUENT_FRAGMENT )
            notesViewModel.deleteNoteFromFrequentList( position );
        else
            notesViewModel.deleteNoteFromArchiveList( position );
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
        if ( noteTitle.equals( "" ) &&
                noteDescription.equals( "" ) ) {
            return;
        }
        else {
            Note note = new Note( noteTitle, noteDescription, DateProvider.getCurrentDate(),
                    new Date() );
            notesViewModel.addNote( note );
        }
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