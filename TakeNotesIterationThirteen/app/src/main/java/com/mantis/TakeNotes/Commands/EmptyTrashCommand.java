package com.mantis.TakeNotes.Commands;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.mantis.TakeNotes.Adapters.NotesAdapter;
import com.mantis.TakeNotes.Dialogs.ConfirmationDialog;
import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.R;

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
