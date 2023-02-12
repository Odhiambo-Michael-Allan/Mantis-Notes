package com.mantis.TakeNotes.UI.HomeFragment;

import android.text.Html;
import android.view.Menu;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.R;
import com.mantis.TakeNotes.UI.MenuManagers.ViewMenuManager.ViewMenuManager;
import com.mantis.TakeNotes.databinding.FragmentHomeBinding;

public class HomeFragmentViewMenuManager extends ViewMenuManager {

    private FragmentHomeBinding binding;

    public HomeFragmentViewMenuManager(Fragment owner, NotesViewModel notesViewModel,
                                       FragmentHomeBinding binding ) {
        super( owner, notesViewModel );
        this.binding = binding;
        super.observeLayoutState();
    }

    @Override
    protected RecyclerView getRecyclerView() {
        return binding.homeFragmentContent.notesRecyclerView.recyclerview;
    }

    @Override
    protected void setEmptyText() {
        binding.homeFragmentContent.textEmpty.setText( Html.fromHtml(
                owner.getString( R.string.text_empty_message ) ) );
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
