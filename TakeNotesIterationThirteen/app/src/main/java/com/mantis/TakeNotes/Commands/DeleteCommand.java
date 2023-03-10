package com.mantis.TakeNotes.Commands;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.mantis.TakeNotes.Dialogs.ConfirmationDialog;
import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.R;
import com.mantis.TakeNotes.data.source.local.Note;

import java.util.ArrayList;
import java.util.List;

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
                notesViewModel.doneEditing();
                return;
            }

            @Override
            public void onYesSelected() {
                notesViewModel.deleteNotesIn( notesToBeDeleted );
                notesViewModel.doneEditing();
            }
        } );
        confirmationDialog.show( ( ( AppCompatActivity ) context ).getSupportFragmentManager(),
                "confirmation" );
    }
}
