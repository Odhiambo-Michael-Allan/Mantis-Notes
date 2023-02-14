package com.mantis.TakeNotes.Models;

import com.mantis.TakeNotes.data.source.NoteRepository;
import com.mantis.TakeNotes.data.source.local.Note;

public class FrequentFragmentModel extends FragmentModel {

    public FrequentFragmentModel( NotesViewModel notesViewModel, NoteRepository noteRepository ) {
        super( notesViewModel, noteRepository );
    }

    @Override
    protected boolean noteIsMine( Note note ) {
        return note.getOwner() == NotesViewModel.FREQUENT_FRAGMENT;
    }

    @Override
    protected int getMyId() {
        return NotesViewModel.FREQUENT_FRAGMENT;
    }

}
