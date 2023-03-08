package com.mantis.takenotes.Models;

/**
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.lifecycle.Observer;
import com.mantis.takenotes.Repository.Note;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;

/**
 * This class employs the Mediator pattern. The "REAPER" observes the trash fragment notes list.
 * Whenever there are notes in the trash, the reaper goes through all the notes and decreases
 * their time left by a second every second. When a note's time is up, the note will delete
 * itself. The reaper is simply created and forgotten about..
 *
 * @author - Michael Allan Odhiambo
 * @email - odhiambomichaelallan@gmail.com
 * @date - 4th March 2023
 */

public class Reaper {

    private static Reaper INSTANCE;
    private final NotesViewModel notesViewModel;
    private Thread reaper;
    private List<Note> trashNotesList = new ArrayList<>();
    private boolean threadIsAlive = false;
    private Observer<List<Note>> trashedNotesObserver;

    public Reaper( NotesViewModel notesViewModel ) {
        this.notesViewModel = notesViewModel;
        observeTrashNotes();
    }

    private void observeTrashNotes() {
        trashedNotesObserver = notes -> {
            trashNotesList = notes;
            if ( trashNotesList.isEmpty() )
                stopReaper();
            else
                startReaper();
        };
        notesViewModel.getTrashFragmentNotesList().observeForever( trashedNotesObserver );
    }

    private void stopReaper() {
        threadIsAlive = false;
        if ( reaper == null )
            return;
        reaper.interrupt();
        reaper = null;
    }

    private void startReaper() {
        if ( reaper != null )
            return;
        threadIsAlive = true;
        reaper = new Thread( createRunnable() );
        reaper.start();
    }

    private Runnable createRunnable() {
        Runnable runnable = () -> {
            while ( threadIsAlive ) {
                decreaseNotesTimeLeftBy( 1000 );
                try {
                    Thread.sleep( 1000 );
                } catch ( InterruptedException e ) {}
            }
        };
        return runnable;
    }

    private void decreaseNotesTimeLeftBy( int milliseconds ) {
        Iterator i = trashNotesList.iterator();
        while ( i.hasNext() ) {
            Note note = ( Note ) i.next();
            note.decreaseTimeLeftBy( milliseconds );
        }
    }

    public static Reaper getInstance( NotesViewModel notesViewModel ) {
        if ( INSTANCE == null ) {
            synchronized ( Reaper.class ) {
                if ( INSTANCE == null )
                    INSTANCE = new Reaper( notesViewModel );
            }
        }
        return INSTANCE;
    }

    public void onCleared() {
        stopReaper();
        INSTANCE = null;
        notesViewModel.getTrashFragmentNotesList().removeObserver( trashedNotesObserver );
    }
}
