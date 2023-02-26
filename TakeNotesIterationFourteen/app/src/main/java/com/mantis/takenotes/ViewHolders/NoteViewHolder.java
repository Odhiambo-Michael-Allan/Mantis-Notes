package com.mantis.takenotes.ViewHolders;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;

import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.CheckBox;

import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.mantis.takenotes.Adapters.NotesAdapter;
import com.mantis.takenotes.R;
import com.mantis.takenotes.Utils.Logger;

import com.mantis.takenotes.data.source.local.Note;
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

    public abstract void bindData( Note bindableObject );

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

    public void highlightQuery( String query ) {
        TextView noteTitleTextView = this.noteView.findViewById( R.id.note_title_text_view );
        Logger.log( "Note Title TextView: " + noteTitleTextView );
        Logger.log( "Note title text: " + noteTitleTextView.getText().toString() );
        TextView noteDescriptionTextView = this.noteView.findViewById(
                R.id.note_description_text_view );
        Logger.log( "Note description text: " + noteDescriptionTextView.getText().toString() );
        highlightText( query, noteTitleTextView );
        highlightText( query, noteDescriptionTextView );
    }

    private void highlightText( String text, TextView textView ) {
        SpannableString spannableString = new SpannableString( textView.getText() );
        // Remove previous spans..
        BackgroundColorSpan[] backgroundColorSpans = spannableString.getSpans( 0, spannableString.length(), BackgroundColorSpan.class );
        for ( BackgroundColorSpan span : backgroundColorSpans )
            spannableString.removeSpan( span );
        // Search for all occurrences of the keyword in the string..
        int indexOfKeyword = spannableString.toString().indexOf( text );
        while ( indexOfKeyword >= 0 ) {
            // Create a background color span on the keyword..
            spannableString.setSpan( new BackgroundColorSpan( Color.parseColor( "#d7d7d7" ) ), indexOfKeyword, indexOfKeyword + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
            indexOfKeyword = spannableString.toString().indexOf( text, indexOfKeyword + text.length() );
        }
        textView.setText( spannableString );
    }

    public interface NoteViewHolderListener {
        void onClick( View view, int viewHolderPosition );
        void onLongClick( int viewHolderPosition );
        void onCheckButtonChange( boolean check );
    }
}
