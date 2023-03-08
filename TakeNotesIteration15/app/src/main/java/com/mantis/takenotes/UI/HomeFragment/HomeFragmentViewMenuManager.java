package com.mantis.takenotes.UI.HomeFragment;

import android.text.Html;
import android.view.Menu;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.mantis.takenotes.Models.NotesViewModel;

import com.mantis.takenotes.R;
import com.mantis.takenotes.UI.MenuManagers.ViewMenuManager.ViewMenuManager;
import com.mantis.takenotes.databinding.TakeNotesFragmentHomeBinding;

public class HomeFragmentViewMenuManager extends ViewMenuManager {

    private TakeNotesFragmentHomeBinding binding;

    public HomeFragmentViewMenuManager( Fragment owner, NotesViewModel notesViewModel,
                                        TakeNotesFragmentHomeBinding binding ) {
        super( owner, notesViewModel );
        this.binding = binding;
        super.observeLayoutState();
    }

    @Override
    protected RecyclerView getRecyclerView() {
        return binding.homeFragmentContent.notesRecyclerView.recyclerView;
    }

    @Override
    protected void setEmptyText() {
        binding.homeFragmentContent.textEmpty.setText( Html.fromHtml(
                owner.getString( R.string.home_fragment_empty_message ) ) );
    }

    @Override
    protected View getEmptyView() {
        return binding.homeFragmentContent.layoutEmpty;
    }

    @Override
    protected Menu getMenu() {
        return binding.homeFragmentContent.appBarLayout.toolbar.getMenu();
    }
}
