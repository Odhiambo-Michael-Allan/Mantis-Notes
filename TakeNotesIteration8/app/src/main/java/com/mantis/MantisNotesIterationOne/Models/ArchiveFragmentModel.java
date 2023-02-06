package com.mantis.MantisNotesIterationOne.Models;

import androidx.lifecycle.Observer;

import com.mantis.MantisNotesIterationOne.data.source.NoteRepository;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.ArchiveNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.HomeNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.NoteReference;

import java.util.Iterator;
import java.util.List;

public class ArchiveFragmentModel extends FragmentModel {

    public ArchiveFragmentModel( NotesViewModel notesViewModel, NoteRepository noteRepository ) {
        super( notesViewModel, noteRepository );
    }

    @Override
    protected void attachReferenceTableObserver( Observer observer ) {
        noteRepository.getArchiveNotesReferences().observeForever( observer );
    }

    @Override
    protected NoteReference createReference(int noteId ) {
        return new ArchiveNoteReference( noteId );
    }

    @Override
    protected void insertReference( NoteReference reference ) {
        noteRepository.insertArchiveNoteReference( reference );
    }

    @Override
    protected void deleteTableReference( NoteReference referenceToBeRemoved ) {
        noteRepository.deleteArchiveNoteReference( referenceToBeRemoved.getNoteReferenceId() );
    }

    @Override
    public void deleteAll() {
        noteRepository.deleteAllArchiveNotesReferences();
    }

    @Override
    protected void removeReferenceTableObserver( Observer observer ) {
        noteRepository.getArchiveNotesReferences().removeObserver( observer );
    }
}
