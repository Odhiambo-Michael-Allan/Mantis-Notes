package com.mantis.MantisNotesIterationOne.Adapters;

import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.mantis.MantisNotesIterationOne.Models.NotesViewModel;
import com.mantis.MantisNotesIterationOne.Utils.Logger;
import com.mantis.MantisNotesIterationOne.Utils.SortingUtil;
import com.mantis.MantisNotesIterationOne.data.source.local.Note;
import com.mantis.MantisNotesIterationOne.ViewHolders.NoteViewHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder> {


    private List<Note> data;
    private View emptyView;
    private NotesViewModel notesViewModel;
    private boolean editInProgress;
    private ArrayList<NoteAdapterListener> listeners = new ArrayList<>();
    private ArrayList<NoteViewHolder> noteViewHolders = new ArrayList<>();

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
                if ( adapterPosition >= 0 )
                    getData().get( adapterPosition ).setChecked( check );
            }
        } );
        noteViewHolders.add( noteViewHolder );
        return noteViewHolder;
    }

    protected abstract View getNoteView( @NonNull ViewGroup parent );

    protected abstract NoteViewHolder getNoteViewHolder( View view, NotesAdapter adapter );

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
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData( List<Note> data ) {
        this.data = data;
        if ( data.size() == 0 ) {
            notifyListenersRecyclerViewEmpty( true );
            showEmptyView( true );
        }
        else {
            notifyListenersRecyclerViewEmpty( false );
            showEmptyView( false );
        }
        sortData();
        notifyDataSetChanged();
    }

    private void notifyListenersRecyclerViewEmpty( boolean empty ) {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            NoteAdapterListener listener = ( NoteAdapterListener ) i.next();
            listener.onRecyclerViewEmpty( empty );
        }
    }


    public List<Note> getData() {
        return this.data;
    }

    public void setEmptyView( View view ) {
        this.emptyView = view;
    }

    private void showEmptyView( boolean show ) {
        if ( show )
            this.emptyView.setVisibility( View.VISIBLE );
        else
            this.emptyView.setVisibility( View.GONE );
    }

    public void setNotesViewModel( NotesViewModel viewModel ) {
        this.notesViewModel = viewModel;
    }

    public void addListener( NoteAdapterListener listener ) {
        listeners.add( listener );
    }

    public void sortData() {
        List<Note> data = SortingUtil.sortList( getData(),
                notesViewModel.getCurrentAscendingConfigOption(),
                notesViewModel.getCurrentSortingStrategyConfigOption() );
        this.data = data;
        notifyDataSetChanged();

    }

    public void checkAllNotes( boolean check ) {
        Iterator i = getData().iterator();
        while ( i.hasNext() ) {
            Note note = ( Note ) i.next();
            note.setChecked( check );
        }
        notifyDataSetChanged();
    }

    public void editStatusChanged( Boolean edit ) {
        this.editInProgress = edit;
        if ( edit ) {
            notifyViewHoldersToShowCheckBox();
        }
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

    public void deleteSelectedNotes() {
        List<Note> unselectedNotes = new ArrayList<>(), selectedNotes = new ArrayList<>();
        Iterator i = getData().iterator();
        while ( i.hasNext() ) {
            Note note = ( Note ) i.next();
            if ( note.isChecked() )
                selectedNotes.add( note );
            else
                unselectedNotes.add( note );
        }
        setData( unselectedNotes );
        notesViewModel.deleteReferencesIn( selectedNotes );
    }

    public interface NoteAdapterListener {
        void onViewHolderClicked( View view, int viewHolderPosition );
        void onViewHolderLongClicked( int viewHolderPosition );
        void onRecyclerViewEmpty( boolean isEmpty );
    }
}
