package com.mantis.TakeNotes.UI.TrashFragment;

import android.view.Menu;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.R;
import com.mantis.TakeNotes.UI.MenuManagers.ViewMenuManager.ViewMenuManager;
import com.mantis.TakeNotes.databinding.FragmentArchiveBinding;
import com.mantis.TakeNotes.databinding.FragmentTrashBinding;

public class TrashFragmentViewMenuManager extends ViewMenuManager {

    private FragmentTrashBinding binding;

    public TrashFragmentViewMenuManager( Fragment owner, NotesViewModel notesViewModel,
                                         FragmentTrashBinding binding ) {
        super( owner, notesViewModel );
        this.binding = binding;
    }
    @Override
    protected RecyclerView getRecyclerView() {
        return binding.trashFragmentContent.trashFragmentNotesRecyclerView.recyclerview;
    }

    @Override
    protected void setEmptyText() {
        binding.trashFragmentContent.textEmpty.setText( owner.getString( R.string.trash ) );
    }

    @Override
    protected View getEmptyView() {
        return binding.trashFragmentContent.layoutEmpty;
    }

    @Override
    protected Menu getMenu() {
        return binding.trashFragmentContent.trashFragmentAppBarLayout.toolbar.getMenu();
    }
}
