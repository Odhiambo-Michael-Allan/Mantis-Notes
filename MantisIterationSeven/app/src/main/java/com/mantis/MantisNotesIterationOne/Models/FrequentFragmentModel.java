package com.mantis.MantisNotesIterationOne.Models;

import androidx.lifecycle.Observer;

import com.mantis.MantisNotesIterationOne.data.source.NoteRepository;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.FrequentNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.HomeNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.NoteReference;

import java.util.Iterator;
import java.util.List;

public class FrequentFragmentModel extends FragmentModel {

    public FrequentFragmentModel( NotesViewModel notesViewModel, NoteRepository noteRepository ) {
        super( notesViewModel, noteRepository );
    }

    @Override
    protected void attachReferenceTableObserver( Observer observer ) {
        noteRepository.getFrequentNoteReferences().observeForever( observer );
    }

    @Override
    protected NoteReference getReference( int noteId ) {
        return new FrequentNoteReference( noteId );
    }

    @Override
    protected void insertReference( NoteReference reference ) {
        noteRepository.insertFrequentNoteReference( reference );
    }

    @Override
    public void deleteReference( int referencedNoteId) {
        if ( this.getReferenceList().size() < 1 )
            return;
        super.deleteReference(referencedNoteId);
    }

    @Override
    protected void deleteTableReference( NoteReference referenceToBeRemoved ) {
        noteRepository.deleteFrequentNoteReference( referenceToBeRemoved.getNoteReferenceId() );
        notesViewModel.deleteHomeFragmentNoteReference( referenceToBeRemoved.getNoteReferenceId() );
    }

    @Override
    public void deleteAll() {
        noteRepository.deleteAllFrequentNoteReferences();
    }

    @Override
    protected void removeReferenceTableObserver( Observer observer ) {
        noteRepository.getFrequentNoteReferences().removeObserver( observer );
    }
}
