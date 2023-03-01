package com.mantis.takenotes.Commands;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import com.mantis.takenotes.Dialogs.ConfirmationDialog;

import com.mantis.takenotes.Models.NotesViewModel;
import com.mantis.takenotes.R;
import com.mantis.takenotes.data.source.local.Note;

import java.util.List;

public class ArchiveCommand implements Command {

    private Context context;
    private List<Note> notesToBeArchived;
    private NotesViewModel notesViewModel;
    private boolean archive;

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
