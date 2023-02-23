package com.mantis.TakeNotes.UI.ArchiveFragment;

import android.view.Menu;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.R;
import com.mantis.TakeNotes.UI.MenuManagers.ViewMenuManager.ViewMenuManager;
import com.mantis.TakeNotes.databinding.FragmentArchiveBinding;

public class ArchiveFragmentViewMenuManager extends ViewMenuManager {

    private FragmentArchiveBinding binding;

    public ArchiveFragmentViewMenuManager( Fragment owner, NotesViewModel notesViewModel, FragmentArchiveBinding binding ) {
        super( owner, notesViewModel );
        this.binding = binding;
        super.observeLayoutState();
    }
    @Override
    protected RecyclerView getRecyclerView() {
        return binding.archiveFragmentContent.archiveFragmentNotesRecyclerView.recyclerview;
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
        return binding.archiveFragmentContent.archiveFragmentAppBarLayout.toolbar.getMenu();
    }
}
