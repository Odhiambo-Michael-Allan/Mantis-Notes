package com.mantis.MantisNotesIterationOne.data.source;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mantis.MantisNotesIterationOne.data.source.local.Note;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.NoteReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class FakeDataSource implements NoteDataSource {

    private ArrayList<Note> notesTable = new ArrayList<>();
    private MutableLiveData<List<Note>> observableNotesTable = new MutableLiveData<>();
    private ArrayList<NoteReference> homeNotesReferenceTable = new ArrayList<>();
    private MutableLiveData<List<NoteReference>> observableHomeNotesReferenceTable = new MutableLiveData<>();
    private ArrayList<NoteReference> frequentNotesReferenceTable = new ArrayList<>();
    private MutableLiveData<List<NoteReference>> observableFrequentNotesReferenceTable = new MutableLiveData<>();
    private ArrayList<NoteReference> archiveNotesReferenceTable = new ArrayList<>();
    private MutableLiveData<List<NoteReference>> observableArchiveNotesReferenceTable = new MutableLiveData<>();
    private ArrayList<NoteReference> trashNotesReferenceTable = new ArrayList<>();
    private MutableLiveData<List<NoteReference>> observableTrashNotesReferenceTable = new MutableLiveData<>();

    private int noteCount = 0, homeNoteReferenceCount = 0, frequentNoteReferenceCount = 0,
            archiveNoteReferenceCount = 0, trashNoteReferenceCount = 0;

    private int ascending = 0, sortingStrategy = 0, layoutType = 0;
    private MutableLiveData<Integer> observableAscendingConfig = new MutableLiveData<>();
    private MutableLiveData<Integer> observableSortingStrategyConfig = new MutableLiveData<>();
    private MutableLiveData<Integer> observableLayoutTypeConfig = new MutableLiveData<>();

    public FakeDataSource() {
        observableNotesTable.postValue( notesTable );
        observableHomeNotesReferenceTable.postValue( homeNotesReferenceTable );
        observableFrequentNotesReferenceTable.postValue( frequentNotesReferenceTable );
        observableArchiveNotesReferenceTable.postValue( archiveNotesReferenceTable );
        observableTrashNotesReferenceTable.postValue( trashNotesReferenceTable );
    }

    @Override
    public LiveData<List<Note>> getNotesById( int[] ids ) {
        ArrayList<Note> notesWithGivenIds = new ArrayList<>();
        Iterator i = notesTable.iterator();
        while ( i.hasNext() ) {
            Note note_i = ( Note ) i.next();
            for ( int j = 0; j < ids.length; j++ ) {
                if ( note_i.getId() == ids[ j ] )
                    notesWithGivenIds.add( note_i );
            }
        }
        MutableLiveData<List<Note>> results = new MutableLiveData<>();
        results.setValue( notesWithGivenIds );
        return results;
    }

    @Override
    public LiveData<List<Note>> getAllNotes() {
        return observableNotesTable;
    }

    @Override
    public void insertNote( Note note ) {
        notesTable.add( note );
        note.setId( noteCount++ );
        observableNotesTable.postValue( notesTable );
    }

    @Override
    public void deleteNote( int noteId ) {
        Iterator i = notesTable.iterator();
        while ( i.hasNext() ) {
            Note note = ( Note ) i.next();
            if ( note.getId() == noteId )
                i.remove();
        }
        observableNotesTable.postValue( notesTable );
    }

    @Override
    public void deleteAllNotes() {
        notesTable.clear();
        observableNotesTable.postValue( notesTable );
    }

    @Override
    public void updateNote( int noteId, String newTitle, String newDescription, Date dateLastModified, int newAccessCount ) {
        Iterator i = notesTable.iterator();
        while ( i.hasNext() ) {
            Note note = ( Note ) i.next();
            if ( note.getId() == noteId ) {
                note.setTitle( newTitle );
                note.setDescription( newDescription );
                note.setDateLastModified( dateLastModified );
                note.setAccessCount( newAccessCount );
            }
        }
    }

    @Override
    public void updateNoteOwner( int noteId, int newOwner ) {
        Iterator i = notesTable.iterator();
        while ( i.hasNext() ) {
            Note note = ( Note ) i.next();
            if ( note.getId() == noteId )
                note.setOwner( newOwner );
        }
    }

    @Override
    public LiveData<List<NoteReference>> getHomeNotesReferences() {
        return observableHomeNotesReferenceTable;
    }

    @Override
    public void insertHomeNoteReference( NoteReference noteReference ) {
        noteReference.setId( homeNoteReferenceCount++ );
        homeNotesReferenceTable.add( noteReference );
        observableHomeNotesReferenceTable.postValue(homeNotesReferenceTable);
    }

    @Override
    public void deleteHomeNoteReference( int referenceId ) {
        Iterator i = homeNotesReferenceTable.iterator();
        while ( i.hasNext() ) {
            NoteReference noteReference = ( NoteReference ) i.next();
            if ( noteReference.getId() == referenceId )
                i.remove();
        }
        observableHomeNotesReferenceTable.postValue(homeNotesReferenceTable);
    }

    @Override
    public void deleteAllHomeNotesReferences() {
        homeNotesReferenceTable.clear();
        observableHomeNotesReferenceTable.postValue(homeNotesReferenceTable);
    }

    @Override
    public void deleteFrequentNoteReference( int referenceId ) {
        Iterator i = frequentNotesReferenceTable.iterator();
        while ( i.hasNext() ) {
            NoteReference noteReference = ( NoteReference ) i.next();
            if ( noteReference.getId() == referenceId )
                i.remove();
        }
        observableFrequentNotesReferenceTable.postValue( frequentNotesReferenceTable );
    }

    @Override
    public void deleteAllFrequentNoteReferences() {
        frequentNotesReferenceTable.clear();
        observableFrequentNotesReferenceTable.postValue( frequentNotesReferenceTable );
    }

    @Override
    public void insertFrequentNoteReference( NoteReference noteReference ) {
        frequentNotesReferenceTable.add( noteReference );
        noteReference.setId( frequentNoteReferenceCount++ );
        observableFrequentNotesReferenceTable.postValue( frequentNotesReferenceTable );
    }

    @Override
    public LiveData<List<NoteReference>> getFrequentNoteReferences() {
        return observableFrequentNotesReferenceTable;
    }

    @Override
    public void insertArchiveNoteReference( NoteReference noteReference ) {
        archiveNotesReferenceTable.add( noteReference );
        noteReference.setId( archiveNoteReferenceCount++ );
        observableArchiveNotesReferenceTable.postValue( archiveNotesReferenceTable );
    }

    @Override
    public LiveData<List<NoteReference>> getArchiveNotesReferences() {
        return observableArchiveNotesReferenceTable;
    }

    @Override
    public void deleteArchiveNoteReference( int referenceId ) {
        Iterator i = archiveNotesReferenceTable.iterator();
        while ( i.hasNext() ) {
            NoteReference noteReference = ( NoteReference ) i.next();
            if ( noteReference.getId() == referenceId )
                i.remove();
        }
        observableArchiveNotesReferenceTable.postValue( archiveNotesReferenceTable );
    }

    @Override
    public void deleteAllArchiveNotesReferences() {
        archiveNotesReferenceTable.clear();
        observableArchiveNotesReferenceTable.postValue( archiveNotesReferenceTable );
    }

    @Override
    public void insertTrashNoteReference( NoteReference reference ) {
        trashNotesReferenceTable.add( reference );
        reference.setId( trashNoteReferenceCount++ );
        observableTrashNotesReferenceTable.postValue( trashNotesReferenceTable );
    }

    @Override
    public LiveData<List<NoteReference>> getTrashNotesReferences() {
        return observableTrashNotesReferenceTable;
    }

    @Override
    public void deleteTrashNoteReference( int referenceId ) {
        Iterator i = trashNotesReferenceTable.iterator();
        while ( i.hasNext() ) {
            NoteReference noteReference = ( NoteReference ) i.next();
            if ( noteReference.getId() == referenceId )
                i.remove();
        }
        observableTrashNotesReferenceTable.postValue( trashNotesReferenceTable );
    }

    @Override
    public void deleteAllTrashNotesReferences() {
        trashNotesReferenceTable.clear();
        observableTrashNotesReferenceTable.postValue( trashNotesReferenceTable );
    }

    @Override
    public LiveData<Integer> getAscending() {
        return observableAscendingConfig;
    }

    @Override
    public LiveData<Integer> getSortingStrategy() {
        return observableSortingStrategyConfig;
    }

    @Override
    public LiveData<Integer> getLayoutType() {
        return observableLayoutTypeConfig;
    }

    @Override
    public void updateLayoutTypeConfig( int newLayoutTypeConfig ) {
        observableLayoutTypeConfig.postValue( newLayoutTypeConfig );
    }
}
