package com.mantis.takenotes.UI.FrequentlyUsedFragment;

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
import com.mantis.takenotes.databinding.TakeNotesFragmentFrequentlyUsedBinding;

public class FrequentlyUsedFragmentEditMenuManager extends EditMenuManager {

    private TakeNotesFragmentFrequentlyUsedBinding binding;

    public FrequentlyUsedFragmentEditMenuManager( Fragment owner,
                                                  NotesAdapter notesAdapter,
                                                  NotesViewModel notesViewModel,
                                                  TakeNotesFragmentFrequentlyUsedBinding binding ) {
        super( owner, notesAdapter, notesViewModel );
        this.binding = binding;
    }
    @Override
    protected NotesAdapter getAdapter() {
        return ( NotesAdapter ) binding.frequentlyUsedFragmentContent
                .notesRecyclerView.recyclerView.getAdapter();
    }

    @Override
    protected CollapsingToolbarLayout getCollapsingToolbarLayout() {
        return binding.frequentlyUsedFragmentContent
                .appBarLayout
                .collapsingToolbar;
    }

    @Override
    protected View getEditOptions() {
        return binding.frequentlyUsedFragmentContent.appBarLayout.editOptions;
    }

    @Override
    protected Toolbar getMainToolbar() {
        return binding.frequentlyUsedFragmentContent.appBarLayout.toolbar;
    }

    @Override
    protected void showFloatingActionButton( boolean editingEnabled ) {
        return;
    }

    @Override
    protected TextView getMainTitleTextView() {
        return binding.frequentlyUsedFragmentContent.appBarLayout.mainTitle;
    }

    @Override
    protected Toolbar getEditingToolbar() {
        return binding.frequentlyUsedFragmentContent.editOptionToolbar;
    }

    @Override
    protected String getTitle() {
        return owner.getString( R.string.frequent_fragment );
    }

    @Override
    protected RecyclerView getRecyclerView() {
        return binding.frequentlyUsedFragmentContent
                .notesRecyclerView.recyclerView;
    }
}
