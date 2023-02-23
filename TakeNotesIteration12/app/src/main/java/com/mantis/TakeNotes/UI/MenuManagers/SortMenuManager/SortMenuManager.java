package com.mantis.TakeNotes.UI.MenuManagers.SortMenuManager;


import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.mantis.TakeNotes.Adapters.NotesAdapter;
import com.mantis.TakeNotes.Dialogs.SortOptionDialog;
import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.Utils.Logger;

public abstract class SortMenuManager {

    private Fragment owner;
    private NotesViewModel notesViewModel;
    private NotesAdapter notesAdapter;

    public SortMenuManager( Fragment owner, NotesViewModel notesViewModel,
                            NotesAdapter notesAdapter ) {
        this.owner = owner;
        this.notesViewModel = notesViewModel;
        this.notesAdapter = notesAdapter;
    }

    private void updateAdapter( NotesAdapter notesAdapter ) {
        this.notesAdapter = notesAdapter;
    }



    protected void observeSortingConfiguration() {
        observeAscendingOption();
        observeSortingStrategyConfigOption();
    }

    private void observeAscendingOption() {
        notesViewModel.getAscendingConfigOption()
                .observe( owner.getViewLifecycleOwner(), new Observer<Integer>() {
                    @Override
                    public void onChanged( Integer integer ) {
                        updateAdapter( getAdapter() );
                        notesAdapter.sortData();
                    }
                } );
    }

    private void observeSortingStrategyConfigOption() {
        notesViewModel.getSortingStrategyConfigOption()
                .observe( owner.getViewLifecycleOwner(), new Observer<Integer>() {
                    @Override
                    public void onChanged( Integer integer ) {
                        updateAdapter( getAdapter() );
                        notesAdapter.sortData();
                    }
                } );
    }

    protected void configureSortMenuItem() {
        MenuItem sortMenuItem = getSortMenuItem();
        sortMenuItem.setOnMenuItemClickListener(
                new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick( MenuItem item ) {
                        SortOptionDialog sortOptionDialog = SortOptionDialog.newInstance(
                                notesViewModel.getCurrentSortingStrategyConfigOption(),
                                notesViewModel.getCurrentAscendingConfigOption() );
                        sortOptionDialog.addListener( new SortOptionDialog.SortOptionDialogListener() {
                            @Override
                            public void onTitleSelected() {
                                notesViewModel.updateSortingStrategyConfig( NotesViewModel.TITLE );
                            }

                            @Override
                            public void onDateCreatedSelected() {
                                notesViewModel.updateSortingStrategyConfig( NotesViewModel.DATE_CREATED );
                            }

                            @Override
                            public void onDateModifiedSelected() {
                                notesViewModel.updateSortingStrategyConfig( NotesViewModel.DATE_MODIFIED );
                            }

                            @Override
                            public void onAscendingSelected() {
                                notesViewModel.updateAscendingConfig( NotesViewModel.ASCENDING );
                            }

                            @Override
                            public void onDescendingSelected() {
                                notesViewModel.updateAscendingConfig( NotesViewModel.DESCENDING );
                            }
                        } );
                        sortOptionDialog.show( ( (AppCompatActivity) owner.getContext() )
                                .getSupportFragmentManager(), "sort-options" );
                        return true;
                    }
                } );
    }

    protected abstract MenuItem getSortMenuItem();
    protected abstract NotesAdapter getAdapter();
}
