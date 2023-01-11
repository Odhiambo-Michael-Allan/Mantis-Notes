package com.mantis.MantisNotesIterationOne;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.mantis.MantisNotesIterationOne.Models.DateProvider;
import com.mantis.MantisNotesIterationOne.Models.Note;
import com.mantis.MantisNotesIterationOne.Models.NotesViewModel;

import java.util.ArrayList;
import java.util.Date;

/**
 * Tests that thoroughly test the NotesViewModel class.
 * @Author Michael Allan Odhiambo
 * @E-mail odhiambomichaelallan@gmail.com
 */

public class ViewModelTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();
    private NotesViewModel notesViewModel;
    private MockNotesViewModelListener listener;
    private MockMenuConfigurator mockMenuConfigurator;
    private Note note1, note2, note3, note4, note5, note6, note7, note8, note9, note10, emptyNote;

    @Before
    public void setup() {
        notesViewModel = new NotesViewModel();
        listener = new MockNotesViewModelListener();
        mockMenuConfigurator = new MockMenuConfigurator();

        notesViewModel.getLayoutState().observeForever( new Observer<Integer>() {
            @Override
            public void onChanged( Integer integer ) {
                listener.layoutStateChanged();
            }
        } );

        notesViewModel.getNotes().observeForever( new Observer<ArrayList<Note>>() {
            @Override
            public void onChanged( ArrayList<Note> notes ) {
                listener.notesListChanged();
                listener.setNotesList( notes );
            }
        } );

        notesViewModel.getArchivedNotes().observeForever(new Observer<ArrayList<Note>>() {
            @Override
            public void onChanged( ArrayList<Note> notes ) {
                listener.archiveNotesListChanged();
                listener.setArchiveList( notes );
            }
        } );

        notesViewModel.getFrequentedNotes().observeForever( new Observer<ArrayList<Note>>() {
            @Override
            public void onChanged(ArrayList<Note> notes) {
                listener.frequentsListChanged();
                listener.setFrequentList( notes );
            }
        } );

        notesViewModel.getTrashedNotes().observeForever(new Observer<ArrayList<Note>>() {
            @Override
            public void onChanged( ArrayList<Note> notes ) {
                listener.setTrashList( notes );
            }
        } );

        mockMenuConfigurator.listener = new MockMenuConfiguratorListener() {
            @Override
            public void onSimpleListOptionSelected() {
                notesViewModel.setCurrentLayoutState( NotesViewModel.LAYOUT_STATE_SIMPLE_LIST );
            }

            @Override
            public void onListOptionSelected() {
                notesViewModel.setCurrentLayoutState( NotesViewModel.LAYOUT_STATE_LIST );
            }

            @Override
            public void onGridOptionSelected() {
                notesViewModel.setCurrentLayoutState( NotesViewModel.LAYOUT_STATE_GRID );
            }
        };


        note1 = new Note( "First Note", "This is the first note",
                DateProvider.getCurrentDate(), new Date() );
        note2 = new Note( "Second Note", "This is the second note",
                DateProvider.getCurrentDate(), new Date() );
        note3 = new Note( "Third Note", "This is the third note",
                DateProvider.getCurrentDate(), new Date() );
        note4 = new Note( "Fourth Note", "This is the fourth note",
                DateProvider.getCurrentDate(), new Date() );
        note5 = new Note( "Fifth Note", "This is the fifth note",
                DateProvider.getCurrentDate(), new Date() );
        note6 = new Note( "Sixth Note", "This is the fifth note",
                DateProvider.getCurrentDate(), new Date() );
        note7 = new Note( "Seventh Note", "This is the seventh note",
                DateProvider.getCurrentDate(), new Date() );
        note8 = new Note( "Eighth Note", "This is the eighth note",
                DateProvider.getCurrentDate(), new Date() );
        note9 = new Note( "Ninth Note", "This is the ninth note",
                DateProvider.getCurrentDate(), new Date() );
        note10 = new Note( "Tenth Note", "This is the tenth note",
                DateProvider.getCurrentDate(), new Date() );
        emptyNote = new Note( "", "", DateProvider.getCurrentDate(),
                new Date() );
    }

    @Test
    public void testSendsNotesListChangeNotificationAfterSettingAscending() {
        notesViewModel.setAscending( true );
        Assert.assertTrue( "Listener should have received notes list change notification" +
                " after setting ascending", listener.isReceivedNotesListChangeNotification() );
    }

    @Test
    public void testSendsNotesListChangeNotificationAfterSettingDescending() {
        notesViewModel.setAscending( false );
        Assert.assertTrue( "Listener should have received notes list change notification " +
                "after setting descending", listener.isReceivedNotesListChangeNotification() );
    }

    @Test
    public void testSendsNotesListChangeNotificationAfterSettingSortingStrategy() {
        Assert.assertTrue( "Listener should have received notes list change notification " +
                "after setting sorting strategy", listener.isReceivedNotesListChangeNotification() );
    }


    @Test
    public void testSendsNotesListChangeNotification() {
        Assert.assertTrue( "Listener should have received notes list change notification",
                listener.isReceivedNotesListChangeNotification() );
    }

    @Test
    public void testSendsLayoutChangeNotification() {
        Assert.assertTrue( "Listener should have received layout state change notification",
                listener.isReceivedLayoutStateChange() );
    }

    @Test
    public void testSendsLayoutStateChangeNotificationAfterSimpleListMenuSelection() {
        mockMenuConfigurator.simpleListOptionSelected();
        Assert.assertTrue( "Listener should have received layout change notification " +
                "after view model was notified of simple list option selection",
                listener.isReceivedLayoutStateChange() );
    }

    @Test
    public void testSendsLayoutStateChangeNotificationAfterListOptionMenuSelection() {
        mockMenuConfigurator.listOptionSelected();
        Assert.assertTrue( "Listener should have received layout change notification " +
                "after view model was notified of list option selection",
                listener.isReceivedLayoutStateChange() );
    }

    @Test
    public void testSendsLayoutStateChangeNotificationAfterGridOptionMenuSelection() {
        mockMenuConfigurator.gridOptionSelected();
        Assert.assertTrue( "Listener should have received layout change notification " +
                "after view model was notified of grid option selection.",
                listener.isReceivedLayoutStateChange() );
    }

    @Test
    public void testSendsNotesChangedNotificationAfterAddingNote() {
        notesViewModel.addNote( note1 );
        Assert.assertTrue( "Listener should have received notes change notification " +
                "after adding a note.", listener.isReceivedNotesListChangeNotification() );
    }

    @Test
    public void testArchivesNoteFromNotesListProperly() {
        notesViewModel.addNote( note1 );
        notesViewModel.archiveNoteFromNotesListAt( 0 );
        Assert.assertTrue( "Listener should have received archive notes change notification " +
                "after note is archived from notes list",
                listener.isReceivedArchiveNotesListChangeNotification() );
        Assert.assertTrue( "Listener should have received notes list change notification " +
                "after archive action", listener.isReceivedNotesListChangeNotification() );
        Assert.assertTrue( "Listener should have received frequented list change " +
                "notification after archive action",
                listener.isReceivedFrequentsListChangeNotification() );
    }

    @Test
    public void testNoteIsProperlyAddedToNotesList() {
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        notesViewModel.addNote( emptyNote );
        Assert.assertTrue( "Notes size should be two",
                listener.notesList.size() == 2 );
    }

    @Test
    public void testNoteIsProperlyAddedToFrequentNotesList() {
        note1.incrementAccessCount();
        note1.incrementAccessCount();
        note1.incrementAccessCount();
        note1.incrementAccessCount();
        notesViewModel.addNote( note1 );
        notesViewModel.editNoteInNotesList( new Note( "edited note 1", "",
                DateProvider.getCurrentDate(), new Date() ), 0 );
        Assert.assertTrue( "Notes list size should be one",
                listener.notesList.size() == 1 );
        Assert.assertTrue( "Frequents list size should be one",
                listener.frequentList.size() == 1 );

    }

    @Test
    public void testNoteIsProperlyRemovedFromFrequentNotesListWhenEditedFromNotesList() {
        note1.incrementAccessCount();
        note1.incrementAccessCount();
        note1.incrementAccessCount();
        note1.incrementAccessCount();
        notesViewModel.addNote( note1 );
        notesViewModel.editNoteInNotesList( new Note( "edited note 1", "",
                DateProvider.getCurrentDate(), new Date() ), 0 );
        notesViewModel.editNoteInNotesList( emptyNote, 0 );
        Assert.assertTrue( "Frequent notes list size should be zero",
                listener.frequentList.size() == 0 );
    }

    @Test
    public void testNoteProperlyRemovedFromNotesListWhenEditedFromFrequentsList() {
        note3.incrementAccessCount();
        note3.incrementAccessCount();
        note3.incrementAccessCount();
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        notesViewModel.addNote( note3 );
        Assert.assertTrue( "Notes list size should be three",
                listener.notesList.size() == 3 );
        notesViewModel.editNoteInNotesList( new Note( "edited note 3", "",
                DateProvider.getCurrentDate(), new Date() ), 2 );
        Assert.assertTrue( "Frequents list size should be one",
                listener.frequentList.size() == 1 );
        notesViewModel.editNoteInFrequentsList( emptyNote, 0 );
        Assert.assertTrue( "Notes list size should be zero",
                listener.notesList.size() == 2 );
        Assert.assertTrue( "Frequent list size should be zero",
                listener.frequentList.size() == 0 );
    }

    @Test
    public void testNoteIsProperlyArchivedFromNotesList() {
        for ( int i = 0; i < 5; i++ )
            note1.incrementAccessCount();
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        notesViewModel.editNoteInNotesList( note8, 0 );
        Assert.assertTrue( "Frequents list size should be one",
                listener.frequentList.size() == 1 );
        notesViewModel.archiveNoteFromNotesListAt( 0 );
        Assert.assertTrue( "Notes list size should be one",
                listener.notesList.size() == 1 );
        Assert.assertTrue( "Frequents list size should be zero",
                listener.frequentList.size() == 0 );
        Assert.assertTrue( "Archive list size should be one",
                listener.archiveList.size() == 1 );
    }

    @Test
    public void testNoteIsProperlyArchivedFromFrequentsList() {
        for ( int i = 0; i < 5; i++ )
            note1.incrementAccessCount();
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        notesViewModel.editNoteInNotesList( note8, 0 );
        Assert.assertTrue( "Frequents list size should be one",
                listener.frequentList.size() == 1 );
        notesViewModel.archiveNoteFromFrequentedListAt( 0 );
        Assert.assertTrue( "Notes list size should be one",
                listener.notesList.size() == 1 );
        Assert.assertTrue( "Frequents list size should be zero",
                listener.frequentList.size() == 0 );
        Assert.assertTrue( "Archive list size should be one",
                listener.archiveList.size() == 1 );
    }

    @Test
    public void testNoteProperlyUnarchived() {
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        notesViewModel.archiveNoteFromNotesListAt( 0 );
        Assert.assertTrue( "Notes list size should be 1",
                listener.notesList.size() == 1 );
        notesViewModel.unarchiveNoteAt( 0 );
        Assert.assertTrue( "Notes list size should be 2",
                listener.notesList.size() == 2 );
        Assert.assertTrue( "Archive list size should be 0",
                listener.archiveList.size() == 0 );
    }

    @Test
    public void testNoteProperlyTrashedFromNotesList() {
        for ( int i = 0; i <= 4; i++ ) {
            note2.incrementAccessCount();
            note3.incrementAccessCount();
        }
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        notesViewModel.addNote( note3 );
        notesViewModel.editNoteInNotesList( note7, 1 );
        notesViewModel.editNoteInNotesList( note8, 2 );
        Assert.assertTrue( "Frequented list size should be 2",
                listener.frequentList.size() == 2 );
        // After trashing, the notes list is sorted..
        notesViewModel.trashNoteFromNotesListAtPosition( 2 );
        notesViewModel.trashNoteFromNotesListAtPosition( 0 );
        Assert.assertTrue( "Notes list size should be 1",
                listener.notesList.size() == 1 );
        Assert.assertTrue( "Frequented list size should be 0",
                listener.frequentList.size() == 0 );
    }

    @Test
    public void testNoteProperlyTrashedFromFrequentsList() {
        for ( int i = 0; i <= 4; i++ ) {
            note2.incrementAccessCount();
            note3.incrementAccessCount();
        }
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        notesViewModel.addNote( note3 );
        notesViewModel.editNoteInNotesList( note7, 1 );
        notesViewModel.editNoteInNotesList( note8, 2 );
        Assert.assertTrue( "Frequented list size should be 2",
                listener.frequentList.size() == 2 );
        notesViewModel.trashNoteFromFrequentListAtPosition( 1 );
        notesViewModel.trashNoteFromFrequentListAtPosition( 0 );
        Assert.assertTrue( "Frequents notes list should be 0",
                listener.frequentList.size() == 0 );
        Assert.assertTrue( "Notes list should be 1",
                listener.notesList.size() == 1 );
    }

    @Test
    public void testNoteProperlyTrashedFromArchiveList() {
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        notesViewModel.archiveNoteFromNotesListAt( 0 );
        Assert.assertTrue( "Archive list size should be 1",
                listener.archiveList.size() == 1 );
        notesViewModel.trashNoteFromArchiveListAtPosition( 0 );
        Assert.assertTrue( "Archive list size should be 0",
                listener.archiveList.size() == 0 );
    }

    @Test
    public void testTrashListIsProperlyEmptied() {
        notesViewModel.addNote( note1 );
        notesViewModel.addNote( note2 );
        notesViewModel.addNote( note3 );
        notesViewModel.trashNoteFromNotesListAtPosition( 2 );
        notesViewModel.trashNoteFromNotesListAtPosition( 1 );
        notesViewModel.trashNoteFromNotesListAtPosition( 0 );
        Assert.assertTrue("Trash list size should be 3",
                listener.trashList.size() == 3 );
        notesViewModel.emptyTrashList();
        Assert.assertTrue( "Trash list size should be 0",
                listener.trashList.size() == 0 );
    }


    /**
     * This class mocks a ViewModel listener. We'll use it to verify that the view model sends the
     * appropriate notifications during state change.
     */
    private class MockNotesViewModelListener {

        private boolean receivedLayoutStateChange = false;
        private boolean receivedNotesListChangeNotification = false;
        private boolean receivedArchiveNotesListChangeNotification = false;
        private boolean receivedFrequentsListChangeNotification = false;
        private ArrayList<Note> notesList;
        private ArrayList<Note> frequentList;
        private ArrayList<Note> archiveList;
        private ArrayList<Note> trashList;

        MockNotesViewModelListener() {}

        void layoutStateChanged() {
            receivedLayoutStateChange = true;
        }

        void notesListChanged() {
            receivedNotesListChangeNotification = true;
        }

        void archiveNotesListChanged() {
            receivedArchiveNotesListChangeNotification = true;
        }

        void frequentsListChanged() {
            receivedFrequentsListChangeNotification = true;
        }

        boolean isReceivedLayoutStateChange() {
            return receivedLayoutStateChange;
        }

        boolean isReceivedNotesListChangeNotification() {
            return this.receivedNotesListChangeNotification;
        }

        boolean isReceivedArchiveNotesListChangeNotification() {
            return this.receivedArchiveNotesListChangeNotification;
        }

        boolean isReceivedFrequentsListChangeNotification() {
            return this.receivedFrequentsListChangeNotification;
        }

        void setNotesList( ArrayList<Note> notesList ) {
            this.notesList = notesList;
            System.out.println( "Notes list size: " + this.notesList.size() );
        }

        void setFrequentList( ArrayList<Note> frequentList ) {
            this.frequentList = frequentList;
            System.out.println( "Frequents list size from listener: " + this.frequentList.size() );
        }
        void setArchiveList( ArrayList<Note> archiveList ) {
            this.archiveList = archiveList;
        }

        void setTrashList( ArrayList<Note> trashList ) {
            this.trashList = trashList;
        }
    }


    /**
     * This class mocks the Menu configurator that creates the menus and notifies listeners of
     * menu clicks. We'll use this to see if the view model responds correctly to menu clicks.
     */
    private class MockMenuConfigurator {
        MockMenuConfiguratorListener listener;

        void simpleListOptionSelected() {
            listener.onSimpleListOptionSelected();
        }

        void listOptionSelected() {
            listener.onListOptionSelected();
        }

        void gridOptionSelected() {
            listener.onGridOptionSelected();
        }
    }

    interface MockMenuConfiguratorListener {
        void onSimpleListOptionSelected();
        void onListOptionSelected();
        void onGridOptionSelected();
    }
}
