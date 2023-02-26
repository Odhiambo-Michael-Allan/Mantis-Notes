package com.mantis.takenotes.data.source.local;

import androidx.lifecycle.LiveData;
import com.mantis.takenotes.data.source.NoteDataSource;
import com.mantis.takenotes.data.source.local.Dao.ConfigurationsDao;

import com.mantis.takenotes.data.source.local.Dao.NoteDao;
import com.mantis.takenotes.data.source.local.Dao.QueryDao;

import java.util.Date;
import java.util.List;

public class LocalDataSource implements NoteDataSource {

    private NoteDao noteDao;
    private ConfigurationsDao configurationsDao;
    private QueryDao queryDao;

    public LocalDataSource( NoteDao noteDao, ConfigurationsDao configurationsDao, QueryDao queryDao ) {
        this.noteDao = noteDao;
        this.configurationsDao = configurationsDao;
        this.queryDao = queryDao;
    }

    @Override
    public LiveData<List<Note>> getNotesById(int[] ids ) {
        return noteDao.getNotesById( ids );
    }

    @Override
    public LiveData<List<Note>> getAllNotes() {
        return noteDao.getAllNotes();
    }

    @Override
    public void insertNote( Note note ) {
        noteDao.insertNote( note );
    }

    @Override
    public void deleteNote( int noteId ) {
        noteDao.deleteNote( noteId );
    }

    @Override
    public void deleteAllNotes() {
        noteDao.deleteAll();
    }

    @Override
    public void updateNote( int noteId, String newTitle, String newDescription,
                            Date dateLastModified, int newAccessCount ) {
        noteDao.updateNote( noteId, newTitle, newDescription, dateLastModified, newAccessCount );
    }

    @Override
    public void updateNoteOwner(int noteId, int newOwner) {
        noteDao.updateNoteOwner( noteId, newOwner );
    }

    @Override
    public void saveTimeLeft( int noteId, long timeLeft ) {
        noteDao.saveTimeLeft( noteId, timeLeft );
    }

    @Override
    public void setDateNoteWasLastDeleted( int noteId, Date date ) {
        noteDao.setDateNoteWasLastDeleted( noteId, date );
    }

    @Override
    public void updateAccessCount( int noteId, int accessCount ) {
        noteDao.updateAccessCount( noteId, accessCount );
    }

    @Override
    public void insertConfiguration( Configuration configuration ) {
        configurationsDao.insertConfiguration( configuration );
    }

    @Override
    public LiveData<Integer> getAscending() {
        return configurationsDao.getAscending();
    }

    @Override
    public LiveData<Integer> getSortingStrategy() {
        return configurationsDao.getSortingStrategy();
    }

    @Override
    public LiveData<Integer> getLayoutType() {
        return configurationsDao.getLayoutType();
    }

    @Override
    public void updateLayoutTypeConfig( int newLayoutTypeConfig ) {
        configurationsDao.updateLayoutTypeConfig( newLayoutTypeConfig );
    }

    @Override
    public void updateAscendingConfig( int newAscendingConfig ) {
        configurationsDao.updateAscendingConfig( newAscendingConfig );
    }

    @Override
    public void updateSortingStrategyConfig( int newSortingStrategyConfig ) {
        configurationsDao.updateSortingStrategyConfig( newSortingStrategyConfig );
    }

    @Override
    public void insertQuery( Query query ) {
        queryDao.insert( query );
    }

    @Override
    public void deleteQuery( int queryId ) {
        queryDao.deleteQuery( queryId );
    }

    @Override
    public LiveData<List<Query>> getQueries() {
        return queryDao.getQueries();
    }
}
