package com.mantis.TakeNotes.Commands;

import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.data.source.NoteRepository;
import com.mantis.TakeNotes.data.source.local.Note;

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
        this.notesViewModel.doneEditing();
    }
}
