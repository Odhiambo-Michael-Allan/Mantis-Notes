package com.mantis.takenotes.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mantis.takenotes.databinding.TakeNotesConfirmationDialogBinding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConfirmationDialog extends BottomSheetDialogFragment {

    private static final String PROMPT = "prompt";
    private TakeNotesConfirmationDialogBinding binding;
    private List<ConfirmationDialogListener> listeners = new ArrayList<>();

    public static ConfirmationDialog newInstance(  String prompt ) {
        Bundle args = new Bundle();
        args.putString( PROMPT, prompt );
        ConfirmationDialog fragment = new ConfirmationDialog();
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

        binding = TakeNotesConfirmationDialogBinding.inflate( inflater, container, false );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );
        String prompt = getArguments().getString( PROMPT );
        binding.promptTextview.setText( prompt );
        binding.cancelTextview.setOnClickListener( v -> {
            notifyListenersOfCancel();
            dismiss();
        } );
        binding.yesTextview.setOnClickListener( v -> {
            notifyListenersOfYes();
            dismiss();
        } );
    }

    private void notifyListenersOfCancel() {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            ConfirmationDialogListener listener = ( ConfirmationDialogListener ) i.next();
            listener.onCancelSelected();
        }
    }

    private void notifyListenersOfYes() {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            ConfirmationDialogListener listener = ( ConfirmationDialogListener ) i.next();
            listener.onYesSelected();
        }
    }

    public void addListener( ConfirmationDialogListener listener ) {
        listeners.add( listener );
    }

    public interface ConfirmationDialogListener {
        void onCancelSelected();
        void onYesSelected();
    }
}
