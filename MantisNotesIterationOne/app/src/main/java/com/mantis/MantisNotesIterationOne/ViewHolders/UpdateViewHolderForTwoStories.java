package com.mantis.MantisNotesIterationOne.ViewHolders;

import android.view.View;
import android.widget.TextView;

import com.mantis.MantisNotesIterationOne.R;
import com.mantis.MantisNotesIterationOne.data.ArticleBundle;

public class UpdateViewHolderForTwoStories extends ArticleViewHolder {

    public UpdateViewHolderForTwoStories( View view ) {
        super( view );
    }
    @Override
    public void bindData( ArticleBundle articleBundle ) {
        TextView firstHeadlineTextView = articleView.findViewById( R.id.update_view_for_two_stories_first_card_view_headline_text_view );
        firstHeadlineTextView.setText( articleBundle.getArticles().get( 0 ).getHeadline() );
        TextView secondHeadlineTextView = articleView.findViewById( R.id.update_view_for_two_stories_second_card_view_headline_text_view );
        secondHeadlineTextView.setText( articleBundle.getArticles().get( 1 ).getHeadline() );
        TextView secondHighlightTextView = articleView.findViewById( R.id.update_view_for_two_stories_second_card_view_highlight_text_view );
        secondHighlightTextView.setText( articleBundle.getArticles().get( 1 ).getHighlight() );
    }
}
