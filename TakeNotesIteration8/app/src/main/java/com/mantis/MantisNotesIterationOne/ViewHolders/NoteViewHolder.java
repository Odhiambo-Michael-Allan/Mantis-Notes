package com.mantis.MantisNotesIterationOne.ViewHolders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.mantis.MantisNotesIterationOne.Adapters.NotesAdapter;
import com.mantis.MantisNotesIterationOne.R;
import com.mantis.MantisNotesIterationOne.Utils.Logger;
import com.mantis.MantisNotesIterationOne.data.source.local.Note;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class NoteViewHolder extends RecyclerView.ViewHolder {

    protected View noteView;
    private int position;
    private NotesAdapter adapter;
    private ArrayList<NoteViewHolderListener> listeners = new ArrayList<>();
    private boolean viewIsChecked = false;
    private boolean editingInProgress = false;

    public NoteViewHolder( View noteView, NotesAdapter adapter ) {
        super( noteView );
        this.noteView = noteView;
        this.adapter = adapter;
        setupListenersOnNoteView();
        setupListenerOnCheckBox();
    }

    public void setPosition( int position ) {
        this.position = position;
    }

    private void setupListenersOnNoteView() {
        noteView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                if ( editingInProgress ) {
                    CheckBox checkBox = NoteViewHolder.this.noteView.findViewById( R.id.note_check_box );
                    boolean isChecked = checkBox.isChecked();
                    checkBox.setChecked( !isChecked );
                    return;
                }
                notifyListenersOfClick( v, getAdapterPosition() );
            }
        } );

        noteView.setOnLongClickListener( new View.OnLongClickListener() {
            @Override
            public boolean onLongClick( View v ) {
                if ( editingInProgress ) {
                    CheckBox checkBox = NoteViewHolder.this.noteView.findViewById( R.id.note_check_box );
                    boolean isChecked = checkBox.isChecked();
                    checkBox.setChecked( !isChecked );
                    return true;
                }
                notifyListenersOfLongClick( getAdapterPosition() );
                return true;
            }
        } );

    }

    public void check( boolean check ) {
        CheckBox checkBox = this.noteView.findViewById( R.id.note_check_box );
        checkBox.setChecked( check );
    }

    private void setupListenerOnCheckBox() {
        CheckBox checkBox = this.noteView.findViewById( R.id.note_check_box );
        checkBox.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged( CompoundButton compoundButton, boolean isChecked ) {
                viewIsChecked = isChecked;
                notifyListenersOfCheckBoxChange( isChecked );
            }
        } );
    }

    public boolean isChecked() {
        return viewIsChecked;
    }

    public void reset() {
        hideCheckBox();
    }

    public void showCheckBox() {
        editingInProgress = true;
        CheckBox checkBox = this.noteView.findViewById( R.id.note_check_box );
        checkBox.setVisibility( View.VISIBLE );
    }

    public void hideCheckBox() {
        editingInProgress = false;
        CheckBox checkBox = this.noteView.findViewById( R.id.note_check_box );
        checkBox.setVisibility( View.GONE );
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

    private void notifyListenersOfCheckBoxChange( boolean checked ) {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            NoteViewHolderListener listener = ( NoteViewHolderListener ) i.next();
            listener.onCheckButtonChange( checked );
        }
    }

    public interface NoteViewHolderListener {
        void onClick( View view, int viewHolderPosition );
        void onLongClick( int viewHolderPosition );
        void onCheckButtonChange( boolean check );
    }
}
