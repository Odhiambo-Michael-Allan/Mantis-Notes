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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mantis.takenotes.databinding.TakeNotesFeedbackDialogBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author - Michael Allan Odhiambo
 * @email - odhiambomichaelallan@gmail.com
 * @date - 4th March 2023
 */

public class FeedbackDialog extends BottomSheetDialogFragment {

    private final List<FeedbackDialogListener> listenerList = new ArrayList<>();
    private TakeNotesFeedbackDialogBinding binding;

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