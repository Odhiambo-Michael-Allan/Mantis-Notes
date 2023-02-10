package com.mantis.TakeNotes.Models;

import com.mantis.TakeNotes.data.source.NoteRepository;
import com.mantis.TakeNotes.data.source.local.Note;

public class HomeFragmentModel extends FragmentModel {

    public HomeFragmentModel( NotesViewModel notesViewModel, NoteRepository noteRepository ) {
        super( notesViewModel, noteRepository );
    }

    @Override
    public void addNote( Note note ) {
        note.setOwner( NotesViewModel.HOME_FRAGMENT );
        this.noteRepository.insertNote( note );
    }

    @Override
    protected boolean noteIsMine( Note note ) {
        return note.getOwner() == NotesViewModel.HOME_FRAGMENT ||
                note.getOwner() == NotesViewModel.FREQUENT_FRAGMENT;
    }

    @Override
    protected int getMyId() {
        return NotesViewModel.HOME_FRAGMENT;
    }

    @Override
    public void editNote( int noteId, Note placeHolder ) {
        super.editNote( noteId, placeHolder );
        Note currentNote;
        try {
            currentNote = getNoteWithId( noteId );
            if ( currentNote.getAccessCount() > 3 )
                notesViewModel.addFrequentFragmentNote( currentNote );
        }
        catch ( Exception e ) {}
    }
}
