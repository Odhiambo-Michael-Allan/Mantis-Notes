package com.mantis.takenotes.UI.HomeFragment;

import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import com.mantis.takenotes.Adapters.NotesAdapter;
import com.mantis.takenotes.Models.NotesViewModel;
import com.mantis.takenotes.UI.MenuManagers.EditMenuManager.EditMenuManager;

import com.mantis.takenotes.databinding.TakeNotesFragmentHomeBinding;

public class HomeFragmentEditMenuManager extends EditMenuManager {

    private TakeNotesFragmentHomeBinding binding;

    public HomeFragmentEditMenuManager( Fragment owner,
                                        NotesAdapter notesAdapter,
                                        NotesViewModel notesViewModel,
                                        TakeNotesFragmentHomeBinding binding ) {
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
    protected void showFloatingActionButton( boolean editingEnabled ) {
        if ( editingEnabled )
            binding.homeFragmentContent.fab.setVisibility( View.GONE );
        else
            binding.homeFragmentContent.fab.setVisibility( View.VISIBLE );
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
        return binding.homeFragmentContent.notesRecyclerView.recyclerView;
    }

    @Override
    protected NotesAdapter getAdapter() {
        return ( NotesAdapter  ) binding.homeFragmentContent
                .notesRecyclerView.recyclerView.getAdapter();
    }
}
