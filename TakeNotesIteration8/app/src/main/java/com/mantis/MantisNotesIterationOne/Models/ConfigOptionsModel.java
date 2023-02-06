package com.mantis.MantisNotesIterationOne.Models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.mantis.MantisNotesIterationOne.Logger;
import com.mantis.MantisNotesIterationOne.data.source.NoteRepository;
import com.mantis.MantisNotesIterationOne.data.source.local.Configuration;

public class ConfigOptionsModel {

    private NoteRepository noteRepository;
    private Observer<Integer> sortingStrategyConfigObserver,
            ascendingConfigObserver, layoutTypeConfigObserver;
    private int currentAscendingConfig = NotesViewModel.ASCENDING;
    private int currentLayoutTypeConfig = NotesViewModel.LAYOUT_STATE_SIMPLE_LIST;
    private int currentSortingStrategyConfig = NotesViewModel.TITLE;
    private MutableLiveData<Integer> observableSortingStrategyConfig = new MutableLiveData<>();
    private MutableLiveData<Integer> observableAscendingConfig = new MutableLiveData<>();
    private MutableLiveData<Integer> observableLayoutTypeConfig = new MutableLiveData<>();

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

    public int getCurrentLayoutTypeConfig() {
        return currentLayoutTypeConfig;
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
