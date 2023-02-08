package com.mantis.MantisNotesIterationOne.Models;

import androidx.lifecycle.Observer;

import com.mantis.MantisNotesIterationOne.data.source.NoteRepository;
import com.mantis.MantisNotesIterationOne.data.source.local.Note;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.HomeNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.NoteReference;

import java.util.Iterator;
import java.util.List;

public class HomeFragmentModel extends FragmentModel {

    public HomeFragmentModel( NotesViewModel notesViewModel, NoteRepository noteRepository ) {
        super( notesViewModel, noteRepository );
    }

    @Override
    protected void attachReferenceTableObserver( Observer observer ) {
        noteRepository.getHomeNotesReferences().observeForever( observer );
    }


    @Override
    protected NoteReference createReference(int noteId ) {
        return new HomeNoteReference( noteId );
    }

    @Override
    protected void insertReference( NoteReference reference ) {
        noteRepository.insertHomeNoteReference( reference );
    }


    @Override
    protected void deleteTableReference( NoteReference referenceToBeRemoved ) {
        noteRepository.deleteHomeNoteReference( referenceToBeRemoved.getId() );
    }

    @Override
    public void syncWith( List<Note> notesList ) {
        Iterator i = notesList.iterator();
        while ( i.hasNext() ) {
            Note note = ( Note ) i.next();
            if ( note.getOwner() == NotesViewModel.HOME_FRAGMENT ) {
                System.out.println( "NOTE OWNER: " + note.getOwner() );
                super.addNote( note.getId() );
            }
        }
        super.syncWith( notesList );
    }

    @Override
    public void deleteAll() {
        noteRepository.deleteAllHomeNotesReferences();
    }

    @Override
    protected void removeReferenceTableObserver( Observer observer ) {
        noteRepository.getHomeNotesReferences().removeObserver( observer );
    }

    @Override
    public void editNote( int noteId, Note placeHolder ) {
        super.editNote( noteId, placeHolder );
        Note currentNote;
        try {
            currentNote = notesViewModel.getNoteWithId( noteId );
            if ( currentNote.getAccessCount() > 3 )
                notesViewModel.addFrequentFragmentNote( currentNote );
        }
        catch ( Exception e ) {}
    }
}
