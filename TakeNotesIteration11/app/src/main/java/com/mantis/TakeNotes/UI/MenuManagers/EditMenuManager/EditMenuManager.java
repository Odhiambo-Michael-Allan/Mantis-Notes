package com.mantis.TakeNotes.UI.MenuManagers.EditMenuManager;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mantis.TakeNotes.Adapters.NotesAdapter;
import com.mantis.TakeNotes.Dialogs.ConfirmationDialog;
import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.R;

import java.util.Locale;

public abstract class EditMenuManager {

    protected NotesAdapter notesAdapter;
    protected NotesViewModel notesViewModel;
    private boolean allNotesCheckedStatusChangeResultedFromUserAction = true, editStatus = false;
    protected Fragment owner;

    public EditMenuManager( Fragment owner, NotesAdapter notesAdapter,
                            NotesViewModel notesViewModel ) {
        this.notesAdapter = notesAdapter;
        this.notesViewModel = notesViewModel;
        this.owner = owner;
    }

    protected void observeEditingStatus() {
        notesViewModel.getObservableEditStatus().observe(
                owner.getViewLifecycleOwner(),
                new Observer<Boolean>() {
                    @Override
                    public void onChanged( Boolean editingEnabled ) {
                        updateAdapter( getAdapter() );
                        editStatusChanged( editingEnabled );
                    }
                }
        );
    }

    public void updateAdapter( NotesAdapter notesAdapter ) {
        this.notesAdapter = notesAdapter;
        observeNoNoteIsChecked();
        observeAllCheckBox();
        observeAllNotesAreChecked();
        observeNumberOfCheckedNotes();
        observeEditingToolbarMenusItems();
    }

    protected void observeNoNoteIsChecked() {
        notesAdapter.getObservableNoNoteIsChecked().observe(owner.getViewLifecycleOwner(),
                new Observer<Boolean>() {
                    @Override
                    public void onChanged( Boolean noNoteIsChecked ) {
                        showEditingToolBar( !noNoteIsChecked );
                    }
                } );
    }

    protected void showEditingToolBar( boolean show ) {
        Toolbar toolbar = getEditingToolbar();
        adjustRecyclerViewMargin( show );
        if ( show )
            toolbar.setVisibility( View.VISIBLE );
        else
            toolbar.setVisibility( View.GONE );
        hideArchiveOption( toolbar, false );
    }

    private void hideArchiveOption( Toolbar toolbar, boolean hide ) {
        MenuItem archiveMenuItem = toolbar.getMenu().findItem( R.id.archive );
        MenuItem unarchiveMenuItem = toolbar.getMenu().findItem( R.id.unarchive );
        archiveMenuItem.setVisible( !hide );
        unarchiveMenuItem.setVisible( hide );
    }

    protected void observeAllCheckBox() {
        View editOptions = getEditOptions();
        CheckBox allCheckBox = editOptions.findViewById( R.id.all_check_box );
        attachObserverToAllCheckBox( allCheckBox );
    }

    protected abstract View getEditOptions();

