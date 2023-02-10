package com.mantis.TakeNotes.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mantis.TakeNotes.databinding.NoteViewActionDialogVerticalBinding;

import java.util.ArrayList;
import java.util.Iterator;

public class NoteActionDialog extends BottomSheetDialogFragment {

    private static final String SHOW_ARCHIVE_OPTION = "True";
    private NoteViewActionDialogVerticalBinding binding;
    private int noteSelectedPosition;
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

        binding = NoteViewActionDialogVerticalBinding.inflate( inflater, container, false );

        boolean showArchiveOption = getArguments().getBoolean( SHOW_ARCHIVE_OPTION );
        if ( showArchiveOption )
            binding.archiveTextView.setVisibility( View.VISIBLE );
        else
            binding.unarchiveTextView.setVisibility( View.VISIBLE );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );
        binding.archiveTextView.setOnClickListener( v -> { dismiss(); } );
        binding.textViewShare.setOnClickListener( v -> { dismiss(); } );
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
    }
}
