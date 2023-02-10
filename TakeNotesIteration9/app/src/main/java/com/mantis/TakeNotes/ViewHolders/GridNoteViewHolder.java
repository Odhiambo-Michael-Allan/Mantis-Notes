package com.mantis.TakeNotes.ViewHolders;

import android.view.View;
import android.widget.Space;
import android.widget.TextView;

import com.mantis.TakeNotes.Adapters.NotesAdapter;
import com.mantis.TakeNotes.data.source.local.Note;
import com.mantis.TakeNotes.R;

public class GridNoteViewHolder extends NoteViewHolder {

    public GridNoteViewHolder( View view, NotesAdapter adapter ) {
        super( view, adapter );
    }

    public void bindData( Note note ) {
        TextView noteTitleTextView = this.noteView.findViewById(
                R.id.note_title_text_view );
        TextView noteDateTextView = this.noteView.findViewById(
                R.id.note_date_text_view );
        Space space = this.noteView.findViewById( R.id.space );
        noteDateTextView.setText( note.getDate() );
        if ( !note.getTitle().equals( "" ) ) {
            noteTitleTextView.setText(note.getTitle());
            space.setVisibility( View.GONE );
        }
        else {
            noteTitleTextView.setVisibility( View.GONE );
        }
        TextView noteDescriptionTextView = this.noteView
                .findViewById( R.id.note_description_text_view );
        noteDescriptionTextView.setText( note.getDescription() );
    }
}
