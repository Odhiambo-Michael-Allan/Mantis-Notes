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
    private Note note1, note2, note3, note4, note5, note6, note7, note8, note9, note10;

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


    /**
     * This class mocks a ViewModel listener. We'll use it to verify that the view model sends the
     * appropriate notifications during state change.
     */
    private class MockNotesViewModelListener {

        private boolean receivedLayoutStateChange = false;
        private boolean receivedNotesListChangeNotification = false;

        MockNotesViewModelListener() {}

        void layoutStateChanged() {
            receivedLayoutStateChange = true;
        }

        void notesListChanged() {
            receivedNotesListChangeNotification = true;
        }

        boolean isReceivedLayoutStateChange() {
            return receivedLayoutStateChange;
        }

        boolean isReceivedNotesListChangeNotification() {
            return this.receivedNotesListChangeNotification;
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
