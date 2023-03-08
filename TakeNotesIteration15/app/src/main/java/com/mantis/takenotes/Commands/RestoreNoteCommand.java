package com.mantis.takenotes.Commands;

import com.mantis.takenotes.Models.NotesViewModel;
import com.mantis.takenotes.Repository.Note;

import java.util.List;

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
