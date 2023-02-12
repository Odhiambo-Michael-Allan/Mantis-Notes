package com.mantis.TakeNotes.data.source;

import androidx.lifecycle.LiveData;

import com.mantis.TakeNotes.data.source.local.Configuration;
import com.mantis.TakeNotes.data.source.local.Note;

import java.util.Date;
import java.util.List;

public class FakeRepository implements NoteRepository {

    private NoteDataSource noteDataSource = new FakeDataSource();

    public FakeRepository() {}

    @Override
    public void insertNote( Note note ) {
        noteDataSource.insertNote( note );
    }

    @Override
    public LiveData<List<Note>> getNotesById(int[] ids ) {
        return noteDataSource.getNotesById( ids );
    }

    @Override
    public LiveData<List<Note>> getAllNotes() {
        return noteDataSource.getAllNotes();
    }

    @Override
    public void deleteNote( int noteId ) {
        noteDataSource.deleteNote( noteId );
    }

    @Override
    public void deleteAllNotes() {
        noteDataSource.deleteAllNotes();
    }

    @Override
    public void updateNote( int noteId, String newTitle, String newDescription,
                            Date dateLastModified, int newAccessCount ) {
        noteDataSource.updateNote( noteId, newTitle, newDescription, dateLastModified,
                newAccessCount );
    }

    @Override
    public void updateNoteOwner( int noteId, int newOwner ) {
        noteDataSource.updateNoteOwner( noteId, newOwner );
    }

    @Override
    public void insertConfiguration( Configuration configuration ) {

    }

    @Override
    public LiveData<Integer> getAscending() {
        return noteDataSource.getAscending();
    }

    @Override
    public LiveData<Integer> getSortingStrategy() {
        return noteDataSource.getSortingStrategy();
    }

    @Override
    public LiveData<Integer> getLayoutType() {
        return noteDataSource.getLayoutType();
    }

    @Override
    public void updateLayoutTypeConfig( int newLayoutTypeConfig ) {
        noteDataSource.updateLayoutTypeConfig( newLayoutTypeConfig );
    }

    @Override
    public void updateAscendingConfig(int newAscendingConfig) {

    }

    @Override
    public void updateSortingStrategyConfig(int newSortingStrategyConfig) {

    }
}
