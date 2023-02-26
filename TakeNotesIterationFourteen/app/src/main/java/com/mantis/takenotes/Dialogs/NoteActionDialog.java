package com.mantis.takenotes.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mantis.takenotes.R;
import com.mantis.takenotes.databinding.TakeNotesViewActionDialogBinding;

import java.util.ArrayList;
import java.util.Iterator;

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
