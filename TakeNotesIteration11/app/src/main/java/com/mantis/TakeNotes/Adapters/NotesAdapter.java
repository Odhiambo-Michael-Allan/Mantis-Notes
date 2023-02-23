package com.mantis.TakeNotes.Adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.mantis.TakeNotes.Models.NotesViewModel;
import com.mantis.TakeNotes.Utils.Logger;
import com.mantis.TakeNotes.Utils.SortingUtil;
import com.mantis.TakeNotes.data.source.local.Note;
import com.mantis.TakeNotes.ViewHolders.NoteViewHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder> {


    private List<Note> data;
    private View emptyView;
    private NotesViewModel notesViewModel;
    private boolean editInProgress;
    private MutableLiveData<Boolean> allNotesAreChecked = new MutableLiveData<>();
    private MutableLiveData<Boolean> noNotesAreChecked = new MutableLiveData<>();
    private MutableLiveData<Integer> observableNumberOfCheckedNotes = new MutableLiveData<>();
    private int numberOfCheckedNotes = 0;
    private ArrayList<NoteAdapterListener> listeners = new ArrayList<>();
    private ArrayList<NoteViewHolder> noteViewHolders = new ArrayList<>();

    private MutableLiveData<Boolean> observableNoNoteIsChecked = new MutableLiveData<>();

    private String queryToHighlight = null;

    private boolean allNotesCheckedStatusChangeAsAResultOfUserAction = true;

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        View view = getNoteView( parent );
        NoteViewHolder noteViewHolder = getNoteViewHolder( view, this );
        noteViewHolder.addListener( new NoteViewHolder.NoteViewHolderListener() {
            @Override
            public void onClick( View view, int viewHolderPosition ) {
                notifyListenersOfViewHolderClick( view, viewHolderPosition );
            }
            @Override
            public void onLongClick( int viewHolderPosition ) {
                notifyListenersOfViewHolderLongClick( viewHolderPosition );
            }

            @Override
            public void onCheckButtonChange( boolean check ) {
                int adapterPosition = noteViewHolder.getAdapterPosition();
                if ( adapterPosition >= 0 ) {
                    getData().get( adapterPosition ).setChecked( check );
                    updateNumberOfCheckedNotes();
                }
                sendNoNoteIsCheckedNotification();


                allNotesAreChecked.setValue( numberOfCheckedNotes == getData().size() );
                observableNumberOfCheckedNotes.postValue( numberOfCheckedNotes );
            }
        } );
        noteViewHolders.add( noteViewHolder );
        return noteViewHolder;
    }

    private void updateNumberOfCheckedNotes() {
        numberOfCheckedNotes = 0;
        Iterator i = getData().iterator();
        while ( i.hasNext() ) {
            Note note = (Note) i.next();
            if ( note.isChecked() )
                numberOfCheckedNotes++;
        }
        observableNumberOfCheckedNotes.postValue( numberOfCheckedNotes );
    }

    private void sendNoNoteIsCheckedNotification() {
        observableNoNoteIsChecked.postValue( numberOfCheckedNotes == 0 );
    }

    public LiveData<Boolean> getObservableNoNoteIsChecked() {
        return observableNoNoteIsChecked;
    }

    public LiveData<Integer> getObservableNumberOfCheckedNotes() {
        return observableNumberOfCheckedNotes;
    }

    protected abstract View getNoteView( @NonNull ViewGroup parent );

    protected abstract NoteViewHolder getNoteViewHolder( View view, NotesAdapter adapter );

    private void notifyListenersOfViewHolderClick( View view, int viewHolderPosition ) {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            NoteAdapterListener listener = ( NoteAdapterListener ) i.next();
            listener.onViewHolderClicked( view, viewHolderPosition );
        }
    }

    private void notifyListenersOfViewHolderLongClick( int viewHolderPosition ) {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            NoteAdapterListener listener = ( NoteAdapterListener ) i.next();
            listener.onViewHolderLongClicked( viewHolderPosition );
        }
    }

    @Override
    public void onBindViewHolder( @NonNull NoteViewHolder holder, int position ) {
        Note noteAtPosition = getData().get( position );
        if ( editInProgress )
            holder.showCheckBox();
        if ( noteAtPosition.isChecked() )
            holder.check( true );
        else
            holder.check( false );

        holder.bindData( data.get( position ) );
        holder.setPosition( position );
        if ( queryToHighlight != null )
            holder.highlightQuery( queryToHighlight );
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData( List<Note> data ) {
        this.data = data;
        if ( data.size() == 0 ) {
            notifyListenersRecyclerViewEmpty( true );
            showEmptyView( true );
        }
        else {
            notifyListenersRecyclerViewEmpty( false );
            showEmptyView( false );
        }
        sortData();
        notifyListenersOfNoteSizeChange( getData().size() );
        
        notifyDataSetChanged();
    }

    private void notifyListenersRecyclerViewEmpty( boolean empty ) {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            NoteAdapterListener listener = ( NoteAdapterListener ) i.next();
            listener.onRecyclerViewEmpty( empty );
        }
    }


    public List<Note> getData() {
        return this.data;
    }

    public void setEmptyView( View view ) {
        this.emptyView = view;
    }

    private void showEmptyView( boolean show ) {
        if ( show )
            this.emptyView.setVisibility( View.VISIBLE );
        else
            this.emptyView.setVisibility( View.GONE );
    }

    public void setNotesViewModel( NotesViewModel viewModel ) {
        this.notesViewModel = viewModel;
    }

    public void addListener( NoteAdapterListener listener ) {
        listeners.add( listener );
    }

    public void sortData() {
        List<Note> data = SortingUtil.sortList( getData(),
                notesViewModel.getCurrentAscendingConfigOption(),
                notesViewModel.getCurrentSortingStrategyConfigOption() );
        this.data = data;
        notifyDataSetChanged();

    }

    public void checkAllNotes( boolean check ) {
        Iterator i = getData().iterator();
        while ( i.hasNext() ) {
            Note note = (Note) i.next();
            note.setChecked( check );
        }
        notifyDataSetChanged();
    }

    public void editStatusChanged( boolean edit ) {
        this.editInProgress = edit;
        numberOfCheckedNotes = 0;
        sendNoNoteIsCheckedNotification();
        if ( edit )
            notifyViewHoldersToShowCheckBox();
        else {
            checkAllNotes( false );
            notifyViewHoldersToHideCheckBox();
        }
    }

    public void notifyViewHoldersToShowCheckBox() {
        Iterator i = noteViewHolders.iterator();
        while ( i.hasNext() ) {
            NoteViewHolder noteViewHolder = ( NoteViewHolder ) i.next();
            noteViewHolder.showCheckBox();
        }
    }

    public void notifyViewHoldersToHideCheckBox() {
        Iterator i = noteViewHolders.iterator();
        while ( i.hasNext() ) {
            NoteViewHolder noteViewHolder = ( NoteViewHolder ) i.next();
            noteViewHolder.hideCheckBox();
        }
    }

    private void notifyListenersOfNoteSizeChange( int newSize ) {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            NoteAdapterListener listener = ( NoteAdapterListener ) i.next();
            listener.onNoteNotesSizeChange( newSize );
        }
    }

    public void deleteSelectedNotes() {
        List<Note> unselectedNotes = new ArrayList<>(), selectedNotes = new ArrayList<>();
        Iterator i = getData().iterator();
        while ( i.hasNext() ) {
            Note note = ( Note ) i.next();
            if ( note.isChecked() )
                selectedNotes.add( note );
            else
                unselectedNotes.add( note );
        }
        setData( unselectedNotes );
        notesViewModel.deleteReferencesIn( selectedNotes );
    }

    public void permanentlyDeleteSelectedNotes() {
        List<Note> unselectedNotes = new ArrayList<>(), selectedNotes = new ArrayList<>();
        Iterator i = getData().iterator();
        while ( i.hasNext() ) {
            Note note = ( Note ) i.next();
            if ( note.isChecked() )
                selectedNotes.add( note );
            else
                unselectedNotes.add( note );
        }
        setData( unselectedNotes );
        notesViewModel.permanentlyDeleteNotesIn( selectedNotes );
    }

    public LiveData<Boolean> getAllNotesAreCheckedStatus() {
        return allNotesAreChecked;
    }

    public void notifyViewHoldersToHighlightTextMatching( String query ) {
        queryToHighlight = query;
        notifyDataSetChanged();
    }

    public interface NoteAdapterListener {
        void onViewHolderClicked( View view, int viewHolderPosition );
        void onViewHolderLongClicked( int viewHolderPosition );
        void onRecyclerViewEmpty( boolean isEmpty );
        void onNoteNotesSizeChange( int newNotesSize );
    }
}
