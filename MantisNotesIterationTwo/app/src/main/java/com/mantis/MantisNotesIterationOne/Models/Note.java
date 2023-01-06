package com.mantis.MantisNotesIterationOne.Models;

public class Note {

    private String title;
    private String description;
    private String time;
    private int accessCount;

    public Note( String title, String description, String time ) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.accessCount = 0;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getTime() {
        return this.time;
    }

    public int getAccessCount() {
        return this.accessCount;
    }

    public void incrementAccessCount() {
        this.accessCount++;
    }

    public void setAccessCount( int accessCount ) {
        this.accessCount = accessCount;
    }
}
