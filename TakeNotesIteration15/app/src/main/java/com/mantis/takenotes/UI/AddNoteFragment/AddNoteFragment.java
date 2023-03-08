package com.mantis.takenotes.UI.AddNoteFragment;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mantis.takenotes.Commands.ShareCommand;
import com.mantis.takenotes.Models.NotesViewModel;

import com.mantis.takenotes.Models.NotesViewModelFactory;
import com.mantis.takenotes.R;
import com.mantis.takenotes.Utils.DateProvider;

import com.mantis.takenotes.Utils.Logger;
import com.mantis.takenotes.Utils.ToastProvider;
import com.mantis.takenotes.Repository.NoteRepository;

import com.mantis.takenotes.Repository.NoteRepositoryImpl;
import com.mantis.takenotes.Repository.Note;
import com.mantis.takenotes.databinding.TakeNotesFragmentAddNoteBinding;

import java.util.Arrays;
import java.util.Date;

import java.util.UUID;

public class AddNoteFragment extends Fragment {

//    public static final int HOME_FRAGMENT = 1;
//    public static final int FREQUENT_FRAGMENT = 2;
//    public static final int ARCHIVE_FRAGMENT = 3;
//    public static final int TRASH_FRAGMENT = 4;
//    public static final int SEARCH_FRAGMENT = 5;
    public static final String TIME_REMAINING = "Time Remaining";
    public static final String TIME_UP = "Time up";
    private static final int MAX_CHARACTERS_ALLOWED_IN_TITLE = 2000;

