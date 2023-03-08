package com.mantis.takenotes.data.source.local.Dao;

/**
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;

import androidx.room.Query;
import com.mantis.takenotes.data.source.local.NoteEntity;
import java.util.Date;

import java.util.List;

/**
 * @author - Michael Allan Odhiambo
 * @email - odhiambomichaelallan@gmail.com
 * @date - 4th March 2023
 */

@Dao
public interface NoteDao {

    @Query( "SELECT * FROM notes_table WHERE id IN(:ids)" )
    LiveData<List<NoteEntity>> getNotesById( int[] ids );

    @Query( "SELECT * FROM notes_table" )
    LiveData<List<NoteEntity>> getAllNotes();

    @Insert
    void insertNote( NoteEntity noteEntity );

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

    @Query( "UPDATE notes_table SET timeLeft = :newTimeLeft WHERE id = :noteId" )
    void saveTimeLeft( int noteId, long newTimeLeft );

    @Query( "UPDATE notes_table SET dateNoteWasLastDeleted = :dateNoteWasLastDeleted " +
            "WHERE id = :noteId" )
    void setDateNoteWasLastDeleted( int noteId, Date dateNoteWasLastDeleted );

    @Query( "UPDATE notes_table SET accessCount = :accessCount WHERE id = :noteId" )
    void updateAccessCount( int noteId, int accessCount );
}
