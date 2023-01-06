package com.mantis.MantisNotesIterationOne.UI;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.mantis.MantisNotesIterationOne.Dialogs.NoteActionDialog;
import com.mantis.MantisNotesIterationOne.Models.Note;
import com.mantis.MantisNotesIterationOne.R;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    private View noteView;
    private int position;

    public NoteViewHolder( View noteView ) {
        super( noteView );
        this.noteView = noteView;
        setupListenersOnNoteView();
    }

    public void setPosition( int position ) {
        this.position = position;
    }

    private void setupListenersOnNoteView() {
        noteView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                HomeFragmentDirections.ActionNavHomeToNavAddNote action =
                        HomeFragmentDirections.actionNavHomeToNavAddNote(
                                NoteViewHolder.this.position );
                Navigation.findNavController( v ).navigate( action );
            }
        } );

        noteView.setOnLongClickListener( new View.OnLongClickListener() {
            @Override
            public boolean onLongClick( View v ) {
                NoteActionDialog actionDialog = NoteActionDialog.newInstance();
                actionDialog.show( ((AppCompatActivity) noteView.getContext()).getSupportFragmentManager(), "note-icons" );
                return true;
            }
        } );
    }

    public void bindData( Note note ) {
        TextView noteTitle = noteView.findViewById( R.id.note_title_text_view );
        noteTitle.setText( note.getTitle() );
        TextView noteTextView = noteView.findViewById( R.id.note_description_text_view );
        noteTextView.setText( note.getDescription() );
        TextView timeTextView = noteView.findViewById( R.id.time_text_view );
        timeTextView.setText( note.getTime() );
        TextView noteTextViewInsideCardView = noteView.findViewById( R.id.note_text_view );
        noteTextViewInsideCardView.setText( note.getTitle() + note.getDescription() );
    }
}
