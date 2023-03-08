package com.mantis.takenotes.ViewHolders;

import android.view.View;
import android.widget.TextView;
import com.mantis.takenotes.Adapters.NotesAdapter;

import com.mantis.takenotes.Repository.Note;
import com.mantis.takenotes.R;

public class SimpleListViewHolder extends NoteViewHolder {

    public SimpleListViewHolder( View view, NotesAdapter adapter ) {
        super( view, adapter );
    }
    @Override
    public void bindData( Note note ) {
        TextView noteTitle = noteView.findViewById( R.id.note_title_text_view );
        noteTitle.setText( note.getTitle() );
        TextView noteTextView = noteView.findViewById( R.id.note_description_text_view );
        noteTextView.setText( note.getDescription() );
        TextView timeTextView = noteView.findViewById( R.id.time_text_view );
        timeTextView.setText( note.getDate() );
        TextView noteTextViewInsideCardView = noteView.findViewById( R.id.note_text_view );
        noteTextViewInsideCardView.setText( note.getTitle() + note.getDescription() );
    }
}
