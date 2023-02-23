package com.mantis.TakeNotes.UI.TrashFragment;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.mantis.TakeNotes.Adapters.NotesAdapter;
import com.mantis.TakeNotes.Commands.Command;
import com.mantis.TakeNotes.Commands.DeleteCommand;
import com.mantis.TakeNotes.Commands.RestoreNoteCommand;
import com.mantis.TakeNotes.Dialogs.ConfirmationDialog;
import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.R;
import com.mantis.TakeNotes.UI.MenuManagers.EditMenuManager.EditMenuManager;
import com.mantis.TakeNotes.data.source.local.Note;
import com.mantis.TakeNotes.databinding.FragmentTrashBinding;

import java.util.List;

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
                return true;
            }
        } );
    }


    @Override
    protected void observeEditingToolbarMenusItems() {
        MenuItem restoreMenuItem = getEditingToolbar().getMenu().findItem( R.id.restore );
        restoreMenuItem.setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick( @NonNull MenuItem menuItem ) {
                List<Note> notesToRestore = notesAdapter.getSelectedNotes();
                Command restoreCommand = new RestoreNoteCommand( notesToRestore,
                        notesViewModel );
                restoreCommand.execute();
                return true;
            }
        } );
    }
}
