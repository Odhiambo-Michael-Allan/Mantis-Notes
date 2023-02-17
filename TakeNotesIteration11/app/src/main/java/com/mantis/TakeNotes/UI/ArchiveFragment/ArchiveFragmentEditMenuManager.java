package com.mantis.TakeNotes.UI.ArchiveFragment;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.mantis.TakeNotes.Adapters.NotesAdapter;
import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.R;
import com.mantis.TakeNotes.UI.MenuManagers.EditMenuManager.EditMenuManager;
import com.mantis.TakeNotes.databinding.FragmentArchiveBinding;

public class ArchiveFragmentEditMenuManager extends EditMenuManager {

    private FragmentArchiveBinding binding;

    public ArchiveFragmentEditMenuManager( Fragment owner, NotesAdapter notesAdapter,
                                          NotesViewModel notesViewModel, FragmentArchiveBinding binding ) {
        super( owner, notesAdapter, notesViewModel );
        this.binding = binding;
        super.observeEditingStatus();
    }
    @Override
    protected NotesAdapter getAdapter() {
        return ( NotesAdapter ) binding.archiveFragmentContent
                .archiveFragmentNotesRecyclerView.recyclerview.getAdapter();
    }

    @Override
    protected CollapsingToolbarLayout getCollapsingToolbarLayout() {
        return binding.archiveFragmentContent.archiveFragmentAppBarLayout.collapsingToolbar;
    }

    @Override
    protected View getEditOptions() {
        return binding.archiveFragmentContent.archiveFragmentAppBarLayout.editOptions;
    }

    @Override
    protected Toolbar getMainToolbar() {
        return binding.archiveFragmentContent.archiveFragmentAppBarLayout.toolbar;
    }

    @Override
    protected void showFloatingActionButton( boolean editingEnabled ) {
        return;
    }

    @Override
    protected TextView getMainTitleTextView() {
        return binding.archiveFragmentContent.archiveFragmentAppBarLayout.mainTitle;
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
        return binding.archiveFragmentContent.archiveFragmentNotesRecyclerView.recyclerview;
    }
}
