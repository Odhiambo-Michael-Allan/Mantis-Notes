package com.mantis.takenotes.Commands;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.mantis.takenotes.Dialogs.FeedbackDialog;

import com.mantis.takenotes.R;
import com.mantis.takenotes.Utils.ToastProvider;
import com.mantis.takenotes.databinding.TakeNotesFeedbackDialogBinding;

public class SendFeedbackCommand implements Command {

    private Context context;
    private TakeNotesFeedbackDialogBinding binding;

    public SendFeedbackCommand( Context context ) {
        this.context = context;
    }

    @Override
    public void execute() {
        FeedbackDialog feedbackDialog = FeedbackDialog.newInstance();
        feedbackDialog.show( ( ( AppCompatActivity) context ).getSupportFragmentManager(),
                "note-actions" );
        feedbackDialog.addListener( new FeedbackDialog.FeedbackDialogListener() {
            @Override
            public void onViewCreated() {
                binding = feedbackDialog.getBinding();
                hideSecondStepViews( 8 );
                attachListenersToRelevantViews( feedbackDialog );
            }
        } );
    }

    private void hideSecondStepViews( int visible ) {
        binding.describeIssueTextview.setVisibility( visible );
        binding.sensitiveContentDisclaimer.setVisibility( visible );
        binding.divider.setVisibility( visible );
        binding.describeIssueTextview.setVisibility( visible );
        binding.sendIcon.setVisibility( visible );
        binding.issueEditText.setVisibility( visible );
    }

    private void attachListenersToRelevantViews( FeedbackDialog dialog ) {
        attachListenerToCancelView( dialog );
        attachListenerToGetStartedButton();
        attachListenerToSendIcon( dialog );
    }

    private void attachListenerToCancelView( FeedbackDialog dialog ) {
        binding.cancelImageview.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        } );
    }

    private void attachListenerToGetStartedButton() {
        binding.getStartedButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                hideSecondStepViews( 0 );
                hideFirstStepViews( 8 );
            }
        } );
    }

    private void hideFirstStepViews( int visibility ) {
        binding.welcomeTextView.setText( context.getString( R.string.feedback_second_step_message ) );
        binding.messageTextview.setVisibility( visibility );
        binding.getStartedButton.setVisibility( visibility );
    }

    private void attachListenerToSendIcon( FeedbackDialog dialog ) {
        binding.sendIcon.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                String issue = binding.issueEditText.getText().toString();
                if ( issue.equals( "" ) )
                    ToastProvider.showToast( context, "Kindly provide an issue" );
                else {
                    sendFeedback( dialog );
                    dialog.dismiss();
                }
            }
        } );
    }

    private void sendFeedback( FeedbackDialog dialog ) {
        Intent intent = new Intent( Intent.ACTION_SENDTO );
        intent.setData( Uri.parse( "mailto:" ) );
        intent.putExtra( Intent.EXTRA_SUBJECT, context.getString( R.string.email_subject ) );
        intent.putExtra( Intent.EXTRA_EMAIL, new String[] { "takenotesappbymantis@gmail.com" } );
        intent.putExtra( Intent.EXTRA_TEXT, binding.issueEditText.getText().toString() );
        context.startActivity( Intent.createChooser( intent, "Send Email" ) );
    }
}
