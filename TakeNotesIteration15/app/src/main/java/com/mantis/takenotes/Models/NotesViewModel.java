package com.mantis.takenotes.Models;

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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import androidx.lifecycle.ViewModel;
import com.mantis.takenotes.Utils.MenuConfigurator;
import com.mantis.takenotes.Repository.NoteRepository;

import com.mantis.takenotes.Repository.Note;
import com.mantis.takenotes.Utils.NoteMapper;
import com.mantis.takenotes.data.source.local.NoteEntity;

import com.mantis.takenotes.data.source.local.Query;
import java.util.ArrayList;
import java.util.Date;

import java.util.Iterator;
import java.util.List;

/**
 * @author - Michael Allan Odhiambo
 * @email - odhiambomichaelallan@gmail.com
 * @date - 4th March 2023
 */

public class NotesViewModel extends ViewModel {

    public static int LAYOUT_STATE_GRID = 1;
    public static int LAYOUT_STATE_LIST = 2;
    public static int LAYOUT_STATE_SIMPLE_LIST = 3;
    public static int TITLE = 6;
    public static int HOME_FRAGMENT = 9;
    public static int FREQUENT_FRAGMENT = 10;
    public static int ARCHIVE_FRAGMENT = 11;
    public static int TRASH_FRAGMENT = 12;
    public static int ASCENDING = 13;
    public static int DATE_CREATED = 7;
    public static int DATE_MODIFIED = 8;
    public static int DESCENDING = 14;

    public static final int TIME_TRASHED_NOTE_HAS_TO_LIVE = 1296000000;  // 15 DAYS

    private final NoteRepository noteRepository;
    private List<Note> cachedNotesList = new ArrayList<>();

    private Observer<List<NoteEntity>> notesTableObserver;
    private ConfigOptionsModel configOptionsModel;

    private Reaper reaper;

    private final MutableLiveData<List<Note>> observableHomeFragmentNotes =
            new MutableLiveData<>();
    private final MutableLiveData<List<Note>> observableFrequentFragmentNotes =
            new MutableLiveData<>();
    private final MutableLiveData<List<Note>> observableArchiveFragmentNotes =
            new MutableLiveData<>();
    private final MutableLiveData<List<Note>> observableTrashFragmentNotes =
            new MutableLiveData<>();

    public NotesViewModel( NoteRepository noteRepository ) {
        this.noteRepository = noteRepository;
        initializeConfigOptionsModel();
        observeNotesTable();
        addListenerToMenuConfigurator();
        setupReaper();
    }

    private void initializeConfigOptionsModel() {
        configOptionsModel = new ConfigOptionsModel( this.noteRepository );
    }

    private void observeNotesTable() {
        notesTableObserver = noteEntities -> {
            cachedNotesList = convertEntitiesToNotes( noteEntities );
            deliverAppropriateNotesToEachFragment();
        };
        this.noteRepository.getAllNotes().observeForever( notesTableObserver );
    }

    private List<Note> convertEntitiesToNotes( List<NoteEntity> noteEntities ) {
        List<Note> notesList = new ArrayList<>();
        for ( NoteEntity noteEntity : noteEntities )
            notesList.add( NoteMapper.entityToNote( noteEntity, this.noteRepository ) );
        return notesList;
    }

    private void deliverAppropriateNotesToEachFragment() {
        List<Note> homeFragmentNotes = new ArrayList<>(),
                frequentFragmentNotes = new ArrayList<>(),
                archiveFragmentNotes = new ArrayList<>(),
                trashFragmentNotes = new ArrayList<>();
        for ( Note note : cachedNotesList )
            if ( note.getOwner() == HOME_FRAGMENT || note.getOwner() == FREQUENT_FRAGMENT ) {
                homeFragmentNotes.add( note );
                if ( note.getOwner() == FREQUENT_FRAGMENT )
                    frequentFragmentNotes.add( note );
            }
            else if ( note.getOwner() == ARCHIVE_FRAGMENT )
                archiveFragmentNotes.add( note );
            else
                trashFragmentNotes.add( note );
        observableHomeFragmentNotes.postValue( homeFragmentNotes );
        observableFrequentFragmentNotes.postValue( frequentFragmentNotes );
        observableArchiveFragmentNotes.postValue( archiveFragmentNotes );
        observableTrashFragmentNotes.postValue( trashFragmentNotes );
    }

