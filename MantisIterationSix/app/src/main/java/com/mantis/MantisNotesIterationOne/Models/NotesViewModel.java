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
    private ArrayList<Note> trashNotesList = new ArrayList<>();
    private ArrayList<Note> archivedNotesList = new ArrayList<>();
    private MutableLiveData<ArrayList<Note>> notes = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Note>> frequentedNotes = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Note>> trashedNotes = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Note>> archivedNotes = new MutableLiveData<>();
    private int currentLayoutState = LAYOUT_STATE_SIMPLE_LIST;
    private MutableLiveData<Integer> layoutState = new MutableLiveData<>();

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
        if ( note.getTitle().equals( "" ) && note.getDescription().equals( "" ) )
            return;
        notesList.add( note );
        sortNotesList();
    }


    public void archiveNoteFromNotesListAt( int position ) {
        this.archiveNote( notesList, position );
    }

    public void archiveNoteFromFrequentedListAt( int position ) {
        this.archiveNote( frequentedNotesList, position );
    }

    private void archiveNote( ArrayList<Note> list, int position ) {
        Note note = list.get( position );
        notesList.remove( note );
        sortNotesList();
        frequentedNotesList.remove( note );
        archivedNotesList.add( note );
        frequentedNotes.postValue( frequentedNotesList );
        archivedNotes.postValue( archivedNotesList );
    }

    public void unarchiveNoteAt( int position ) {
        Note note = archivedNotesList.get( position );
        archivedNotesList.remove( note );
        archivedNotes.postValue( archivedNotesList );
        notesList.add( note );
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

    public Note getTrashedNoteAt(int position ) {
        return trashNotesList.get( position );
    }

    public int getCurrentLayoutState() {
        return this.currentLayoutState;
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

    public LiveData<ArrayList<Note>> getTrashedNotes() {
        return this.trashedNotes;
    }

    public void permanentlyDeleteNote( Note note ) {
        trashNotesList.remove( note );
        trashedNotes.postValue(trashNotesList);
    }

    public void trashNoteFromNotesListAtPosition(int position ) {
        this.trashNote( notesList, position );
    }

    public void trashNoteFromFrequentListAtPosition(int position ) {
        this.trashNote( frequentedNotesList, position );
    }

    public void trashNoteFromArchiveListAtPosition(int position ) {
        this.trashNote( archivedNotesList, position );
    }

    private void trashNote( ArrayList<Note> notesList, int position ) {
        Note note = notesList.get( position );
        this.notesList.remove( note );
        frequentedNotesList.remove( note );
        archivedNotesList.remove( note );
        trashNotesList.add( note );
        sortNotesList();
        frequentedNotes.postValue( frequentedNotesList );
        trashedNotes.postValue(trashNotesList);
        archivedNotes.postValue( archivedNotesList );
    }

    public void emptyTrashList() {
        trashNotesList.clear();
        trashedNotes.postValue( trashNotesList );
    }

    public void editNoteInNotesList( Note note, int position ) {
        Note currentNoteAtPosition = notesList.get( position );
        if ( note.getTitle().equals( "" ) && note.getDescription().equals( "" ) ) {
            notesList.remove( currentNoteAtPosition );
            sortNotesList();
            frequentedNotesList.remove( currentNoteAtPosition );
            frequentedNotes.postValue( frequentedNotesList );
            return;
        }
        if ( noteHasBeenModified( note, currentNoteAtPosition ) ) {
            currentNoteAtPosition.setDateLastModified( note.getDateCreated() );
        }
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

    public void editNoteInFrequentsList( Note note, int position ) {
        Note currentNoteAtPosition = frequentedNotesList.get( position );
        if ( note.getTitle().equals( "" ) && note.getDescription().equals( "" ) ) {
            notesList.remove( currentNoteAtPosition );
            sortNotesList();
            frequentedNotesList.remove( currentNoteAtPosition );
        }
        if ( noteHasBeenModified( note, currentNoteAtPosition ) ) {
            currentNoteAtPosition.setDateLastModified( note.getDateCreated() );
        }
        currentNoteAtPosition.incrementAccessCount();
        currentNoteAtPosition.setTitle( note.getTitle() );
        currentNoteAtPosition.setDescription( note.getDescription() );
        frequentedNotes.postValue( frequentedNotesList );
        sortNotesList();
    }


    public void editNoteInArchivesList( Note note, int position ) {
        Note currentNoteAtPosition = archivedNotesList.get( position );
        if ( note.getTitle().equals( "" ) && note.getDescription().equals( "" ) ) {
            archivedNotesList.remove( currentNoteAtPosition );
        }
        if ( noteHasBeenModified( note, currentNoteAtPosition ) ) {
            currentNoteAtPosition.setDateLastModified( note.getDateCreated() );
        }
        currentNoteAtPosition.incrementAccessCount();
        currentNoteAtPosition.setTitle( note.getTitle() );
        currentNoteAtPosition.setDescription( note.getDescription() );
        archivedNotes.postValue( archivedNotesList );
    }

    private boolean noteHasBeenModified( Note note, Note currentNoteAtPosition ) {
        return !( note.getTitle().equals( currentNoteAtPosition.getTitle() )
                && note.getDescription().equals( currentNoteAtPosition.getDescription() ) );
    }

}
