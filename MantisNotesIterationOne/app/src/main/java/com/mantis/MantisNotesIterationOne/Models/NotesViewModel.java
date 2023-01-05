package com.mantis.MantisNotesIterationOne.Models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class NotesViewModel extends ViewModel {

    private ArrayList<Note> notesList = new ArrayList<>();
    private MutableLiveData<ArrayList<Note>> notes = new MutableLiveData<>();

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
}
