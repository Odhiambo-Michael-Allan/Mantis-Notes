package com.mantis.MantisNotesIterationOne.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mantis.MantisNotesIterationOne.Models.Note;
import com.mantis.MantisNotesIterationOne.R;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private ArrayList<Note> data;
    private View emptyView;
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.notes_view, parent, false );
        return new NoteViewHolder( view );
    }

    @Override
    public void onBindViewHolder( @NonNull NoteViewHolder holder, int position ) {
        holder.bindData( data.get( position ) );
        holder.setPosition( position );
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData( ArrayList<Note> data ) {
        this.data = data;
        if ( data.size() == 0 )
            showEmptyView( true );
        else
            showEmptyView( false );
        notifyDataSetChanged();
    }

    public void setEmptyView( View view ) {
        this.emptyView = view;
    }

    private void showEmptyView( boolean show ) {
        if ( show )
            this.emptyView.setVisibility( View.VISIBLE );
        else
            this.emptyView.setVisibility( View.GONE );
    }
}
