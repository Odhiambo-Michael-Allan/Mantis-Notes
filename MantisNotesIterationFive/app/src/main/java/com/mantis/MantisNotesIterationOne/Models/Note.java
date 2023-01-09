package com.mantis.MantisNotesIterationOne.Models;

import java.util.Date;

public class Note {

    private String title;
    private String description;
    private String date;
    private int accessCount;
    private Date dateCreated, dateLastModified;

    public Note( String title, String description, String date, Date dateCreated ) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.accessCount = 0;
        this.dateCreated = dateCreated;
        this.dateLastModified = this.dateCreated;
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

    public Date getDateCreated() {
        return this.dateCreated;
    }

    public void setDateLastModified( Date date ) {
        this.dateLastModified = date;
    }

    public Date getDatelastModified() {
        return this.dateLastModified;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public void incrementAccessCount() {
        this.accessCount++;
    }

}
