package com.mantis.TakeNotes.data.source.local;

public class Query {

    private String description, dateSubmited;

    public Query( String description, String dateSubmited ) {
        this.description = description;
        this.dateSubmited = dateSubmited;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDateSubmited() {
        return this.dateSubmited;
    }
}
