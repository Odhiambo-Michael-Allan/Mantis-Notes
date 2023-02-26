package com.mantis.takenotes;


import android.content.Context;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.mantis.takenotes.Utils.DateProvider;

import com.mantis.takenotes.data.source.local.Dao.ConfigurationsDao;
import com.mantis.takenotes.data.source.local.Note;
import com.mantis.takenotes.data.source.local.Dao.NoteDao;

import com.mantis.takenotes.data.source.local.NoteRoomDatabase;
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
}
