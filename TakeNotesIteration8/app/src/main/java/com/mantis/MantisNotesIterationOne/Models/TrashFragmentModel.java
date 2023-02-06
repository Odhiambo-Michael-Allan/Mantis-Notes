package com.mantis.MantisNotesIterationOne.Models;

import androidx.lifecycle.Observer;

import com.mantis.MantisNotesIterationOne.data.source.NoteRepository;
import com.mantis.MantisNotesIterationOne.data.source.local.Note;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.HomeNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.NoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.TrashNoteReference;

import java.util.Iterator;
import java.util.List;

public class TrashFragmentModel extends FragmentModel {

    public TrashFragmentModel( NotesViewModel notesViewModel, NoteRepository noteRepository ) {
        super( notesViewModel, noteRepository );
    }

    @Override
    public void addNote( int noteId ) {
        Note note = notesViewModel.getNoteWithId( noteId );
        note.setOwner( NotesViewModel.TRASH_FRAGMENT );
        noteRepository.updateNoteOwner( noteId, note.getOwner() );
        super.addNote( noteId );
    }

    @Override
    protected void attachReferenceTableObserver( Observer observer ) {
        noteRepository.getTrashNotesReferences().observeForever( observer );
    }

    @Override
    protected NoteReference createReference(int noteId ) {
        return new TrashNoteReference( noteId );
    }

    @Override
    protected void insertReference( NoteReference reference ) {
        noteRepository.insertTrashNoteReference( reference );
    }

    @Override
    protected void deleteTableReference( NoteReference referenceToBeRemoved ) {
        noteRepository.deleteTrashNoteReference( referenceToBeRemoved.getNoteReferenceId() );
        noteRepository.deleteNote( referenceToBeRemoved.getNoteReferenceId() );  // Permanently delete the note..
    }

    @Override
    public void deleteAll() {
        noteRepository.deleteAllTrashNotesReferences();
        noteRepository.deleteAllNotes();
    }

    @Override
    protected void removeReferenceTableObserver( Observer observer ) {
        noteRepository.getArchiveNotesReferences().removeObserver( observer );
    }

    @Override
    public void editNote( int noteId, Note placeHolder ) {
        // DO NOTHING SINCE A NOTE CANNOT BE EDITED WHILE IN THE TRASH..
    }
}
