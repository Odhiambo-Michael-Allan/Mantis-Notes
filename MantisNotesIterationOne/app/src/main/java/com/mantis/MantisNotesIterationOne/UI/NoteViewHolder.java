package com.mantis.MantisNotesIterationOne.UI;

import android.view.View;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.mantis.MantisNotesIterationOne.Models.Note;
import com.mantis.MantisNotesIterationOne.R;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    private View noteView;

    public NoteViewHolder( View noteView ) {
        super( noteView );
        this.noteView = noteView;
        setupListenerOnNoteView();
    }

    private void setupListenerOnNoteView() {
        noteView.setOnClickListener( Navigation.createNavigateOnClickListener(
                R.id.action_nav_home_to_nav_add_note, null ) );
    }

    public void bindData( Note note ) {
        TextView noteTitle = noteView.findViewById( R.id.note_title_text_view );
        noteTitle.setText( note.getTitle() );
        TextView noteTextView = noteView.findViewById( R.id.note_description_text_view );
        noteTextView.setText( note.getDescription() );
        TextView timeTextView = noteView.findViewById( R.id.time_text_view );
        timeTextView.setText( note.getTime() );
    }
}
