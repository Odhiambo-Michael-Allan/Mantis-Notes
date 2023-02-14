package com.mantis.TakeNotes.Models;

import com.mantis.TakeNotes.data.source.NoteRepository;
import com.mantis.TakeNotes.data.source.local.Note;

public class ArchiveFragmentModel extends FragmentModel {

    public ArchiveFragmentModel( NotesViewModel notesViewModel, NoteRepository noteRepository ) {
        super( notesViewModel, noteRepository );
    }

    @Override
    protected boolean noteIsMine( Note note ) {
        return note.getOwner() == NotesViewModel.ARCHIVE_FRAGMENT;
    }

    @Override
    protected int getMyId() {
        return NotesViewModel.ARCHIVE_FRAGMENT;
    }


}
