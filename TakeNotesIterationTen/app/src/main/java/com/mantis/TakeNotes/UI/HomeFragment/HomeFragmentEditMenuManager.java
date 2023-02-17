package com.mantis.TakeNotes.UI.HomeFragment;

import android.content.res.Resources;
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
import com.mantis.TakeNotes.databinding.FragmentHomeBinding;

public class HomeFragmentEditMenuManager extends EditMenuManager {

    private FragmentHomeBinding binding;
    public HomeFragmentEditMenuManager( Fragment owner,
                                        NotesAdapter notesAdapter,
                                        NotesViewModel notesViewModel,
                                        FragmentHomeBinding binding ) {
        super( owner, notesAdapter, notesViewModel );
        this.binding = binding;
        super.observeEditingStatus();

    }

    @Override
    protected void setMainTitle( boolean editingEnabled ) {
        if ( editingEnabled )
            binding.homeFragmentContent.appBarLayout.mainTitle.setText( "Select Notes" );
        else
            binding.homeFragmentContent.appBarLayout.mainTitle.setText( "All Notes" );
    }

    @Override
    protected TextView getMainTitleTextView() {
        return binding.homeFragmentContent.appBarLayout.mainTitle;
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

    @Override
    protected NotesAdapter getAdapter() {
        return ( NotesAdapter  ) binding.homeFragmentContent
                .notesRecyclerView.recyclerview.getAdapter();
    }
}
