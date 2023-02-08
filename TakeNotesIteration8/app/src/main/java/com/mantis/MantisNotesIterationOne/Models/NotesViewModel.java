package com.mantis.MantisNotesIterationOne.Models;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.mantis.MantisNotesIterationOne.Utils.MenuConfigurator;
import com.mantis.MantisNotesIterationOne.data.source.NoteRepository;
import com.mantis.MantisNotesIterationOne.data.source.local.Note;

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
    // Cache the contents of notesTable here to make access easier..
    private List<Note> notesTableCache = new ArrayList<>();
    private MutableLiveData<Integer> layoutState = new MutableLiveData<>();

    private FragmentModel homeFragmentModel, frequentFragmentModel,
            archiveFragmentModel, trashFragmentModel;
    private Observer<List<Note>> notesTableObserver;
    private ConfigOptionsModel configOptionsModel;

    private MutableLiveData<Boolean> observableEditStatus = new MutableLiveData<>();

    public NotesViewModel( NoteRepository noteRepository ) {
        this.noteRepository = noteRepository;
        initializeFragmentModels();
        initializeConfigOptionsModel();
        observeNotesTable();
        addListenerToMenuConfigurator();
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
                notesTableCache = notes;
            }
        };
        this.noteRepository.getAllNotes().observeForever( notesTableObserver );
    }

    private void addListenerToMenuConfigurator() {
        MenuConfigurator.addListener( new MenuConfigurator.MenuConfigurationListener() {
            @Override
            public void onEditOptionSelected() {
                observableEditStatus.postValue( true );
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
            note.setOwner( HOME_FRAGMENT );
            this.noteRepository.insertNote( note );
        }
    }

    public LiveData<Boolean> getObservableEditStatus() {
        return observableEditStatus;
    }

    public void doneEditing() {
        observableEditStatus.postValue( false );
    }

    public boolean noteInHome( int noteId ) {
        return homeFragmentModel.noteInList( noteId );
    }

    public boolean noteInArchives( int noteId ) {
        return archiveFragmentModel.noteInList( noteId );
    }

    public boolean noteInTrash( int noteId ) {
        return trashFragmentModel.noteInList( noteId );
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


    /**
     * When a note is deleted from the home fragment notes list, it is added to the trash
     * fragment notes list..
     * @param referencedNoteId
     */
    public void deleteHomeFragmentNoteReference( int referencedNoteId ) {
        homeFragmentModel.deleteReference( referencedNoteId );
        trashFragmentModel.addNote( referencedNoteId );
    }

    public void deleteFrequentFragmentNoteReference( int referencedNoteId ) {
        frequentFragmentModel.deleteReference( referencedNoteId );
    }

    public void deleteArchiveFragmentNoteReference( int noteId ) {
        archiveFragmentModel.deleteReference( noteId );
    }

    /**
     * When a note is archived from the home fragment notes list, it is removed from the home
     * fragment notes list and added to the archive fragment notes list..
     * @param noteId
     */
    public void archiveHomeFragmentNote( int noteId ) {
        homeFragmentModel.deleteReference( noteId );
        archiveFragmentModel.addNote( noteId );
    }

    public void archiveFrequentFragmentNote( int noteId ) {
        frequentFragmentModel.deleteReference( noteId );
        archiveFragmentModel.addNote( noteId );
    }

    public void unarchiveNote( int noteId ) {
        Note note = noteRepository.getNotesById( new int[] { noteId } ).getValue().get( 0 );
        note.resetAccessCount();
        archiveFragmentModel.deleteReference( noteId );
        homeFragmentModel.addNote( noteId );
    }

    public void emptyTrashList() {
        trashFragmentModel.deleteAll();
    }


    public void addFrequentFragmentNote( Note note ) {
        frequentFragmentModel.addNote( note.getId() );
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

    public Note getNoteWithId( int noteId ) {
        Iterator i = notesTableCache.iterator();
        while ( i.hasNext() ) {
            Note note = ( Note ) i.next();
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

    public int getCurrentLayoutTypeConfig() {
        return configOptionsModel.getCurrentLayoutTypeConfig();
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

    public void deleteReferencesIn( List<Note> notes ) {
        Iterator i = notes.iterator();
        while ( i.hasNext() ) {
            Note note = ( Note ) i.next();
            homeFragmentModel.deleteReference( note.getId() );
            frequentFragmentModel.deleteReference( note.getId() );
            archiveFragmentModel.deleteReference( note.getId() );
            trashFragmentModel.deleteReference( note.getId() );
        }
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

}
