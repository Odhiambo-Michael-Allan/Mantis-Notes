package com.mantis.takenotes.Adapters;

/**
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.mantis.takenotes.Models.NotesViewModel;
import com.mantis.takenotes.Utils.SortingUtil;
import com.mantis.takenotes.Repository.Note;

import com.mantis.takenotes.ViewHolders.NoteViewHolder;
import java.util.ArrayList;
import java.util.Iterator;

import java.util.List;

/**
 * @author - Michael Allan Odhiambo
 * @email - odhiambomichaelallan@gmail.com
 * @date - 4th March 2023
 */

public abstract class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private final MutableLiveData<Boolean> allNotesAreChecked = new MutableLiveData<>();
    private final MutableLiveData<Integer> observableNumberOfCheckedNotes = new MutableLiveData<>();
    private final ArrayList<NoteAdapterListener> listeners = new ArrayList<>();
    private final ArrayList<NoteViewHolder> noteViewHolders = new ArrayList<>();
    private final MutableLiveData<Boolean> observableNoNoteIsChecked = new MutableLiveData<>();

    private NotesViewModel notesViewModel;
    private boolean editInProgress;
    private List<Note> data = new ArrayList<>();
    private int numberOfCheckedNotes = 0;
    private String queryToHighlight = null;

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        View view = getNoteView( parent );
        NoteViewHolder noteViewHolder = getNoteViewHolder( view, this );
        noteViewHolder.addListener( new NoteViewHolder.NoteViewHolderListener() {
            @Override
            public void onClick( View view, int viewHolderPosition ) {
                notifyListenersOfViewHolderClick( view, viewHolderPosition );
            }
            @Override
            public void onLongClick( int viewHolderPosition ) {
                notifyListenersOfViewHolderLongClick( viewHolderPosition );
            }
            @Override
            public void onCheckButtonChange( boolean check ) {
                int adapterPosition = noteViewHolder.getAdapterPosition();
                if ( adapterPosition >= 0 ) {
                    getData().get( adapterPosition ).setChecked( check );
                    updateNumberOfCheckedNotes();
                }
                sendNoNoteIsCheckedNotification();
                allNotesAreChecked.setValue( numberOfCheckedNotes == getData().size() );
                observableNumberOfCheckedNotes.postValue( numberOfCheckedNotes );
            }
        } );
        noteViewHolders.add( noteViewHolder );
        return noteViewHolder;
    }

    private void notifyListenersOfViewHolderClick( View view, int viewHolderPosition ) {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            NoteAdapterListener listener = ( NoteAdapterListener ) i.next();
            listener.onViewHolderClicked( view, viewHolderPosition );
        }
    }

    private void notifyListenersOfViewHolderLongClick( int viewHolderPosition ) {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            NoteAdapterListener listener = ( NoteAdapterListener ) i.next();
            listener.onViewHolderLongClicked( viewHolderPosition );
        }
    }

    private void updateNumberOfCheckedNotes() {
        numberOfCheckedNotes = 0;
        Iterator i = getData().iterator();
        while ( i.hasNext() ) {
            Note note = (Note) i.next();
            if ( note.isChecked() )
                numberOfCheckedNotes++;
        }
        observableNumberOfCheckedNotes.postValue( numberOfCheckedNotes );
    }

    private void sendNoNoteIsCheckedNotification() {
        observableNoNoteIsChecked.postValue( numberOfCheckedNotes == 0 );
    }

    @Override
    public void onBindViewHolder( @NonNull NoteViewHolder holder, int position ) {
        Note noteAtPosition = getData().get( position );
        if ( editInProgress )
            holder.showCheckBox();
        if ( noteAtPosition.isChecked() )
            holder.check( true );
        else
            holder.check( false );
        holder.bindData( data.get( position ) );
        holder.setPosition( position );
        if ( queryToHighlight != null )
            holder.highlightQuery( queryToHighlight );
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData( List<Note> data ) {
        this.data = data;
        if ( data.size() == 0 )
            notifyListenersRecyclerViewEmpty( true );
        else
            notifyListenersRecyclerViewEmpty( false );
        sortData();
        notifyListenersOfNoteSizeChange( getData().size() );
        notifyDataSetChanged();
    }

    private void notifyListenersRecyclerViewEmpty( boolean empty ) {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            NoteAdapterListener listener = ( NoteAdapterListener ) i.next();
            listener.onRecyclerViewEmpty( empty );
        }
    }

    public void setNotesViewModel( NotesViewModel viewModel ) {
        this.notesViewModel = viewModel;
    }

    public void sortData() {
        List<Note> data = SortingUtil.sortList( getData(),
                notesViewModel.getCurrentAscendingConfigOption(),
                notesViewModel.getCurrentSortingStrategyConfigOption() );
        this.data = data;
        notifyDataSetChanged();

    }

    public void addListener( NoteAdapterListener listener ) {
        listeners.add( listener );
    }

    public void checkAllNotes( boolean check ) {
        Iterator i = getData().iterator();
        while ( i.hasNext() ) {
            Note note = ( Note ) i.next();
            note.setChecked( check );
        }
        notifyDataSetChanged();
    }

    public void editStatusChanged( boolean edit ) {
        this.editInProgress = edit;
        numberOfCheckedNotes = 0;
        sendNoNoteIsCheckedNotification();
        if ( edit )
            notifyViewHoldersToShowCheckBox();
        else {
            checkAllNotes( false );
            notifyViewHoldersToHideCheckBox();
        }
    }

    public void notifyViewHoldersToShowCheckBox() {
        Iterator i = noteViewHolders.iterator();
        while ( i.hasNext() ) {
            NoteViewHolder noteViewHolder = ( NoteViewHolder ) i.next();
            noteViewHolder.showCheckBox();
        }
    }

    public void notifyViewHoldersToHideCheckBox() {
        Iterator i = noteViewHolders.iterator();
        while ( i.hasNext() ) {
            NoteViewHolder noteViewHolder = ( NoteViewHolder ) i.next();
            noteViewHolder.hideCheckBox();
        }
    }

    private void notifyListenersOfNoteSizeChange( int newSize ) {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            NoteAdapterListener listener = ( NoteAdapterListener ) i.next();
            listener.onNoteNotesSizeChange( newSize );
        }
    }

    public List<Note> getSelectedNotes() {
        List<Note> selectedNotes = new ArrayList<>();
        Iterator i = getData().iterator();
        while ( i.hasNext() ) {
            Note note = ( Note ) i.next();
            if ( note.isChecked() )
                selectedNotes.add( note );
        }
        return selectedNotes;
    }

    public void notifyViewHoldersToHighlightTextMatching( String query ) {
        queryToHighlight = query;
        notifyDataSetChanged();
    }

    public LiveData<Boolean> getObservableNoNoteIsChecked() {
        return observableNoNoteIsChecked;
    }

    public LiveData<Integer> getObservableNumberOfCheckedNotes() {
        return observableNumberOfCheckedNotes;
    }

    protected abstract View getNoteView( @NonNull ViewGroup parent );

    protected abstract NoteViewHolder getNoteViewHolder( View view, NotesAdapter adapter );

    public List<Note> getData() {
        return this.data;
    }

    public LiveData<Boolean> getAllNotesAreCheckedStatus() {
        return allNotesAreChecked;
    }

    public interface NoteAdapterListener {
        void onViewHolderClicked( View view, int viewHolderPosition );
        void onViewHolderLongClicked( int viewHolderPosition );
        void onRecyclerViewEmpty( boolean isEmpty );
        void onNoteNotesSizeChange( int newNotesSize );
    }
}
