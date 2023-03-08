package com.mantis.takenotes.Repository;

/**
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.Iterator;
import java.util.List;
import com.mantis.takenotes.Models.NotesViewModel;

import com.mantis.takenotes.Utils.NoteMapper;

/**
 * This class represents a note in the application.
 *
 * @author - Michael Allan Odhiambo
 * @email -  odhiambomichaelallan@gmail.com
 * @date - 4th March 2023
 */

public class Note {

    private int id;
    private String title;
    private String description;
    private int accessCount;
    private Date dateCreated;
    private Date dateLastModified;
    private int owner;
    private long timeLeft;
    private final Date dateNoteWasLastDeleted;
    private final NoteRepository noteRepository;

    private boolean isChecked;
    private final List<NoteObserver> observerList = new ArrayList<>();

    public Note( String title, String description, Date dateCreated, int owner ) {
        this( 0, title, description, 0, dateCreated, owner, 0L,
                null, null );
    }

    public Note( int id, String title, String description, int accessCount, Date dateCreated,
                 int owner, long timeLeft, Date dateNoteWasLastDeleted, NoteRepository noteRepository ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.accessCount = accessCount;
        this.dateCreated = dateCreated;
        this.dateLastModified = this.dateCreated;
        this.owner = owner;
        this.timeLeft = timeLeft;
        this.dateNoteWasLastDeleted = dateNoteWasLastDeleted;
        this.noteRepository = noteRepository;
        if ( getOwner() == NotesViewModel.TRASH_FRAGMENT )
            adjustTimeLeft();
    }

    public void adjustTimeLeft() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = new Date();
        calendar.setTime( currentDate );
        long currentTimeInMilliseconds = calendar.getTimeInMillis();
        calendar.setTime( dateNoteWasLastDeleted );
        long dateNoteWasLastDeletedInMilliseconds = calendar.getTimeInMillis();
        long timeElapsed = currentTimeInMilliseconds - dateNoteWasLastDeletedInMilliseconds;
        decreaseTimeLeftBy( timeElapsed );
    }

    public void decreaseTimeLeftBy( long millisecond ) {
        this.timeLeft -= millisecond;
        notifyListenersOfTimeLeftDecreased();
        checkIfMyTimeIsUp( noteRepository );
    }

    private void notifyListenersOfTimeLeftDecreased() {
        Iterator i = observerList.iterator();
        while ( i.hasNext() ) {
            NoteObserver listener = (NoteObserver) i.next();
            listener.onTimeLeftDecreased( timeLeft );
        }
    }

    private void checkIfMyTimeIsUp( NoteRepository noteRepository ) {
        if ( this.timeLeft < 1 ) {
            notifyListenersOfTimeUp();
            this.delete( noteRepository );
        }
    }

    private void notifyListenersOfTimeUp() {
        Iterator i = observerList.iterator();
        while ( i.hasNext() ) {
            NoteObserver listener = (NoteObserver) i.next();
            listener.onTimeUp( this.id );
        }
    }

    public void addObserver( NoteObserver observer ) {
        if ( observerAlreadyExists( observer ) )
            return;
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

    public void removeObserver( String observerId ) {
        Iterator i = observerList.iterator();
        while ( i.hasNext() ) {
            NoteObserver observer = ( NoteObserver ) i.next();
            if ( observer.getId().equals( observerId ) )
                i.remove();
        }
    }

    public void restore( NoteRepository noteRepository ) {
        observerList.clear();
        timeLeft = 0;
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
        noteRepository.insertNote( NoteMapper.noteToEntity( this ) );
    }

    public void edit( String newTitle, String newDescription, NoteRepository noteRepository ) {
        if ( newTitle.equals( "" ) && newDescription.equals( "" ) ) {
            noteRepository.deleteNote( getId() );
            return;
        }
        setAccessCount( getAccessCount() + 1 );
        noteRepository.updateNote( getId(), newTitle, newDescription, new Date(), getAccessCount() );
        if ( getAccessCount() > 3 )
            noteRepository.updateNoteOwner( getId(), NotesViewModel.FREQUENT_FRAGMENT );
    }

    public void delete( NoteRepository noteRepository ) {
        if ( getOwner() == NotesViewModel.TRASH_FRAGMENT )
            noteRepository.deleteNote( getId() );
        else {
            noteRepository.saveTimeLeft( getId(), NotesViewModel.TIME_TRASHED_NOTE_HAS_TO_LIVE );
            noteRepository.setDateNoteWasLastDeleted( this.getId(), new Date() );
            noteRepository.updateNoteOwner( getId(), NotesViewModel.TRASH_FRAGMENT );
        }
    }

    public void archive( NoteRepository noteRepository ) {
        noteRepository.updateNoteOwner( getId(), NotesViewModel.ARCHIVE_FRAGMENT );
    }

    public void unarchive( NoteRepository noteRepository ) {
        this.restore( noteRepository );
    }

    public void saveTimeLeftToDatabase() {
        noteRepository.saveTimeLeft( this.id, this.timeLeft );
    }

    public void saveDateNoteWasLastDeleted( Date date ) {
        noteRepository.setDateNoteWasLastDeleted( getId(), date );
    }


    public Date getDateCreated() {
        return this.dateCreated;
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
        return this.dateLastModified.toString();
    }

    public int getAccessCount() {
        return this.accessCount;
    }

    public void setAccessCount( int accessCount ) {
        this.accessCount = accessCount;
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

    public void setTimeLeft( long timeLeft ) {
        this.timeLeft = timeLeft;
    }

    public long getTimeLeft() {
        return this.timeLeft;
    }

    public interface NoteObserver {
        String getId();
        void onTimeLeftDecreased( long timeLeft );
        void onTimeUp( int noteId );
    }
}