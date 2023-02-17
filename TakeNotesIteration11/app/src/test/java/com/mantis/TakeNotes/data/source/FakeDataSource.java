package com.mantis.TakeNotes.data.source;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mantis.TakeNotes.data.source.local.Note;
import com.mantis.TakeNotes.data.source.local.Configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class FakeDataSource implements NoteDataSource {

    private ArrayList<Note> notesTable = new ArrayList<>();
    private MutableLiveData<List<Note>> observableNotesTable = new MutableLiveData<>();

    private int noteCount = 0, homeNoteReferenceCount = 0, frequentNoteReferenceCount = 0,
            archiveNoteReferenceCount = 0, trashNoteReferenceCount = 0;

    private int ascending = 0, sortingStrategy = 0, layoutType = 0;
    private MutableLiveData<Integer> observableAscendingConfig = new MutableLiveData<>();
    private MutableLiveData<Integer> observableSortingStrategyConfig = new MutableLiveData<>();
    private MutableLiveData<Integer> observableLayoutTypeConfig = new MutableLiveData<>();

    public FakeDataSource() {
        observableNotesTable.postValue( notesTable );
    }

    @Override
    public LiveData<List<Note>> getNotesById(int[] ids ) {
        ArrayList<Note> notesWithGivenIds = new ArrayList<>();
        Iterator i = notesTable.iterator();
        while ( i.hasNext() ) {
            Note note_i = (Note) i.next();
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
            Note note = (Note) i.next();
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
    public void updateNote( int noteId, String newTitle, String newDescription,
                            Date dateLastModified, int newAccessCount ) {
        Iterator i = notesTable.iterator();
        while ( i.hasNext() ) {
            Note note = (Note) i.next();
            if ( note.getId() == noteId ) {
                note.setTitle( newTitle );
                note.setDescription( newDescription );
                note.setDateLastModified( dateLastModified );
                note.setAccessCount( newAccessCount );
            }
        }
        observableNotesTable.postValue( notesTable );
    }

    @Override
    public void updateNoteOwner( int noteId, int newOwner ) {
        Iterator i = notesTable.iterator();
        while ( i.hasNext() ) {
            Note note = (Note) i.next();
            if ( note.getId() == noteId )
                note.setOwner( newOwner );
        }
        observableNotesTable.postValue( notesTable );
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
}
