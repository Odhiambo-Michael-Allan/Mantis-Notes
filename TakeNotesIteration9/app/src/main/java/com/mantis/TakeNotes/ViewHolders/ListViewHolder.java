package com.mantis.TakeNotes.ViewHolders;

import android.view.View;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mantis.TakeNotes.Adapters.NotesAdapter;
import com.mantis.TakeNotes.data.source.local.Note;
import com.mantis.TakeNotes.R;

public class ListViewHolder extends NoteViewHolder {

    public ListViewHolder( @NonNull View itemView, NotesAdapter adapter )  {
        super( itemView, adapter );
    }

    @Override
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
