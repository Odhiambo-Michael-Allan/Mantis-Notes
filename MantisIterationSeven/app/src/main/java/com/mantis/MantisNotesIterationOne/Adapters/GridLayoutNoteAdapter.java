package com.mantis.MantisNotesIterationOne.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.mantis.MantisNotesIterationOne.R;
import com.mantis.MantisNotesIterationOne.ViewHolders.GridNoteViewHolder;
import com.mantis.MantisNotesIterationOne.ViewHolders.NoteViewHolder;

public class GridLayoutNoteAdapter extends NotesAdapter {
    @Override
    protected View getNoteView( @NonNull ViewGroup parent ) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.grid_note_view, parent, false );
        return view;
    }

    @Override
    protected NoteViewHolder getNoteViewHolder( View view, NotesAdapter adapter ) {
        return new GridNoteViewHolder( view, adapter );
    }
}
