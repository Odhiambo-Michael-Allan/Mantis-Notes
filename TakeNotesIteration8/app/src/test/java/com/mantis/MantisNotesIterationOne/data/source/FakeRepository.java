package com.mantis.MantisNotesIterationOne.data.source;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mantis.MantisNotesIterationOne.data.source.local.Configuration;
import com.mantis.MantisNotesIterationOne.data.source.local.Note;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.ArchiveNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.FrequentNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.HomeNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.NoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.TrashNoteReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class FakeRepository implements NoteRepository {

    private NoteDataSource noteDataSource = new FakeDataSource();

    public FakeRepository() {}

    @Override
    public void insertNote( Note note ) {
        noteDataSource.insertNote( note );
    }

    @Override
    public LiveData<List<Note>> getNotesById( int[] ids ) {
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
    public void insertHomeNoteReference( NoteReference noteReference ) {
        noteDataSource.insertHomeNoteReference( noteReference );
    }

    @Override
    public LiveData<List<NoteReference>> getHomeNotesReferences() {
        return noteDataSource.getHomeNotesReferences();
    }

    @Override
    public void deleteHomeNoteReference( int noteReferencedId) {
        noteDataSource.deleteHomeNoteReference(noteReferencedId);
    }

    @Override
    public void deleteAllHomeNotesReferences() {
        noteDataSource.deleteAllHomeNotesReferences();
    }

    @Override
    public void insertFrequentNoteReference( NoteReference noteReference ) {
        noteDataSource.insertFrequentNoteReference( noteReference );
    }

    @Override
    public LiveData<List<NoteReference>> getFrequentNoteReferences() {
        return noteDataSource.getFrequentNoteReferences();
    }

    @Override
    public void deleteFrequentNoteReference( int noteReferencedId) {
        noteDataSource.deleteFrequentNoteReference(noteReferencedId);
    }

    @Override
    public void deleteAllFrequentNoteReferences() {
        noteDataSource.deleteAllFrequentNoteReferences();
    }

    @Override
    public void insertArchiveNoteReference( NoteReference noteReference ) {
        noteDataSource.insertArchiveNoteReference( noteReference );
    }

    @Override
    public LiveData<List<NoteReference>> getArchiveNotesReferences() {
        return noteDataSource.getArchiveNotesReferences();
    }

    @Override
    public void deleteArchiveNoteReference( int noteReferencedId) {
        noteDataSource.deleteArchiveNoteReference(noteReferencedId);
    }

    @Override
    public void deleteAllArchiveNotesReferences() {
        noteDataSource.deleteAllArchiveNotesReferences();
    }

    @Override
    public void insertTrashNoteReference( NoteReference noteReference ) {
        noteDataSource.insertTrashNoteReference( noteReference );
    }

    @Override
    public LiveData<List<NoteReference>> getTrashNotesReferences() {
        return noteDataSource.getTrashNotesReferences();
    }

    @Override
    public void deleteTrashNoteReference( int noteReferencedId) {
        noteDataSource.deleteTrashNoteReference(noteReferencedId);
    }

    @Override
    public void deleteAllTrashNotesReferences() {
        noteDataSource.deleteAllTrashNotesReferences();
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
