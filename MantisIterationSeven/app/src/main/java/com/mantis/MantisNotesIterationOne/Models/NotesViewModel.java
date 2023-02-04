package com.mantis.MantisNotesIterationOne.Models;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.mantis.MantisNotesIterationOne.Logger;
import com.mantis.MantisNotesIterationOne.data.source.NoteRepository;
import com.mantis.MantisNotesIterationOne.data.source.local.Note;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.ArchiveNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.FrequentNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.HomeNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.TrashNoteReference;

import java.util.List;

public class NotesViewModel extends ViewModel {

    public static int LAYOUT_STATE_GRID = 1;
    public static int LAYOUT_STATE_LIST = 2;
    public static int LAYOUT_STATE_SIMPLE_LIST = 3;
    public static int TITLE = 6;
    public static int DATE_CREATED = 7;
    public static int DATE_MODIFIED = 8;

    private NoteRepository noteRepository;
    private int currentLayoutState = LAYOUT_STATE_SIMPLE_LIST;
    private MutableLiveData<Integer> layoutState = new MutableLiveData<>();

    private FragmentModel homeFragmentModel, frequentFragmentModel,
            archiveFragmentModel, trashFragmentModel;

    private boolean ascending;
    private int currentSortingStrategy;

    public NotesViewModel( NoteRepository noteRepository ) {
        this.noteRepository = noteRepository;
        clearAllTables();
        initializeFragmentModels();
    }

    private void clearAllTables() {
        this.noteRepository.deleteAllNotes();
        this.noteRepository.deleteAllHomeNotesReferences();
        this.noteRepository.deleteAllFrequentNoteReferences();
        this.noteRepository.deleteAllArchiveNotesReferences();
        this.noteRepository.deleteAllTrashNotesReferences();
    }

    private void initializeFragmentModels() {
        homeFragmentModel = new HomeFragmentModel( this, this.noteRepository );
        frequentFragmentModel = new FrequentFragmentModel( this, this.noteRepository );
        archiveFragmentModel = new ArchiveFragmentModel( this, this.noteRepository );
        trashFragmentModel = new TrashFragmentModel( this, this.noteRepository );
    }

    public void addNote( Note note ) {
        if ( !( note.getTitle().equals( "" ) && note.getDescription().equals( "" ) ) ) {
            Logger.log( "Adding note" );
            this.noteRepository.insertNote( note );
        }
    }

    public boolean noteInHome( int noteId ) {
        return homeFragmentModel.noteInList( noteId );
    }

    public boolean noteInArchives( int noteId ) {
        return archiveFragmentModel.noteInList( noteId );
    }

    public boolean noteInTrash( int noteId ) {
        return trashFragmentModel.noteInList( noteId );
    }

    public LiveData<List<Note>> getHomeFragmentNotesList() {
        return homeFragmentModel.getObservableNotesList();
    }

    public LiveData<List<Note>> getFrequentFragmentNotesList() {
        return frequentFragmentModel.getObservableNotesList();
    }

    public LiveData<List<Note>> getArchiveFragmentNotesList() {
        return archiveFragmentModel.getObservableNotesList();
    }

    public LiveData<List<Note>> getTrashFragmentNotesList() {
        return trashFragmentModel.getObservableNotesList();
    }

    /**
     * When a note is deleted from the home fragment notes list, it is added to the trash
     * fragment notes list..
     * @param referencedNoteId
     */
    public void deleteHomeFragmentNoteReference( int referencedNoteId ) {
        homeFragmentModel.deleteReference( referencedNoteId );
        trashFragmentModel.addNote( referencedNoteId );
    }

    public void deleteFrequentFragmentNoteReference( int referencedNoteId ) {
        frequentFragmentModel.deleteReference( referencedNoteId );
        homeFragmentModel.deleteReference( referencedNoteId );
    }

    public void deleteArchiveFragmentNoteReference( int noteId ) {
        archiveFragmentModel.deleteReference( noteId );
    }

    /**
     * When a note is archived from the home fragment notes list, it is removed from the home
     * fragment notes list and added to the archive fragment notes list..
     * @param noteId
     */
    public void archiveHomeFragmentNote( int noteId ) {
        homeFragmentModel.deleteReference( noteId );
        archiveFragmentModel.addNote( noteId );
    }

    public void archiveFrequentFragmentNote( int noteId ) {
        frequentFragmentModel.deleteReference( noteId );
        archiveFragmentModel.addNote( noteId );
    }

    public void unarchiveNote( int noteId ) {
        Note note = noteRepository.getNotesById( new int[] { noteId } ).getValue().get( 0 );
        note.resetAccessCount();
        archiveFragmentModel.deleteReference( noteId );
        homeFragmentModel.addNote( noteId );
    }

    public void emptyTrashList() {
        trashFragmentModel.deleteAll();
    }


    public void addFrequentFragmentNote( Note note ) {
        frequentFragmentModel.addNote( note.getId() );
    }

    public NoteRepository getNoteRepository() {
        return this.noteRepository;
    }
    @Override
    public void onCleared() {
        // DON'T FORGET TO REMOVE THE OBSERVERS!!
        homeFragmentModel.onCleared();
        frequentFragmentModel.onCleared();
        archiveFragmentModel.onCleared();
        trashFragmentModel.onCleared();
    }

    public void editHomeFragmentNote( int noteId, Note placeHolder ) {
        homeFragmentModel.editNote( noteId, placeHolder );
    }

    public void editFrequentFragmentNote( int noteId, Note placeHolder ) {
        frequentFragmentModel.editNote( noteId, placeHolder );
    }

    public void editArchiveFragmentNote( int noteId, Note placeHolder ) {
        archiveFragmentModel.editNote( noteId, placeHolder );
    }

    public Note getNoteWithId( int noteId ) {
        Note note = noteRepository.getNotesById( new int[] { noteId } ).getValue().get( 0 );
        return note;
    }
}
