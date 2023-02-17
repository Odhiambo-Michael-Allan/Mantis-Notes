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

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private Thread executor;
    private boolean fragmentIsAlive = true;
    private Calendar calendar = Calendar.getInstance();

    Handler handler;

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
        setupHandler();
        showTrashFragmentComponents();
    }

    private void setupHandler() {
         handler = new Handler(Looper.getMainLooper() ) {
            @Override
            public void handleMessage( Message message ) {
                String timeRemaining = message.getData().get( TIME_REMAINING ).toString();
                binding.trashFragmentBottomBanner.setText( getString( R.string.trash_content_bottom_banner, timeRemaining ) );
            }
        };
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
        if ( fragmentNavigatedFrom == TRASH_FRAGMENT ) {
            fragmentIsAlive = false;
            if ( executor != null )
                executor.interrupt();
            executor = null;
            controller.navigateUp();
            return;
        }
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
        showTimeRemaining();
    }

    private void showTimeRemaining() {
        startReaper();
    }

    private void startReaper() {
        if ( executor != null )
            return;
        executor = new Thread( createRunnable() );
        executor.start();
    }

    private Runnable createRunnable() {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {

                Date dateNoteCreated = noteSelected.getDateCreated();
                calendar.setTime( dateNoteCreated );
                calendar.add( Calendar.MINUTE, 3 );

                Date dateNoteIsToBeDeleted = calendar.getTime();
                long timeDifference = dateNoteIsToBeDeleted.getTime() - dateNoteCreated.getTime();
                long daysRemaining = timeDifference / ( 24 * 60 * 60 * 1000 );
                long hoursRemaining = timeDifference / ( 60 * 60 * 1000 );
                long minutesRemaining = timeDifference / ( 60 * 1000 ) % 60;
                long secondsRemaining = timeDifference / 1000 % 60;
                String timeRemaining = String.format("%s days %s hours %s minutes %s seconds",
                        daysRemaining,
                        hoursRemaining,
                        minutesRemaining,
                        secondsRemaining);


                Bundle newBundle = new Bundle();
                Message newMessage = new Message();
                newBundle.putString( TIME_REMAINING, timeRemaining );

                newMessage.setData( newBundle );
                handler.sendMessage( newMessage );

                while ( fragmentIsAlive && dateNoteCreated.before( dateNoteIsToBeDeleted ) ) {
                    calendar.setTime( dateNoteCreated );
                    calendar.add( Calendar.SECOND, 1 );
                    dateNoteCreated = calendar.getTime();
                    timeDifference = dateNoteIsToBeDeleted.getTime() - dateNoteCreated.getTime();
                    daysRemaining = timeDifference / ( 24 * 60 * 60 * 1000 );
                    hoursRemaining = timeDifference / ( 60 * 60 * 1000 );
                    minutesRemaining = timeDifference / ( 60 * 1000 ) % 60;
                    secondsRemaining = timeDifference / 1000 % 60;

                    timeRemaining = String.format("%s days %s hours %s minutes %s seconds",
                            daysRemaining,
                            hoursRemaining,
                            minutesRemaining,
                            secondsRemaining);
                    newMessage = new Message();
                    newBundle.clear();
                    newBundle.putString( TIME_REMAINING, timeRemaining );
                    newMessage.setData( newBundle );
                    handler.sendMessage( newMessage );
                    try {
                        Thread.sleep( 1000 );
                    } catch ( InterruptedException e ) {}

                }
            }
        };
        return runnable;
    }

    private void populateOtherFragmentViews( Note note ) {
        isEditing = true;
        binding.noteTitle.setText( note.getTitle() );
        binding.addNoteEditText.setText( note.getDescription() );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentIsAlive = false;
        if ( executor != null )
            executor.interrupt();
        executor = null;
    }
}
