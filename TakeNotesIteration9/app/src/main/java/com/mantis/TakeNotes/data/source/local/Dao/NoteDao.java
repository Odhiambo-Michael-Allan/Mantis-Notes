package com.mantis.TakeNotes.data.source.local.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.mantis.TakeNotes.data.source.local.Note;

import java.util.Date;
import java.util.List;

@Dao
public interface NoteDao {

    @Query( "SELECT * FROM notes_table WHERE id IN(:ids)" )
    LiveData<List<Note>> getNotesById( int[] ids );

    @Query( "SELECT * FROM notes_table" )
    LiveData<List<Note>> getAllNotes();

    @Insert
    void insertNote( Note note );

    @Query( "DELETE FROM notes_table WHERE id = :noteId" )
    void deleteNote( int noteId );

    @Query( "DELETE FROM notes_table" )
    void deleteAll();

    @Query( "UPDATE notes_table SET title = :newTitle, description = " +
            ":newDescription, dateLastModified = :dateLastModified," +
            "accessCount = :newAccessCount WHERE id = :noteId" )
    void updateNote( int noteId, String newTitle, String newDescription, Date dateLastModified,
                     int newAccessCount );

    @Query( "UPDATE notes_table SET owner = :newOwner WHERE id = :noteId" )
    void updateNoteOwner( int noteId, int newOwner );
}
