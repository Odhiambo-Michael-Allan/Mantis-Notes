package com.mantis.takenotes;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import com.mantis.takenotes.Utils.DateProvider;
import com.mantis.takenotes.Models.NotesViewModel;

import com.mantis.takenotes.data.source.FakeRepository;
import com.mantis.takenotes.Repository.Note;
import org.junit.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotesViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private NotesViewModel notesViewModel;
    private FakeRepository noteRepository;
    private Note note1, note2, note3, note4, note5, note6;

    @Before
    public void setup() {
        noteRepository = new FakeRepository();
        notesViewModel = new NotesViewModel( noteRepository );
        note1 = new Note( "Note1", "This is note 1", new Date(),
                NotesViewModel.HOME_FRAGMENT );
        note2 = new Note( "Note2", "This is note 2", new Date(),
                NotesViewModel.HOME_FRAGMENT );
        note3 = new Note( "Note3", "This is note 3", new Date(),
                NotesViewModel.HOME_FRAGMENT );
        note4 = new Note( "Note4", "This is note 4", new Date(),
                NotesViewModel.HOME_FRAGMENT );
        note5 = new Note( "Note5", "This is note 5", new Date(),
                NotesViewModel.HOME_FRAGMENT );
        note6 = new Note( "Note6", "This is note 6", new Date(),
                NotesViewModel.HOME_FRAGMENT );
    }

    // ------------------------- Home Fragment Model Tests -------------------------

    @Test
    public void testHomeNotesReferenceTableIsInitializedProperly() throws Exception {
        List<Note> homeFragmentNotesList = LiveDataTestUtil.getValue(
                notesViewModel.getHomeFragmentNotesList() );
        Assert.assertTrue( "Notes list size should be 0",
                homeFragmentNotesList.size() == 0 );
    }

    @Test
    public void testAddNote() throws Exception {
        note1.save( noteRepository );
        note2.save( noteRepository );
        note3.save( noteRepository );
        List<Note> homeFragmentNotesList = LiveDataTestUtil.getValue(
                notesViewModel.getHomeFragmentNotesList() );
        Assert.assertTrue( "Notes list size should be 3",
                homeFragmentNotesList.size() == 3 );
        Assert.assertTrue( "Note 1 owner should be home fragment",
                note1.getOwner() == NotesViewModel.HOME_FRAGMENT );
    }

    @Test
    public void testAddEmptyNote_noteIsNotAdded() throws Exception {
        note1.save( noteRepository );
        note2.save( noteRepository );
        note3.save( noteRepository );
        List<Note> homeFragmentNotesList = LiveDataTestUtil.getValue(
                notesViewModel.getHomeFragmentNotesList() );
        Assert.assertTrue( "Notes list size should be 3",
                homeFragmentNotesList.size() == 3 );
    }

    @Test
    public void testDeleteHomeFragmentNote() throws Exception {
        note1.save( noteRepository );
        note2.save( noteRepository );
        note3.save( noteRepository );
        note1.delete( noteRepository );
        List<Note> homeFragmentNotesList = LiveDataTestUtil.getValue(
                notesViewModel.getHomeFragmentNotesList() );
        Assert.assertTrue( "Notes list size should be 2",
                homeFragmentNotesList.size() == 2 );
        List<Note> trashFragmentNotesList = LiveDataTestUtil.getValue(
                notesViewModel.getTrashFragmentNotesList() );
        Assert.assertTrue( "Trashed notes should be 1",
                trashFragmentNotesList.size() == 1 );
    }

    @Test
    public void testEditNoteInHomeFragment() {
        note1.save( noteRepository );
        note2.save( noteRepository );
        note3.save( noteRepository );
        Note note = notesViewModel.getNoteWithId( 2 );
        note.edit( "Edited third note", "This is the edited third note",
                noteRepository );
        Assert.assertTrue( "Note access count should be 1",
                notesViewModel.getNoteWithId( 2 ).getAccessCount() == 1 );
        Assert.assertTrue( "Title not set correctly",
                notesViewModel.getNoteWithId( 2 ).getTitle().equals(
                        "Edited third note" ) );
        Assert.assertTrue( "Description not set correctly",
                notesViewModel.getNoteWithId( 2 ).getDescription().equals(
                        "This is the edited third note" ) );
    }

    @Test
    public void testEditNoteInHomeFragment_noteShouldBeDeleted() throws Exception {
        note1.save( noteRepository );
        note2.save( noteRepository );
        note1.edit( "", "", noteRepository );
        List<Note> homeFragmentReferenceList = LiveDataTestUtil.getValue(
                notesViewModel.getHomeFragmentNotesList() );
        Assert.assertTrue( "Notes list should be 1",
                homeFragmentReferenceList.size() == 1 );
    }

    @Test
    public void testAscendingConfigOptionIsSetCorrectly() throws Exception {
        int ascendingConfig = LiveDataTestUtil.getValue( notesViewModel
                .getAscendingConfigOption() );
        Assert.assertTrue( "Initial ascending config should be " + NotesViewModel.ASCENDING,
                ascendingConfig == NotesViewModel.ASCENDING );
    }

    @Test
    public void testLayoutTypeConfigIsSetCorrectly() throws Exception {
        int layoutTypeConfig = LiveDataTestUtil.getValue( notesViewModel.getLayoutTypeConfig() );
        Assert.assertTrue( "Initial layout type config should be "
                        + NotesViewModel.LAYOUT_STATE_SIMPLE_LIST,
                layoutTypeConfig == NotesViewModel.LAYOUT_STATE_SIMPLE_LIST );
    }

    @Test
    public void testLayoutTypeChangeIsPerformedCorrectly() throws Exception {
        notesViewModel.updateLayoutTypeConfig( NotesViewModel.LAYOUT_STATE_GRID );
        int layoutTypeConfig = LiveDataTestUtil.getValue( notesViewModel.getLayoutTypeConfig() );
        Assert.assertTrue( "Layout type should have changed",
                layoutTypeConfig == NotesViewModel.LAYOUT_STATE_GRID );
    }

    @Test
    public void testDeleteNoteFromHomeFragment_notesMoveToTrashFragment() throws Exception {
        note1.save( noteRepository );
        note2.save( noteRepository );
        note3.save( noteRepository );
        note4.save( noteRepository );
        note5.save( noteRepository );
        List<Note> homeFragmentNotesList = LiveDataTestUtil.getValue(
                notesViewModel.getHomeFragmentNotesList() );

        List<Note> notesToBeDeleted = new ArrayList<>();
        notesToBeDeleted.add( homeFragmentNotesList.get( 0 ) );
        notesToBeDeleted.add( homeFragmentNotesList.get( 1 ) );
        notesToBeDeleted.add( homeFragmentNotesList.get( 2 ) );
        notesViewModel.deleteNotesIn( notesToBeDeleted );
        List<Note> homeNotes = LiveDataTestUtil.getValue( notesViewModel.getHomeFragmentNotesList() );
        List<Note> trashNotes = LiveDataTestUtil.getValue( notesViewModel.getTrashFragmentNotesList() );
        Assert.assertTrue( "Home notes should be 2",
                homeNotes.size() == 2 );
        Assert.assertTrue( "Trash notes should be 3",
                trashNotes.size() == 3 );
    }


    // ------------------------------ Frequent Fragment Notes Tests ------------------------------
    @Test
    public void testFrequentNotesReferenceTableIsInitializedProperly() throws Exception {
        List<Note> frequentNotesReferenceList = LiveDataTestUtil.getValue(
                notesViewModel.getFrequentFragmentNotesList() );
        Assert.assertTrue( "Notes list size should be 0",
                frequentNotesReferenceList.size() == 0 );
    }

    @Test
    public void testNoteIsProperlyAddedToTheFrequentNotes() throws Exception {
        note1.save( noteRepository );
        note2.save( noteRepository );
        note3.save( noteRepository );
        note4.save( noteRepository );
        List<Note> homeFragmentNotesList = LiveDataTestUtil.getValue(
                notesViewModel.getHomeFragmentNotesList() );
        for ( int i = 0; i < 5; i++ )
            homeFragmentNotesList.get( 0 ).edit( "New Title",
                    "New Description", noteRepository );
        List<Note> frequentFragmentNotes = LiveDataTestUtil.getValue(
                notesViewModel.getFrequentFragmentNotesList() );
        Assert.assertTrue( "Frequent notes should be 1",
                frequentFragmentNotes.size() == 1 );
    }

    @Test
    public void testNoteIsProperlyEditedInFrequentNotes() throws Exception {
        note1.save( noteRepository );
        note2.save( noteRepository );
        note3.save( noteRepository );
        note4.save( noteRepository );
        List<Note> homeFragmentNotesList = LiveDataTestUtil.getValue(
                notesViewModel.getHomeFragmentNotesList() );
        for ( int i = 0; i < 5; i++ )
            homeFragmentNotesList.get( 3 ).edit( "New Title",
                    "New Description", noteRepository );
        List<Note> frequentFragmentNotesList = LiveDataTestUtil.getValue(
                notesViewModel.getFrequentFragmentNotesList() );
        frequentFragmentNotesList.get( 0 ).edit( "New Title", "New Description",
                noteRepository );
        Assert.assertTrue( notesViewModel.getNoteWithId( frequentFragmentNotesList.get( 0 ).getId() )
                .getTitle().equals( "New Title" ) );
    }

    @Test
    public void testNoteIsProperlyEditedInFrequentNotes_noteIsDeleted() throws Exception {
        note1.save( noteRepository );
        note2.save( noteRepository );
        note3.save( noteRepository );
        note4.save( noteRepository );
        List<Note> homeFragmentNotesList = LiveDataTestUtil.getValue(
                notesViewModel.getHomeFragmentNotesList() );
        for ( int i = 0; i < 5; i++ )
            homeFragmentNotesList.get( 2 ).edit( "New Title", "New Description",
                    noteRepository );
        List<Note> frequentFragmentNotesList = LiveDataTestUtil.getValue(
                notesViewModel.getFrequentFragmentNotesList() );
        frequentFragmentNotesList.get( 0 ).edit( "", "", noteRepository );
        List<Note> homeFragmentNotes = LiveDataTestUtil.getValue(
                notesViewModel.getHomeFragmentNotesList() );
        List<Note> frequentFragmentNotes = LiveDataTestUtil.getValue(
                notesViewModel.getFrequentFragmentNotesList() );
        Assert.assertTrue( "Home fragment notes should be 3",
                homeFragmentNotes.size() == 3 );
        Assert.assertTrue( "Frequent fragment notes should be 0",
                frequentFragmentNotes.size() == 0 );
    }

    @Test
    public void testNoteInFrequentNotesIsArchivedProperly() throws Exception {
        note1.save( noteRepository );
        note2.save( noteRepository );
        note3.save( noteRepository );
        note4.save( noteRepository );
        List<Note> homeFragmentNotesList = LiveDataTestUtil.getValue(
                notesViewModel.getHomeFragmentNotesList() );
        for ( int i = 0; i < 5; i++ )
            homeFragmentNotesList.get( 2 ).edit( "New Title", "New Description",
                    noteRepository );
        List<Note> frequentFragmentNotesList = LiveDataTestUtil.getValue(
                notesViewModel.getFrequentFragmentNotesList() );
        frequentFragmentNotesList.get( 0 ).archive( noteRepository );
        List<Note> homeFragmentNotes = LiveDataTestUtil.getValue( notesViewModel.getHomeFragmentNotesList() );
        List<Note> frequentFragmentNotes = LiveDataTestUtil.getValue( notesViewModel.getFrequentFragmentNotesList() );
        Assert.assertTrue( "Home fragment notes should be 3",
                homeFragmentNotes.size() == 3 );
        Assert.assertTrue( "Frequent fragment notes should be 0",
                frequentFragmentNotes.size() == 0 );
    }


