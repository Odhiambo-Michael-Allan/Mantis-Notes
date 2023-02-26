package com.mantis.takenotes.Dialogs;

import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mantis.takenotes.databinding.TakeNotesFeedbackDialogBinding;

import java.util.ArrayList;
import java.util.List;

public class FeedbackDialog extends BottomSheetDialogFragment {

    private TakeNotesFeedbackDialogBinding binding;
    private List<FeedbackDialogListener> listenerList = new ArrayList<>();

    public FeedbackDialog() {
        // Required empty public constructor
    }

    public static FeedbackDialog newInstance() {
        FeedbackDialog fragment = new FeedbackDialog();
        Bundle args = new Bundle();
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        binding = TakeNotesFeedbackDialogBinding.inflate( inflater, container,
                false );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );
        notifyListenersViewHasBeenCreated();
    }

    private void notifyListenersViewHasBeenCreated() {
        for ( FeedbackDialogListener listener : listenerList )
            listener.onViewCreated();
    }

    public void addListener( FeedbackDialogListener listener ) {
        listenerList.add( listener );
    }

    public TakeNotesFeedbackDialogBinding getBinding() {
        return this.binding;
    }

    public interface FeedbackDialogListener {
        void onViewCreated();
    }
}