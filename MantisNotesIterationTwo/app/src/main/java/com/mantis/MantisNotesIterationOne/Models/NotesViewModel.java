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
        note.setAccessCount( currentNoteAtPosition.getAccessCount() );
        if ( currentNoteAtPosition.getAccessCount() > 3 ) {
            frequentedNotesList.add( note );
            frequentedNotes.postValue( frequentedNotesList );
        }
        notesList.set( position, note );
        notes.postValue( notesList );
    }

    public LiveData<ArrayList<Note>> getDeletedNotes() {
        return this.deletedNotes;
    }

}