    private TakeNotesFragmentAddNoteBinding binding;
    private NotesViewModel notesViewModel;
    private NavController controller;
    private boolean isEditing;
    private int noteId;
    //private int fragmentNavigatedFrom;
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
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        binding = TakeNotesFragmentAddNoteBinding.inflate( inflater, container, false );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ) {
        setupNotesViewModel();
        setupToolbar();
        observeInputFields();
        showTrashFragmentComponents();
    }

    private void setupNotesViewModel() {
        NoteRepository noteRepository = NoteRepositoryImpl.getRepository(
                this.getActivity().getApplication() );
        NotesViewModelFactory factory = new NotesViewModelFactory( noteRepository );
        notesViewModel = new ViewModelProvider( requireActivity(), factory ).get(
                NotesViewModel.class );
        getNoteSelected();
    }

    private void getNoteSelected() {
        if ( noteId == -1 )  // If this condition is true, then we know for sure the user is creating a new note..
            return;
        // At this point, we know the user is editing an existing note..
        noteSelected = notesViewModel.getNoteWithId( noteId );
    }

    private void setupToolbar() {
        controller = NavHostFragment.findNavController( this );
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder( controller.getGraph() ).build();
        NavigationUI.setupWithNavController( binding.topToolbar, controller, appBarConfiguration );
        binding.topToolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                handleBackNavigation();
            }
        } );
        binding.topToolbar.setTitle( "" );
        binding.topToolbar.inflateMenu( R.menu.take_notes_add_note_fragment_options_menu );
        attachListenersToMenuItems();
    }

    private void attachListenersToMenuItems() {
        attachListenerToDeleteOption();
        attachListenerToShareOption();
        attachListenerToRestoreOption();
    }

    private void attachListenerToDeleteOption() {
        MenuItem deleteMenuItem = binding.topToolbar.getMenu().findItem( R.id.delete_option );
        deleteMenuItem.setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick( @NonNull MenuItem menuItem ) {
                if ( noteSelected != null )
                    noteSelected.delete( notesViewModel.getNoteRepository() );
                controller.navigateUp();
                return true;
            }
        } );
    }

    private void attachListenerToShareOption() {
        MenuItem shareMenuItem = binding.topToolbar.getMenu().findItem( R.id.share_option );
        shareMenuItem.setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick( @NonNull MenuItem menuItem ) {
                Note note = new Note( binding.noteTitleEditText.getText().toString(),
                        binding.noteDescriptionEditText.getText().toString(), new Date(),
                        NotesViewModel.HOME_FRAGMENT );
                if ( note.getTitle().equals( "" ) && note.getDescription().equals( "" ) ) {
                    ToastProvider.showToast( getContext(), getString( R.string.cannot_send_empty_message ) );
                    return true;
                }
                new ShareCommand( getContext(), Arrays.asList( new Note[] { note } ),
                        notesViewModel ).execute();
                return true;
            }
        } );
    }

    private void attachListenerToRestoreOption() {
        MenuItem restoreMenuItem = binding.topToolbar.getMenu().findItem( R.id.restore_option );
        restoreMenuItem.setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick( @NonNull MenuItem menuItem ) {
                noteSelected.restore( notesViewModel.getNoteRepository() );
                controller.navigateUp();
                return true;
            }
        } );
    }


    private void observeInputFields() {
        observeTitleEditText();
        observeDescriptionEditText();
    }

    private void observeTitleEditText() {
        observeNoteTitleEditTextFocusChange();
        observeNoteTitleTextChange();
    }

    private void observeNoteTitleEditTextFocusChange() {
        binding.noteTitleEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange( View view, boolean isFocused ) {
                if ( isFocused ) {
                    binding.textLimitCardView.setVisibility(View.VISIBLE);
                    binding.noteTextLimit.setText( getString( R.string.note_title_text_limit, binding.noteTitleEditText.getText().length() ) );
                }
                else
                    binding.textLimitCardView.setVisibility( View.GONE );
            }
        } );
    }

    private void observeNoteTitleTextChange() {
        binding.noteTitleEditText.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged( CharSequence charSequence, int i, int i1, int i2 ) {}

            @Override
            public void onTextChanged( CharSequence charSequence, int i, int i1, int i2 ) {
                if ( charSequence.length() > MAX_CHARACTERS_ALLOWED_IN_TITLE ) {
                    binding.noteTitleEditText.getText().delete( charSequence.length() - 1, charSequence.length() );
                    ToastProvider.showToast( getContext(), getString( R.string.title_too_long ) );
                }
                else
                    binding.noteTextLimit.setText( getString( R.string.note_title_text_limit, charSequence.length() ) );
            }

            @Override
            public void afterTextChanged( Editable editable ) {}
        } );
    }

    private void observeDescriptionEditText() {
        observeNoteDescriptionFocusChange();
        observeNoteDescriptionTextChange();
    }

    private void observeNoteDescriptionFocusChange() {
        binding.noteDescriptionEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused ) {
                if ( isFocused ) {
                    binding.textLimitCardView.setVisibility(View.VISIBLE);
                    binding.noteTextLimit.setText( getString( R.string.note_description_text_limit, binding.noteDescriptionEditText.getText().length() ) );
                }
                else
                    binding.textLimitCardView.setVisibility( View.GONE );
            }
        } );
    }

    private void observeNoteDescriptionTextChange() {
        binding.noteDescriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.noteTextLimit.setText( getString( R.string.note_description_text_limit, charSequence.length() ) );
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void showTrashFragmentComponents() {
        if ( noteSelected != null && noteSelected.getOwner() == NotesViewModel.TRASH_FRAGMENT ) {
            showTrashFragmentViews();
            showShareOption( false );
            return;
        }
        showShareOption( true );

    }

    private void showTrashFragmentViews() {
        binding.noteTitleEditText.setVisibility( View.GONE );
        binding.noteDescriptionEditText.setVisibility( View.GONE );
        binding.trashFragmentBottomBanner.setVisibility( View.VISIBLE );
        binding.trashNoteTitleTextView.setVisibility( View.VISIBLE );
        binding.trashNoteDescriptionTextView.setVisibility( View.VISIBLE );
        binding.trashContentBottomDivider.setVisibility( View.VISIBLE );
    }

    private void showShareOption( boolean show ) {
        MenuItem shareMenuItem = binding.topToolbar.getMenu().findItem( R.id.share_option );
        shareMenuItem.setVisible( show );
        MenuItem restoreMenuItem = binding.topToolbar.getMenu().findItem( R.id.restore_option );
        restoreMenuItem.setVisible( !show );
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
        populateViews( noteSelected );
        if ( noteSelected.getOwner() == NotesViewModel.TRASH_FRAGMENT ) {
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
        String newTitle = binding.noteTitleEditText.getText().toString();
        String newDescription = binding.noteDescriptionEditText.getText().toString();
        notesViewModel.getNoteWithId( noteId ).edit( newTitle, newDescription,
                notesViewModel.getNoteRepository() );
    }

    private void addNoteToModel() {
        String noteTitle = binding.noteTitleEditText.getText().toString();
        String noteDescription = binding.noteDescriptionEditText.getText().toString();
        Note note = new Note( noteTitle, noteDescription, new Date(),
                NotesViewModel.HOME_FRAGMENT );
        note.save( this.notesViewModel.getNoteRepository() );
    }

    private void populateViews( Note note ) {
        if ( note.getOwner() == NotesViewModel.TRASH_FRAGMENT )
            populateTrashFragmentViews( note );
        else
            populateOtherFragmentViews( note );
    }

    private void populateTrashFragmentViews( Note note ) {
        binding.trashNoteTitleTextView.setText( note.getTitle() );
        binding.trashNoteDescriptionTextView.setText( note.getDescription() );
    }

    private void populateOtherFragmentViews( Note note ) {
        isEditing = true;
        binding.noteTitleEditText.setText( note.getTitle() );
        binding.noteDescriptionEditText.setText( note.getDescription() );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.log( "FRAGMENT IS BEING DESTROYED" );
        if ( noteSelected != null && noteSelected.getOwner() == NotesViewModel.TRASH_FRAGMENT )
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
        long hoursRemaining = timeLeft / ( 60 * 60 * 1000 ) % 24;
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
