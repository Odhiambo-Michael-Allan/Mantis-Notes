package com.mantis.MantisNotesIterationOne.data.source.local.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mantis.MantisNotesIterationOne.data.source.local.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Query( "SELECT * FROM notes_table WHERE id IN(:ids)" )
    LiveData<List<Note>> getNotesById( int[] ids );

    @Query( "SELECT * FROM notes_table" )
    LiveData<List<Note>> getAllNotes();

    @Insert
    void insertNote( Note note );

    @Query( "DELETE FROM notes_table WHERE id IN (:noteId)" )
    void deleteNote( int noteId );

    @Query( "DELETE FROM notes_table" )
    void deleteAll();
}
