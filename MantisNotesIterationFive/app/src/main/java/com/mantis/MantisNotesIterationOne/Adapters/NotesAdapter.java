package com.mantis.MantisNotesIterationOne.Adapters;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mantis.MantisNotesIterationOne.Models.Note;
import com.mantis.MantisNotesIterationOne.Models.NotesViewModel;
import com.mantis.MantisNotesIterationOne.R;
import com.mantis.MantisNotesIterationOne.ViewHolders.NoteViewHolder;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private ArrayList<Note> data;
    private View emptyView;
    private NotesViewModel viewModel;
    private ArrayList<NoteAdapterListener> listeners = new ArrayList<>();

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
        } );
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
        holder.bindData( data.get( position ) );
        holder.setPosition( position );
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData( ArrayList<Note> data, Menu menu ) {
        this.data = data;
        if ( data.size() == 0 ) {
            showEmptyView(true);
            hide( menu );
        }
        else {
            showEmptyView(false);
            show( menu );
        }
        notifyDataSetChanged();
    }

    private void hide( Menu menu ) {
        if ( menu == null )
            return;
        for ( int i = 0; i < menu.size(); i++ )
            menu.getItem( i ).setVisible( false );
    }

    private void show( Menu menu ) {
        if ( menu == null )
            return;
        for ( int i = 0; i < menu.size(); i++ )
            menu.getItem( i ).setVisible( true );
    }

    public ArrayList<Note> getData() {
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
        this.viewModel = viewModel;
    }

    public void deleteNoteAt( int position ) {
        Note note = this.data.get( position );
        this.viewModel.deleteNote( note );
    }

    public void archiveNoteAt( int position ) {
        this.viewModel.archiveNoteAt( position );
    }

    public void addListener( NoteAdapterListener listener ) {
        listeners.add( listener );
    }

    public interface NoteAdapterListener {
        void onViewHolderClicked( View view, int viewHolderPosition );
        void onViewHolderLongClicked( int viewHolderPosition );
    }
}
