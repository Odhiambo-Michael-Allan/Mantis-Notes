package com.mantis.MantisNotesIterationOne.data.source;

import androidx.lifecycle.LiveData;

import com.mantis.MantisNotesIterationOne.data.source.local.Note;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.ArchiveNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.FrequentNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.HomeNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.NoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.TrashNoteReference;

import java.util.List;

public interface NoteRepository {
    void insertNote( Note note );
    LiveData<List<Note>> getNotesById( int[] ids );
    LiveData<List<Note>> getAllNotes();
    void deleteNote( int noteId );
    void deleteAllNotes();


    // ----------- Home Notes Table --------------
    void insertHomeNoteReference( NoteReference noteReference );
    LiveData<List<NoteReference>> getHomeNotesReferences();
    void deleteHomeNoteReference( int noteReferencedId );
    void deleteAllHomeNotesReferences();

    // ------------ Frequent Notes Table --------------
    void insertFrequentNoteReference( NoteReference noteReference );
    LiveData<List<NoteReference>> getFrequentNoteReferences();
    void deleteFrequentNoteReference( int noteReferencedId );
    void deleteAllFrequentNoteReferences();

    // ------------ Archive Notes Table -------------
    void insertArchiveNoteReference( NoteReference noteReference );
    LiveData<List<NoteReference>> getArchiveNotesReferences();
    void deleteArchiveNoteReference( int noteReferencedId );
    void deleteAllArchiveNotesReferences();

    // ------------ Trash Notes Table ---------------
    void insertTrashNoteReference( NoteReference noteReference );
    LiveData<List<NoteReference>> getTrashNotesReferences();
    void deleteTrashNoteReference( int noteReferencedId );
    void deleteAllTrashNotesReferences();
}
