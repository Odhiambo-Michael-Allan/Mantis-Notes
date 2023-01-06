package com.mantis.MantisNotesIterationOne.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mantis.MantisNotesIterationOne.databinding.DialogActionsBinding;

public class NoteActionDialog extends BottomSheetDialogFragment {

    private DialogActionsBinding binding;

    public static NoteActionDialog newInstance() {
        Bundle args = new Bundle();
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
        return binding.getRoot();
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );
        binding.pinTextView.setOnClickListener( v -> { dismiss(); } );
        binding.textViewEdit.setOnClickListener( v -> { dismiss(); } );
        binding.textViewDelete.setOnClickListener( v -> { dismiss(); } );
    }
}
