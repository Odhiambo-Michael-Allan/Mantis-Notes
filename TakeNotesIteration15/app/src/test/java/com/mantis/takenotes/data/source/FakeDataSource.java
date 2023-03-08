package com.mantis.takenotes.data.source;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.mantis.takenotes.Repository.Note;

import com.mantis.takenotes.data.source.local.Configuration;
import com.mantis.takenotes.data.source.local.NoteEntity;
import com.mantis.takenotes.data.source.local.Query;

import java.util.ArrayList;
import java.util.Date;

import java.util.Iterator;
import java.util.List;

public class FakeDataSource implements NoteDataSource {

    private final ArrayList<NoteEntity> notesTable = new ArrayList<>();
    private final MutableLiveData<List<NoteEntity>> observableNotesTable = new MutableLiveData<>();

    private int noteCount = 0;
    private final int frequentNoteReferenceCount = 0;
    private final int archiveNoteReferenceCount = 0;
    private final int trashNoteReferenceCount = 0;

    private final int ascending = 0;
    private final int sortingStrategy = 0;
    private final int layoutType = 0;
    private final MutableLiveData<Integer> observableAscendingConfig = new MutableLiveData<>();
    private final MutableLiveData<Integer> observableSortingStrategyConfig = new MutableLiveData<>();
    private final MutableLiveData<Integer> observableLayoutTypeConfig = new MutableLiveData<>();

    public FakeDataSource() {
        observableNotesTable.postValue( notesTable );
    }

    @Override
    public LiveData<List<NoteEntity>> getNotesById( int[] ids ) {
        ArrayList<NoteEntity> notesWithGivenIds = new ArrayList<>();
        Iterator i = notesTable.iterator();
        while ( i.hasNext() ) {
            NoteEntity note_i = ( NoteEntity ) i.next();
            for ( int j = 0; j < ids.length; j++ ) {
                if ( note_i.id == ids[ j ] )
                    notesWithGivenIds.add( note_i );
            }
        }
        MutableLiveData<List<NoteEntity>> results = new MutableLiveData<>();
        results.setValue( notesWithGivenIds );
        return results;
    }

    @Override
    public LiveData<List<NoteEntity>> getAllNotes() {
        return observableNotesTable;
    }

    @Override
    public void insertNote( NoteEntity noteEntity ) {
        noteEntity.id = noteCount++;
        notesTable.add( noteEntity );
        observableNotesTable.postValue( notesTable );
    }

    @Override
    public void deleteNote( int noteId ) {
        Iterator i = notesTable.iterator();
        while ( i.hasNext() ) {
            NoteEntity note = ( NoteEntity ) i.next();
            if ( note.id == noteId )
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
    public void updateNote( int noteId, String newTitle, String newDescription,
                            Date dateLastModified, int newAccessCount ) {
        Iterator i = notesTable.iterator();
        while ( i.hasNext() ) {
            NoteEntity note = ( NoteEntity ) i.next();
            if ( note.id == noteId ) {
                note.title = newTitle;
                note.description = newDescription;
                note.dateLastModified = dateLastModified;
                note.accessCount = newAccessCount;
            }
        }
        observableNotesTable.postValue( notesTable );
    }

    @Override
    public void updateNoteOwner( int noteId, int newOwner ) {
        Iterator i = notesTable.iterator();
        while ( i.hasNext() ) {
            NoteEntity note = ( NoteEntity ) i.next();
            if ( note.id == noteId )
                note.owner = newOwner;
        }
        observableNotesTable.postValue( notesTable );
    }

    @Override
    public void saveTimeLeft( int noteId, long timeLeft ) {
    }

    @Override
    public void setDateNoteWasLastDeleted( int noteId, Date date ) {

    }

    @Override
    public void updateAccessCount( int noteId, int accessCount ) {
        for ( NoteEntity note : notesTable )
            if ( note.id == noteId )
                note.accessCount = accessCount;
    }

    @Override
    public void insertConfiguration( Configuration configuration ) {

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

    @Override
    public void updateAscendingConfig( int newAscendingConfig ) {

    }

    @Override
    public void updateSortingStrategyConfig( int newSortingStrategyConfig ) {

    }

    @Override
    public void insertQuery(Query query) {

    }

    @Override
    public void deleteQuery(int queryId) {

    }

    @Override
    public LiveData<List<Query>> getQueries() {
        return null;
    }
}
