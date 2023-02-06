package com.mantis.MantisNotesIterationOne.data.source;

import androidx.lifecycle.LiveData;

import com.mantis.MantisNotesIterationOne.data.source.local.Configuration;
import com.mantis.MantisNotesIterationOne.data.source.local.Note;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.ArchiveNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.FrequentNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.HomeNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.NoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.TrashNoteReference;

import java.util.Date;
import java.util.List;

public interface NoteDataSource {
    // ----------- Notes Table --------------
    void insertNote( Note note );
    LiveData<List<Note>> getNotesById( int[] ids );
    LiveData<List<Note>> getAllNotes();
    void deleteNote( int noteId );
    void deleteAllNotes();
    void updateNote( int noteId, String newTitle, String newDescription, Date dateLastModified,
                     int newAccessCount );
    void updateNoteOwner( int noteId, int newOwner );


    // ----------- Home Notes Table --------------
    void insertHomeNoteReference( NoteReference noteReference );
    LiveData<List<NoteReference>> getHomeNotesReferences();
    void deleteHomeNoteReference( int referenceId );
    void deleteAllHomeNotesReferences();

    // ------------ Frequent Notes Table --------------
    void insertFrequentNoteReference( NoteReference noteReference );
    LiveData<List<NoteReference>> getFrequentNoteReferences();
    void deleteFrequentNoteReference( int referenceId );
    void deleteAllFrequentNoteReferences();

    // ------------ Archive Notes Table -------------
    void insertArchiveNoteReference( NoteReference noteReference );
    LiveData<List<NoteReference>> getArchiveNotesReferences();
    void deleteArchiveNoteReference( int referenceId );
    void deleteAllArchiveNotesReferences();

    // ------------ Trash Notes Table ---------------
    void insertTrashNoteReference( NoteReference noteReference );
    LiveData<List<NoteReference>> getTrashNotesReferences();
    void deleteTrashNoteReference( int referenceId );
    void deleteAllTrashNotesReferences();

    // -------------- Configuration Options ----------------
    void insertConfiguration( Configuration configuration );
    LiveData<Integer> getAscending();
    LiveData<Integer> getSortingStrategy();
    LiveData<Integer> getLayoutType();
    void updateLayoutTypeConfig( int newLayoutTypeConfig );
}
