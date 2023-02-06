package com.mantis.MantisNotesIterationOne.data.source;


// SINGLETON..

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.mantis.MantisNotesIterationOne.data.source.local.Configuration;
import com.mantis.MantisNotesIterationOne.data.source.local.LocalDataSource;
import com.mantis.MantisNotesIterationOne.data.source.local.Note;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.NoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteRoomDatabase;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultNoteRepository implements NoteRepository {

    private static DefaultNoteRepository INSTANCE;
    private NoteDataSource noteDataSource;

    public DefaultNoteRepository( NoteDataSource noteDataSource ) {
        this.noteDataSource = noteDataSource;
    }

    public static DefaultNoteRepository getRepository( Application application ) {
        if ( INSTANCE == null ) {
            synchronized( DefaultNoteRepository.class ) {
                if ( INSTANCE == null ) {
                    NoteRoomDatabase database = NoteRoomDatabase.getDatabase( application );
                    INSTANCE = new DefaultNoteRepository( new LocalDataSource( database.noteDao(),
                            database.homeNotesDao(), database.frequentNotesDao(),
                            database.archiveNotesDao(), database.trashNotesDao(),
                            database.configurationsDao() ) );
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public LiveData<List<Note>> getNotesById( int[] ids) {
        return this.noteDataSource.getNotesById( ids );
    }

    @Override
    public LiveData<List<Note>> getAllNotes() {
        return this.noteDataSource.getAllNotes();
    }

    @Override
    public void insertNote( Note note ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.insertNote( note ); } );
        executor.shutdown();
    }

    @Override
    public void deleteNote( int noteId ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.deleteNote( noteId ); } );
        executor.shutdown();
    }

    @Override
    public void deleteAllNotes() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.deleteAllNotes(); } );
        executor.shutdown();
    }

    @Override
    public void updateNote( int noteId, String newTitle, String newDescription,
                            Date dateLastModified, int newAccessCount ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.updateNote( noteId, newTitle, newDescription,
                dateLastModified, newAccessCount ); } );
        executor.shutdown();
    }

    @Override
    public void updateNoteOwner( int noteId, int newOwner ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.updateNoteOwner( noteId, newOwner ); } );
        executor.shutdown();
    }

    @Override
    public LiveData<List<NoteReference>> getHomeNotesReferences() {
        return this.noteDataSource.getHomeNotesReferences();
    }

    @Override
    public void insertHomeNoteReference( NoteReference homeNoteReference ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.insertHomeNoteReference( homeNoteReference ); } );
        executor.shutdown();
    }

    @Override
    public void deleteHomeNoteReference( int noteReferencedId) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.deleteHomeNoteReference(noteReferencedId); } );
        executor.shutdown();
    }

    @Override
    public void deleteAllHomeNotesReferences() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.deleteAllHomeNotesReferences(); } );
        executor.shutdown();
    }

    @Override
    public void deleteFrequentNoteReference( int noteReferencedId) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.deleteFrequentNoteReference(noteReferencedId); } );
        executor.shutdown();
    }

    @Override
    public void deleteAllFrequentNoteReferences() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.deleteAllFrequentNoteReferences(); } );
        executor.shutdown();
    }

    @Override
    public void insertFrequentNoteReference( NoteReference reference ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.insertFrequentNoteReference( reference ); } );
        executor.shutdown();
    }

    @Override
    public LiveData<List<NoteReference>> getFrequentNoteReferences() {
        return this.noteDataSource.getFrequentNoteReferences();
    }

    @Override
    public void insertArchiveNoteReference( NoteReference reference ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.insertArchiveNoteReference( reference ); } );
        executor.shutdown();
    }

    @Override
    public LiveData<List<NoteReference>> getArchiveNotesReferences() {
        return this.noteDataSource.getArchiveNotesReferences();
    }

    @Override
    public void deleteArchiveNoteReference( int noteReferencedId) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.deleteArchiveNoteReference(noteReferencedId); } );
        executor.shutdown();
    }

    @Override
    public void deleteAllArchiveNotesReferences() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.deleteAllArchiveNotesReferences(); } );
        executor.shutdown();
    }

    @Override
    public void insertTrashNoteReference( NoteReference reference ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.insertTrashNoteReference( reference ); } );
        executor.shutdown();
    }

    @Override
    public LiveData<List<NoteReference>> getTrashNotesReferences() {
        return this.noteDataSource.getTrashNotesReferences();
    }

    @Override
    public void deleteTrashNoteReference( int noteReferencedId) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.deleteTrashNoteReference(noteReferencedId); } );
        executor.shutdown();
    }

    @Override
    public void deleteAllTrashNotesReferences() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.deleteAllTrashNotesReferences(); } );
        executor.shutdown();
    }

    @Override
    public void insertConfiguration( Configuration configuration ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.insertConfiguration( configuration ); } );
        executor.shutdown();
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
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.updateLayoutTypeConfig( newLayoutTypeConfig ); } );
        executor.shutdown();
    }

    @Override
    public void updateAscendingConfig( int newAscendingConfig ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.updateAscendingConfig( newAscendingConfig ); } );
        executor.shutdown();
    }

    @Override
    public void updateSortingStrategyConfig( int newSortingStrategyConfig ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> { this.noteDataSource.updateSortingStrategyConfig( newSortingStrategyConfig ); } );
        executor.shutdown();
    }
}
