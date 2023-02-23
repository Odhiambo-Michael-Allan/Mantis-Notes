package com.mantis.TakeNotes.Models;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.mantis.TakeNotes.Utils.MenuConfigurator;
import com.mantis.TakeNotes.data.source.NoteRepository;
import com.mantis.TakeNotes.data.source.local.Note;
import com.mantis.TakeNotes.data.source.local.Query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NotesViewModel extends ViewModel {

    public static int LAYOUT_STATE_GRID = 1;
    public static int LAYOUT_STATE_LIST = 2;
    public static int LAYOUT_STATE_SIMPLE_LIST = 3;
    public static int TITLE = 6;
    public static int DATE_CREATED = 7;
    public static int DATE_MODIFIED = 8;
    public static int HOME_FRAGMENT = 9;
    public static int FREQUENT_FRAGMENT = 10;
    public static int ARCHIVE_FRAGMENT = 11;
    public static int TRASH_FRAGMENT = 12;
    public static int ASCENDING = 13;
    public static int DESCENDING = 14;

    private NoteRepository noteRepository;
    private int currentLayoutState = LAYOUT_STATE_SIMPLE_LIST;
    private List<Note> cachedNotesList = new ArrayList<>();
    private MutableLiveData<Integer> layoutState = new MutableLiveData<>();

    private FragmentModel homeFragmentModel, frequentFragmentModel,
            archiveFragmentModel, trashFragmentModel;
    private Observer<List<Note>> notesTableObserver;
    private ConfigOptionsModel configOptionsModel;
    private MutableLiveData<Boolean> observableEditStatus = new MutableLiveData<>();
    private MutableLiveData<List<Note>> observableNotesList = new MutableLiveData<>();

    // TODO: Move to database..
    private List<Query> recentQueryList = new ArrayList<>();
    private MutableLiveData<List<Query>> observableRecentQueryList = new MutableLiveData<>();



    public NotesViewModel( NoteRepository noteRepository ) {
        this.noteRepository = noteRepository;
        initializeFragmentModels();
        observableNotesList.postValue( new ArrayList<>() );
        initializeConfigOptionsModel();
        observeNotesTable();
        addListenerToMenuConfigurator();
    }

    public LiveData<List<Note>> getObservableNotesList() {
        return observableNotesList;
    }

    private void initializeFragmentModels() {
        homeFragmentModel = new HomeFragmentModel( this, this.noteRepository );
        frequentFragmentModel = new FrequentFragmentModel( this, this.noteRepository );
        archiveFragmentModel = new ArchiveFragmentModel( this, this.noteRepository );
        trashFragmentModel = new TrashFragmentModel( this, this.noteRepository );
    }

    private void initializeConfigOptionsModel() {
        configOptionsModel = new ConfigOptionsModel( this.noteRepository );
    }

    private void observeNotesTable() {
        notesTableObserver = new Observer<List<Note>>() {
            @Override
            public void onChanged( List<Note> notes ) {
                cachedNotesList = notes;
                observableNotesList.postValue( cachedNotesList );
            }
        };
        this.noteRepository.getAllNotes().observeForever( notesTableObserver );
    }

    public void editMenuSelected() {
        observableEditStatus.postValue( true );
    }

    private void addListenerToMenuConfigurator() {
        MenuConfigurator.addListener( new MenuConfigurator.MenuConfigurationListener() {
            @Override
            public void onEditOptionSelected() {
                editMenuSelected();
            }

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

    public void addNote( Note note ) {
        if ( !( note.getTitle().equals( "" ) && note.getDescription().equals( "" ) ) ) {
            homeFragmentModel.addNote( note );
        }
    }

    public LiveData<Boolean> getObservableEditStatus() {
        return observableEditStatus;
    }

    public void doneEditing() {
        observableEditStatus.postValue( false );
    }


    public LiveData<List<Note>> getHomeFragmentNotesList() {
        return homeFragmentModel.getObservableNotesList();
    }

    public LiveData<List<Note>> getFrequentFragmentNotesList() {
        return frequentFragmentModel.getObservableNotesList();
    }

    public LiveData<List<Note>> getArchiveFragmentNotesList() {
        return archiveFragmentModel.getObservableNotesList();
    }

    public LiveData<List<Note>> getTrashFragmentNotesList() {
        return trashFragmentModel.getObservableNotesList();
    }

    public void deleteHomeFragmentNote( int noteId ) {
        homeFragmentModel.deleteNoteWithId( noteId );
    }

    public void deleteFrequentFragmentNote( int noteId ) {
        frequentFragmentModel.deleteNoteWithId( noteId );
    }

    public void deleteArchiveFragmentNote( int noteId ) {
        archiveFragmentModel.deleteNoteWithId( noteId );
    }

    public void archiveHomeFragmentNote( int noteId ) {
        homeFragmentModel.archiveNoteWithId( noteId );
    }

    public void archiveFrequentFragmentNote( int noteId ) {
        frequentFragmentModel.archiveNoteWithId( noteId );
    }

    public void unarchiveNote( int noteId ) {
        archiveFragmentModel.unarchiveNoteWithId( noteId );
    }

    public void emptyTrashList() {
        trashFragmentModel.deleteAll();
    }


    public void addFrequentFragmentNote( Note note ) {
        frequentFragmentModel.addNote( note );
    }

    public NoteRepository getNoteRepository() {
        return this.noteRepository;
    }

    public void editHomeFragmentNote( int noteId, Note placeHolder ) {
        homeFragmentModel.editNote( noteId, placeHolder );
    }

    public void editFrequentFragmentNote( int noteId, Note placeHolder ) {
        frequentFragmentModel.editNote( noteId, placeHolder );
    }

    public void editArchiveFragmentNote( int noteId, Note placeHolder ) {
        archiveFragmentModel.editNote( noteId, placeHolder );
    }

    public Note getNoteWithId(int noteId ) {
        Iterator i = cachedNotesList.iterator();
        while ( i.hasNext() ) {
            Note note = (Note) i.next();
            if ( note.getId() == noteId )
                return note;
        }
        return null;
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


    public void updateLayoutTypeConfig( int newLayoutTypeConfig ) {
        configOptionsModel.updateLayoutTypeConfig( newLayoutTypeConfig );
    }

    public void updateSortingStrategyConfig( int newSortingStrategyConfig ) {
        configOptionsModel.updateSortingStrategyConfig( newSortingStrategyConfig );
    }

    public void updateAscendingConfig( int newAscendingConfig ) {
        configOptionsModel.updateAscendingConfig( newAscendingConfig );
    }

    public void deleteNotesIn( List<Note> notes ) {
        Iterator i = notes.iterator();
        while ( i.hasNext() ) {
            Note note = ( Note ) i.next();
            if ( note.getOwner() == HOME_FRAGMENT )
                deleteHomeFragmentNote( note.getId() );
            else if ( note.getOwner() == FREQUENT_FRAGMENT )
                deleteFrequentFragmentNote( note.getId() );
            else if ( note.getOwner() == ARCHIVE_FRAGMENT )
                deleteArchiveFragmentNote( note.getId() );
            else
                deleteTrashFragmentNote( note.getId() );
            
        }
    }

    public void archiveNotesIn( List<Note> notes ) {
        Iterator i = notes.iterator();
        while ( i.hasNext() ) {
            Note note = ( Note ) i.next();
            note.moveToArchive( this.noteRepository );
        }
    }

    public void unarchiveNotesIn( List<Note> notes ) {
        for ( Note note : notes )
            note.restore( this.noteRepository );
    }

    public void permanentlyDeleteNotesIn( List<Note> notes ) {
        Iterator i = notes.iterator();
        while ( i.hasNext() ) {
            Note note = ( Note ) i.next();
            deleteTrashFragmentNote( note.getId() );
        }
    }

    public void deleteTrashFragmentNote( int noteId ) {
        trashFragmentModel.deleteNoteWithId( noteId );
    }

    public List<Note> searchNotesMatching(String queryString ) {
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

    @Override
    public void onCleared() {
        // DON'T FORGET TO REMOVE THE OBSERVERS!!
        this.noteRepository.getAllNotes().removeObserver( notesTableObserver );
        homeFragmentModel.onCleared();
        frequentFragmentModel.onCleared();
        archiveFragmentModel.onCleared();
        trashFragmentModel.onCleared();
        configOptionsModel.onCleared();
    }


    public void addQuery( Query query ) {
        recentQueryList.add( query );
        observableRecentQueryList.postValue( recentQueryList );
    }

    public LiveData<List<Query>> getSearchHistory() {
        return observableRecentQueryList;
    }

}
