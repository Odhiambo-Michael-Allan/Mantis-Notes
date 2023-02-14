package com.mantis.TakeNotes.data.source.local;

import java.util.Date;

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
    @Ignore
    private boolean isChecked;

    public Note(String title, String description, String date, Date dateCreated ) {
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

    public void incrementAccessCount() {
        this.accessCount++;
    }

    public void resetAccessCount() {
        this.accessCount = 0;
    }

    public void setChecked( boolean checked ) {
        this.isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }

}
