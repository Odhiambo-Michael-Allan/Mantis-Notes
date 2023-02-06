package com.mantis.MantisNotesIterationOne.data.source.local.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.HomeNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.NoteReference;

import java.util.List;

@Dao
public interface HomeNotesDao {

    @Query( "SELECT * FROM home_notes_table" )
    LiveData<List<NoteReference>> getHomeNotes();

    @Insert
    void insertHomeNote( HomeNoteReference homeNoteReference );

    @Query( "DELETE FROM home_notes_table WHERE id IN (:noteId)" )
    void deleteHomeNote(int noteId );

    @Query( "DELETE FROM home_notes_table" )
    void deleteAllHomeNotes();
}
