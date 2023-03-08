package com.mantis.takenotes.UI.FrequentlyUsedFragment;

import android.view.Menu;
import android.view.View;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.RecyclerView;
import com.mantis.takenotes.Models.NotesViewModel;
import com.mantis.takenotes.R;

import com.mantis.takenotes.UI.MenuManagers.ViewMenuManager.ViewMenuManager;
import com.mantis.takenotes.databinding.TakeNotesFragmentFrequentlyUsedBinding;

public class FrequentlyUsedFragmentViewMenuManager extends ViewMenuManager {

    private TakeNotesFragmentFrequentlyUsedBinding binding;
    public FrequentlyUsedFragmentViewMenuManager( Fragment owner, NotesViewModel notesViewModel,
                                                  TakeNotesFragmentFrequentlyUsedBinding binding ) {
        super( owner, notesViewModel );
        this.binding = binding;
        super.observeLayoutState();
    }
    @Override
    protected RecyclerView getRecyclerView() {
        return binding.frequentlyUsedFragmentContent
                .notesRecyclerView.recyclerView;
    }

    @Override
    protected void setEmptyText() {
        binding.frequentlyUsedFragmentContent
                .textEmpty.setText( owner.getString( R.string.no_notes ) );
    }

    @Override
    protected View getEmptyView() {
        return binding.frequentlyUsedFragmentContent.layoutEmpty;
    }

    @Override
    protected Menu getMenu() {
        return binding.frequentlyUsedFragmentContent
                .appBarLayout.toolbar.getMenu();
    }
}
