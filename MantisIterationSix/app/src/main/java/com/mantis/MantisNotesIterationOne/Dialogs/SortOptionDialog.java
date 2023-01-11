package com.mantis.MantisNotesIterationOne.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.mantis.MantisNotesIterationOne.Logger;
import com.mantis.MantisNotesIterationOne.Models.NotesViewModel;
import com.mantis.MantisNotesIterationOne.R;
import com.mantis.MantisNotesIterationOne.databinding.SortMenuItemActionDialogBinding;

import java.util.ArrayList;
import java.util.Iterator;

public class SortOptionDialog extends BottomSheetDialogFragment {

    private static final String CURRENT_SORTING_STRATEGY = "Current Sorting Strategy";
    private static final String ASCENDING = "Ascending";

    private SortMenuItemActionDialogBinding binding;
    private ArrayList<SortOptionDialogListener> listeners = new ArrayList<>();

    public static SortOptionDialog newInstance( int currentSortingStrategy,
                                                boolean ascending ) {
        Bundle arguments = new Bundle();
        arguments.putInt( CURRENT_SORTING_STRATEGY, currentSortingStrategy );
        arguments.putBoolean( ASCENDING, ascending );
        SortOptionDialog sortOptionDialog = new SortOptionDialog();
        sortOptionDialog.setArguments( arguments );
        return sortOptionDialog;
    }

    @Override
    public void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState ) {
        binding = SortMenuItemActionDialogBinding.inflate( inflater, container, false );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );
        configureListeners();
        checkAppropriateOptions( getArguments() );
    }

    private void configureListeners() {
        binding.cancelTextview.setOnClickListener( v -> { dismiss(); } );
        binding.doneTextview.setOnClickListener( v -> {
            sendNotifications();
            dismiss();
        } );
    }

    private void sendNotifications() {
        sendLayoutStrategyNotification();
        sendAscendingOrDescendingNotification();
    }

    private void sendLayoutStrategyNotification() {
        int checkedRadioButtonId = binding.layoutStrategyGroup.getCheckedRadioButtonId();
        if ( checkedRadioButtonId == R.id.title_radiobutton )
            notifyListenersTitleChecked();
        else if ( checkedRadioButtonId == R.id.date_created_radiobutton )
            notifyListenersDateCreatedChecked();
        else
            notifyListenersDateLastModifiedChecked();
    }

    private void notifyListenersTitleChecked() {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            SortOptionDialogListener listener = ( SortOptionDialogListener ) i.next();
            listener.onTitleSelected();
        }
    }

    private void notifyListenersDateCreatedChecked() {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            SortOptionDialogListener listener = ( SortOptionDialogListener ) i.next();
            listener.onDateCreatedSelected();
        }
    }

    private void notifyListenersDateLastModifiedChecked() {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            SortOptionDialogListener listener = ( SortOptionDialogListener ) i.next();
            listener.onDateModifiedSelected();
        }
    }

    private void sendAscendingOrDescendingNotification() {
        int checkedRadioButtonId = binding.ascendingDescendingGroup.getCheckedRadioButtonId();
        if ( checkedRadioButtonId == R.id.ascending_radiobutton )
            notifyListenersAscendingOptionSelected();
        else
            notifyListenersDescendOptionSelected();
    }

    private void notifyListenersAscendingOptionSelected() {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            SortOptionDialogListener listener = ( SortOptionDialogListener ) i.next();
            listener.onAscendingSelected();
        }
    }

    private void notifyListenersDescendOptionSelected() {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            SortOptionDialogListener listener = ( SortOptionDialogListener ) i.next();
            listener.onDescendingSelected();
        }
    }

    private void checkAppropriateOptions( Bundle arguments ) {
        int sortingStrategy = ( int ) arguments.get( CURRENT_SORTING_STRATEGY );
        boolean ascending = ( boolean ) arguments.get( ASCENDING );
        Logger.log( "CURRENT SORTING STRATEGY: " + sortingStrategy );
        Logger.log( "ASCENDING : " + ascending );
        checkAppropriateOptionOnSortingStrategySection( sortingStrategy );
        checkAppropriateOptionOnAscendingOrDescendingSection( ascending );
    }

    private void checkAppropriateOptionOnSortingStrategySection( int sortingStrategy ) {
        if ( sortingStrategy == NotesViewModel.TITLE )
            checkTitle();
        else if ( sortingStrategy == NotesViewModel.DATE_CREATED )
            checkDateCreated();
        else
            checkDateLastModified();
    }

    private void checkTitle() {
        binding.titleRadiobutton.setChecked( true );
    }

    private void checkDateCreated() {
        binding.dateCreatedRadiobutton.setChecked( true );
    }

    private void checkDateLastModified() {
        binding.dateModifiedRadiobutton.setChecked( true );
    }

    private void checkAppropriateOptionOnAscendingOrDescendingSection( boolean ascending ) {
        if ( ascending )
            checkAscendingOption();
        else
            checkDescendingOption();
    }

    private void checkAscendingOption() {
        binding.ascendingRadiobutton.setChecked( true );
    }

    private void checkDescendingOption() {
        binding.descendingRadiobutton.setChecked( true );
    }

    public void addListener( SortOptionDialogListener listener ) {
        listeners.add( listener );
    }

    public interface SortOptionDialogListener {
        void onTitleSelected();
        void onDateCreatedSelected();
        void onDateModifiedSelected();
        void onAscendingSelected();
        void onDescendingSelected();
    }

}
