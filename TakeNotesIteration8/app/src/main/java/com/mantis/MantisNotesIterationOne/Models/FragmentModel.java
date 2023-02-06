package com.mantis.MantisNotesIterationOne.Models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.mantis.MantisNotesIterationOne.data.source.NoteRepository;
import com.mantis.MantisNotesIterationOne.data.source.local.Note;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.NoteReference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class FragmentModel {

    protected NotesViewModel notesViewModel;
    protected NoteRepository noteRepository;
    private MutableLiveData<List<Note>> observableNotesList = new MutableLiveData<>();
    private Observer notesListObserver, referenceTableObserver;
    private List<NoteReference> referenceList = new ArrayList<>();

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
                List<Note> notesList = ( List<Note> ) o;
                syncWith( notesList );
            }
        };

        referenceTableObserver = new Observer() {
            @Override
            public void onChanged( Object o ) {
                FragmentModel.this.referenceList = ( List<NoteReference> ) o;
                updateNotesListWith( referenceList );
            }
        };
    }

    private void attachObservers() {
        noteRepository.getAllNotes().observeForever( notesListObserver );
        attachReferenceTableObserver( referenceTableObserver );
    }

    protected abstract void attachReferenceTableObserver( Observer observer );

    public void addNote( int noteId ) {
        if ( noteInList( noteId ) )
            return;
        NoteReference reference = createReference( noteId );
        insertReference( reference );
    }

    protected abstract NoteReference createReference( int noteId );
    protected abstract void insertReference( NoteReference reference );

    protected NoteReference getReference( int referencedNoteId ) {
        Iterator i = referenceList.iterator();
        while ( i.hasNext() ) {
            NoteReference noteReference = ( NoteReference ) i.next();
            if ( noteReference.getNoteReferenceId() == referencedNoteId )
                return noteReference;
        }
        return null;
    }

    private void updateNotesListWith( List<NoteReference> references ) {
        int[] referencedNoteIds = new int[ references.size() ];
        int count = 0;
        Iterator i = references.iterator();
        while ( i.hasNext() ) {
            NoteReference reference = ( NoteReference ) i.next();
            referencedNoteIds[ count++ ] = reference.getNoteReferenceId();
        }
        LiveData<List<Note>> searchResults = this.noteRepository.getNotesById( referencedNoteIds );
        Observer<List<Note>> searchResultsObserver = new Observer<List<Note>>() {
            @Override
            public void onChanged( List<Note> notes ) {
                observableNotesList.postValue( notes );
                searchResults.removeObserver( this );
            }
        };
        searchResults.observeForever( searchResultsObserver );
    }

    public void editNote( int noteId, Note placeHolder ) {
        Note currentNote = notesViewModel.getNoteWithId( noteId );
        currentNote.setAccessCount( currentNote.getAccessCount() + 1 );
        if ( placeHolder.getTitle().equals( "" ) && placeHolder.getDescription().equals( "" ) ) {
            deleteReference( noteId );
            // Delete Note..
            noteRepository.deleteNote( noteId );
            return;
        }
        noteRepository.updateNote( noteId, placeHolder.getTitle(), placeHolder.getDescription(),
                placeHolder.getDateCreated(), currentNote.getAccessCount() );
        if ( currentNote.getAccessCount() > 3 )
            notesViewModel.addFrequentFragmentNote( currentNote );
    }

    public void deleteReference( int referencedNoteId ) {
        if ( !noteInList( referencedNoteId ) )
            return;
        NoteReference referenceToBeRemoved = getReference( referencedNoteId );
        deleteTableReference( referenceToBeRemoved );
    }


    protected abstract void deleteTableReference( NoteReference noteReference );

    public void syncWith( List<Note> notesList ) {
        updateNotesListWith( getReferenceList() );
    }

    protected List<NoteReference> getReferenceList() {
        if ( this.referenceList == null )
            return new ArrayList<>();
        return this.referenceList;
    }

    public boolean noteInList( int noteId ) {
        NoteReference noteReference = getReference( noteId );
        if ( noteReference == null )
            return false;
        return true;
    }

    public LiveData<List<Note>> getObservableNotesList() {
        return this.observableNotesList;
    }

    public abstract void deleteAll();

    public void onCleared() {
        noteRepository.getAllNotes().removeObserver( notesListObserver );
        removeReferenceTableObserver( referenceTableObserver );
    }

    protected abstract void removeReferenceTableObserver( Observer observer );
}