//    // ------------------------------ Archive Fragment Model Tests -------------------------------
    @Test
    public void testArchiveNotesReferenceTableIsInitializedProperly() throws Exception {
        List<Note> archiveNotesReferenceList = LiveDataTestUtil.getValue(
                notesViewModel.getArchiveFragmentNotesList() );
        Assert.assertTrue( "Notes list size should be 0",
                archiveNotesReferenceList.size() == 0 );
    }

    @Test
    public void testArchiveNoteIsUnarchivedProperly() throws Exception {
        note1.save( noteRepository );
        note2.save( noteRepository );
        List<Note> homeFragmentNotesList = LiveDataTestUtil.getValue(
                notesViewModel.getHomeFragmentNotesList() );
        for ( int i = 0; i < 5; i++ )
            homeFragmentNotesList.get( 1 ).edit( "New Title", "",
                    noteRepository );
        Assert.assertTrue( "Note access count should be 5",
                homeFragmentNotesList.get( 1 ).getAccessCount() == 5 );

        homeFragmentNotesList.get( 1 ).archive( noteRepository );
        homeFragmentNotesList = LiveDataTestUtil.getValue( notesViewModel.getHomeFragmentNotesList() );
        Assert.assertTrue( "Home fragment notes should be 1",
                homeFragmentNotesList.size() == 1 );
        List<Note> archiveFragmentNotesList = LiveDataTestUtil.getValue( notesViewModel.getArchiveFragmentNotesList() );
        Assert.assertTrue( "Archived notes should be 1",
                archiveFragmentNotesList.size() == 1 );

        archiveFragmentNotesList.get( 0 ).unarchive( noteRepository );

        archiveFragmentNotesList = LiveDataTestUtil.getValue( notesViewModel.getArchiveFragmentNotesList() );
        Assert.assertTrue( "Archived notes should be 0",
                archiveFragmentNotesList.size() == 0 );
    }

    // ------------------------------- Trash Fragment Model Tests --------------------------------
    @Test
    public void testTrashNotesReferenceTableIsInitializedProperly() throws Exception {
        List<Note> trashNotesReferenceList = LiveDataTestUtil.getValue(
                notesViewModel.getTrashFragmentNotesList() );
        Assert.assertTrue( "Notes list size should be 0",
                trashNotesReferenceList.size() == 0 );
    }

    @Test
    public void testNoteIsAddedToTrashProperly() throws Exception {
        note1.save( noteRepository );
        note2.save( noteRepository );
        note3.save( noteRepository );
        List<Note> homeFragmentNotesList = LiveDataTestUtil.getValue(
                notesViewModel.getHomeFragmentNotesList() );
        homeFragmentNotesList.get( 0 ).delete( noteRepository );
        homeFragmentNotesList.get( 1 ).delete( noteRepository );
        List<Note> homeFragmentNotes = LiveDataTestUtil.
                getValue( notesViewModel.getHomeFragmentNotesList() );
        List<Note> trashFragmentNotes = LiveDataTestUtil.
                getValue( notesViewModel.getTrashFragmentNotesList() );
        Assert.assertTrue( "Home fragment notes should be 2",
                homeFragmentNotes.size() == 1 );
        Assert.assertTrue( "Trash fragment notes should be 2",
                trashFragmentNotes.size() == 2 );
    }
}
