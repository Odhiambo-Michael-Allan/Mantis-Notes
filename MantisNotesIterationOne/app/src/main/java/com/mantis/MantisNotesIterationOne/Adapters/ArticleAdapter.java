package com.mantis.MantisNotesIterationOne.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mantis.MantisNotesIterationOne.R;
import com.mantis.MantisNotesIterationOne.ViewHolders.ArticleViewHolder;
import com.mantis.MantisNotesIterationOne.ViewHolders.UpdateViewHolderForFourStoriesInGrid;
import com.mantis.MantisNotesIterationOne.ViewHolders.UpdateViewHolderForOneStoryWithHighlight;
import com.mantis.MantisNotesIterationOne.ViewHolders.UpdateViewHolderForTwoStories;
import com.mantis.MantisNotesIterationOne.data.ArticleBundle;

import java.util.ArrayList;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleViewHolder> {

    private List<ArticleBundle> data = new ArrayList<>();

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        switch ( viewType ) {
            case 0:
                return createTwoStoriesViewHolder( parent );
            case 2:
                return createFourStoriesGridViewHolder( parent );
            default:
                return createOneStoryWithNoImageViewHolder( parent );
        }
    }

    private ArticleViewHolder createTwoStoriesViewHolder( ViewGroup parent ) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.update_view_for_two_stories, parent, false );
        return new UpdateViewHolderForTwoStories( view );
    }

    private ArticleViewHolder createFourStoriesGridViewHolder( ViewGroup parent ) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.update_grid_view, parent, false );
        return new UpdateViewHolderForFourStoriesInGrid( view );
    }

    private ArticleViewHolder createOneStoryWithNoImageViewHolder( ViewGroup parent ) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.update_view_for_one_story_with_highlight, parent, false );
        return new UpdateViewHolderForOneStoryWithHighlight( view );
    }

    @Override
    public void onBindViewHolder( @NonNull ArticleViewHolder holder, int position ) {
        holder.bindData( getData().get( position ) );
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }

    private List<ArticleBundle> getData() {
        return data;
    }

    public void setData( List<ArticleBundle> data ) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType( int position ) {
        return position % 3;
    }
}
