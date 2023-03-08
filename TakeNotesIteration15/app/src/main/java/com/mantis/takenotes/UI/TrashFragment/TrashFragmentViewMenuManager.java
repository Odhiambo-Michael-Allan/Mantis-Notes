package com.mantis.takenotes.UI.TrashFragment;

import android.view.Menu;
import android.view.View;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import com.mantis.takenotes.Models.NotesViewModel;

import com.mantis.takenotes.R;
import com.mantis.takenotes.UI.MenuManagers.ViewMenuManager.ViewMenuManager;
import com.mantis.takenotes.Utils.MenuConfigurator;

import com.mantis.takenotes.databinding.TakeNotesFragmentArchiveBinding;
import com.mantis.takenotes.databinding.TakeNotesFragmentTrashBinding;

public class TrashFragmentViewMenuManager extends ViewMenuManager {

    private TakeNotesFragmentTrashBinding binding;

    public TrashFragmentViewMenuManager( Fragment owner, NotesViewModel notesViewModel,
                                         TakeNotesFragmentTrashBinding binding ) {
        super( owner, notesViewModel );
        this.binding = binding;
        observeLayoutState();
    }
    @Override
    protected RecyclerView getRecyclerView() {
        return binding.trashFragmentContent.notesRecyclerView.recyclerView;
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

    @Override
    protected void observeLayoutState() {
        notesViewModel.getLayoutTypeConfig().observe( owner.getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged( Integer integer ) {
                configureRecyclerView( integer );
            }
        } );
    }
}
