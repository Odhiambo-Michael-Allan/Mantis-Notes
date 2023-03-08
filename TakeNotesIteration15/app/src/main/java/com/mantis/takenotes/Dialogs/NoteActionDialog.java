package com.mantis.takenotes.Dialogs;

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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.mantis.takenotes.databinding.TakeNotesViewActionDialogBinding;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author - Michael Allan Odhiambo
 * @email - odhiambomichaelallan@gmail.com
 * @date - 4th March 2023
 */

public class NoteActionDialog extends BottomSheetDialogFragment {

    private static final String SHOW_ARCHIVE_OPTION = "True";
    private TakeNotesViewActionDialogBinding binding;
    private ArrayList<NoteActionDialogListener> listeners = new ArrayList<>();


    public static NoteActionDialog newInstance(  boolean showArchiveOption ) {
        Bundle args = new Bundle();
        args.putBoolean( SHOW_ARCHIVE_OPTION, showArchiveOption );
        NoteActionDialog fragment = new NoteActionDialog();
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState ) {

        binding = TakeNotesViewActionDialogBinding.inflate( inflater, container, false );
        boolean showArchiveOption = getArguments().getBoolean( SHOW_ARCHIVE_OPTION );
        if ( showArchiveOption ) {
            binding.archiveTextView.setVisibility(View.VISIBLE);
            binding.unarchiveTextView.setVisibility( View.GONE );
        }
        else {
            binding.unarchiveTextView.setVisibility(View.VISIBLE);
            binding.archiveTextView.setVisibility( View.GONE );
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );
        binding.shareTextView.setOnClickListener( v -> {
            notifyListenersOfShare();
            dismiss();
        } );
        binding.textViewDelete.setOnClickListener( v -> {
            notifyListenersOfDelete();
            dismiss();
        } );
        binding.archiveTextView.setOnClickListener( v -> {
            notifyListenersOfArchive();
            dismiss();
        });
        binding.unarchiveTextView.setOnClickListener( v -> {
            notifyListenersOfUnarchive();
            dismiss();
        } );
        binding.textViewSendFeedback.setOnClickListener( v -> {
            notifyListenersOfSendFeedback();
            dismiss();
        });
    }

    private void notifyListenersOfShare() {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            NoteActionDialogListener listener = ( NoteActionDialogListener ) i.next();
            listener.onShareSelected();
        }
    }

    private void notifyListenersOfSendFeedback() {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            NoteActionDialogListener listener = ( NoteActionDialogListener ) i.next();
            listener.onSendFeedbackSelected();
        }
    }

    private void notifyListenersOfDelete() {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            NoteActionDialogListener listener = ( NoteActionDialogListener ) i.next();
            listener.deleteSelected();
        }
    }

    private void notifyListenersOfArchive() {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            NoteActionDialogListener listener = ( NoteActionDialogListener ) i.next();
            listener.onArchiveSelected();
        }
    }

    private void notifyListenersOfUnarchive() {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            NoteActionDialogListener listener = ( NoteActionDialogListener ) i.next();
            listener.onUnarchiveSelected();
        }
    }

    public void addListener( NoteActionDialogListener listener ) {
        listeners.add( listener );
    }

    public interface NoteActionDialogListener {
        void deleteSelected();
        void onArchiveSelected();
        void onUnarchiveSelected();
        void onShareSelected();
        void onSendFeedbackSelected();
    }
}
