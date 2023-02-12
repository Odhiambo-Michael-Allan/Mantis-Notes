package com.mantis.TakeNotes.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mantis.TakeNotes.R;
import com.mantis.TakeNotes.ViewHolders.RecentSearchViewHolder;
import com.mantis.TakeNotes.data.source.local.Query;

import java.util.ArrayList;
import java.util.List;

public class RecentSearchesAdapter extends RecyclerView.Adapter<RecentSearchViewHolder> {

    private List<Query> recentQueries = new ArrayList<>();

    @NonNull
    @Override
    public RecentSearchViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.recent_search_view, parent, false );
        return new RecentSearchViewHolder( view );
    }

    @Override
    public void onBindViewHolder( @NonNull RecentSearchViewHolder holder, int position ) {
        holder.bindData( getRecentQueryAt( position ) );
    }

    private Query getRecentQueryAt( int position ) {
        return recentQueries.get( position );
    }

    public void setData( List<Query> recentQueries ) {
        this.recentQueries = recentQueries;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return recentQueries.size();
    }

    public boolean hasItems() {
        return recentQueries.size() > 0;
    }
}
