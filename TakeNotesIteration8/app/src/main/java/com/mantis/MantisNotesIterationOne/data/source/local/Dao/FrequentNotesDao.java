package com.mantis.MantisNotesIterationOne.data.source.local.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.FrequentNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.NoteReference;

import java.util.List;

@Dao
public interface FrequentNotesDao {

    @Query( "DELETE FROM frequent_notes WHERE id IN (:noteId)" )
    void deleteFrequentNote( int noteId );

    @Query( "DELETE FROM frequent_notes" )
    void deleteAllFrequentNotes();

    @Insert( onConflict = OnConflictStrategy.IGNORE )
    void insertFrequentNote( FrequentNoteReference reference );

    @Query( "SELECT * FROM frequent_notes" )
    LiveData<List<NoteReference>> getFrequentNotes();
}
