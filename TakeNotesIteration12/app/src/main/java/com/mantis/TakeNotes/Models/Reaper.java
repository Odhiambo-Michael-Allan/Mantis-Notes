package com.mantis.TakeNotes.Models;

import androidx.lifecycle.Observer;

import com.mantis.TakeNotes.Utils.Logger;
import com.mantis.TakeNotes.data.source.NoteRepository;
import com.mantis.TakeNotes.data.source.local.Note;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Reaper {

    private static final int TIME_NOTE_HAS_TO_LIVE = 2*60000;
    private FragmentModel fragmentModel;
    private Thread reaper;
    private List<Note> trashNotesList = new ArrayList<>();
    private Note.NoteObserver noteObserver;
    private boolean threadIsAlive = false;
    private NoteRepository noteRepository;

    public Reaper( FragmentModel fragmentModel, NoteRepository noteRepository ) {
        this.fragmentModel = fragmentModel;
        setupNoteObserver();
        observeTrashNotes();
        this.noteRepository = noteRepository;
    }

    private void setupNoteObserver() {
        noteObserver = new NoteObserver( UUID.randomUUID().toString() );
    }

    private void observeTrashNotes() {
        fragmentModel.getObservableNotesList().observeForever(
                new Observer<List<Note>>() {
                    @Override
                    public void onChanged( List<Note> notes ) {
                        syncIncomingNotes( notes );
                        trashNotesList = notes;
                        attachNotesListenerToAllTheNotes();
                        initializeNotesTimeLeft();
                        notifyNotesTheyAreInTrash();
                        if ( trashNotesList.isEmpty() )
                            stopReaper();
                        else
                            startReaper();
                    }
                } );
    }

    private void notifyNotesTheyAreInTrash() {
        Iterator i = trashNotesList.iterator();
        while ( i.hasNext() ) {
            Note note = ( Note ) i.next();
            note.notifyNoteItsInTheTrash( noteRepository );
        }
    }

    private void syncIncomingNotes( List<Note> incomingNotes ) {
        Iterator i = incomingNotes.iterator();
        while ( i.hasNext() ) {
            Note note = ( Note ) i.next();
            Note existingNote = getNoteIfItExists( note );
            if ( existingNote != null ) {
                note.initializeTimeLeft(existingNote.getTimeLeft());
                note.addObservers( existingNote.getObservers() );
                note.setNoteHasAlreadyBeenNotifiedItsInTrash( existingNote.getHasAlreadyBeenNotifiedItsInTrash() );
                note.setChecked( existingNote.isChecked() );
            }
        }
    }

    private Note getNoteIfItExists( Note note ) {
        Iterator i = trashNotesList.iterator();
        while ( i.hasNext() ) {
            Note currentNote = ( Note ) i.next();
            if ( currentNote.getId() == note.getId() )
                return currentNote;
        }
        return null;
    }

    private void attachNotesListenerToAllTheNotes() {
        Iterator i = trashNotesList.iterator();
        while ( i.hasNext() ) {
            Note note = ( Note ) i.next();
            note.addObserver( noteObserver );
        }
    }

    private void initializeNotesTimeLeft() {
        Iterator i = trashNotesList.iterator();
        while ( i.hasNext() ) {
            Note note = ( Note ) i.next();
            note.initializeTimeLeft( TIME_NOTE_HAS_TO_LIVE );
        }
    }

    private void stopReaper() {
        threadIsAlive = false;
        if ( reaper == null )
            return;
        reaper.interrupt();
        reaper = null;
    }

    private void startReaper() {
        if ( reaper != null )
            return;
        threadIsAlive = true;
        reaper = new Thread( createRunnable() );
        reaper.start();
    }

    private Runnable createRunnable() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while ( threadIsAlive ) {
                    decreaseNotesTimeLeftBy( noteRepository, 1000 );
                    try {
                        Thread.sleep( 1000 );
                    } catch ( InterruptedException e ) {}
                }
                Logger.log( "REAPER SHUTTING DOWN.." );
            }
        };
        return runnable;
    }

    private void decreaseNotesTimeLeftBy( NoteRepository noteRepository, int milliseconds ) {
        Iterator i = trashNotesList.iterator();
        while ( i.hasNext() ) {
            Note note = ( Note ) i.next();
            note.decreaseTimeLeftBy( 1000, noteRepository );
        }
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
            // Do Nothing;
        }

        @Override
        public void onTimeUp( int noteId ) {
            fragmentModel.deleteNoteWithId( noteId );
        }
    }

    public void onCleared( NoteRepository noteRepository ) {
        Iterator i = trashNotesList.iterator();
        while ( i.hasNext() ) {
            Note note = ( Note ) i.next();
            note.saveTimeLeftToDatabase( noteRepository );
        }
    }
}
