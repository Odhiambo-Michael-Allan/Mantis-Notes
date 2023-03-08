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

import com.mantis.takenotes.Repository.NoteRepository;
import com.mantis.takenotes.data.source.local.Configuration;

/**
 * This class acts as a model or manager for all the configuration options used within the
 * application.
 *
 * @author - Michael Allan Odhiambo
 * @email - odhiambomichaelallan@gmail.com
 * @date - 4th March 2023
 */

public class ConfigOptionsModel {

    private final NoteRepository noteRepository;
    private final MutableLiveData<Integer> observableSortingStrategyConfig = new MutableLiveData<>();
    private final MutableLiveData<Integer> observableAscendingConfig = new MutableLiveData<>();
    private final MutableLiveData<Integer> observableLayoutTypeConfig = new MutableLiveData<>();

    private Observer<Integer> sortingStrategyConfigObserver,
            ascendingConfigObserver, layoutTypeConfigObserver;
    private int currentAscendingConfig = NotesViewModel.ASCENDING;
    private int currentLayoutTypeConfig = NotesViewModel.LAYOUT_STATE_SIMPLE_LIST;
    private int currentSortingStrategyConfig = NotesViewModel.TITLE;

    public ConfigOptionsModel( NoteRepository noteRepository ) {
        this.noteRepository = noteRepository;
        initializeObservers();
        attachObservers();
    }

    private void initializeObservers() {
        initializeSortingStrategyConfigObserver();
        initializeAscendingConfigObserver();
        initializeLayoutTypeConfigObserver();
    }

    private void initializeSortingStrategyConfigObserver() {
        sortingStrategyConfigObserver = new Observer<Integer>() {
            @Override
            public void onChanged( Integer integer ) {
                if ( integer == null )
                    return;
                currentSortingStrategyConfig = integer;
                observableSortingStrategyConfig.postValue( currentSortingStrategyConfig );
            }
        };
    }

    private void initializeAscendingConfigObserver() {
        ascendingConfigObserver = new Observer<Integer>() {
            @Override
            public void onChanged( Integer integer ) {
                if ( integer == null )
                    return;
                currentAscendingConfig = integer;
                observableAscendingConfig.postValue( integer );
            }
        };
    }

    private void initializeLayoutTypeConfigObserver() {
        layoutTypeConfigObserver = new Observer<Integer>() {
            @Override
            public void onChanged( Integer integer ) {
                if ( integer == null ) {
                    createDefaultConfiguration();
                    return;
                }
                currentLayoutTypeConfig = integer;
                observableLayoutTypeConfig.postValue( currentLayoutTypeConfig );
            }
        };
    }

    private void createDefaultConfiguration() {
        Configuration defaultConfiguration = new Configuration( currentAscendingConfig,
                currentSortingStrategyConfig, currentLayoutTypeConfig );
        noteRepository.insertConfiguration( defaultConfiguration );
    }

    private void attachObservers() {
        observeSortingStrategyConfig();
        observeAscendingConfig();
        observeLayoutTypeConfig();
    }

    private void observeSortingStrategyConfig() {
        LiveData<Integer> sortingStrategyConfig = noteRepository.getSortingStrategy();
        sortingStrategyConfig.observeForever( sortingStrategyConfigObserver );
    }

    private void observeAscendingConfig() {
        LiveData<Integer> ascendingConfig = noteRepository.getAscending();
        ascendingConfig.observeForever( ascendingConfigObserver );
    }

    private void observeLayoutTypeConfig() {
        LiveData<Integer> layoutTypeConfig = noteRepository.getLayoutType();
        layoutTypeConfig.observeForever( layoutTypeConfigObserver );
    }

    public LiveData<Integer> getSortingStrategyConfigOption() {
        if ( observableSortingStrategyConfig.getValue() == null )
            observableSortingStrategyConfig.postValue( currentLayoutTypeConfig );
        return observableSortingStrategyConfig;
    }

    public LiveData<Integer> getAscendingConfigOption() {
        if ( observableAscendingConfig.getValue() == null )
            observableAscendingConfig.postValue( currentAscendingConfig );
        return observableAscendingConfig;
    }

    public int getCurrentAscendingConfigOption() {
        return currentAscendingConfig;
    }

    public LiveData<Integer> getLayoutTypeConfigOption() {
        if ( observableLayoutTypeConfig.getValue() == null )
            observableLayoutTypeConfig.postValue( currentLayoutTypeConfig );
        return observableLayoutTypeConfig;
    }

    public int getCurrentSortingStrategyConfig() {
        return currentSortingStrategyConfig;
    }

    public void updateLayoutTypeConfig( int newLayoutTypeConfig ) {
        currentLayoutTypeConfig = newLayoutTypeConfig;
        observableLayoutTypeConfig.postValue( currentLayoutTypeConfig );
        noteRepository.updateLayoutTypeConfig( newLayoutTypeConfig );
    }

    public void updateSortingStrategyConfig( int newSortingStrategyConfig ) {
        currentSortingStrategyConfig = newSortingStrategyConfig;
        observableSortingStrategyConfig.postValue( currentSortingStrategyConfig );
        noteRepository.updateSortingStrategyConfig( newSortingStrategyConfig );
    }

    public void updateAscendingConfig( int newAscendingConfig ) {
        currentAscendingConfig = newAscendingConfig;
        observableAscendingConfig.postValue( currentAscendingConfig );
        noteRepository.updateAscendingConfig( newAscendingConfig );
    }

    public void onCleared() {
        removeObservers();
    }

    private void removeObservers() {
        noteRepository.getAscending().removeObserver( ascendingConfigObserver );
        noteRepository.getLayoutType().removeObserver( layoutTypeConfigObserver );
    }
}
