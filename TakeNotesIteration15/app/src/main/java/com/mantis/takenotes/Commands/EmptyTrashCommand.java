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

import com.mantis.takenotes.Adapters.NotesAdapter;
import com.mantis.takenotes.Dialogs.ConfirmationDialog;
import com.mantis.takenotes.Models.NotesViewModel;
import com.mantis.takenotes.R;

/**
 * @author - Michael Allan Odhiambo
 * @email - odhiambomichaelallan@gmail.com
 * @date - 4th March 2023
 */

public class EmptyTrashCommand implements Command {

    private Context context;
    private NotesViewModel notesViewModel;
    private NotesAdapter notesAdapter;

    public EmptyTrashCommand( Context context, NotesViewModel notesViewModel, NotesAdapter notesAdapter ) {
        this.context = context;
        this.notesViewModel = notesViewModel;
        this.notesAdapter = notesAdapter;
    }
    @Override
    public void execute() {
        showConfirmationDialog( this.notesAdapter.getItemCount() );
    }

    private void showConfirmationDialog( int noteCount ) {
        ConfirmationDialog confirmationDialog = ConfirmationDialog.newInstance(
                this.context.getString( noteCount > 1 ? R.string.permanently_delete_multiple_notes
                        : R.string.permanently_delete_single_note, noteCount ) );
        confirmationDialog.addListener( new ConfirmationDialog.ConfirmationDialogListener() {
            @Override
            public void onCancelSelected() {
                return;
            }

            @Override
            public void onYesSelected() {
                notesViewModel.emptyTrashList();
            }
        } );
        confirmationDialog.show( ( (AppCompatActivity) this.context ).getSupportFragmentManager(),
                "confirmation" );
    }
}
