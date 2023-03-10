package com.mantis.takenotes.UI.MenuManagers.EditMenuManager;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import com.mantis.takenotes.Adapters.NotesAdapter;
import com.mantis.takenotes.Commands.ArchiveCommand;
import com.mantis.takenotes.Commands.DeleteCommand;

import com.mantis.takenotes.Commands.ShareCommand;
import com.mantis.takenotes.Models.NotesViewModel;
import com.mantis.takenotes.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class EditMenuManager {

    protected NotesAdapter notesAdapter;
    protected NotesViewModel notesViewModel;
    private boolean allNotesCheckedStatusChangeResultedFromUserAction = true, editStatus = false;
    protected Fragment owner;
    private List<EditMenuManagerListener> listenerList = new ArrayList<>();

    public EditMenuManager( Fragment owner, NotesAdapter notesAdapter,
                            NotesViewModel notesViewModel ) {
        this.notesAdapter = notesAdapter;
        this.notesViewModel = notesViewModel;
        this.owner = owner;
    }

    public void updateEditingStatus( boolean editingStatus ) {
        updateAdapter( getAdapter() );
        editStatusChanged( editingStatus );
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

    protected void hideArchiveOption( Toolbar toolbar, boolean hide ) {
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
        observeDeleteMenuItem();
        observeArchiveMenuItem();
        observeUnarchiveMenuItem();
        observeShareMenuItem();
    }

    protected void observeDeleteMenuItem() {
        MenuItem deleteMenuItem = getEditingToolbar().getMenu().findItem( R.id.delete );
        deleteMenuItem.setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick( @NonNull MenuItem menuItem ) {
                new DeleteCommand( owner.getContext(), notesAdapter.getSelectedNotes(),
                        notesViewModel,
                        notesAdapter.getSelectedNotes().size() > 1 ? R.string.delete_multiple_notes :
                                R.string.delete_single_note ).execute();
                updateEditingStatus( false );
                notifyListenersOfEditStatusChange( false );
                return true;
            }
        } );
    }

    private void observeArchiveMenuItem() {
        MenuItem archiveMenuItem = getEditingToolbar().getMenu().findItem( R.id.archive );
        archiveMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick( @NonNull MenuItem menuItem ) {
                new ArchiveCommand( owner.getContext(), notesAdapter.getSelectedNotes(),
                        notesViewModel, true ).execute();
                updateEditingStatus( false );
                notifyListenersOfEditStatusChange( false );
                return true;
            }
        } );
    }

    private void observeUnarchiveMenuItem() {
        MenuItem unarchiveMenuItem = getEditingToolbar().getMenu().findItem( R.id.unarchive );
        unarchiveMenuItem.setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick( @NonNull MenuItem menuItem ) {
                new ArchiveCommand( owner.getContext(), notesAdapter.getSelectedNotes(),
                        notesViewModel, false ).execute();
                updateEditingStatus( false );
                notifyListenersOfEditStatusChange( false );
                return true;
            }
        } );
    }

    private void observeShareMenuItem() {
        MenuItem shareMenuItem = getEditingToolbar().getMenu().findItem( R.id.share );
        shareMenuItem.setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick( @NonNull MenuItem menuItem ) {
                new ShareCommand( owner.getContext(), notesAdapter.getSelectedNotes(),
                        notesViewModel ).execute();
                updateEditingStatus( false );
                notifyListenersOfEditStatusChange( false );
                return true;
            }
        } );
    }


    void notifyAdapterOfEditStatusChange( boolean editingEnabled ) {
        notesAdapter.editStatusChanged( editingEnabled );
    }

    protected abstract TextView getMainTitleTextView();
    protected abstract Toolbar getEditingToolbar();
    protected abstract String getTitle();
    protected abstract RecyclerView getRecyclerView();


    public void addListener( EditMenuManagerListener listener ) {
        listenerList.add( listener );
    }

    protected void notifyListenersOfEditStatusChange( boolean editing ) {
        for ( EditMenuManagerListener listener : listenerList )
            listener.editStatusChange( editing );
    }

    public interface EditMenuManagerListener {
        void editStatusChange( boolean editing );
    }
}
