package com.mantis.TakeNotes.data.source.local.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.mantis.TakeNotes.data.source.local.NoteReferences.HomeNoteReference;
import com.mantis.TakeNotes.data.source.local.NoteReferences.NoteReference;

import java.util.List;

@Dao
public interface HomeNotesDao {

    @Query( "SELECT * FROM home_notes_table" )
    LiveData<List<NoteReference>> getHomeNotes();

    @Insert
    void insertHomeNote( HomeNoteReference homeNoteReference );

    @Query( "DELETE FROM home_notes_table WHERE noteReferenceId IN (:noteId)" )
    void deleteHomeNoteReferenceReferencingNoteWithId( int noteId );

    @Query( "DELETE FROM home_notes_table" )
    void deleteAllHomeNotes();
}
