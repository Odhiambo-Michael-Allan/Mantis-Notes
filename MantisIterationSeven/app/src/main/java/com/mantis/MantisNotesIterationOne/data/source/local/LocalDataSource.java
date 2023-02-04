package com.mantis.MantisNotesIterationOne.data.source.local;

import androidx.lifecycle.LiveData;

import com.mantis.MantisNotesIterationOne.Logger;
import com.mantis.MantisNotesIterationOne.data.source.NoteDataSource;
import com.mantis.MantisNotesIterationOne.data.source.local.Dao.ArchiveNotesDao;
import com.mantis.MantisNotesIterationOne.data.source.local.Dao.FrequentNotesDao;
import com.mantis.MantisNotesIterationOne.data.source.local.Dao.HomeNotesDao;
import com.mantis.MantisNotesIterationOne.data.source.local.Dao.NoteDao;
import com.mantis.MantisNotesIterationOne.data.source.local.Dao.TrashNotesDao;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.ArchiveNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.FrequentNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.HomeNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.NoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.TrashNoteReference;

import java.util.List;

public class LocalDataSource implements NoteDataSource {

    private NoteDao noteDao;
    private HomeNotesDao homeNotesDao;
    private FrequentNotesDao frequentNotesDao;
    private ArchiveNotesDao archiveNotesDao;
    private TrashNotesDao trashNotesDao;

    public LocalDataSource( NoteDao noteDao, HomeNotesDao homeNotesDao, FrequentNotesDao
                            frequentNotesDao, ArchiveNotesDao archiveNotesDao,
                            TrashNotesDao trashNotesDao ) {
        this.noteDao = noteDao;
        this.homeNotesDao = homeNotesDao;
        this.frequentNotesDao = frequentNotesDao;
        this.archiveNotesDao = archiveNotesDao;
        this.trashNotesDao = trashNotesDao;
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
        Logger.log( "INSERTING NOTE TO DATABASE" );
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
    public LiveData<List<NoteReference>> getHomeNotesReferences() {
        return homeNotesDao.getHomeNotes();
    }

    @Override
    public void insertHomeNoteReference( NoteReference homeNoteReference ) {
        Logger.log( "INSERTING HOME NOTE REFERENCE TO DATABASE" );
        homeNotesDao.insertHomeNote( ( HomeNoteReference ) homeNoteReference );
    }

    @Override
    public void deleteHomeNoteReference( int referenceId ) {
        homeNotesDao.deleteHomeNote( referenceId );
    }

    @Override
    public void deleteAllHomeNotesReferences() {
        homeNotesDao.deleteAllHomeNotes();
    }

    @Override
    public void deleteFrequentNoteReference(int referenceId) {
        frequentNotesDao.deleteFrequentNote(referenceId);
    }

    @Override
    public void deleteAllFrequentNoteReferences() {
        frequentNotesDao.deleteAllFrequentNotes();
    }

    @Override
    public void insertFrequentNoteReference(NoteReference reference ) {
        frequentNotesDao.insertFrequentNote( ( FrequentNoteReference ) reference );
    }

    @Override
    public LiveData<List<NoteReference>> getFrequentNoteReferences() {
        return frequentNotesDao.getFrequentNotes();
    }

    @Override
    public void insertArchiveNoteReference(NoteReference reference ) {
        archiveNotesDao.insertArchiveNote( ( ArchiveNoteReference ) reference );
    }

    @Override
    public LiveData<List<NoteReference>> getArchiveNotesReferences() {
        return archiveNotesDao.getArchivedNotes();
    }

    @Override
    public void deleteArchiveNoteReference(int noteId ) {
        archiveNotesDao.deleteArchiveNote( noteId );
    }

    @Override
    public void deleteAllArchiveNotesReferences() {
        archiveNotesDao.deleteAllArchivedNotes();
    }

    @Override
    public void insertTrashNoteReference(NoteReference reference ) {
        trashNotesDao.insertTrashNote( ( TrashNoteReference ) reference );
    }

    @Override
    public LiveData<List<NoteReference>> getTrashNotesReferences() {
        return trashNotesDao.getTrashNotes();
    }

    @Override
    public void deleteTrashNoteReference(int noteId ) {
        trashNotesDao.deleteTrashNote( noteId );
    }

    @Override
    public void deleteAllTrashNotesReferences() {
        trashNotesDao.deleteAllTrashNotes();
    }
}
