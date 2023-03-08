package com.mantis.takenotes.data.source;

import androidx.lifecycle.LiveData;

import com.mantis.takenotes.Repository.NoteRepository;
import com.mantis.takenotes.data.source.local.Configuration;
import com.mantis.takenotes.Repository.Note;
import com.mantis.takenotes.data.source.local.NoteEntity;
import com.mantis.takenotes.data.source.local.Query;

import java.util.Date;
import java.util.List;

public class FakeRepository implements NoteRepository {

    private NoteDataSource noteDataSource = new FakeDataSource();

    public FakeRepository() {}

    @Override
    public void insertNote( NoteEntity noteEntity ) {
        noteDataSource.insertNote( noteEntity );
    }

    @Override
    public LiveData<List<NoteEntity>> getNotesById( int[] ids ) {
        return noteDataSource.getNotesById( ids );
    }

    @Override
    public LiveData<List<NoteEntity>> getAllNotes() {
        return noteDataSource.getAllNotes();
    }

    @Override
    public void deleteNote( int noteId ) {
        noteDataSource.deleteNote( noteId );
    }

    @Override
    public void deleteAllNotes() {
        noteDataSource.deleteAllNotes();
    }

    @Override
    public void updateNote( int noteId, String newTitle, String newDescription,
                            Date dateLastModified, int newAccessCount ) {
        noteDataSource.updateNote( noteId, newTitle, newDescription, dateLastModified,
                newAccessCount );
    }

    @Override
    public void updateNoteOwner( int noteId, int newOwner ) {
        noteDataSource.updateNoteOwner( noteId, newOwner );
    }

    @Override
    public void saveTimeLeft( int noteId, long timeLeft ) {
        noteDataSource.saveTimeLeft( noteId, timeLeft );
    }

    @Override
    public void setDateNoteWasLastDeleted( int noteId, Date date ) {
    }

    @Override
    public void updateAccessCount( int noteId, int accessCount ) {
        noteDataSource.updateAccessCount( noteId, accessCount );
    }

    @Override
    public void insertConfiguration( Configuration configuration ) {

    }

    @Override
    public LiveData<Integer> getAscending() {
        return noteDataSource.getAscending();
    }

    @Override
    public LiveData<Integer> getSortingStrategy() {
        return noteDataSource.getSortingStrategy();
    }

    @Override
    public LiveData<Integer> getLayoutType() {
        return noteDataSource.getLayoutType();
    }

    @Override
    public void updateLayoutTypeConfig( int newLayoutTypeConfig ) {
        noteDataSource.updateLayoutTypeConfig( newLayoutTypeConfig );
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
