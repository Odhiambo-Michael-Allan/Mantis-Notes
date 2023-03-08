package com.mantis.takenotes.UI.ArchiveFragment;

import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import com.mantis.takenotes.Adapters.NotesAdapter;
import com.mantis.takenotes.Models.NotesViewModel;
import com.mantis.takenotes.R;

import com.mantis.takenotes.UI.MenuManagers.EditMenuManager.EditMenuManager;
import com.mantis.takenotes.databinding.TakeNotesFragmentArchiveBinding;

public class ArchiveFragmentEditMenuManager extends EditMenuManager {

    private TakeNotesFragmentArchiveBinding binding;

    public ArchiveFragmentEditMenuManager( Fragment owner, NotesAdapter notesAdapter,
                                           NotesViewModel notesViewModel,
                                           TakeNotesFragmentArchiveBinding binding ) {
        super( owner, notesAdapter, notesViewModel );
        this.binding = binding;
    }
    @Override
    protected NotesAdapter getAdapter() {
        return ( NotesAdapter ) binding.archiveFragmentContent.
                notesRecyclerView.recyclerView.getAdapter();
    }

    @Override
    protected CollapsingToolbarLayout getCollapsingToolbarLayout() {
        return binding.archiveFragmentContent.appBarLayout.collapsingToolbar;
    }

    @Override
    protected View getEditOptions() {
        return binding.archiveFragmentContent.appBarLayout.editOptions;
    }

    @Override
    protected Toolbar getMainToolbar() {
        return binding.archiveFragmentContent.appBarLayout.toolbar;
    }

    @Override
    protected void showFloatingActionButton( boolean editingEnabled ) {
        return;
    }

    @Override
    protected TextView getMainTitleTextView() {
        return binding.archiveFragmentContent.appBarLayout.mainTitle;
    }

    @Override
    protected Toolbar getEditingToolbar() {
        return binding.archiveFragmentContent.editOptionToolbar;
    }

    @Override
    protected String getTitle() {
        return owner.getString( R.string.archive );
    }

    @Override
    protected RecyclerView getRecyclerView() {
        return binding.archiveFragmentContent.notesRecyclerView.recyclerView;
    }

    @Override
    protected void showEditingToolBar( boolean show ) {
        super.showEditingToolBar( show );
        if ( show )
            hideArchiveOption( getEditingToolbar(), true );
    }
}
