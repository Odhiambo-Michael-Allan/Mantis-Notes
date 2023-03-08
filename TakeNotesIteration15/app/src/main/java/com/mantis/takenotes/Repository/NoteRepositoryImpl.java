package com.mantis.takenotes.Repository;

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

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.mantis.takenotes.data.source.NoteDataSource;

import com.mantis.takenotes.data.source.local.Configuration;
import com.mantis.takenotes.data.source.local.LocalDataSource;
import com.mantis.takenotes.data.source.local.NoteEntity;

import com.mantis.takenotes.data.source.local.NoteRoomDatabase;
import com.mantis.takenotes.data.source.local.Query;
import java.util.Date;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author - Michael Allan Odhiambo
 * @email - odhiambomichaelallan@gmail.com
 * @date - 4th March 2023
 */

public class NoteRepositoryImpl implements NoteRepository {

    private static NoteRepositoryImpl INSTANCE;
    private final NoteDataSource noteDataSource;

    public NoteRepositoryImpl( NoteDataSource noteDataSource ) {
        this.noteDataSource = noteDataSource;
    }

    public static NoteRepositoryImpl getRepository( Application application ) {
        if ( INSTANCE == null ) {
            synchronized( NoteRepositoryImpl.class ) {
                if ( INSTANCE == null ) {
                    NoteRoomDatabase database = NoteRoomDatabase.getDatabase( application );
                    INSTANCE = new NoteRepositoryImpl( new LocalDataSource( database.noteDao(),
                            database.configurationsDao(), database.queryDao() ) );
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public LiveData<List<NoteEntity>> getNotesById( int[] ids ) {
        return this.noteDataSource.getNotesById( ids );
    }

    @Override
    public LiveData<List<NoteEntity>> getAllNotes() {
        return this.noteDataSource.getAllNotes();
    }

    @Override
    public void insertNote( NoteEntity noteEntity ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.insertNote( noteEntity ); } );
        executor.shutdown();
    }

    @Override
    public void deleteNote( int noteId ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.deleteNote( noteId ); } );
        executor.shutdown();
    }

    @Override
    public void deleteAllNotes() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( this.noteDataSource::deleteAllNotes );
        executor.shutdown();
    }

    @Override
    public void updateNote( int noteId, String newTitle, String newDescription,
                            Date dateLastModified, int newAccessCount ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.updateNote( noteId, newTitle, newDescription,
                dateLastModified, newAccessCount ); } );
        executor.shutdown();
    }

    @Override
    public void updateNoteOwner( int noteId, int newOwner ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.updateNoteOwner( noteId, newOwner ); } );
        executor.shutdown();
    }

    @Override
    public void saveTimeLeft( int noteId, long timeLeft ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.saveTimeLeft( noteId, timeLeft ); } );
        executor.shutdown();
    }

    @Override
    public void setDateNoteWasLastDeleted( int noteId, Date date ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.setDateNoteWasLastDeleted( noteId, date ); } );
        executor.shutdown();
    }

    @Override
    public void updateAccessCount( int noteId, int accessCount ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.updateAccessCount( noteId, accessCount );} );
        executor.shutdown();
    }

    @Override
    public void insertConfiguration( Configuration configuration ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.insertConfiguration( configuration ); } );
        executor.shutdown();
    }

    @Override
    public LiveData<Integer> getAscending() {
        return noteDataSource.getAscending();
    }

    @Override
    public LiveData<Integer> getSortingStrategy() {
        return noteDataSource.getSortingStrategy();
    }

    @Override
    public LiveData<Integer> getLayoutType() {
        return noteDataSource.getLayoutType();
    }

    @Override
    public void updateLayoutTypeConfig( int newLayoutTypeConfig ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.updateLayoutTypeConfig( newLayoutTypeConfig ); } );
        executor.shutdown();
    }

    @Override
    public void updateAscendingConfig( int newAscendingConfig ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.updateAscendingConfig( newAscendingConfig ); } );
        executor.shutdown();
    }

    @Override
    public void updateSortingStrategyConfig( int newSortingStrategyConfig ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.updateSortingStrategyConfig( newSortingStrategyConfig ); } );
        executor.shutdown();
    }

    @Override
    public void insertQuery( Query query ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.insertQuery( query );} );
        executor.shutdown();
    }

    @Override
    public void deleteQuery( int queryId ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.deleteQuery( queryId );} );
        executor.shutdown();
    }

    @Override
    public LiveData<List<Query>> getQueries() {
        return this.noteDataSource.getQueries();
    }
}
