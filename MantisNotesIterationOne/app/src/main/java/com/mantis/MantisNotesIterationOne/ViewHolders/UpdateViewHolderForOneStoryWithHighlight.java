package com.mantis.MantisNotesIterationOne.ViewHolders;

import android.view.View;
import android.widget.TextView;

import com.mantis.MantisNotesIterationOne.R;
import com.mantis.MantisNotesIterationOne.data.ArticleBundle;

public class UpdateViewHolderForOneStoryWithHighlight extends ArticleViewHolder {

    public UpdateViewHolderForOneStoryWithHighlight( View view ) {
        super( view );
    }
    @Override
    public void bindData( ArticleBundle articleBundle ) {
        TextView headlineTextView = articleView.findViewById( R.id.update_view_for_one_story_with_highlight_headline_text_view );
        headlineTextView.setText( articleBundle.getArticles().get( 0 ).getHeadline() );
        TextView highlightTextView = articleView.findViewById( R.id.update_view_for_one_story_with_highlight_highlight_text_view );
        highlightTextView.setText( articleBundle.getArticles().get( 0 ).getHighlight() );

    }
}
