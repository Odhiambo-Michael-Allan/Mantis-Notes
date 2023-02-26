package com.mantis.takenotes.UI.MenuManagers.ViewMenuManager;

import android.view.Menu;
import android.view.View;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import com.mantis.takenotes.Adapters.NotesAdapter;

import com.mantis.takenotes.Models.NotesViewModel;
import com.mantis.takenotes.Utils.MenuConfigurator;
import com.mantis.takenotes.Utils.RecyclerViewConfigurator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class ViewMenuManager {

    protected Fragment owner;
    protected NotesViewModel notesViewModel;
    private NotesAdapter notesAdapter;
    private List<ViewMenuManagerListener> listeners = new ArrayList<>();

    public ViewMenuManager( Fragment owner, NotesViewModel notesViewModel ) {
        this.owner = owner;
        this.notesViewModel = notesViewModel;
    }

    protected void observeLayoutState() {
        notesViewModel.getLayoutTypeConfig().observe( owner.getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged( Integer integer ) {
                configureRecyclerView( integer );
                MenuConfigurator.checkSelectedLayoutType( integer, getMenu() );
            }
        } );
    }

    protected void configureRecyclerView( int layoutType ) {
        RecyclerView recyclerView = getRecyclerView();
        createAppropriateLayoutType( layoutType, recyclerView );
        configureRecyclerViewComponents( recyclerView );
        notifyListenersOfAdapterChange();
    }

    protected abstract RecyclerView getRecyclerView();

    private void createAppropriateLayoutType( int layoutType, RecyclerView recyclerView ) {
        if ( layoutType == NotesViewModel.LAYOUT_STATE_SIMPLE_LIST )
            createSimpleListLayout( recyclerView );
        else if ( layoutType == NotesViewModel.LAYOUT_STATE_GRID )
            createGridLayout( recyclerView );
        else
            createListLayout( recyclerView );
    }

    private void createSimpleListLayout( RecyclerView recyclerView ) {
        RecyclerViewConfigurator.configureSimpleListRecyclerView( recyclerView, owner.getContext() );
    }

    private void createGridLayout( RecyclerView recyclerView ) {
        RecyclerViewConfigurator.configureGridLayoutRecyclerView( recyclerView, owner.getContext() );
    }

    private void createListLayout( RecyclerView recyclerView ) {
        RecyclerViewConfigurator.configureListRecyclerView( recyclerView, owner.getContext() );
    }

    private void configureRecyclerViewComponents( RecyclerView recyclerView ) {
        setEmptyText();
        notesAdapter = ( NotesAdapter ) recyclerView.getAdapter();
        notesAdapter.setEmptyView( getEmptyView() );
        notesAdapter.setNotesViewModel( notesViewModel );
    }

    protected abstract void setEmptyText();
    protected abstract View getEmptyView();

    private void notifyListenersOfAdapterChange() {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            ViewMenuManagerListener listener = ( ViewMenuManagerListener ) i.next();
            listener.onAdapterChange( this.notesAdapter );
        }
    }

    protected abstract Menu getMenu();

    public void addListener( ViewMenuManagerListener listener ) {
        listeners.add( listener );
    }

    public interface ViewMenuManagerListener {
        void onAdapterChange( NotesAdapter newNotesAdapter );
    }

}
