package com.mantis.MantisNotesIterationOne.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mantis.MantisNotesIterationOne.databinding.DialogActionsBinding;

import java.util.ArrayList;
import java.util.Iterator;

public class NoteActionDialog extends BottomSheetDialogFragment {

    private static final String NOTE_POSITION = "Note Position";
    private DialogActionsBinding binding;
    private int noteSelectedPosition;
    private ArrayList<NoteActionDialogListener> listeners = new ArrayList<>();


    public static NoteActionDialog newInstance( int noteSelectedPosition ) {
        Bundle args = new Bundle();
        args.putInt( NOTE_POSITION, noteSelectedPosition );
        NoteActionDialog fragment = new NoteActionDialog();
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState ) {
        binding = DialogActionsBinding.inflate( inflater, container, false );
        noteSelectedPosition = getArguments().getInt( NOTE_POSITION );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );
        binding.pinTextView.setOnClickListener( v -> { dismiss(); } );
        binding.textViewEdit.setOnClickListener( v -> { dismiss(); } );
        binding.textViewDelete.setOnClickListener( v -> {
            notifyListenersOfDelete();
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

    public void addListener( NoteActionDialogListener listener ) {
        listeners.add( listener );
    }

    public interface NoteActionDialogListener {
        void deleteSelected();
    }
}
