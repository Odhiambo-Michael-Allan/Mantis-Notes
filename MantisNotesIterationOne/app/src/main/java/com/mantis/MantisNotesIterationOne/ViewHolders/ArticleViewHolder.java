package com.mantis.MantisNotesIterationOne.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mantis.MantisNotesIterationOne.R;
import com.mantis.MantisNotesIterationOne.data.Article;
import com.mantis.MantisNotesIterationOne.data.ArticleBundle;

import java.util.List;


public abstract class ArticleViewHolder extends RecyclerView.ViewHolder {

    protected View articleView;

    public ArticleViewHolder( @NonNull View articleView ) {
        super( articleView );
        this.articleView = articleView;
    }

    public abstract void bindData( ArticleBundle articleBundle );
}
