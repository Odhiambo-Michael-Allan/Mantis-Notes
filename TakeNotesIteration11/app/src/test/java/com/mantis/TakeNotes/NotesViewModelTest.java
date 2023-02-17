package com.mantis.TakeNotes;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.mantis.TakeNotes.Utils.DateProvider;
import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.data.source.FakeRepository;
import com.mantis.TakeNotes.data.source.local.Note;

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
    private FakeRepository fakeRepository;
    private Note note1, note2, note3, note4, note5, note6;

    @Before
    public void setup() {
        fakeRepository = new FakeRepository();
        notesViewModel = new NotesViewModel( fakeRepository );
        note1 = new Note( "Note1", "This is note 1", DateProvider.getCurrentDate(),
                new Date() );
        note1.setId( 1 );
        note2 = new Note( "Note2", "This is note 2", DateProvider.getCurrentDate(),
                new Date() );
        note2.setId( 2 );
        note3 = new Note( "Note3", "This is note 3", DateProvider.getCurrentDate(),
                new Date() );
        note3.setId( 3 );
        note4 = new Note( "Note4", "This is note 4", DateProvider.getCurrentDate(),
                new Date() );
        note4.setId( 4 );
        note5 = new Note( "Note5", "This is note 5", DateProvider.getCurrentDate(),
                new Date() );
        note5.setId( 5 );
        note6 = new Note( "Note6", "This is note 6", DateProvider.getCurrentDate(),
                new Date() );
        note6.setId( 6 );
    }

    // ------------------------- Home Fragment Model Tests -------------------------

    @Test
    public void testHomeNotesReferenceTableIsInitializedProperly() throws Exception {
        List<Note> homeNotesReferenceList = LiveDataTestUtil.getValue(
                notesViewModel.getHomeFragmentNotesList() );
        Assert.assertTrue( "Notes list size should be 0",
                homeNotesReferenceList.size() == 0 );
    }

    @Test
    public void testAddNote() throws Exception {
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        notesViewModel.addNote( note3 );
        List<Note> homeNotesReferenceList = LiveDataTestUtil.getValue(
                notesViewModel.getHomeFragmentNotesList() );
        Assert.assertTrue( "Notes list size should be 3",
                homeNotesReferenceList.size() == 3 );
        Assert.assertTrue( "Note 1 owner should be home fragment",
                note1.getOwner() == NotesViewModel.HOME_FRAGMENT );
    }

    @Test
    public void testAddEmptyNote_noteIsNotAdded() throws Exception {
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        notesViewModel.addNote( note3 );
        notesViewModel.addNote( new Note( "", "", DateProvider.getCurrentDate(),
                new Date() ) );
        List<Note> homeNotesReferenceList = LiveDataTestUtil.getValue(
                notesViewModel.getHomeFragmentNotesList() );
        Assert.assertTrue( "Notes list size should be 3",
                homeNotesReferenceList.size() == 3 );
    }

    @Test
    public void testDeleteHomeFragmentNote() throws Exception {
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        notesViewModel.addNote( note3 );
        notesViewModel.deleteHomeFragmentNote( note1.getId() );
        List<Note> homeFragmentReferenceList = LiveDataTestUtil.getValue(
                notesViewModel.getHomeFragmentNotesList() );
        Assert.assertTrue( "Notes list size should be 2",
                homeFragmentReferenceList.size() == 2 );
        Assert.assertTrue( "Note 1 owner should be trash fragment",
                note1.getOwner() == NotesViewModel.TRASH_FRAGMENT );
    }

    @Test
    public void testEditNoteInHomeFragment() {
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        notesViewModel.addNote( note3 );
        Note placeHolder = new Note( "Edited third note", "This is the edited third" +
                " note.", DateProvider.getCurrentDate(), new Date() );
        notesViewModel.editHomeFragmentNote( note3.getId(), placeHolder );
        Assert.assertTrue( "Note access count should be 1",
                notesViewModel.getNoteWithId( note3.getId() ).getAccessCount() == 1 );
        Assert.assertTrue( "Title not set correctly",
                notesViewModel.getNoteWithId( note3.getId() ).getTitle().equals(
                        placeHolder.getTitle() ) );
        Assert.assertTrue( "Description not set correctly",
                notesViewModel.getNoteWithId( note3.getId() ).getDescription().equals(
                        placeHolder.getDescription() ) );
    }

    @Test
    public void testEditNoteInHomeFragment_noteShouldBeDeleted() throws Exception {
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        Note placeHolder = new Note( "", "",
                DateProvider.getCurrentDate(), new Date() );
        notesViewModel.editHomeFragmentNote( note1.getId(), placeHolder );
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
    public void testDeleteReferencesInHomeTable_referencesMoveToTrashTable() throws Exception {
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        notesViewModel.addNote( note3 );
        notesViewModel.addNote( note4 );
        notesViewModel.addNote( note5 );
        List<Note> referenceIds = new ArrayList<>();
        referenceIds.add( note1 );
        referenceIds.add( note2 );
        referenceIds.add( note3 );
        notesViewModel.deleteReferencesIn( referenceIds );
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
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        notesViewModel.addNote( note3 );
        notesViewModel.addNote( note4 );
        Note placeHolder = new Note( "Edited third note", "This is the edited third" +
                " note.", DateProvider.getCurrentDate(), new Date() );
        for ( int i = 0; i < 5; i++ )
            notesViewModel.editHomeFragmentNote( note3.getId(), placeHolder );
        List<Note> frequentFragmentNotes = LiveDataTestUtil.getValue(
                notesViewModel.getFrequentFragmentNotesList() );
        Assert.assertTrue( "Frequent notes should be 1",
                frequentFragmentNotes.size() == 1 );
    }

    @Test
    public void testNoteIsProperlyEditedInFrequentNotes() throws Exception {
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        notesViewModel.addNote( note3 );
        notesViewModel.addNote( note4 );
        Note placeHolder = new Note( "Edited third note", "This is the edited third" +
                " note.", DateProvider.getCurrentDate(), new Date() );
        for ( int i = 0; i < 5; i++ )
            notesViewModel.editHomeFragmentNote( note3.getId(), placeHolder );
        Note newPlaceHolder = new Note( "Third note edited from frequents", "",
                DateProvider.getCurrentDate(), new Date() );
        notesViewModel.editFrequentFragmentNote( note3.getId(), newPlaceHolder );
        Assert.assertTrue( notesViewModel.getNoteWithId( note3.getId() )
                .getTitle().equals( newPlaceHolder.getTitle() ) );
    }

    @Test
    public void testNoteIsProperlyEditedInFrequentNotes_noteIsDeleted() throws Exception {
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        notesViewModel.addNote( note3 );
        notesViewModel.addNote( note4 );
        Note placeHolder = new Note( "Edited third note", "This is the edited third" +
                " note.", DateProvider.getCurrentDate(), new Date() );
        for ( int i = 0; i < 5; i++ )
            notesViewModel.editHomeFragmentNote( note3.getId(), placeHolder );
        Note newPlaceHolder = new Note( "", "",
                DateProvider.getCurrentDate(), new Date() );
        notesViewModel.editFrequentFragmentNote( note3.getId(), newPlaceHolder );
        List<Note> homeFragmentNotes = LiveDataTestUtil.getValue( notesViewModel.getHomeFragmentNotesList() );
        List<Note> frequentFragmentNotes = LiveDataTestUtil.getValue( notesViewModel.getFrequentFragmentNotesList() );
        Assert.assertTrue( "Home fragment notes should be 3",
                homeFragmentNotes.size() == 3 );
        Assert.assertTrue( "Frequent fragment notes should be 0",
                frequentFragmentNotes.size() == 0 );
    }

    @Test
    public void testNoteInFrequentNotesIsArchivedProperly() throws Exception {
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        notesViewModel.addNote( note3 );
        notesViewModel.addNote( note4 );
        Note placeHolder = new Note( "Edited third note", "This is the edited third" +
                " note.", DateProvider.getCurrentDate(), new Date() );
        for ( int i = 0; i < 5; i++ )
            notesViewModel.editHomeFragmentNote( note3.getId(), placeHolder );
        notesViewModel.archiveFrequentFragmentNote( note3.getId() );
        List<Note> homeFragmentNotes = LiveDataTestUtil.getValue( notesViewModel.getHomeFragmentNotesList() );
        List<Note> frequentFragmentNotes = LiveDataTestUtil.getValue( notesViewModel.getFrequentFragmentNotesList() );
        Assert.assertTrue( "Home fragment notes should be 3",
                homeFragmentNotes.size() == 3 );
        Assert.assertTrue( "Frequent fragment notes should be 0",
                frequentFragmentNotes.size() == 0 );
    }


    // ------------------------------ Archive Fragment Model Tests -------------------------------
    @Test
    public void testArchiveNotesReferenceTableIsInitializedProperly() throws Exception {
        List<Note> archiveNotesReferenceList = LiveDataTestUtil.getValue(
                notesViewModel.getArchiveFragmentNotesList() );
        Assert.assertTrue( "Notes list size should be 0",
                archiveNotesReferenceList.size() == 0 );
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
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        notesViewModel.addNote( note3 );
        notesViewModel.deleteHomeFragmentNote( note1.getId() );
        notesViewModel.deleteHomeFragmentNote( note2.getId() );
        notesViewModel.addNote( note4 );
        List<Note> homeFragmentNotes = LiveDataTestUtil.
                getValue( notesViewModel.getHomeFragmentNotesList() );
        List<Note> trashFragmentNotes = LiveDataTestUtil.
                getValue( notesViewModel.getTrashFragmentNotesList() );
        Assert.assertTrue( "Home fragment notes should be 2",
                homeFragmentNotes.size() == 2 );
        Assert.assertTrue( "Trash fragment notes should be 2",
                trashFragmentNotes.size() == 2 );
    }
}
