package com.mantis.MantisNotesIterationOne.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.mantis.MantisNotesIterationOne.Adapters.NotesAdapter;
import com.mantis.MantisNotesIterationOne.Dialogs.NoteActionDialog;
import com.mantis.MantisNotesIterationOne.Logger;
import com.mantis.MantisNotesIterationOne.Models.Note;
import com.mantis.MantisNotesIterationOne.R;

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
                notifyListenersOfClick( v, NoteViewHolder.this.position );
            }
        } );

        noteView.setOnLongClickListener( new View.OnLongClickListener() {
            @Override
            public boolean onLongClick( View v ) {
                NoteActionDialog actionDialog = NoteActionDialog.
                        newInstance( getAdapterPosition() );
                Logger.log( String.format( "Selected note position: %d", getAdapterPosition() ) );
                actionDialog.addListener( new NoteActionDialog.NoteActionDialogListener() {
                    @Override
                    public void deleteSelected() {
                        adapter.deleteNoteAt( getAdapterPosition() );
                    }
                } );
                actionDialog.show( ((AppCompatActivity) noteView.getContext())
                        .getSupportFragmentManager(), "note-icons" );
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

    public interface NoteViewHolderListener {
        void onClick( View view, int viewHolderPosition );
    }
}