    private void addListenerToMenuConfigurator() {
        MenuConfigurator.addListener( new MenuConfigurator.MenuConfigurationListener() {
            @Override
            public void onSimpleListOptionSelected() {
                configOptionsModel.updateLayoutTypeConfig( LAYOUT_STATE_SIMPLE_LIST );
            }

            @Override
            public void onGridOptionSelected() {
                configOptionsModel.updateLayoutTypeConfig( LAYOUT_STATE_GRID );
            }

            @Override
            public void onListOptionSelected() {
                configOptionsModel.updateLayoutTypeConfig( LAYOUT_STATE_LIST );
            }
        } );
    }

    private void setupReaper() {
        reaper = Reaper.getInstance( this );
    }

    public Note getNoteWithId( int noteId ) {
        Iterator i = cachedNotesList.iterator();
        while ( i.hasNext() ) {
            Note note = (Note) i.next();
            if ( note.getId() == noteId )
                return note;
        }
        return null;
    }

    public List<Note> getNotesMatching( String queryString ) {
        if ( queryString.equals( "" ) )
            return new ArrayList<>();
        List<Note> notesMatchingQuery = new ArrayList<>();
        Iterator i = cachedNotesList.iterator();
        while ( i.hasNext() ) {
            Note note = (Note) i.next();
            if ( noteMatchesQuery( note, queryString.toLowerCase() ) )
                notesMatchingQuery.add( note );
        }
        return notesMatchingQuery;
    }

    private boolean noteMatchesQuery(Note note, String queryString ) {
        if ( note.getTitle().toLowerCase().contains( queryString ) ||
                note.getDescription().toLowerCase().contains( queryString ) )
            return true;
        return false;
    }

    public void deleteNotesIn( List<Note> notes ) {
        for ( Note note : notes )
            note.delete( this.noteRepository );
    }

    public void archiveNotesIn( List<Note> notes ) {
        for ( Note note : notes )
            note.archive( this.noteRepository );
    }

    public void unarchiveNotesIn( List<Note> notes ) {
        for ( Note note : notes )
            note.unarchive( this.noteRepository );
    }

    public void emptyTrashList() {
        for ( Note note : cachedNotesList )
            if ( note.getOwner() == TRASH_FRAGMENT )
                note.delete( this.noteRepository );
    }

    public void updateLayoutTypeConfig( int newLayoutTypeConfig ) {
        configOptionsModel.updateLayoutTypeConfig( newLayoutTypeConfig );
    }

    public void updateSortingStrategyConfig( int newSortingStrategyConfig ) {
        configOptionsModel.updateSortingStrategyConfig( newSortingStrategyConfig );
    }

    public void updateAscendingConfig( int newAscendingConfig ) {
        configOptionsModel.updateAscendingConfig( newAscendingConfig );
    }

    public LiveData<List<Note>> getHomeFragmentNotesList() {
        return observableHomeFragmentNotes;
    }

    public LiveData<List<Note>> getFrequentFragmentNotesList() {
        return observableFrequentFragmentNotes;
    }

    public LiveData<List<Note>> getArchiveFragmentNotesList() {
        return observableArchiveFragmentNotes;
    }

    public LiveData<List<Note>> getTrashFragmentNotesList() {
        return observableTrashFragmentNotes;
    }

    public NoteRepository getNoteRepository() {
        return this.noteRepository;
    }

    public LiveData<Integer> getAscendingConfigOption() {
        return configOptionsModel.getAscendingConfigOption();
    }

    public boolean getCurrentAscendingConfigOption() {
        int ascending = configOptionsModel.getCurrentAscendingConfigOption();
        if ( ascending == NotesViewModel.ASCENDING )
            return true;
        return false;
    }

    public LiveData<Integer> getLayoutTypeConfig() {
        return configOptionsModel.getLayoutTypeConfigOption();
    }

    public LiveData<Integer> getSortingStrategyConfigOption() {
        return configOptionsModel.getSortingStrategyConfigOption();
    }

    public int getCurrentSortingStrategyConfigOption() {
        return configOptionsModel.getCurrentSortingStrategyConfig();
    }

    public LiveData<List<Query>> getSearchHistory() {
        return this.noteRepository.getQueries();
    }

    @Override
    public void onCleared() {
        this.noteRepository.getAllNotes().removeObserver( notesTableObserver );
        for ( Note note : cachedNotesList )
            if ( note.getOwner() == TRASH_FRAGMENT ) {
                note.saveTimeLeftToDatabase();
                note.saveDateNoteWasLastDeleted( new Date() );
            }
        reaper.onCleared();
        configOptionsModel.onCleared();
    }
}
