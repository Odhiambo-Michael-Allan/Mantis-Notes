package com.mantis.TakeNotes.UI.MenuManagers.EditMenuManager;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mantis.TakeNotes.Adapters.NotesAdapter;
import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.R;

public abstract class EditMenuManager {

    private NotesAdapter notesAdapter;
    private NotesViewModel notesViewModel;
    private boolean allNotesCheckedStatusChangeResultedFromUserAction = true;

    public EditMenuManager( NotesAdapter notesAdapter, NotesViewModel notesViewModel ) {
        this.notesAdapter = notesAdapter;
        this.notesViewModel = notesViewModel;

    }

    protected void observeAllCheckBox() {
        View editOptions = getEditOptions();
        CheckBox allCheckBox = editOptions.findViewById( R.id.all_check_box );
        attachObserverToAllCheckBox( allCheckBox );
    }

    private void attachObserverToAllCheckBox( CheckBox allCheckBox ) {
        allCheckBox.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged( CompoundButton compoundButton, boolean isChecked ) {
                if ( allNotesCheckedStatusChangeResultedFromUserAction )
                    notesAdapter.checkAllNotes( isChecked );

            }
        });
    }

    protected void observeEditingToolbarMenusItems() {
        MenuItem deleteMenuItem = getEditingToolbar().getMenu().findItem( R.id.delete );
        deleteMenuItem.setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick( @NonNull MenuItem menuItem ) {
                notesAdapter.deleteSelectedNotes();
                notesViewModel.doneEditing();
                return true;
            }
        } );
    }


    public void allNotesChecked( boolean checked ) {
        allNotesCheckedStatusChangeResultedFromUserAction = false;
        View editOptions = getEditOptions();
        CheckBox allCheckBox = editOptions.findViewById( R.id.all_check_box );
        allCheckBox.setChecked( checked );
        allNotesCheckedStatusChangeResultedFromUserAction = true;
    }

    public void editStatusChanged( boolean editingEnabled ) {
        notifyAdapterOfEditStatusChange( editingEnabled );
        setCollapsingToolbarLayoutTitle( editingEnabled );
        showEditOptions( editingEnabled );
        showEditingToolBar( editingEnabled );
        showMainToolbar( editingEnabled );
        adjustRecyclerViewMargin( editingEnabled );
        showFloatingActionButton( editingEnabled );
    }

    void showEditOptions( boolean show ) {
        View editOptions = getEditOptions();
        CheckBox allCheckBox = editOptions.findViewById( R.id.all_check_box );
        allCheckBox.setChecked( false );
        if ( show )
            editOptions.setVisibility( View.VISIBLE );
        else
            editOptions.setVisibility( View.GONE );
    }


    void showMainToolbar( boolean editingEnabled ) {
        Toolbar toolbar = getMainToolbar();
        if ( editingEnabled )
            toolbar.setVisibility( View.GONE );
        else
            toolbar.setVisibility( View.VISIBLE );
    }

    void showEditingToolBar( boolean show ) {
        Toolbar toolbar = getEditingToolbar();
        if ( show )
            toolbar.setVisibility( View.VISIBLE );
        else
            toolbar.setVisibility(View.GONE);
    }

    private void adjustRecyclerViewMargin( boolean editingEnabled ) {
        RecyclerView recyclerView = getRecyclerView();
        ViewGroup.MarginLayoutParams layoutParams = ( ViewGroup.MarginLayoutParams )
                recyclerView.getLayoutParams();
        if ( editingEnabled )
            layoutParams.bottomMargin = 150;
        else
            layoutParams.bottomMargin = 0;
        recyclerView.setLayoutParams( layoutParams );
    }

    private void showFloatingActionButton( boolean editingEnabled ) {
        FloatingActionButton floatingActionButton = getFloatingActionButton();
        if ( editingEnabled )
            floatingActionButton.setVisibility( View.GONE );
        else
            floatingActionButton.setVisibility( View.VISIBLE );
    }

    void notifyAdapterOfEditStatusChange( boolean editingEnabled ) {
        notesAdapter.editStatusChanged( editingEnabled );
    }

    void setCollapsingToolbarLayoutTitle( boolean editingEnabled ) {
        CollapsingToolbarLayout collapsingToolbarLayout = getCollapsingToolbarLayout();
        if ( editingEnabled )
            collapsingToolbarLayout.setTitle( "Select Notes" );
        else
            collapsingToolbarLayout.setTitle( getTitle() );
    }

    public void updateAdapter( NotesAdapter notesAdapter ) {
        this.notesAdapter = notesAdapter;
    }

    protected abstract View getEditOptions();
    protected abstract Toolbar getMainToolbar();
    protected abstract Toolbar getEditingToolbar();
    protected abstract CollapsingToolbarLayout getCollapsingToolbarLayout();
    protected abstract String getTitle();
    protected abstract RecyclerView getRecyclerView();
    protected abstract FloatingActionButton getFloatingActionButton();
}
