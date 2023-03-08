package com.mantis.takenotes.UI.ArchiveFragment;

import android.view.Menu;
import android.view.View;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.RecyclerView;
import com.mantis.takenotes.Models.NotesViewModel;
import com.mantis.takenotes.R;

import com.mantis.takenotes.UI.MenuManagers.ViewMenuManager.ViewMenuManager;
import com.mantis.takenotes.databinding.TakeNotesFragmentArchiveBinding;

public class ArchiveFragmentViewMenuManager extends ViewMenuManager {

    private TakeNotesFragmentArchiveBinding binding;

    public ArchiveFragmentViewMenuManager( Fragment owner, NotesViewModel notesViewModel,
                                           TakeNotesFragmentArchiveBinding binding ) {
        super( owner, notesViewModel );
        this.binding = binding;
        super.observeLayoutState();
    }
    @Override
    protected RecyclerView getRecyclerView() {
        return binding.archiveFragmentContent.notesRecyclerView.recyclerView;
    }

    @Override
    protected void setEmptyText() {
        binding.archiveFragmentContent.textEmpty.setText( owner.getString( R.string.no_notes ) );
    }

    @Override
    protected View getEmptyView() {
        return binding.archiveFragmentContent.layoutEmpty;
    }

    @Override
    protected Menu getMenu() {
        return binding.archiveFragmentContent.appBarLayout.toolbar.getMenu();
    }
}
