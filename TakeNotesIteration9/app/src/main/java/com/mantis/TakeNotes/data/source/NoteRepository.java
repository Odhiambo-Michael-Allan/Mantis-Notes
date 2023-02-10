package com.mantis.TakeNotes.data.source;

import androidx.lifecycle.LiveData;

import com.mantis.TakeNotes.data.source.local.Configuration;
import com.mantis.TakeNotes.data.source.local.Note;

import java.util.Date;
import java.util.List;

public interface NoteRepository {
    void insertNote( Note note );
    LiveData<List<Note>> getNotesById( int[] ids );
    LiveData<List<Note>> getAllNotes();
    void deleteNote( int noteId );
    void deleteAllNotes();
    void updateNote( int noteId, String newTitle, String newDescription, Date dateLastModified,
                     int newAccessCount );
    void updateNoteOwner( int noteId, int newOwner );

    // -------------- Configuration Options ----------------
    void insertConfiguration( Configuration configuration );
    LiveData<Integer> getAscending();
    LiveData<Integer> getSortingStrategy();
    LiveData<Integer> getLayoutType();
    void updateLayoutTypeConfig( int newLayoutTypeConfig );
    void updateAscendingConfig( int newAscendingConfig );
    void updateSortingStrategyConfig( int newSortingStrategyConfig );
}
