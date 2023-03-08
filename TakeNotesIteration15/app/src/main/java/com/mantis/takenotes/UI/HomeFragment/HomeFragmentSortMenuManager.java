package com.mantis.takenotes.UI.HomeFragment;

import android.view.MenuItem;
import androidx.fragment.app.Fragment;
import com.mantis.takenotes.Adapters.NotesAdapter;

import com.mantis.takenotes.Models.NotesViewModel;
import com.mantis.takenotes.R;
import com.mantis.takenotes.UI.MenuManagers.SortMenuManager.SortMenuManager;

import com.mantis.takenotes.databinding.TakeNotesFragmentHomeBinding;

public class HomeFragmentSortMenuManager extends SortMenuManager {

    private TakeNotesFragmentHomeBinding binding;

    public HomeFragmentSortMenuManager( Fragment owner, NotesViewModel notesViewModel,
                                       NotesAdapter notesAdapter, TakeNotesFragmentHomeBinding binding ) {
        super( owner, notesViewModel, notesAdapter );
        this.binding = binding;
        super.configureSortMenuItem();
        super.observeSortingConfiguration();
    }
    @Override
    protected MenuItem getSortMenuItem() {
        return binding.homeFragmentContent.appBarLayout.toolbar.getMenu().findItem(
                R.id.sort_option );
    }

    @Override
    protected NotesAdapter getAdapter() {
        return ( NotesAdapter ) binding.homeFragmentContent.notesRecyclerView.recyclerView.getAdapter();
    }
}
