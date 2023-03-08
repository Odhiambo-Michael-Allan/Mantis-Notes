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

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import com.mantis.takenotes.Dialogs.ConfirmationDialog;
import com.mantis.takenotes.Models.NotesViewModel;

import com.mantis.takenotes.Repository.Note;

import java.util.List;

/**
 * @author - Michael Allan Odhiambo
 * @email - odhiambomichaelallan@gmail.com
 * @date - 4th March 2023
 */

public class DeleteCommand implements Command {

    private Context context;
    private List<Note> notesToBeDeleted;
    private NotesViewModel notesViewModel;
    private int promptId;

    public DeleteCommand( Context context, List<Note> notesToBeDeleted, NotesViewModel notesViewModel,
                         int promptId ) {
        this.context = context;
        this.notesToBeDeleted = notesToBeDeleted;
        this.notesViewModel = notesViewModel;
        this.promptId = promptId;
    }
    @Override
    public void execute() {
        showConfirmationDialog();
    }

    protected void showConfirmationDialog() {
        ConfirmationDialog confirmationDialog = ConfirmationDialog.newInstance(
                context.getString( promptId, notesToBeDeleted.size() ) );
        confirmationDialog.addListener( new ConfirmationDialog.ConfirmationDialogListener() {
            @Override
            public void onCancelSelected() {
                // Do Nothing..
            }

            @Override
            public void onYesSelected() {
                notesViewModel.deleteNotesIn( notesToBeDeleted );
            }
        } );
        confirmationDialog.show( ( ( AppCompatActivity ) context ).getSupportFragmentManager(),
                "confirmation" );
    }
}
