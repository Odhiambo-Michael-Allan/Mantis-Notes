package com.mantis.MantisNotesIterationOne.Models;

public class Note {

    private String title;
    private String description;
    private String date;
    private int accessCount;

    public Note( String title, String description, String date ) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.accessCount = 0;
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

    public void setTitle( String title ) {
        this.title = title;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public void incrementAccessCount() {
        this.accessCount++;
    }

    public void setAccessCount( int accessCount ) {
        this.accessCount = accessCount;
    }
}
