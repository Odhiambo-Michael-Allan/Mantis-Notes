package com.mantis.takenotes.data.source;

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
import com.mantis.takenotes.Repository.Note;
import com.mantis.takenotes.data.source.local.Configuration;
import com.mantis.takenotes.data.source.local.NoteEntity;
import com.mantis.takenotes.data.source.local.Query;

import java.util.Date;
import java.util.List;

/**
 * @author - Michael Allan Odhiambo
 * @email - odhiambomichaelallan@gmail.com
 * @date - 4th March 2023
 */

public interface NoteDataSource {
    // ----------- Notes Table --------------
    void insertNote( NoteEntity noteEntity );
    LiveData<List<NoteEntity>> getNotesById( int[] ids );
    LiveData<List<NoteEntity>> getAllNotes();
    void deleteNote( int noteId );
    void deleteAllNotes();
    void updateNote( int noteId, String newTitle, String newDescription, Date dateLastModified,
                     int newAccessCount );
    void updateNoteOwner( int noteId, int newOwner );
    void saveTimeLeft( int noteId, long timeLeft );
    void setDateNoteWasLastDeleted( int noteId, Date date );
    void updateAccessCount( int noteId, int accessCount );

    // -------------- Configuration Options ----------------
    void insertConfiguration( Configuration configuration );
    LiveData<Integer> getAscending();
    LiveData<Integer> getSortingStrategy();
    LiveData<Integer> getLayoutType();
    void updateLayoutTypeConfig( int newLayoutTypeConfig );
    void updateAscendingConfig( int newAscendingConfig );
    void updateSortingStrategyConfig( int newSortingStrategyConfig );

    // ----------------- Recent Queries -------------------------

    void insertQuery( Query query );
    void deleteQuery( int queryId );
    LiveData<List<Query>> getQueries();
}
