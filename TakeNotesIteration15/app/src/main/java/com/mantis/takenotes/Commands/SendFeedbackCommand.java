package com.mantis.takenotes.Commands;

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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.mantis.takenotes.Dialogs.FeedbackDialog;

import com.mantis.takenotes.R;
import com.mantis.takenotes.Utils.ToastProvider;
import com.mantis.takenotes.databinding.TakeNotesFeedbackDialogBinding;

/**
 * @author - Michael Allan Odhiambo
 * @email - odhiambomichaelallan@gmail.com
 * @date - 4th March 2023
 */

public class SendFeedbackCommand implements Command {

    private final Context context;
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
