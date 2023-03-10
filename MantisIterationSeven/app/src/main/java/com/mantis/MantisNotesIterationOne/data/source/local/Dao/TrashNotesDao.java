package com.mantis.MantisNotesIterationOne.data.source.local.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.NoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.TrashNoteReference;

import java.util.List;

@Dao
public interface TrashNotesDao {

    @Insert( onConflict = OnConflictStrategy.IGNORE )
    void insertTrashNote( TrashNoteReference reference );

    @Query( "SELECT * FROM trash_table" )
    LiveData<List<NoteReference>> getTrashNotes();

    @Query( "DELETE FROM trash_table WHERE id IN (:noteId)" )
    void deleteTrashNote( int noteId );

    @Query( "DELETE FROM trash_table" )
    void deleteAllTrashNotes();
}


