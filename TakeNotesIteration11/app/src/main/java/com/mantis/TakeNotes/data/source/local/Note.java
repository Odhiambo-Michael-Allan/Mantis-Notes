package com.mantis.TakeNotes.data.source.local;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

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
    @Ignore
    private boolean isChecked;
    @Ignore
    private List<NoteObserver> listenerList = new ArrayList<>();
    @Ignore
    private Calendar calendar = Calendar.getInstance();

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
        setTimeLeft( timeLeft );
    }

    public void setTimeLeft( long timeLeft ) {
        this.timeLeft = timeLeft;
    }

    public long getTimeLeft() {
        return this.timeLeft;
    }

    public void addListener( NoteObserver listener ) {
        listenerList.add( listener );
    }

    public void decreaseTimeLeftBy( int millisecond ) {
        this.timeLeft -= millisecond;
        notifyListenersOfTimeLeftDecreased();
        checkIfMyTimeIsUp();
    }

    private void checkIfMyTimeIsUp() {
        if ( this.timeLeft < 1 )
            notifyListenersOfTimeUp();
    }

    private void notifyListenersOfTimeLeftDecreased() {
        Iterator i = listenerList.iterator();
        while ( i.hasNext() ) {
            NoteObserver listener = (NoteObserver) i.next();
            listener.onTimeLeftDecreased( timeLeft );
        }
    }

    private void notifyListenersOfTimeUp() {
        Iterator i = listenerList.iterator();
        while ( i.hasNext() ) {
            NoteObserver listener = (NoteObserver) i.next();
            listener.onTimeUp( this.id );
        }
    }

    public void removeObserver( NoteObserver observer ) {
        listenerList.remove( observer );
    }

    public interface NoteObserver {
        void onTimeLeftDecreased( long timeLeft );
        void onTimeUp( int noteId );
    }

}
