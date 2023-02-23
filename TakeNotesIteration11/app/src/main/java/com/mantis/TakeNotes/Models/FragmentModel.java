package com.mantis.TakeNotes.Models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.mantis.TakeNotes.data.source.NoteRepository;
import com.mantis.TakeNotes.data.source.local.Note;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class FragmentModel {

    protected NotesViewModel notesViewModel;
    protected NoteRepository noteRepository;
    private MutableLiveData<List<Note>> observableNotesList = new MutableLiveData<>();
    private Observer notesListObserver;
    protected List<Note> cachedNotesList = new ArrayList<>();

    public FragmentModel( NotesViewModel notesViewModel, NoteRepository noteRepository ) {
        this.notesViewModel = notesViewModel;
        this.noteRepository = noteRepository;
        initializeObservers();
        attachObservers();
    }

    private void initializeObservers() {
        notesListObserver = new Observer() {
            @Override
            public void onChanged( Object o ) {
                cachedNotesList = ( List<Note> ) o;
                updateMyNotes();
            }
        };
    }

    protected void updateMyNotes() {
        List<Note> myNotes = new ArrayList<>();
        Iterator i = cachedNotesList.iterator();
        while ( i.hasNext() ) {
            Note note = (Note) i.next();
            if ( noteIsMine( note ) )
                myNotes.add( note );
        }
        observableNotesList.postValue( myNotes );
    }

    private void attachObservers() {
        notesViewModel.getObservableNotesList().observeForever( notesListObserver );
    }

    protected abstract boolean noteIsMine( Note note );

    public void addNote( Note note ) {
        note.setOwner( getMyId() );
        this.noteRepository.updateNoteOwner( note.getId(), getMyId() );
    }

    protected abstract int getMyId();

    public void editNote( int noteId, Note placeHolder ) {
        if ( placeHolder.getTitle().equals( "" ) && placeHolder.getDescription().equals( "" ) ) {
            // Delete Note..
            deleteNoteWithId( noteId );
            return;
        }
        Note currentNote = getNoteWithId( noteId );
        placeHolder.setAccessCount( currentNote.getAccessCount() + 1 );
        updateNote( noteId, placeHolder );
        if ( currentNote.getAccessCount() > 3 )
            notesViewModel.addFrequentFragmentNote( currentNote );
    }

    protected Note getNoteWithId(int noteId ) {
        Iterator i = cachedNotesList.iterator();
        while ( i.hasNext() ) {
            Note note = (Note) i.next();
            if ( note.getId() == noteId )
                return note;
        }
        return null;
    }

    public void deleteNoteWithId( int noteId ) {
        Note note = getNoteWithId( noteId );
        note.setOwner( NotesViewModel.TRASH_FRAGMENT );
        this.noteRepository.updateNoteOwner( note.getId(), NotesViewModel.TRASH_FRAGMENT );
    }

    private void updateNote( int noteId, Note placeHolder ) {
        Note noteBeingUpdated = getNoteWithId( noteId );
        noteBeingUpdated.setTitle( placeHolder.getTitle() );
        noteBeingUpdated.setDescription( placeHolder.getDescription() );
        noteBeingUpdated.setDateLastModified( placeHolder.getDateCreated() );
        noteBeingUpdated.setAccessCount( placeHolder.getAccessCount() );
        noteRepository.updateNote( noteId, placeHolder.getTitle(),
                placeHolder.getDescription(), placeHolder.getDateCreated(),
                placeHolder.getAccessCount() );
    }

    public LiveData<List<Note>> getObservableNotesList() {
        return this.observableNotesList;
    }

    public void archiveNoteWithId( int noteId ) {
        Note note = getNoteWithId( noteId );
        note.setOwner( NotesViewModel.ARCHIVE_FRAGMENT );
        this.noteRepository.updateNoteOwner( noteId, NotesViewModel.ARCHIVE_FRAGMENT );
    }

    public void unarchiveNoteWithId( int noteId ) {
        Note note = getNoteWithId( noteId );
        // ToDo: Update Access Count..
        note.setOwner( NotesViewModel.HOME_FRAGMENT );
        this.noteRepository.updateNoteOwner( noteId, NotesViewModel.HOME_FRAGMENT );
    }

    public void deleteAll() {}

    public void onCleared() {
        noteRepository.getAllNotes().removeObserver( notesListObserver );
    }

}
