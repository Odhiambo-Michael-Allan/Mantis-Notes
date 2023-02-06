package com.mantis.MantisNotesIterationOne.Models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.mantis.MantisNotesIterationOne.Logger;
import com.mantis.MantisNotesIterationOne.data.source.NoteRepository;
import com.mantis.MantisNotesIterationOne.data.source.local.Configuration;

public class ConfigOptionsModel {

    private NoteRepository noteRepository;
    private Observer<Integer> ascendingConfigObserver, layoutTypeConfigObserver;
    private int currentAscendingConfig = NotesViewModel.ASCENDING;
    private int currentLayoutTypeConfig = NotesViewModel.LAYOUT_STATE_SIMPLE_LIST;
    private int currentSortingStrategyConfig = NotesViewModel.TITLE;
    private MutableLiveData<Integer> observableAscendingConfig = new MutableLiveData<>();
    private MutableLiveData<Integer> observableLayoutTypeConfig = new MutableLiveData<>();

    public ConfigOptionsModel( NoteRepository noteRepository ) {
        this.noteRepository = noteRepository;
        initializeObservers();
        attachObservers();
    }



    private void initializeObservers() {
        initializeAscendingConfigObserver();
        initializeLayoutTypeConfigObserver();
    }

    private void initializeAscendingConfigObserver() {
        ascendingConfigObserver = new Observer<Integer>() {
            @Override
            public void onChanged( Integer integer ) {
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
        if ( observableAscendingConfig.getValue() == null ) {
            Configuration defaultConfiguration = new Configuration(currentAscendingConfig,
                    currentSortingStrategyConfig, currentLayoutTypeConfig );
            noteRepository.insertConfiguration( defaultConfiguration );
        }
    }

    private void attachObservers() {
        observeAscendingConfig();
        observeLayoutTypeConfig();
    }

    private void observeAscendingConfig() {
        LiveData<Integer> ascendingConfig = noteRepository.getAscending();
        ascendingConfig.observeForever( ascendingConfigObserver );
    }

    private void observeLayoutTypeConfig() {
        LiveData<Integer> layoutTypeConfig = noteRepository.getLayoutType();
        layoutTypeConfig.observeForever( layoutTypeConfigObserver );
    }

    public LiveData<Integer> getAscendingConfigOption() {
        if ( observableAscendingConfig.getValue() == null )
            observableAscendingConfig.postValue( currentAscendingConfig );
        return observableAscendingConfig;
    }

    public LiveData<Integer> getLayoutTypeConfigOption() {
        if ( observableLayoutTypeConfig.getValue() == null )
            observableLayoutTypeConfig.postValue( currentLayoutTypeConfig );
        return observableLayoutTypeConfig;
    }

    public int getCurrentLayoutTypeConfig() {
        return currentLayoutTypeConfig;
    }

    public void updateLayoutTypeConfig( int newLayoutTypeConfig ) {
        currentLayoutTypeConfig = newLayoutTypeConfig;
        observableLayoutTypeConfig.postValue( currentLayoutTypeConfig );
        noteRepository.updateLayoutTypeConfig( newLayoutTypeConfig );
    }

    public void onCleared() {
        removeObservers();
    }

    private void removeObservers() {
        noteRepository.getAscending().removeObserver( ascendingConfigObserver );
        noteRepository.getLayoutType().removeObserver( layoutTypeConfigObserver );
    }
}
