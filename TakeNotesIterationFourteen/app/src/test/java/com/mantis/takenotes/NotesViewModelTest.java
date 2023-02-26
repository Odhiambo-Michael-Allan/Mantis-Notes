package com.mantis.takenotes;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import com.mantis.takenotes.Utils.DateProvider;
import com.mantis.takenotes.Models.NotesViewModel;

import com.mantis.takenotes.data.source.FakeRepository;
import com.mantis.takenotes.data.source.local.Note;
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
        notesViewModel = new NotesViewModel(noteRepository);
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
        List<Note> homeFragmentNotesList = LiveDataTestUtil.getValue(
                notesViewModel.getHomeFragmentNotesList() );
        Assert.assertTrue( "Notes list size should be 0",
                homeFragmentNotesList.size() == 0 );
    }

    @Test
    public void testAddNote() throws Exception {
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        notesViewModel.addNote( note3 );
        List<Note> homeFragmentNotesList = LiveDataTestUtil.getValue(
                notesViewModel.getHomeFragmentNotesList() );
        Assert.assertTrue( "Notes list size should be 3",
                homeFragmentNotesList.size() == 3 );
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
        List<Note> homeFragmentNotesList = LiveDataTestUtil.getValue(
                notesViewModel.getHomeFragmentNotesList() );
        Assert.assertTrue( "Notes list size should be 3",
                homeFragmentNotesList.size() == 3 );
    }

    @Test
    public void testDeleteHomeFragmentNote() throws Exception {
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        notesViewModel.addNote( note3 );
        note1.delete( noteRepository );
        List<Note> homeFragmentNotesList = LiveDataTestUtil.getValue(
                notesViewModel.getHomeFragmentNotesList() );
        Assert.assertTrue( "Notes list size should be 2",
                homeFragmentNotesList.size() == 2 );
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
        note3.edit( placeHolder.getTitle(), placeHolder.getDescription(), noteRepository );
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
        note1.edit(placeHolder.getTitle(), placeHolder.getDescription(), noteRepository );
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
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        notesViewModel.addNote( note3 );
        notesViewModel.addNote( note4 );
        notesViewModel.addNote( note5 );
        List<Note> notesToBeDeleted = new ArrayList<>();
        notesToBeDeleted.add( note1 );
        notesToBeDeleted.add( note2 );
        notesToBeDeleted.add( note3 );
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
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        notesViewModel.addNote( note3 );
        notesViewModel.addNote( note4 );
        Note placeHolder = new Note( "Edited third note", "This is the edited third" +
                " note.", DateProvider.getCurrentDate(), new Date() );
        for ( int i = 0; i < 5; i++ )
            note3.edit(placeHolder.getTitle(), placeHolder.getDescription(), noteRepository );
        List<Note> frequentFragmentNotes = LiveDataTestUtil.getValue(
                notesViewModel.getFrequentFragmentNotesList() );
        Assert.assertTrue( "Frequent notes should be 1",
                frequentFragmentNotes.size() == 1 );
    }

    @Test
    public void testNoteIsProperlyEditedInFrequentNotes() {
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        notesViewModel.addNote( note3 );
        notesViewModel.addNote( note4 );
        Note placeHolder = new Note( "Edited third note", "This is the edited third" +
                " note.", DateProvider.getCurrentDate(), new Date() );
        for ( int i = 0; i < 5; i++ )
            note3.edit(placeHolder.getTitle(), placeHolder.getDescription(), noteRepository );
        Note newPlaceHolder = new Note( "Third note edited from frequents", "",
                DateProvider.getCurrentDate(), new Date() );
        note3.edit(newPlaceHolder.getTitle(), newPlaceHolder.getDescription(), noteRepository );
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
            note3.edit(placeHolder.getTitle(), placeHolder.getDescription(), noteRepository );
        Note newPlaceHolder = new Note( "", "",
                DateProvider.getCurrentDate(), new Date() );
        note3.edit(newPlaceHolder.getTitle(), newPlaceHolder.getDescription(), noteRepository );
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
            note3.edit(placeHolder.getTitle(), placeHolder.getDescription(), noteRepository );
        note3.archive( noteRepository );
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

    @Test
    public void testArchiveNoteIsUnarchivedProperly() throws Exception {
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        Note placeHolder = new Note( "Edited third note", "This is the edited third" +
                " note.", DateProvider.getCurrentDate(), new Date() );

        for ( int i = 0; i < 5; i++ )
            note1.edit(placeHolder.getTitle(), placeHolder.getDescription(), noteRepository );

        Assert.assertTrue( "Note access count should be 5",
                note1.getAccessCount() == 5 );

        note1.archive( noteRepository );
        List<Note> archiveFragmentNotes = LiveDataTestUtil.getValue( notesViewModel.getArchiveFragmentNotesList() );
        Assert.assertTrue( "Archived notes should be 1",
                archiveFragmentNotes.size() == 1 );
        note1.unarchive( noteRepository );
        archiveFragmentNotes = LiveDataTestUtil.getValue( notesViewModel.getArchiveFragmentNotesList() );
        Assert.assertTrue( "Archived notes should be 0",
                archiveFragmentNotes.size() == 0 );
        Assert.assertTrue( "Note access count should be 0",
                note1.getAccessCount() == 0 );
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
        note1.delete( noteRepository );
        note2.delete( noteRepository );
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
