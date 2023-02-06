package com.mantis.MantisNotesIterationOne.data.source.local.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.ArchiveNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.NoteReference;

import java.util.List;

@Dao
public interface ArchiveNotesDao {

    @Insert( onConflict = OnConflictStrategy.IGNORE )
    void insertArchiveNote( ArchiveNoteReference reference );

    @Query( "SELECT * FROM archive_table" )
    LiveData<List<NoteReference>> getArchivedNotes();

    @Query( "DELETE FROM archive_table WHERE id IN (:noteId)" )
    void deleteArchiveNote( int noteId );

    @Query( "DELETE FROM archive_table" )
    void deleteAllArchivedNotes();
}
