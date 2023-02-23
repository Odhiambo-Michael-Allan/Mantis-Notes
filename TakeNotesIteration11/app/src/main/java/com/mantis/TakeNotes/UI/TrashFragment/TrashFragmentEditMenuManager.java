package com.mantis.TakeNotes.UI.TrashFragment;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.mantis.TakeNotes.Adapters.NotesAdapter;
import com.mantis.TakeNotes.Dialogs.ConfirmationDialog;
import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.R;
import com.mantis.TakeNotes.UI.MenuManagers.EditMenuManager.EditMenuManager;
import com.mantis.TakeNotes.databinding.FragmentTrashBinding;

public class TrashFragmentEditMenuManager extends EditMenuManager {

    private FragmentTrashBinding binding;

    public TrashFragmentEditMenuManager( Fragment owner, NotesAdapter notesAdapter,
                                         NotesViewModel notesViewModel,
                                         FragmentTrashBinding binding ) {
        super( owner, notesAdapter, notesViewModel );
        this.binding = binding;
        super.observeEditingStatus();
    }
    @Override
    protected View getEditOptions() {
        return binding.trashFragmentContent.trashFragmentAppBarLayout.editOptions;
    }

    @Override
    protected NotesAdapter getAdapter() {
        return ( NotesAdapter ) binding.trashFragmentContent.trashFragmentNotesRecyclerView
                .recyclerview.getAdapter();
    }

    @Override
    protected CollapsingToolbarLayout getCollapsingToolbarLayout() {
        return binding.trashFragmentContent.trashFragmentAppBarLayout.collapsingToolbar;
    }

    @Override
    protected Toolbar getMainToolbar() {
        return binding.trashFragmentContent.trashFragmentAppBarLayout.toolbar;
    }

    @Override
    protected void showFloatingActionButton( boolean editingEnabled ) {
        return;
    }

    @Override
    protected TextView getMainTitleTextView() {
        return binding.trashFragmentContent.trashFragmentAppBarLayout.mainTitle;
    }

    @Override
    protected Toolbar getEditingToolbar() {
        return binding.trashFragmentContent.editOptionToolbar;
    }

    @Override
    protected String getTitle() {
        return owner.getString( R.string.trash );
    }

    @Override
    protected RecyclerView getRecyclerView() {
        return binding.trashFragmentContent.trashFragmentNotesRecyclerView.recyclerview;
    }

    @Override
    protected void showEditingToolBar( boolean show ) {
        Toolbar toolbar = getEditingToolbar();
        super.adjustRecyclerViewMargin( show );
        if ( show )
            toolbar.setVisibility( View.VISIBLE );
        else
            toolbar.setVisibility( View.GONE );
    }

    @Override
    protected void showConfirmationDialog( int noteCount ) {
        ConfirmationDialog confirmationDialog = ConfirmationDialog.newInstance(
                owner.getString( R.string.permanently_delete_notes, noteCount ) );
        confirmationDialog.addListener( new ConfirmationDialog.ConfirmationDialogListener() {
            @Override
            public void onCancelSelected() {
                notesViewModel.doneEditing();
                return;
            }

            @Override
            public void onYesSelected() {
                notesAdapter.permanentlyDeleteSelectedNotes();
                notesViewModel.doneEditing();
            }
        } );
        confirmationDialog.show( ( (AppCompatActivity) owner.getContext() ).getSupportFragmentManager(),
                "confirmation" );
    }
}
