package com.mantis.MantisNotesIterationOne;


import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mantis.MantisNotesIterationOne.Utils.DateProvider;
import com.mantis.MantisNotesIterationOne.data.source.local.Dao.ConfigurationsDao;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.ArchiveNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.Dao.ArchiveNotesDao;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.FrequentNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.Dao.FrequentNotesDao;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.HomeNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.Dao.HomeNotesDao;
import com.mantis.MantisNotesIterationOne.data.source.local.Note;
import com.mantis.MantisNotesIterationOne.data.source.local.Dao.NoteDao;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.NoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteRoomDatabase;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.TrashNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.Dao.TrashNotesDao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

@RunWith( AndroidJUnit4.class )
public class NoteRoomDatabaseTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private NoteRoomDatabase database;
    private NoteDao noteDao;
    private HomeNotesDao homeNotesDao;
    private FrequentNotesDao frequentNotesDao;
    private ArchiveNotesDao archiveNotesDao;
    private TrashNotesDao trashNotesDao;
    private ConfigurationsDao configurationsDao;
    private Note note1, note2, note3;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        // Use an in-memory database because the information stored here disappears when the
        // process is killed..
        database = Room.inMemoryDatabaseBuilder( context, NoteRoomDatabase.class )
                .allowMainThreadQueries()  // Allowing main thread queries just for testing..
                .build();
        noteDao = database.noteDao();
        homeNotesDao = database.homeNotesDao();
        frequentNotesDao = database.frequentNotesDao();
        archiveNotesDao = database.archiveNotesDao();
        trashNotesDao = database.trashNotesDao();
        configurationsDao = database.configurationsDao();
        note1 = new Note( "Note1", "This is the first note",
                DateProvider.getCurrentDate(), new Date() );
        note2 = new Note( "Note2", "This is the second note",
                DateProvider.getCurrentDate(), new Date() );
        note3 = new Note( "Note3", "This is the third note",
                DateProvider.getCurrentDate(), new Date() );
    }

    // ---------------------------- Notes Table Tests ------------------------------

    @Test
    public void testInsert() throws Exception {
        noteDao.insertNote( note1 );
        noteDao.insertNote( note2 );
        noteDao.insertNote( note3 );
        List<Note> allNotes = LiveDataTestUtil.getValue( noteDao.getAllNotes() );
        Assert.assertTrue( "Notes list size should be 3",
                allNotes.size() == 3 );
    }

    @Test
    public void testDelete() throws Exception {
        noteDao.insertNote( note1 );
        noteDao.insertNote( note2 );
        noteDao.insertNote( note3 );
        List<Note> allNotes = LiveDataTestUtil.getValue( noteDao.getAllNotes() );
        noteDao.deleteNote( allNotes.get( 1 ).getId() );
        allNotes = LiveDataTestUtil.getValue( noteDao.getAllNotes() );
        Assert.assertTrue( "Notes should be 2",
                allNotes.size() == 2 );
    }

    @Test
    public void testDeleteAll() throws Exception {
        noteDao.insertNote( note1 );
        noteDao.insertNote( note2 );
        noteDao.insertNote( note3 );
        noteDao.deleteAll();
        List<Note> allNotes = LiveDataTestUtil.getValue( noteDao.getAllNotes() );
        Assert.assertTrue( "Notes should be 0",
                allNotes.size() == 0 );
    }

    // ---------------------------- Home Table Tests -----------------------------

    @Test
    public void testInsertIntoHomeNotesTable() throws Exception {
        HomeNoteReference reference1 = new HomeNoteReference( 1 );
        HomeNoteReference reference2 = new HomeNoteReference( 2 );
        homeNotesDao.insertHomeNote( reference1 );
        homeNotesDao.insertHomeNote( reference2 );
        List<NoteReference> homeNotes = LiveDataTestUtil.getValue( homeNotesDao.getHomeNotes() );
        Assert.assertTrue( "Home notes should be 2",
                homeNotes.size() == 2 );
    }

    @Test
    public void testDeleteNoteFromHomeNotesTable() throws Exception {
        HomeNoteReference reference1 = new HomeNoteReference( 1 );
        HomeNoteReference reference2 = new HomeNoteReference( 2 );
        homeNotesDao.insertHomeNote( reference1 );
        homeNotesDao.insertHomeNote( reference2 );
        List<NoteReference> homeNotes = LiveDataTestUtil.getValue( homeNotesDao.getHomeNotes() );
        homeNotesDao.deleteHomeNote( homeNotes.get( 1 ).getId() );
        homeNotes = LiveDataTestUtil.getValue( homeNotesDao.getHomeNotes() );
        Assert.assertTrue( "Home notes should be 1",
                homeNotes.size() == 1 );
    }

    @Test
    public void testDeleteAllNotesFromHomeNotesTable() throws Exception {
        HomeNoteReference reference1 = new HomeNoteReference( 1 );
        HomeNoteReference reference2 = new HomeNoteReference( 2 );
        homeNotesDao.insertHomeNote( reference1 );
        homeNotesDao.insertHomeNote( reference2 );
        homeNotesDao.deleteAllHomeNotes();
        List<NoteReference> homeNotes = LiveDataTestUtil.getValue( homeNotesDao.getHomeNotes() );
        Assert.assertTrue( "Home notes should be 0",
                homeNotes.size() == 0 );
    }

    @Test
    public void noteInHomeNotesTableReturnsTheCorrectNoteItIsReferencingInNotesList()
            throws Exception {
        noteDao.insertNote( note1 );
        noteDao.insertNote( note2 );
        noteDao.insertNote( note3 );
        List<Note> allNotes = LiveDataTestUtil.getValue( noteDao.getAllNotes() );
        for ( int i = 0; i < allNotes.size(); i++ ) {
            HomeNoteReference reference = new HomeNoteReference( allNotes.get( i ).getId() );
            homeNotesDao.insertHomeNote( reference );
        }
        List<NoteReference> homeNotes = LiveDataTestUtil.getValue( homeNotesDao.getHomeNotes() );
        Assert.assertTrue( homeNotes.get( 0 ).getNoteReferenceId() ==
                allNotes.get( 0 ).getId() );
        Assert.assertTrue( homeNotes.get( 1 ).getNoteReferenceId() ==
                allNotes.get( 1 ).getId() );
        Assert.assertTrue( homeNotes.get( 2 ).getNoteReferenceId() ==
                allNotes.get( 2 ).getId() );
        int [] noteIds = new int[ homeNotes.size() ];
        for ( int i = 0; i < noteIds.length; i++ )
            noteIds[ i ] = homeNotes.get( i ).getNoteReferenceId();
        List<Note> referencedNotes = LiveDataTestUtil.getValue( noteDao.getNotesById( noteIds ) );
        Assert.assertTrue( referencedNotes.get( 1 ).getId() == allNotes.get( 1 ).getId() );
    }

    // -------------------------- Frequent Notes Table Tests ----------------------------
    @Test
    public void testInsertIntoFrequentNotesTable() throws Exception {
        FrequentNoteReference reference1 = new FrequentNoteReference( 1 );
        FrequentNoteReference reference2 = new FrequentNoteReference( 2 );
        frequentNotesDao.insertFrequentNote( reference1 );
        frequentNotesDao.insertFrequentNote( reference2 );
        List<NoteReference> frequentNotes = LiveDataTestUtil.getValue(
                frequentNotesDao.getFrequentNotes() );
        Assert.assertTrue( "Frequent notes should be 2",
                frequentNotes.size() == 2 );
    }

    @Test
    public void testDeleteFromFrequentNotesTable() throws Exception {
        FrequentNoteReference reference1 = new FrequentNoteReference( 1 );
        FrequentNoteReference reference2 = new FrequentNoteReference( 2 );
        frequentNotesDao.insertFrequentNote( reference1 );
        frequentNotesDao.insertFrequentNote( reference2 );
        List<NoteReference> frequentNotes = LiveDataTestUtil
                .getValue( frequentNotesDao.getFrequentNotes() );
        frequentNotesDao.deleteFrequentNote( frequentNotes.get( 1 ).getId() );
        frequentNotes = LiveDataTestUtil.getValue( frequentNotesDao.getFrequentNotes() );
        Assert.assertTrue( "Frequent notes should be 1",
                frequentNotes.size() == 1 );
    }

    @Test
    public void testDeleteAllNotesFromFrequentNotesTable() throws Exception {
        FrequentNoteReference reference1 = new FrequentNoteReference( 1 );
        FrequentNoteReference reference2 = new FrequentNoteReference( 2 );
        frequentNotesDao.insertFrequentNote( reference1 );
        frequentNotesDao.insertFrequentNote( reference2 );
        frequentNotesDao.deleteAllFrequentNotes();
        List<NoteReference> frequentNotes = LiveDataTestUtil.getValue(
                frequentNotesDao.getFrequentNotes() );
        Assert.assertTrue( "Frequent notes should be 0",
                frequentNotes.size() == 0 );
    }


    // ----------------------------- Archive Table Tests ----------------------------------

    @Test
    public void testInsertNoteInArchiveTable() throws Exception {
        ArchiveNoteReference reference1 = new ArchiveNoteReference( 1 );
        ArchiveNoteReference reference2 = new ArchiveNoteReference( 2 );
        archiveNotesDao.insertArchiveNote( reference1 );
        archiveNotesDao.insertArchiveNote( reference2 );
        List<NoteReference> archivedNotes = LiveDataTestUtil.getValue( archiveNotesDao
                .getArchivedNotes() );
        Assert.assertTrue( "Archived notes should be 2",
                archivedNotes.size() == 2 );
    }

    @Test
    public void testDeleteNoteFromArchiveTable() throws Exception {
        ArchiveNoteReference reference1 = new ArchiveNoteReference( 1 );
        ArchiveNoteReference reference2 = new ArchiveNoteReference( 2 );
        archiveNotesDao.insertArchiveNote( reference1 );
        archiveNotesDao.insertArchiveNote( reference2 );
        List<NoteReference> archivedNotes = LiveDataTestUtil.getValue(
                archiveNotesDao.getArchivedNotes() );
        archiveNotesDao.deleteArchiveNote( archivedNotes.get( 0 ).getId() );
        archivedNotes = LiveDataTestUtil.getValue( archiveNotesDao.getArchivedNotes() );
        Assert.assertTrue( "Archived notes should be 1",
                archivedNotes.size() == 1 );
    }

    @Test
    public void testDeleteAllNotesFromArchiveTable() throws Exception {
        ArchiveNoteReference reference1 = new ArchiveNoteReference( 1 );
        ArchiveNoteReference reference2 = new ArchiveNoteReference( 2 );
        archiveNotesDao.insertArchiveNote( reference1 );
        archiveNotesDao.insertArchiveNote( reference2 );
        archiveNotesDao.deleteAllArchivedNotes();
        List<NoteReference> archivedNotes = LiveDataTestUtil.getValue( archiveNotesDao
                .getArchivedNotes() );
        Assert.assertTrue( "Archived notes should be 0",
                archivedNotes.size() == 0 );
    }

    // -------------------------------- Trash Table Tests --------------------------------------
    @Test
    public void testInsertNoteIntoTrashTable() throws Exception {
        TrashNoteReference reference1 = new TrashNoteReference( 1 );
        TrashNoteReference reference2 = new TrashNoteReference( 2 );
        trashNotesDao.insertTrashNote( reference1 );
        trashNotesDao.insertTrashNote( reference2 );
        List<NoteReference> trashNotes = LiveDataTestUtil.getValue(
                trashNotesDao.getTrashNotes() );
        Assert.assertTrue( "Trashed notes should be 2",
                trashNotes.size() == 2 );
    }

    @Test
    public void testDeleteNoteFromTrashTable() throws Exception {
        TrashNoteReference reference1 = new TrashNoteReference( 1 );
        TrashNoteReference reference2 = new TrashNoteReference( 2 );
        trashNotesDao.insertTrashNote( reference1 );
        trashNotesDao.insertTrashNote( reference2 );
        List<NoteReference> trashedNotes = LiveDataTestUtil.getValue(
                trashNotesDao.getTrashNotes() );
        trashNotesDao.deleteTrashNote( trashedNotes.get( 0 ).getId() );
        trashedNotes = LiveDataTestUtil.getValue( trashNotesDao.getTrashNotes() );
        Assert.assertTrue( "Trashed notes should be 1",
                trashedNotes.size() == 1 );
    }

    @Test
    public void testDeleteAllNotesFromTrashTable() throws Exception {
        TrashNoteReference reference1 = new TrashNoteReference( 1 );
        TrashNoteReference reference2 = new TrashNoteReference( 2 );
        trashNotesDao.insertTrashNote( reference1 );
        trashNotesDao.insertTrashNote( reference2 );
        trashNotesDao.deleteAllTrashNotes();
        List<NoteReference> trashedNotes = LiveDataTestUtil.getValue(
                trashNotesDao.getTrashNotes() );
        Assert.assertTrue( "Trashed notes should be 0",
                trashedNotes.size() == 0 );
    }

}
