package com.mantis.TakeNotes.UI.FrequentlyUsedFragment;

import android.view.Menu;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.R;
import com.mantis.TakeNotes.UI.MenuManagers.ViewMenuManager.ViewMenuManager;
import com.mantis.TakeNotes.databinding.FragmentFrequentlyUsedBinding;

public class FrequentlyUsedFragmentViewMenuManager extends ViewMenuManager {

    private FragmentFrequentlyUsedBinding binding;
    public FrequentlyUsedFragmentViewMenuManager( Fragment owner, NotesViewModel notesViewModel,
                                                 FragmentFrequentlyUsedBinding binding ) {
        super( owner, notesViewModel );
        this.binding = binding;
        super.observeLayoutState();
    }
    @Override
    protected RecyclerView getRecyclerView() {
        return binding.frequentlyUsedFragmentContent
                .frequentlyUsedFragmentNotesRecyclerView.recyclerview;
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
                .frequentlyUsedFragmentAppBarLayout.toolbar.getMenu();
    }
}
