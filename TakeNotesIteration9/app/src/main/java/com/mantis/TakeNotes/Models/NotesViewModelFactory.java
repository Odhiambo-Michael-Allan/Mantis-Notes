package com.mantis.TakeNotes.Models;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mantis.TakeNotes.data.source.NoteRepository;

public class NotesViewModelFactory implements ViewModelProvider.Factory {

    private NoteRepository noteRepository;

    public NotesViewModelFactory( NoteRepository noteRepository ) {
        this.noteRepository = noteRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create( @NonNull Class<T> modelClass ) {
        return ( T ) new NotesViewModel( noteRepository );
    }
}
