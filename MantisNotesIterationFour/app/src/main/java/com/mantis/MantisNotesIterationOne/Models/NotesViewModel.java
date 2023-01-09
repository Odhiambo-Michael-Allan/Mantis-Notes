package com.mantis.MantisNotesIterationOne.Models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mantis.MantisNotesIterationOne.Logger;
import com.mantis.MantisNotesIterationOne.Utils.MenuConfigurator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class NotesViewModel extends ViewModel {

    public static int VIEW_STATE_GRID = 1;
    public static int VIEW_STATE_LIST = 2;
    public static int VIEW_STATE_SIMPLE_LIST = 3;
    public static int ASCENDING = 4;
    public static int DESCENDING = 5;
    public static int TITLE = 6;
    public static int DATE_CREATED = 7;
    public static int DATE_MODIFIED = 8;

    private ArrayList<Note> notesList = new ArrayList<>();
    private ArrayList<Note> frequentedNotesList = new ArrayList<>();
    private ArrayList<Note> deletedNotesList = new ArrayList<>();
    private MutableLiveData<ArrayList<Note>> notes = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Note>> frequentedNotes = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Note>> deletedNotes = new MutableLiveData<>();
    private int currentViewState = VIEW_STATE_SIMPLE_LIST;
    private MutableLiveData<Integer> currentLayoutState = new MutableLiveData<>();

    // ----------------- Variables holding the sorting strategy -------------------
    private int ascendingOrDescending;
    private int currentSortingStrategy;

    public NotesViewModel() {
        setAscendingOrDescending( ASCENDING );
        setSortingStrategy( TITLE );
        notes.postValue( notesList );
        currentLayoutState.postValue( currentViewState );
        MenuConfigurator.addListener( new MenuConfigurator.MenuConfigurationListener() {
            @Override
            public void onSimpleListOptionSelected() {
                Logger.log( "VIEW MODEL RECEIVED SIMPLE LIST LAYOUT STATE CHANGE REQUEST" );
                currentViewState = VIEW_STATE_SIMPLE_LIST;
                currentLayoutState.postValue( currentViewState );
            }

            @Override
            public void onGridOptionSelected() {
                Logger.log( "VIEW MODEL RECEIVED GRID LAYOUT STATE CHANGE REQUEST" );
                currentViewState = VIEW_STATE_GRID;
                currentLayoutState.postValue( currentViewState );
            }

            @Override
            public void onListOptionSelected() {
                Logger.log( "VIEW MODEL RECEIVED LIST LAYOUT STATE CHANGE REQUEST" );
                currentViewState = VIEW_STATE_LIST;
                currentLayoutState.postValue( currentViewState );
            }

        } );
    }

    private void sortNotesList() {
        if ( currentSortingStrategy == TITLE )
            sortNotesListAccordingToTitle();
        else if ( currentSortingStrategy == DATE_CREATED )
            sortNotesListAccordingToDateCreated();
        else
            sortNotesListAccordingToDateLastModified();
        notes.postValue( notesList );
    }

    private void sortNotesListAccordingToTitle() {
        if ( ascendingOrDescending == ASCENDING )
            sortNotesListAccordingToTitleAscending();
        else
            sortNotesListAccordingToTitleDescending();
    }

    private void sortNotesListAccordingToTitleAscending() {
        ArrayList<Note> notesWithoutTitle = removeNotesWithoutTitle();
        Comparator<Note> titleAscendingComparator = new TitleAscendingComparator();
        Collections.sort( notesList, titleAscendingComparator );
        addNotesWithoutTitleBackToNotesList( notesWithoutTitle );
    }

    private void sortNotesListAccordingToTitleDescending() {
        ArrayList<Note> notesWithoutTitle = removeNotesWithoutTitle();
        Comparator<Note> titleDescendingComparator = new TitleDescendingComparator();
        Collections.sort( notesList, titleDescendingComparator );
        addNotesWithoutTitleBackToNotesList( notesWithoutTitle );
    }

    private ArrayList<Note> removeNotesWithoutTitle() {
        ArrayList<Note> notesWithoutTitle = new ArrayList<>();
        Iterator i = notesList.iterator();
        while ( i.hasNext() ) {
            Note note = ( Note ) i.next();
            if ( note.getTitle().equals( "" ) ) {
                notesWithoutTitle.add( note );
                notesList.remove( note );
            }
        }
        return notesWithoutTitle;
    }

    private void addNotesWithoutTitleBackToNotesList( ArrayList<Note> notesWithoutTitle ) {
        notesList.addAll( notesWithoutTitle );
    }

    // ------------------------------------------------------------------------
    private void sortNotesListAccordingToDateCreated() {
        if ( ascendingOrDescending == ASCENDING )
            sortNotesListAccordingToDateCreatedAscending();
        else
            sortNotesListAccordingToDateCreatedDescending();
    }

    private void sortNotesListAccordingToDateCreatedAscending() {
        Comparator<Note> dateCreatedAscendingComparator = new DateCreatedAscendingComparator();
        Collections.sort( notesList, dateCreatedAscendingComparator );
    }

    private void sortNotesListAccordingToDateCreatedDescending() {
        Comparator<Note> dateCreatedDescendingComparator = new DateCreatedDescendingComparator();
        Collections.sort( notesList, dateCreatedDescendingComparator );
    }

    // -----------------------------------------------------------------------
    private void sortNotesListAccordingToDateLastModified() {
        if ( ascendingOrDescending == ASCENDING )
            sortNotesListAccordingToDateLastModifiedAscending();
        else
            sortNotesListAccordingToDateLastModifiedDescending();
    }

    private void sortNotesListAccordingToDateLastModifiedAscending() {
        Comparator<Note> dateLastModifiedAscendingComparator = new DateLastModifiedAscendingComparator();
        Collections.sort( notesList, dateLastModifiedAscendingComparator );
    }

    private void sortNotesListAccordingToDateLastModifiedDescending() {
        Comparator<Note> dateLastModifiedDescendingComparator = new DateLastModifiedDescendingComparator();
        Collections.sort( notesList, dateLastModifiedDescendingComparator );
    }
    // -----------------------------------------------------------------------


    public int getCurrentSortingStrategy() {
        return currentSortingStrategy;
    }

    public int getAscendingOrDescending() {
        return ascendingOrDescending;
    }

    public int getCurrentLayoutType() {
        return currentViewState;
    }

    public void addNote( Note note ) {
        notesList.add( note );
        sortNotesList();
    }

    public void setSortingStrategy( int sortingStrategy ) {
        Logger.log( "SETTING CURRENT LAYOUT STRATEGY TO: " + sortingStrategy );
        this.currentSortingStrategy = sortingStrategy;
        sortNotesList();
    }

    public void setAscendingOrDescending( int ascendingOrDescending ) {
        Logger.log( "SETTING ASCENDING OR DESCENDING TO: " + ascendingOrDescending );
        this.ascendingOrDescending = ascendingOrDescending;
        sortNotesList();
    }

    public void deleteNoteAt( int position ) {
        notesList.remove( position );
        sortNotesList();
    }

    public LiveData<ArrayList<Note>> getNotes() {
        return notes;
    }

    public LiveData<ArrayList<Note>> getFrequentedNotes() {
        return frequentedNotes;
    }

    public LiveData<Integer> getLayoutState() {
        return currentLayoutState;
    }

    public Note getNoteAtPosition( int position ) {
        return notesList.get( position );
    }

    public void editNote( Note note, int position ) {
        Logger.log( "EDITING NOTE.." );
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
        deletedNotesList.add( note );
        sortNotesList();
        frequentedNotes.postValue( frequentedNotesList );
        deletedNotes.postValue( deletedNotesList );
    }



    // -------------------- Comparators used in sorting -------------------------
    private class TitleAscendingComparator implements Comparator<Note> {

        @Override
        public int compare( Note o1, Note o2 ) {
            return o1.getTitle().compareTo( o2.getTitle() );
        }
    }

    private class TitleDescendingComparator implements Comparator<Note> {

        @Override
        public int compare( Note o1, Note o2 ) {
            return o2.getTitle().compareTo( o1.getTitle() );
        }
    }

    private class DateCreatedAscendingComparator implements Comparator<Note> {

        @Override
        public int compare( Note o1, Note o2 ) {
            if ( o1.getDateCreated().after( o2.getDateCreated() ) )
                return 1;
            else if ( o1.getDateCreated().before( o2.getDateCreated() ) )
                return -1;
            else
                return 0;
        }
    }

    private class DateCreatedDescendingComparator implements Comparator<Note> {

        @Override
        public int compare( Note o1, Note o2 ) {
            if ( o1.getDateCreated().before( o2.getDateCreated() ) )
                return 1;
            else if ( o1.getDateCreated().after( o2.getDateCreated() ) )
                return -1;
            else
                return 0;
        }
    }

    private class DateLastModifiedAscendingComparator implements Comparator<Note> {

        @Override
        public int compare( Note o1, Note o2 ) {
            if ( o1.getDatelastModified().after( o2.getDatelastModified() ) )
                return 1;
            else if ( o1.getDatelastModified().before( o2.getDatelastModified() ) )
                return -1;
            else
                return 0;
        }
    }

    private class DateLastModifiedDescendingComparator implements Comparator<Note> {

        @Override
        public int compare( Note o1, Note o2 ) {
            if ( o1.getDatelastModified().before( o2.getDatelastModified() ) )
                return 1;
            else if ( o1.getDatelastModified().after( o2.getDatelastModified() ) )
                return -1;
            else
                return 0;
        }
    }




}
