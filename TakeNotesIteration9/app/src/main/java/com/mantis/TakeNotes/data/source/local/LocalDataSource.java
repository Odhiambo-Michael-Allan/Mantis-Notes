package com.mantis.TakeNotes.data.source.local;

import androidx.lifecycle.LiveData;

import com.mantis.TakeNotes.data.source.NoteDataSource;
import com.mantis.TakeNotes.data.source.local.Dao.ArchiveNotesDao;
import com.mantis.TakeNotes.data.source.local.Dao.ConfigurationsDao;
import com.mantis.TakeNotes.data.source.local.Dao.FrequentNotesDao;
import com.mantis.TakeNotes.data.source.local.Dao.HomeNotesDao;
import com.mantis.TakeNotes.data.source.local.Dao.NoteDao;
import com.mantis.TakeNotes.data.source.local.Dao.TrashNotesDao;

import java.util.Date;
import java.util.List;

public class LocalDataSource implements NoteDataSource {

    private NoteDao noteDao;
    private HomeNotesDao homeNotesDao;
    private FrequentNotesDao frequentNotesDao;
    private ArchiveNotesDao archiveNotesDao;
    private TrashNotesDao trashNotesDao;
    private ConfigurationsDao configurationsDao;

    public LocalDataSource( NoteDao noteDao, HomeNotesDao homeNotesDao, FrequentNotesDao
                            frequentNotesDao, ArchiveNotesDao archiveNotesDao,
                            TrashNotesDao trashNotesDao, ConfigurationsDao configurationsDao ) {
        this.noteDao = noteDao;
        this.homeNotesDao = homeNotesDao;
        this.frequentNotesDao = frequentNotesDao;
        this.archiveNotesDao = archiveNotesDao;
        this.trashNotesDao = trashNotesDao;
        this.configurationsDao = configurationsDao;
    }

    @Override
    public LiveData<List<Note>> getNotesById( int[] ids ) {
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
}
