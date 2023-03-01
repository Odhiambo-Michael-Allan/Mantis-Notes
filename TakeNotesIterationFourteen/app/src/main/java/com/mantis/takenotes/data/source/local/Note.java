package com.mantis.takenotes.data.source.local;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.mantis.takenotes.Models.NotesViewModel;
import com.mantis.takenotes.Utils.Logger;
import com.mantis.takenotes.data.source.NoteRepository;

@Entity( tableName = "notes_table" )
public class Note {

    @PrimaryKey( autoGenerate = true )
    private int id;
    private String title;
    private String description;
    private String date;
    private int accessCount;
    private Date dateCreated, dateLastModified;
    private int owner;
    private long timeLeft;
    private Date dateNoteWasLastDeleted;
    @Ignore
    private boolean isChecked;
    @Ignore
    private List<NoteObserver> observerList = new ArrayList<>();
    @Ignore
    private Calendar calendar = Calendar.getInstance();
    @Ignore
    private boolean noteHasAlreadyBeenNotifiedItsInTrash = false;

    public Note( String title, String description, String date, Date dateCreated ) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.accessCount = 0;
        this.dateCreated = dateCreated;
        this.dateLastModified = this.dateCreated;
    }

    public void setOwner( int owner ) {
        this.owner = owner;
    }

    public int getId() {
        return this.id;
    }

    public void setId( int newId ) {
        this.id = newId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDate() {
        return this.date;
    }

    public int getAccessCount() {
        return this.accessCount;
    }

    public void setAccessCount( int accessCount ) {
        this.accessCount = accessCount;
    }

    public Date getDateCreated() {
        return this.dateCreated;
    }

    public void setDateLastModified( Date date ) {
        this.dateLastModified = date;
    }

    public Date getDateLastModified() {
        return this.dateLastModified;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public int getOwner() {
        return this.owner;
    }

    public void setChecked( boolean checked ) {
        this.isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void initializeTimeLeft( long timeLeft ) {
        if ( this.timeLeft > 0 )
            return;
        //Logger.log( "Initializing time left for note with id: " + id );
        setTimeLeft( timeLeft );
    }

    public void setTimeLeft( long timeLeft ) {
        this.timeLeft = timeLeft;
    }

    public long getTimeLeft() {
        return this.timeLeft;
    }

    public void addObserver( NoteObserver observer ) {
        if ( observerAlreadyExists( observer ) )
            return;
        //Logger.log( "Adding observer with id: " + observer.getId() );
        observerList.add( observer );
    }

    private boolean observerAlreadyExists( NoteObserver observer ) {
        Iterator i = observerList.iterator();
        while ( i.hasNext() ) {
            NoteObserver noteObserver = ( NoteObserver ) i.next();
            if ( noteObserver.getId().equals( observer.getId() ) )
                return true;
        }
        return false;
    }

    public void decreaseTimeLeftBy( long millisecond, NoteRepository noteRepository ) {
        this.timeLeft -= millisecond;
        //Logger.log( "Note Id: " + this.id + " Time Left: " + this.timeLeft );
        notifyListenersOfTimeLeftDecreased();
        checkIfMyTimeIsUp();
    }

    private void checkIfMyTimeIsUp() {
        if ( this.timeLeft < 1 ) {
            notifyListenersOfTimeUp();
            observerList.clear();  // I'm dying so there's no need for these observers..
        }
    }

    private void notifyListenersOfTimeLeftDecreased() {
        Iterator i = observerList.iterator();
        while ( i.hasNext() ) {
            NoteObserver listener = (NoteObserver) i.next();
            listener.onTimeLeftDecreased( timeLeft );
        }
    }

    private void notifyListenersOfTimeUp() {
        Iterator i = observerList.iterator();
        while ( i.hasNext() ) {
            NoteObserver listener = (NoteObserver) i.next();
            listener.onTimeUp( this.id );
        }
    }

    public void removeObserver( String observerId ) {
        //Logger.log( "Removing observer with id: " + observerId );
        Iterator i = observerList.iterator();
        while ( i.hasNext() ) {
            NoteObserver observer = ( NoteObserver ) i.next();
            if ( observer.getId().equals( observerId ) )
                i.remove();
        }
    }

    public void addObservers( List<NoteObserver> observers ) {
        this.observerList.addAll( observers );
    }

    public List<NoteObserver> getObservers() {
        return observerList;
    }

    public void saveTimeLeftToDatabase( NoteRepository noteRepository ) {
        //Logger.log( "NOTE ID: " + this.id + " SAVING TIME LEFT: " + this.timeLeft );
        noteRepository.saveTimeLeft( this.id, this.timeLeft );
    }

    public void setDateNoteWasLastDeleted( Date date ) {
        this.dateNoteWasLastDeleted = date;
    }

    public Date getDateNoteWasLastDeleted() {
        return this.dateNoteWasLastDeleted;
    }

    public void adjustTimeLeft( NoteRepository noteRepository ) {
        if ( dateNoteWasLastDeleted == null )
            return;
        if ( noteHasAlreadyBeenNotifiedItsInTrash )
            return;
        noteHasAlreadyBeenNotifiedItsInTrash = true;
        Calendar calendar = Calendar.getInstance();
        Date currentDate = new Date();
        calendar.setTime( currentDate );
        long currentTimeInMilliseconds = calendar.getTimeInMillis();
        calendar.setTime( dateNoteWasLastDeleted );
        long dateNoteWasLastDeletedInMilliseconds = calendar.getTimeInMillis();
        long timeElapsed = currentTimeInMilliseconds - dateNoteWasLastDeletedInMilliseconds;
        Logger.log( "TIME ELAPSED: " + timeElapsed );
        decreaseTimeLeftBy( timeElapsed, noteRepository );
    }

    public void setNoteHasAlreadyBeenNotifiedItsInTrash( boolean hasAlreadyBeenNotifiedItsInTrash ) {
        this.noteHasAlreadyBeenNotifiedItsInTrash = hasAlreadyBeenNotifiedItsInTrash;
    }

    public boolean getHasAlreadyBeenNotifiedItsInTrash() {
        return noteHasAlreadyBeenNotifiedItsInTrash;
    }

    public void restore( NoteRepository noteRepository ) {
        observerList.clear();
        timeLeft = 0;
        noteHasAlreadyBeenNotifiedItsInTrash = false;
        accessCount = 0;
        owner = NotesViewModel.HOME_FRAGMENT;
        noteRepository.updateNoteOwner( id, owner );
        noteRepository.updateAccessCount( id, accessCount );
        noteRepository.saveTimeLeft( id, timeLeft );
    }


    public void save( NoteRepository noteRepository ) {
        if ( getTitle().equals( "" ) && getDescription().equals( "" ) )
            return;
        this.setOwner( NotesViewModel.HOME_FRAGMENT );
        noteRepository.insertNote( this );
    }

    public void edit( String newTitle, String newDescription, NoteRepository noteRepository ) {
        if ( newTitle.equals( "" ) && newDescription.equals( "" ) ) {
            noteRepository.deleteNote(getId());
            return;
        }
        setAccessCount( getAccessCount() + 1 );
        noteRepository.updateNote( getId(), newTitle, newDescription, new Date(), getAccessCount() );
        if ( getAccessCount() > 3 )
            noteRepository.updateNoteOwner( getId(), NotesViewModel.FREQUENT_FRAGMENT );
    }

    public void archive( NoteRepository noteRepository ) {
        noteRepository.updateNoteOwner( getId(), NotesViewModel.ARCHIVE_FRAGMENT );
    }

    public void unarchive( NoteRepository noteRepository ) {
        this.restore( noteRepository );
    }

    public void delete( NoteRepository noteRepository ) {
        if ( getOwner() == NotesViewModel.TRASH_FRAGMENT )
            noteRepository.deleteNote( getId() );
        else {
            noteRepository.updateNoteOwner( getId(), NotesViewModel.TRASH_FRAGMENT );
            noteRepository.setDateNoteWasLastDeleted( this.getId(), new Date() );
        }
    }

    public interface NoteObserver {
        String getId();
        void onTimeLeftDecreased( long timeLeft );
        void onTimeUp( int noteId );
    }
}