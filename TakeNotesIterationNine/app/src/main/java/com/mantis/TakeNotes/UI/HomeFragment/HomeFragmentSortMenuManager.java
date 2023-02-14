package com.mantis.TakeNotes.UI.HomeFragment;

import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import com.mantis.TakeNotes.Adapters.NotesAdapter;
import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.R;
import com.mantis.TakeNotes.UI.MenuManagers.SortMenuManager.SortMenuManager;
import com.mantis.TakeNotes.databinding.FragmentHomeBinding;

public class HomeFragmentSortMenuManager extends SortMenuManager {

    private FragmentHomeBinding binding;
    public HomeFragmentSortMenuManager( Fragment owner, NotesViewModel notesViewModel,
                                       NotesAdapter notesAdapter, FragmentHomeBinding binding ) {
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
        return ( NotesAdapter ) binding.homeFragmentContent.notesRecyclerView.recyclerview.getAdapter();
    }
}
