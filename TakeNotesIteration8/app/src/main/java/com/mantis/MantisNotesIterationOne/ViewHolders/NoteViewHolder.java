package com.mantis.MantisNotesIterationOne.ViewHolders;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.mantis.MantisNotesIterationOne.Adapters.NotesAdapter;
import com.mantis.MantisNotesIterationOne.data.source.local.Note;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class NoteViewHolder extends RecyclerView.ViewHolder {

    protected View noteView;
    private int position;
    private NotesAdapter adapter;
    private ArrayList<NoteViewHolderListener> listeners = new ArrayList<>();

    public NoteViewHolder( View noteView, NotesAdapter adapter ) {
        super( noteView );
        this.noteView = noteView;
        this.adapter = adapter;
        setupListenersOnNoteView();
    }

    public void setPosition( int position ) {
        this.position = position;
    }

    private void setupListenersOnNoteView() {
        noteView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                notifyListenersOfClick( v, getAdapterPosition() );
            }
        } );

        noteView.setOnLongClickListener( new View.OnLongClickListener() {
            @Override
            public boolean onLongClick( View v ) {
                notifyListenersOfLongClick( getAdapterPosition() );
                return true;
            }
        } );
    }

    public abstract void bindData( Note note );

    public void addListener( NoteViewHolderListener listener ) {
        listeners.add( listener );
    }

    private void notifyListenersOfClick( View view, int viewHolderPosition ) {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            NoteViewHolderListener listener = ( NoteViewHolderListener ) i.next();
            listener.onClick( view, viewHolderPosition );
        }
    }

    private void notifyListenersOfLongClick( int viewHolderPosition ) {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            NoteViewHolderListener listener = ( NoteViewHolderListener ) i.next();
            listener.onLongClick( viewHolderPosition );
        }
    }

    public interface NoteViewHolderListener {
        void onClick( View view, int viewHolderPosition );
        void onLongClick( int viewHolderPosition );
    }
}
