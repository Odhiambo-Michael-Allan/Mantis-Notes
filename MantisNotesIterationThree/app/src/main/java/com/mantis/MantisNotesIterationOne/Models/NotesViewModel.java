package com.mantis.MantisNotesIterationOne.Models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class NotesViewModel extends ViewModel {

    private ArrayList<Note> notesList = new ArrayList<>();
    private ArrayList<Note> frequentedNotesList = new ArrayList<>();
    private ArrayList<Note> deletedNotesList = new ArrayList<>();
    private MutableLiveData<ArrayList<Note>> notes = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Note>> frequentedNotes = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Note>> deletedNotes = new MutableLiveData<>();

    public NotesViewModel() {
        notes.postValue( notesList );
    }

    public void addNote( Note note ) {
        notesList.add( note );
        notes.postValue( notesList );
    }

    public LiveData<ArrayList<Note>> getNotes() {
        return notes;
    }

    public LiveData<ArrayList<Note>> getFrequentedNotes() {
        return frequentedNotes;
    }

    public Note getNoteAtPosition( int position ) {
        return notesList.get( position );
    }

    public void setNote( Note note, int position ) {
        Note currentNoteAtPosition = notesList.get( position );
        currentNoteAtPosition.incrementAccessCount();
        currentNoteAtPosition.setTitle( note.getTitle() );
        currentNoteAtPosition.setDescription( note.getDescription() );
        if ( currentNoteAtPosition.getAccessCount() > 3 &&
                !frequentedNotesList.contains( currentNoteAtPosition ) ) {
            frequentedNotesList.add( currentNoteAtPosition );
            frequentedNotes.postValue( frequentedNotesList );
        }
        notes.postValue( notesList );
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
        notes.postValue( notesList );
        frequentedNotes.postValue( frequentedNotesList );
        deletedNotes.postValue( deletedNotesList );
    }

}
