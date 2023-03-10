package com.mantis.TakeNotes.Models;

import com.mantis.TakeNotes.data.source.NoteRepository;
import com.mantis.TakeNotes.data.source.local.Note;

import java.util.Iterator;

public class TrashFragmentModel extends FragmentModel {
    
    private Reaper reaper;

    public TrashFragmentModel( NotesViewModel notesViewModel, NoteRepository noteRepository ) {
        super( notesViewModel, noteRepository );
        setupReaper();
    }

    @Override
    protected boolean noteIsMine(Note note) {
        return note.getOwner() == NotesViewModel.TRASH_FRAGMENT;
    }

    @Override
    protected int getMyId() {
        return NotesViewModel.TRASH_FRAGMENT;
    }

    @Override
    public void editNote( int noteId, Note placeHolder ) {
        // DO NOTHING SINCE A NOTE CANNOT BE EDITED WHILE IN THE TRASH..
    }

    @Override
    public void deleteAll() {
        Iterator i = cachedNotesList.iterator();
        while ( i.hasNext() ) {
            Note note = (Note) i.next();
            if ( note.getOwner() == NotesViewModel.TRASH_FRAGMENT )
                this.noteRepository.deleteNote( note.getId() );
        }
    }

    @Override
    public void deleteNoteWithId( int noteId ) {
        this.noteRepository.deleteNote( noteId );
    }

    private void setupReaper() {
        reaper = new Reaper( this, this.noteRepository );
    }

    @Override
    public void onCleared() {
        super.onCleared();
        reaper.onCleared( noteRepository );
    }
}
