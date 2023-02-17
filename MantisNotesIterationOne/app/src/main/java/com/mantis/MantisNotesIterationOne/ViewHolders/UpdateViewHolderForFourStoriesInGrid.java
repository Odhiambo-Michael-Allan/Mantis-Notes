package com.mantis.MantisNotesIterationOne.ViewHolders;

import android.view.View;
import android.widget.TextView;

import com.mantis.MantisNotesIterationOne.R;
import com.mantis.MantisNotesIterationOne.data.Article;
import com.mantis.MantisNotesIterationOne.data.ArticleBundle;

import java.util.List;

public class UpdateViewHolderForFourStoriesInGrid extends ArticleViewHolder {

    public UpdateViewHolderForFourStoriesInGrid(View view ) {
        super( view );
    }
    @Override
    public void bindData(ArticleBundle articleBundle) {
        List<Article> articles = articleBundle.getArticles();
        TextView firstArticle1HeadlineTextView = articleView.findViewById( R.id.first_grid_card_view_title_text_view );
        TextView firstArticleHighlightTextView = articleView.findViewById( R.id.first_grid_card_view_highlight_text_view );
        firstArticle1HeadlineTextView.setText( articles.get( 0 ).getHeadline() );
        firstArticleHighlightTextView.setText( articles.get( 0 ).getHighlight() );

        TextView secondArticleHeadlineTextView = articleView.findViewById( R.id.second_grid_card_view_title_text_view );
        TextView secondArticleHighlightTextView = articleView.findViewById( R.id.second_grid_card_view_highlight_text_view );
        secondArticleHeadlineTextView.setText( articles.get( 1 ).getHeadline() );
        secondArticleHighlightTextView.setText( articles.get( 1 ).getHighlight() );

        TextView thirdArticleHeadlineTextView = articleView.findViewById( R.id.third_grid_card_view_title_text_view );
        TextView thirdArticleHighlightTextView = articleView.findViewById( R.id.third_grid_card_view_highlight_text_view );
        thirdArticleHeadlineTextView.setText( articles.get( 2 ).getHeadline() );
        thirdArticleHighlightTextView.setText( articles.get( 2 ).getHeadline() );

        TextView fourthArticleHeadlineTextView = articleView.findViewById( R.id.fourth_grid_card_view_title_text_view );
        TextView fourthArticleHighlightTextView = articleView.findViewById( R.id.fourth_grid_card_view_highlight_text_view );
        fourthArticleHeadlineTextView.setText( articles.get( 3 ).getHeadline() );
        fourthArticleHighlightTextView.setText( articles.get( 3 ).getHighlight() );
    }
}
