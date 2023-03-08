package com.mantis.takenotes.Commands;

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

import com.mantis.takenotes.Models.NotesViewModel;
import com.mantis.takenotes.Repository.Note;

import java.util.List;

/**
 * @author - Michael Allan Odhiambo
 * @email - odhiambomichaelallan@gmail.com
 * @date - 4th March 2023
 */

public class RestoreNoteCommand implements Command {

    private List<Note> notesToRestore;
    private NotesViewModel notesViewModel;

    public RestoreNoteCommand( List<Note> notesToRestore, NotesViewModel notesViewModel ) {
        this.notesToRestore = notesToRestore;
        this.notesViewModel = notesViewModel;
    }

    @Override
    public void execute() {
        for ( Note note : notesToRestore )
            note.restore( this.notesViewModel.getNoteRepository() );
    }
}
