package com.mantis.MantisNotesIterationOne.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mantis.MantisNotesIterationOne.Models.Note;
import com.mantis.MantisNotesIterationOne.Models.NotesViewModel;
import com.mantis.MantisNotesIterationOne.R;
import com.mantis.MantisNotesIterationOne.UI.NoteViewHolder;

import java.util.ArrayList;
import java.util.Iterator;

public class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private ArrayList<Note> data;
    private View emptyView;
    private NotesViewModel viewModel;
    private ArrayList<NoteAdapterListener> listeners = new ArrayList<>();

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.notes_view, parent, false );
        NoteViewHolder noteViewHolder = new NoteViewHolder( view, this );
        noteViewHolder.addListener(new NoteViewHolder.NoteViewHolderListener() {
            @Override
            public void onClick( View view, int viewHolderPosition ) {
                notifyListenersOfViewHolderClick( view, viewHolderPosition );
            }
        } );
        return noteViewHolder;
    }

    private void notifyListenersOfViewHolderClick( View view, int viewHolderPosition ) {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            NoteAdapterListener listener = ( NoteAdapterListener ) i.next();
            listener.onViewHolderClicked( view, viewHolderPosition );
        }
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

    public void setNotesViewModel( NotesViewModel viewModel ) {
        this.viewModel = viewModel;
    }

    public void deleteNoteAt( int position ) {
        Note note = this.data.get( position );
        this.viewModel.deleteNote( note );
    }

    public void addListener( NoteAdapterListener listener ) {
        listeners.add( listener );
    }

    public interface NoteAdapterListener {
        void onViewHolderClicked( View view, int viewHolderPosition );
    }
}