    private void attachObserverToAllCheckBox( CheckBox allCheckBox ) {
        allCheckBox.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged( CompoundButton compoundButton, boolean isChecked ) {
                if ( allNotesCheckedStatusChangeResultedFromUserAction )
                    notesAdapter.checkAllNotes( isChecked );

            }
        } );
    }

    protected void observeAllNotesAreChecked() {
        notesAdapter.getAllNotesAreCheckedStatus().observe( owner.getViewLifecycleOwner(),
                new Observer<Boolean>() {
                    @Override
                    public void onChanged( Boolean aBoolean ) {
                        allNotesChecked( aBoolean );
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

    protected void observeNumberOfCheckedNotes() {
        notesAdapter.getObservableNumberOfCheckedNotes().observe( owner.getViewLifecycleOwner(),
                new Observer<Integer>() {
                    @Override
                    public void onChanged( Integer integer ) {
                        setNumberOfCheckedNotes( integer );
                    }
                } );
    }

    public void setNumberOfCheckedNotes( int numberOfCheckedNotes ) {
        if ( !editStatus )
            return;
        if ( numberOfCheckedNotes < 1 ) {
            setCollapsingToolbarLayoutTitle( true );
            setMainTitle( true );
        }
        else {
            String title = numberOfCheckedNotes > 1 ? String.format(Locale.CANADA,
                    "%d Selected Notes", numberOfCheckedNotes ) : String.format( Locale.CANADA,
                    "%d Selected Note", numberOfCheckedNotes );
            setCollapsingTitle( title );
            setTitle( title );
        }
    }

    void setCollapsingToolbarLayoutTitle( boolean editingEnabled ) {
        if ( editingEnabled )
            setCollapsingTitle( owner.getString( R.string.select_notes ) );
        else
            setCollapsingTitle( getTitle() );
    }

    void setCollapsingTitle( String title ) {
        CollapsingToolbarLayout collapsingToolbarLayout = getCollapsingToolbarLayout();
        collapsingToolbarLayout.setTitle( title );
    }

    public void editStatusChanged( boolean editingEnabled ) {
        editStatus = editingEnabled;
        notifyAdapterOfEditStatusChange( editingEnabled );
        setCollapsingToolbarLayoutTitle( editingEnabled );
        setMainTitle( editingEnabled );
        showEditOptions( editingEnabled );
        showMainToolbar( editingEnabled );
        showFloatingActionButton( editingEnabled );
        showEditingToolBar( editingEnabled );
    }

    protected abstract NotesAdapter getAdapter();

    protected abstract CollapsingToolbarLayout getCollapsingToolbarLayout();

    protected void setMainTitle( boolean editingEnabled ) {
        if ( editingEnabled )
            setTitle( owner.getString( R.string.select_notes ) );
        else
            setTitle( getTitle() );
    }

    private void setTitle( String title ) {
        TextView mainTitleTextView = getMainTitleTextView();
        mainTitleTextView.setText( title );
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

    protected abstract Toolbar getMainToolbar();

    protected void adjustRecyclerViewMargin( boolean editingEnabled ) {
        RecyclerView recyclerView = getRecyclerView();
        ViewGroup.MarginLayoutParams layoutParams = ( ViewGroup.MarginLayoutParams )
                recyclerView.getLayoutParams();
        if ( editingEnabled )
            layoutParams.bottomMargin = 150;
        else
            layoutParams.bottomMargin = 0;
        recyclerView.setLayoutParams( layoutParams );
    }

    protected abstract void showFloatingActionButton( boolean editingEnabled );

    protected void observeEditingToolbarMenusItems() {
        MenuItem deleteMenuItem = getEditingToolbar().getMenu().findItem( R.id.delete );
        deleteMenuItem.setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick( @NonNull MenuItem menuItem ) {
                showConfirmationDialog( notesAdapter.getObservableNumberOfCheckedNotes().getValue() );
                return true;
            }
        } );
    }

    protected void showConfirmationDialog( int noteCount ) {
        ConfirmationDialog confirmationDialog = ConfirmationDialog.newInstance(
                owner.getString( R.string.move_notes_to_trash, noteCount ) );
        confirmationDialog.addListener( new ConfirmationDialog.ConfirmationDialogListener() {
            @Override
            public void onCancelSelected() {
                notesViewModel.doneEditing();
                return;
            }

            @Override
            public void onYesSelected() {
                notesAdapter.deleteSelectedNotes();
                notesViewModel.doneEditing();
            }
        } );
        confirmationDialog.show( ( (AppCompatActivity) owner.getContext() ).getSupportFragmentManager(),
                "confirmation" );
    }

    void notifyAdapterOfEditStatusChange( boolean editingEnabled ) {
        notesAdapter.editStatusChanged( editingEnabled );
    }

    protected abstract TextView getMainTitleTextView();
    protected abstract Toolbar getEditingToolbar();
    protected abstract String getTitle();
    protected abstract RecyclerView getRecyclerView();



}
