package com.mantis.MantisNotesIterationOne;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.mantis.MantisNotesIterationOne.Models.NotesViewModel;

public class ViewModelTest {

    @Rule
    InstantTaskExecutorRule rule = new InstantTaskExecutorRule();
    private NotesViewModel notesViewModel;
    private NotesViewModelListener listener;

    @Before
    public void setup() {
        notesViewModel = new NotesViewModel();
        listener = new NotesViewModelListener();
    }

    @Test
    public void testSendsLayoutChangeNotification() {
        notesViewModel.getLayoutState().observe(new LifecycleOwner() {
            @NonNull
            @Override
            public Lifecycle getLifecycle() {
                return null;
            }
        }, new Observer<Integer>() {
            @Override
            public void onChanged( Integer integer ) {
                listener.layoutTypeChanged();
            }
        } );
    }

    private class NotesViewModelListener {
        NotesViewModelListener() {}

        void layoutTypeChanged() {
            Logger.log( "RECEIVED LAYOUT CHANGE NOTIFICATION" );
        }
    }
}
