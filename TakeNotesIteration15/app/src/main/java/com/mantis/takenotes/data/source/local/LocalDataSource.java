package com.mantis.takenotes.data.source.local;

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
import com.mantis.takenotes.data.source.NoteDataSource;
import com.mantis.takenotes.data.source.local.Dao.ConfigurationsDao;

import com.mantis.takenotes.data.source.local.Dao.NoteDao;
import com.mantis.takenotes.data.source.local.Dao.QueryDao;
import java.util.Date;

import java.util.List;

public class LocalDataSource implements NoteDataSource {

    private final NoteDao noteDao;
    private final ConfigurationsDao configurationsDao;
    private final QueryDao queryDao;

    public LocalDataSource( NoteDao noteDao, ConfigurationsDao configurationsDao,
                            QueryDao queryDao ) {
        this.noteDao = noteDao;
        this.configurationsDao = configurationsDao;
        this.queryDao = queryDao;
    }

    @Override
    public LiveData<List<NoteEntity>> getNotesById( int[] ids ) {
        return noteDao.getNotesById( ids );
    }

    @Override
    public LiveData<List<NoteEntity>> getAllNotes() {
        return noteDao.getAllNotes();
    }

    @Override
    public void insertNote( NoteEntity noteEntity ) {
        noteDao.insertNote( noteEntity );
    }

    @Override
    public void deleteNote( int noteId ) {
        noteDao.deleteNote( noteId );
    }

    @Override
    public void deleteAllNotes() {
        noteDao.deleteAll();
    }

    @Override
    public void updateNote( int noteId, String newTitle, String newDescription,
                            Date dateLastModified, int newAccessCount ) {
        noteDao.updateNote( noteId, newTitle, newDescription, dateLastModified, newAccessCount );
    }

    @Override
    public void updateNoteOwner(int noteId, int newOwner) {
        noteDao.updateNoteOwner( noteId, newOwner );
    }

    @Override
    public void saveTimeLeft( int noteId, long timeLeft ) {
        noteDao.saveTimeLeft( noteId, timeLeft );
    }

    @Override
    public void setDateNoteWasLastDeleted( int noteId, Date date ) {
        noteDao.setDateNoteWasLastDeleted( noteId, date );
    }

    @Override
    public void updateAccessCount( int noteId, int accessCount ) {
        noteDao.updateAccessCount( noteId, accessCount );
    }

    @Override
    public void insertConfiguration( Configuration configuration ) {
        configurationsDao.insertConfiguration( configuration );
    }

    @Override
    public LiveData<Integer> getAscending() {
        return configurationsDao.getAscending();
    }

    @Override
    public LiveData<Integer> getSortingStrategy() {
        return configurationsDao.getSortingStrategy();
    }

    @Override
    public LiveData<Integer> getLayoutType() {
        return configurationsDao.getLayoutType();
    }

    @Override
    public void updateLayoutTypeConfig( int newLayoutTypeConfig ) {
        configurationsDao.updateLayoutTypeConfig( newLayoutTypeConfig );
    }

    @Override
    public void updateAscendingConfig( int newAscendingConfig ) {
        configurationsDao.updateAscendingConfig( newAscendingConfig );
    }

    @Override
    public void updateSortingStrategyConfig( int newSortingStrategyConfig ) {
        configurationsDao.updateSortingStrategyConfig( newSortingStrategyConfig );
    }

    @Override
    public void insertQuery( Query query ) {
        queryDao.insert( query );
    }

    @Override
    public void deleteQuery( int queryId ) {
        queryDao.deleteQuery( queryId );
    }

    @Override
    public LiveData<List<Query>> getQueries() {
        return queryDao.getQueries();
    }
}
