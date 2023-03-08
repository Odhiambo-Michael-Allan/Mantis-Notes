package com.mantis.takenotes.UI.TrashFragment;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import com.mantis.takenotes.Adapters.NotesAdapter;
import com.mantis.takenotes.Commands.DeleteCommand;

import com.mantis.takenotes.Commands.RestoreNoteCommand;
import com.mantis.takenotes.Models.NotesViewModel;

import com.mantis.takenotes.R;
import com.mantis.takenotes.UI.MenuManagers.EditMenuManager.EditMenuManager;
import com.mantis.takenotes.Repository.Note;

import com.mantis.takenotes.databinding.TakeNotesFragmentTrashBinding;

import java.util.List;

public class TrashFragmentEditMenuManager extends EditMenuManager {

    private TakeNotesFragmentTrashBinding binding;

    public TrashFragmentEditMenuManager( Fragment owner, NotesAdapter notesAdapter,
                                         NotesViewModel notesViewModel,
                                         TakeNotesFragmentTrashBinding binding ) {
        super( owner, notesAdapter, notesViewModel );
        this.binding = binding;
    }
    @Override
    protected View getEditOptions() {
        return binding.trashFragmentContent.trashFragmentAppBarLayout.editOptions;
    }

    @Override
    protected NotesAdapter getAdapter() {
        return ( NotesAdapter ) binding.trashFragmentContent.notesRecyclerView
                .recyclerView.getAdapter();
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
        return binding.trashFragmentContent.notesRecyclerView.recyclerView;
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
    protected void observeEditingToolbarMenusItems() {
        observeRestoreMenuItem();
        observeDeleteMenuItem();
    }

    private void observeRestoreMenuItem() {
        MenuItem restoreMenuItem = getEditingToolbar().getMenu().findItem( R.id.restore );
        restoreMenuItem.setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick( @NonNull MenuItem menuItem ) {
                List<Note> notesToRestore = notesAdapter.getSelectedNotes();
                new RestoreNoteCommand( notesToRestore,
                        notesViewModel ).execute();
                TrashFragmentEditMenuManager.super.updateEditingStatus( false );
                TrashFragmentEditMenuManager.super.notifyListenersOfEditStatusChange( false );
                return true;
            }
        } );
    }

    @Override
    protected void observeDeleteMenuItem() {
        MenuItem deleteMenuItem = getEditingToolbar().getMenu().findItem( R.id.delete );
        deleteMenuItem.setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick( @NonNull MenuItem menuItem ) {
                new DeleteCommand( owner.getContext(), notesAdapter.getSelectedNotes(),
                        notesViewModel,
                        notesAdapter.getSelectedNotes().size() > 1 ?
                                R.string.permanently_delete_multiple_notes :
                                R.string.permanently_delete_single_note).execute();
                TrashFragmentEditMenuManager.super.updateEditingStatus( false );
                TrashFragmentEditMenuManager.super.notifyListenersOfEditStatusChange( false );
                return true;
            }
        } );
    }
}
