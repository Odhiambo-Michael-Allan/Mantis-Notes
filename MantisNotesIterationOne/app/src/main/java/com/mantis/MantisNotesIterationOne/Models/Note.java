package com.mantis.MantisNotesIterationOne.Models;

public class Note {

    private String title;
    private String description;
    private String time;

    public Note( String title, String description, String time ) {
        this.title = title;
        this.description = description;
        this.time = time;
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
}
