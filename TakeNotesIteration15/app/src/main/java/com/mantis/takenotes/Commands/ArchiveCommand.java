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
import com.mantis.takenotes.R;
import com.mantis.takenotes.Repository.Note;

import java.util.List;

/**
 * @author - Michael Allan Odhiambo
 * @email - odhiambomichaelallan@gmail.com
 * @date - 4th March 2023
 */
public class ArchiveCommand implements Command {

    private final Context context;
    private final List<Note> notesToBeArchived;
    private final NotesViewModel notesViewModel;
    private final boolean archive;

    public ArchiveCommand( Context context, List<Note> notesToBeArchived,
                           NotesViewModel notesViewModel, boolean archive ) {
        this.context = context;
        this.notesToBeArchived = notesToBeArchived;
        this.notesViewModel = notesViewModel;
        this.archive = archive;
    }


    @Override
    public void execute() {
        showConfirmationDialog();
    }

    protected void showConfirmationDialog() {
        String prompt = getPrompt();
        ConfirmationDialog confirmationDialog = ConfirmationDialog.newInstance( prompt );
        confirmationDialog.addListener( new ConfirmationDialog.ConfirmationDialogListener() {
            @Override
            public void onCancelSelected() {
                // Do Nothing..
            }

            @Override
            public void onYesSelected() {
                if ( archive )
                    notesViewModel.archiveNotesIn( notesToBeArchived );
                else
                    notesViewModel.unarchiveNotesIn( notesToBeArchived );
            }
        } );
        confirmationDialog.show( ( (AppCompatActivity) context ).getSupportFragmentManager(),
                "confirmation" );
    }

    private String getPrompt() {
        String prompt = "";
        if ( archive && notesToBeArchived.size() > 1 )
            prompt = context.getString( R.string.archive_multiple_notes, notesToBeArchived.size() );
        else if ( archive && notesToBeArchived.size() == 1 )
            prompt = context.getString( R.string.archive_single_note, notesToBeArchived.size() );
        else if ( !archive && notesToBeArchived.size() > 1 )
            prompt = context.getString( R.string.unarchive_multiple_notes, notesToBeArchived.size() );
        else
            prompt = context.getString( R.string.unarchive_single_note, notesToBeArchived.size() );
        return prompt;
    }
}
