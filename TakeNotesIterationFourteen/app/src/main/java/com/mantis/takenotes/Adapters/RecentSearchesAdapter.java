package com.mantis.takenotes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.mantis.takenotes.Models.NotesViewModel;
import com.mantis.takenotes.R;

import com.mantis.takenotes.Utils.Logger;
import com.mantis.takenotes.ViewHolders.RecentSearchViewHolder;
import com.mantis.takenotes.data.source.local.Query;

import java.util.ArrayList;
import java.util.List;

public class RecentSearchesAdapter extends RecyclerView.Adapter<RecentSearchViewHolder> {

    private List<Query> recentQueries = new ArrayList<>();
    private Fragment owner;
    private NotesViewModel notesViewModel;
    private List<RecentSearchesAdapterListener> listeners = new ArrayList<>();

    public RecentSearchesAdapter( Fragment owner, NotesViewModel notesViewModel ) {
        this.owner = owner;
        this.notesViewModel = notesViewModel;
        observeRecentSearches();
    }

    private void observeRecentSearches() {
        notesViewModel.getSearchHistory().observe( owner.getViewLifecycleOwner(), new Observer<List<Query>>() {
            @Override
            public void onChanged( List<Query> queries ) {
                setData( queries );
            }
        } );
    }

    @NonNull
    @Override
    public RecentSearchViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.take_notes_recent_search_view, parent, false );
        RecentSearchViewHolder recentSearchViewHolder = new RecentSearchViewHolder( view );
        recentSearchViewHolder.addListener( new RecentSearchViewHolder.RecentSearchViewHolderListener() {
            @Override
            public void onClick( int adapterPosition ) {
                notifyListenersOfRecentQueryClick( recentQueries.get( adapterPosition ) );
            }

            @Override
            public void onDeleteQuery( int adapterPosition ) {
                recentQueries.get( adapterPosition ).delete(notesViewModel.getNoteRepository() );
            }
        } );
        return recentSearchViewHolder;
    }

    private void notifyListenersOfRecentQueryClick( Query query ) {
        for ( RecentSearchesAdapterListener listener : listeners )
            listener.onRecentQueryClick( query );
    }

    public void addListener( RecentSearchesAdapterListener listener ) {
        listeners.add( listener );
    }

    @Override
    public void onBindViewHolder( @NonNull RecentSearchViewHolder holder, int position ) {
        holder.bindData( getRecentQueryAt( position ) );
    }

    private Query getRecentQueryAt( int position ) {
        return recentQueries.get( position );
    }

    public void setData( List<Query> recentQueries ) {
        Logger.log( "Setting recent search adapter data. Size:  " + recentQueries.size() );
        this.recentQueries = recentQueries;
        notifyListenersOfSearchHistoryChange( this.recentQueries );
        notifyDataSetChanged();
    }

    private void notifyListenersOfSearchHistoryChange( List<Query> searchHistory ) {
        for ( RecentSearchesAdapterListener listener : listeners )
            listener.onSearchHistoryChange( searchHistory );
    }

    public boolean contains( Query query ) {
        for ( Query currentQuery : recentQueries )
            if ( currentQuery.getDescription().equals( query.getDescription() ) )
                return true;
        return false;
    }

    @Override
    public int getItemCount() {
        return recentQueries.size();
    }

    public interface RecentSearchesAdapterListener {
        void onRecentQueryClick( Query query );
        void onSearchHistoryChange( List<Query> searchHistory );
    }
}
