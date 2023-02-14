package com.mantis.TakeNotes.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.mantis.TakeNotes.R;
import com.mantis.TakeNotes.ViewHolders.ListViewHolder;
import com.mantis.TakeNotes.ViewHolders.NoteViewHolder;

public class ListAdapter extends NotesAdapter {

    @Override
    protected View getNoteView(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.list_note_view, parent, false );
        return view;
    }

    @Override
    protected NoteViewHolder getNoteViewHolder( View view, NotesAdapter adapter ) {
        return new ListViewHolder( view, adapter );
    }
}
