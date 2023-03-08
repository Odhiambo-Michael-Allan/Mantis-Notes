package com.mantis.takenotes.Adapters;

/**
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.mantis.takenotes.Models.NotesViewModel;
import com.mantis.takenotes.R;
import com.mantis.takenotes.Utils.Logger;

import com.mantis.takenotes.ViewHolders.RecentSearchViewHolder;
import com.mantis.takenotes.data.source.local.Query;
import java.util.ArrayList;

import java.util.List;

/**
 * @author - Michael Allan Odhiambo
 * @email - odhiambomichaelallan@gmail.com
 * @date - 4th March 2023
 */

public class RecentSearchesAdapter extends RecyclerView.Adapter<RecentSearchViewHolder> {

    private List<Query> recentQueries = new ArrayList<>();
    private Fragment owner;
    private NotesViewModel notesViewModel;
    private List<RecentSearchesAdapterListener> listeners = new ArrayList<>();

    public RecentSearchesAdapter( Fragment owner, NotesViewModel notesViewModel ) {
        this.owner = owner;
        this.notesViewModel = notesViewModel;
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
