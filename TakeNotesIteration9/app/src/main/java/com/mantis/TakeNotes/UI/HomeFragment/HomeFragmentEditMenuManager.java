package com.mantis.TakeNotes.UI.HomeFragment;

import android.view.View;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mantis.TakeNotes.Adapters.NotesAdapter;
import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.UI.MenuManagers.EditMenuManager.EditMenuManager;
import com.mantis.TakeNotes.databinding.FragmentHomeBinding;

public class HomeFragmentEditMenuManager extends EditMenuManager {

    private FragmentHomeBinding binding;
    public HomeFragmentEditMenuManager( NotesAdapter notesAdapter,
                                        NotesViewModel notesViewModel,
                                        FragmentHomeBinding binding ) {
        super( notesAdapter, notesViewModel );
        this.binding = binding;
        super.observeAllCheckBox();
        super.observeEditingToolbarMenusItems();
    }

    @Override
    protected View getEditOptions() {
        return binding.homeFragmentContent.appBarLayout.editOptions;
    }

    @Override
    protected Toolbar getMainToolbar() {
        return binding.homeFragmentContent.appBarLayout.toolbar;
    }

    @Override
    protected Toolbar getEditingToolbar() {
        return binding.homeFragmentContent.editOptionToolbar;
    }

    @Override
    protected CollapsingToolbarLayout getCollapsingToolbarLayout() {
        return binding.homeFragmentContent.appBarLayout.collapsingToolbar;
    }

    @Override
    protected String getTitle() {
        return "Home";
    }

    @Override
    protected RecyclerView getRecyclerView() {
        return binding.homeFragmentContent.notesRecyclerView.recyclerview;
    }

    @Override
    protected FloatingActionButton getFloatingActionButton() {
        return binding.homeFragmentContent.fab;
    }
}
