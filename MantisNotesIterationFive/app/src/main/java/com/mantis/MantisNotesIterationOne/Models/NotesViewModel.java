package com.mantis.MantisNotesIterationOne.Models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mantis.MantisNotesIterationOne.Utils.MenuConfigurator;
import com.mantis.MantisNotesIterationOne.Utils.SortingUtil;

import java.util.ArrayList;

public class NotesViewModel extends ViewModel {

    public static int LAYOUT_STATE_GRID = 1;
    public static int LAYOUT_STATE_LIST = 2;
    public static int LAYOUT_STATE_SIMPLE_LIST = 3;
    public static int TITLE = 6;
    public static int DATE_CREATED = 7;
    public static int DATE_MODIFIED = 8;

    private ArrayList<Note> notesList = new ArrayList<>();
    private ArrayList<Note> frequentedNotesList = new ArrayList<>();
    private ArrayList<Note> deletedNotesList = new ArrayList<>();
    private ArrayList<Note> archivedNotesList = new ArrayList<>();
    private MutableLiveData<ArrayList<Note>> notes = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Note>> frequentedNotes = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Note>> deletedNotes = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Note>> archivedNotes = new MutableLiveData<>();
    private int currentLayoutState = LAYOUT_STATE_SIMPLE_LIST;
    private MutableLiveData<Integer> layoutState = new MutableLiveData<>();

    // ----------------- Variables holding the sorting strategy -------------------
    private boolean ascending;
    private int currentSortingStrategy;

    public NotesViewModel() {
        setAscending( true );
        setSortingStrategy( TITLE );
        setNotesList( notesList );
        setCurrentLayoutState( currentLayoutState );

        MenuConfigurator.addListener( new MenuConfigurator.MenuConfigurationListener() {
            @Override
            public void onSimpleListOptionSelected() {
                setCurrentLayoutState( LAYOUT_STATE_SIMPLE_LIST );
            }

            @Override
            public void onGridOptionSelected() {
                setCurrentLayoutState( LAYOUT_STATE_GRID );
            }

            @Override
            public void onListOptionSelected() {
                setCurrentLayoutState( LAYOUT_STATE_LIST );
            }
        } );
    }

    public void setAscending( boolean ascending ) {
        this.ascending = ascending;
        sortNotesList();
    }

    public void setSortingStrategy( int sortingStrategy ) {
        this.currentSortingStrategy = sortingStrategy;
        sortNotesList();
    }

    public void setNotesList( ArrayList<Note> notesList ) {
        this.notesList = notesList;
        notes.postValue( notesList );
    }

    public void setCurrentLayoutState( int currentLayoutState ) {
        this.currentLayoutState = currentLayoutState;
        layoutState.postValue(currentLayoutState);
    }

    private void sortNotesList() {
        notesList = SortingUtil.sortList( notesList, getAscending(), getCurrentSortingStrategy() );
        notes.postValue( notesList );
    }


    public int getCurrentSortingStrategy() {
        return currentSortingStrategy;
    }

    public boolean getAscending() {
        return ascending;
    }

    public LiveData<Integer> getLayoutState() {
        return this.layoutState;
    }

    public void addNote( Note note ) {
        notesList.add( note );
        sortNotesList();
    }





    public void archiveNoteAt( int position ) {
        Note note = notesList.get( position );
        archivedNotesList.add( note );
        frequentedNotesList.remove( note );
        notesList.remove( note );
        frequentedNotes.postValue( frequentedNotesList );
        archivedNotes.postValue( archivedNotesList );
        sortNotesList();
    }

    public Note getNoteAt( int position ) {
        return notesList.get( position );
    }

    public Note getFrequentedNoteAt( int position ) {
        return frequentedNotesList.get( position );
    }

    public Note getArchivedNoteAt( int position ) {
        return archivedNotesList.get( position );
    }

    public Note getDeletedNoteAt( int position ) {
        return deletedNotesList.get( position );
    }

    public LiveData<ArrayList<Note>> getNotes() {
        return notes;
    }

    public LiveData<ArrayList<Note>> getFrequentedNotes() {
        return frequentedNotes;
    }

    public LiveData<ArrayList<Note>> getArchivedNotes() {
        return this.archivedNotes;
    }

    public int getCurrentLayoutState() {
        return this.currentLayoutState;
    }


    public Note getNoteAtPosition( int position ) {
        return notesList.get( position );
    }

    public void editNote( Note note, int position, ArrayList<Note> notesList ) {
        Note currentNoteAtPosition = notesList.get( position );

        if ( !( note.getTitle().equals( currentNoteAtPosition.getTitle() ) && note.getDescription().equals( currentNoteAtPosition.getDescription() ) ) )
            currentNoteAtPosition.setDateLastModified( note.getDateCreated() );

        currentNoteAtPosition.incrementAccessCount();
        currentNoteAtPosition.setTitle( note.getTitle() );
        currentNoteAtPosition.setDescription( note.getDescription() );

        if ( currentNoteAtPosition.getAccessCount() > 3 &&
                !frequentedNotesList.contains( currentNoteAtPosition ) ) {
            frequentedNotesList.add( currentNoteAtPosition );
            frequentedNotes.postValue( frequentedNotesList );
        }
        sortNotesList();
    }

    public LiveData<ArrayList<Note>> getDeletedNotes() {
        return this.deletedNotes;
    }

    public void deleteNote( Note note ) {
        if ( noteInDeletedNotes( note ) )
            permanentlyDeleteNote( note );
        else
            addNoteToTrash( note );
    }

    private boolean noteInDeletedNotes( Note note ) {
        return deletedNotesList.contains( note );
    }

    private void permanentlyDeleteNote( Note note ) {
        deletedNotesList.remove( note );
        deletedNotes.postValue( deletedNotesList );
    }

    private void addNoteToTrash( Note note ) {
        notesList.remove( note );
        frequentedNotesList.remove( note );
        archivedNotesList.remove( note );
        deletedNotesList.add( note );
        sortNotesList();
        frequentedNotes.postValue( frequentedNotesList );
        archivedNotes.postValue( archivedNotesList );
        deletedNotes.postValue( deletedNotesList );
    }

    public void deleteNoteFromNotesList( int position ) {
        Note note = notesList.get( position );
        notesList.remove( note );
        frequentedNotesList.remove( note );
        deletedNotesList.add( note );
        sortNotesList();
        frequentedNotes.postValue( frequentedNotesList );
        deletedNotes.postValue( deletedNotesList );
    }

    public void deleteNoteFromFrequentList( int position ) {
        Note note = notesList.get( position );
        frequentedNotesList.remove( note );
        notesList.remove( note );
        deletedNotesList.add( note );
        sortNotesList();
        frequentedNotes.postValue( frequentedNotesList );
        deletedNotes.postValue( deletedNotesList );
    }

    public void deleteNoteFromArchiveList( int position ) {
        Note note = archivedNotesList.get( position );
        archivedNotesList.remove( note );
        deletedNotesList.add( note );
        archivedNotes.postValue( archivedNotesList );
        deletedNotes.postValue( deletedNotesList );
    }

    public void editNoteInNotesList( Note note, int position ) {
        this.editNote( note, position, notesList );
    }

    public void editNoteInFrequentsList( Note note, int position ) {
        this.editNote( note, position, frequentedNotesList );
    }

    public void editNoteInArchivesList( Note note, int position ) {
        this.editNote( note, position, archivedNotesList );
    }

}
