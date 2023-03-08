package com.mantis.takenotes.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mantis.takenotes.R;

import com.mantis.takenotes.Utils.DateProvider;
import com.mantis.takenotes.data.source.local.Query;
import java.util.ArrayList;
import java.util.List;

public class RecentSearchViewHolder extends RecyclerView.ViewHolder {

    private View itemView;
    private List<RecentSearchViewHolderListener> listeners = new ArrayList<>();

    public RecentSearchViewHolder( @NonNull View itemView ) {
        super( itemView );
        this.itemView = itemView;
        attachListenersToViews();
    }

    private void attachListenersToViews() {
        TextView queryTextView = this.itemView.findViewById( R.id.query_textview );
        queryTextView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                notifyListenersOfClick( getAdapterPosition() );
            }
        } );
        ImageView deleteQueryIcon = this.itemView.findViewById( R.id.delete_imageview );
        deleteQueryIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyListenersOfDeleteQuery( getAdapterPosition() );
            }
        } );
    }

    public void addListener( RecentSearchViewHolderListener listener ) {
        listeners.add( listener );
    }
    private void notifyListenersOfClick( int adapterPosition ) {
        for ( RecentSearchViewHolderListener listener : listeners )
            listener.onClick( adapterPosition );
    }

    private void notifyListenersOfDeleteQuery( int adapterPosition ) {
        for ( RecentSearchViewHolderListener listener : listeners )
            listener.onDeleteQuery( adapterPosition );
    }

    public void bindData( Query query ) {
        TextView queryTextView = this.itemView.findViewById( R.id.query_textview );
        queryTextView.setText( query.getDescription() );
        TextView dateCreatedTextView = this.itemView.findViewById( R.id.date_textview );
        dateCreatedTextView.setText( DateProvider.getSimpleDateFrom( query.getDateSubmitted() ) );
    }

    public interface RecentSearchViewHolderListener {
        void onClick( int adapterPosition );
        void onDeleteQuery( int adapterPosition );
    }


}
