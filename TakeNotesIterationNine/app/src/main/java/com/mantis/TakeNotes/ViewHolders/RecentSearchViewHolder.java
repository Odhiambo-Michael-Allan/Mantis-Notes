package com.mantis.TakeNotes.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mantis.TakeNotes.Adapters.NotesAdapter;
import com.mantis.TakeNotes.R;
import com.mantis.TakeNotes.data.source.local.Note;
import com.mantis.TakeNotes.data.source.local.Query;

public class RecentSearchViewHolder extends RecyclerView.ViewHolder {

    public RecentSearchViewHolder( @NonNull View itemView ) {
        super( itemView );
    }

    public void bindData( Query query ) {
        TextView queryTextView = this.itemView.findViewById( R.id.query_textview );
        queryTextView.setText( query.getDescription() );
        TextView dateCreatedTextView = this.itemView.findViewById( R.id.date_textview );
        dateCreatedTextView.setText( query.getDateSubmited() );
    }
}
