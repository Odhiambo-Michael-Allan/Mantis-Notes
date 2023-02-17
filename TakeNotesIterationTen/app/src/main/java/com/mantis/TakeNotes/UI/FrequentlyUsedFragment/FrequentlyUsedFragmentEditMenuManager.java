package com.mantis.TakeNotes.UI.FrequentlyUsedFragment;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mantis.TakeNotes.Adapters.NotesAdapter;
import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.R;
import com.mantis.TakeNotes.UI.MenuManagers.EditMenuManager.EditMenuManager;
import com.mantis.TakeNotes.databinding.FragmentFrequentlyUsedBinding;
import com.mantis.TakeNotes.databinding.FragmentHomeBinding;

public class FrequentlyUsedFragmentEditMenuManager extends EditMenuManager {

    private FragmentFrequentlyUsedBinding binding;

    public FrequentlyUsedFragmentEditMenuManager( Fragment owner,
                                                  NotesAdapter notesAdapter,
                                                  NotesViewModel notesViewModel,
                                                  FragmentFrequentlyUsedBinding binding ) {
        super( owner, notesAdapter, notesViewModel );
        this.binding = binding;
        super.observeEditingStatus();
    }
    @Override
    protected NotesAdapter getAdapter() {
        return ( NotesAdapter ) binding.frequentlyUsedFragmentContent
                .frequentlyUsedFragmentNotesRecyclerView
                .recyclerview.getAdapter();
    }

    @Override
    protected CollapsingToolbarLayout getCollapsingToolbarLayout() {
        return binding.frequentlyUsedFragmentContent
                .frequentlyUsedFragmentAppBarLayout
                .collapsingToolbar;
    }

    @Override
    protected View getEditOptions() {
        return binding.frequentlyUsedFragmentContent.frequentlyUsedFragmentAppBarLayout.editOptions;
    }

    @Override
    protected Toolbar getMainToolbar() {
        return binding.frequentlyUsedFragmentContent.frequentlyUsedFragmentAppBarLayout.toolbar;
    }

    @Override
    protected TextView getMainTitleTextView() {
        return binding.frequentlyUsedFragmentContent.frequentlyUsedFragmentAppBarLayout.mainTitle;
    }

    @Override
    protected Toolbar getEditingToolbar() {
        return null;
    }

    @Override
    protected String getTitle() {
        return owner.getString( R.string.frequently_used_fragment );
    }

    @Override
    protected RecyclerView getRecyclerView() {
        return null;
    }

    @Override
    protected FloatingActionButton getFloatingActionButton() {
        return null;
    }
}
