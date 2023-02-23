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

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
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
import java.util.UUID;

public class AddNoteFragment extends Fragment {

    public static final int HOME_FRAGMENT = 1;
    public static final int FREQUENT_FRAGMENT = 2;
    public static final int ARCHIVE_FRAGMENT = 3;
    public static final int TRASH_FRAGMENT = 4;
    public static final int SEARCH_FRAGMENT = 5;
    public static final String TIME_REMAINING = "Time Remaining";
    public static final String TIME_UP = "Time up";

    private FragmentAddNoteBinding binding;
    private NotesViewModel notesViewModel;
    private NavController controller;
    private boolean isEditing;
    private int noteId;
    private int fragmentNavigatedFrom;
    private Note noteSelected;
    private boolean fragmentIsAlive = true;

    private Handler handler;
    private Note.NoteObserver trashedNoteObserver;



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
    public void onCreate( Bundle savedInstanceState ) {
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
        if ( fragmentNavigatedFrom == TRASH_FRAGMENT ) {
            setupHandler();
            setupTrashNoteObserver();
            attachObserverToSelectedNote();
        }
    }

    private void setupHandler() {
        handler = new Handler( Looper.getMainLooper() ) {
            @Override
            public void handleMessage( Message message ) {
                if ( message.getData().get( TIME_UP ) != null ) {
                    controller.navigateUp();
                    return;
                }
                if ( fragmentIsAlive ){
                    String timeRemaining = message.getData().get( TIME_REMAINING ).toString();
                    binding.trashFragmentBottomBanner.setText( getString( R.string.trash_content_bottom_banner, timeRemaining ) );
                }
            }
        };
    }


    private void attachObserverToSelectedNote() {
        noteSelected.addObserver( trashedNoteObserver );
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

        Note placeHolder = new Note( binding.noteTitle.getText().toString(),
                binding.addNoteEditText.getText().toString(),
                DateProvider.getCurrentDate(), new Date() );
        notesViewModel.getNoteWithId( noteId ).edit(placeHolder.getTitle(), placeHolder.getDescription(), notesViewModel.getNoteRepository());
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
        Logger.log( "FRAGMENT IS BEING DESTROYED" );
        if ( fragmentNavigatedFrom == TRASH_FRAGMENT )
            removeObserverFromSelectedNote();
        fragmentIsAlive = false;
        handler = null;
    }

    private void removeObserverFromSelectedNote() {
        noteSelected.removeObserver( trashedNoteObserver.getId() );
    }

    private void setupTrashNoteObserver() {
        trashedNoteObserver = new NoteObserver( UUID.randomUUID().toString() );
    }

    public class NoteObserver implements Note.NoteObserver {

        private String id;

        public NoteObserver( String id ) {
            this.id = id;
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public void onTimeLeftDecreased( long timeLeft ) {
            updateTimeLeftView( timeLeft );
        }

        @Override
        public void onTimeUp( int noteId ) {
            if ( handler == null )
                return;
            Bundle bundle = new Bundle();
            bundle.putString( TIME_UP, "Time's up.." );
            Message message = new Message();
            message.setData( bundle );
            handler.sendMessage( message );
        }
    }

    private void updateTimeLeftView( long timeLeft ) {
        long daysRemaining = timeLeft / ( 24 * 60 * 60 * 1000 );
        long hoursRemaining = timeLeft / ( 60 * 60 * 1000 );
        long minutesRemaining = timeLeft / ( 60 * 1000 ) % 60;
        long secondsRemaining = timeLeft / 1000 % 60;
        String timeRemaining = String.format( "%s days %s hours %s minutes %s seconds",
                daysRemaining,
                hoursRemaining,
                minutesRemaining,
                secondsRemaining );

        Bundle bundle = new Bundle();
        bundle.putString( TIME_REMAINING, timeRemaining );
        Message message = new Message();
        message.setData( bundle );
        if ( handler != null )
            handler.sendMessage( message );
    }
}
