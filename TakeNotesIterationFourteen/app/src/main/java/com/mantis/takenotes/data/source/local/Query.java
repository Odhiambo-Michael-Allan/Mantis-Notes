package com.mantis.takenotes.data.source.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.mantis.takenotes.data.source.NoteRepository;

import java.util.Date;

@Entity( tableName = "recent_queries" )
public class Query {

    @PrimaryKey( autoGenerate = true )
    private int id;
    private String description;
    private Date dateSubmitted;

    public Query( String description, Date dateSubmitted ) {
        this.description = description;
        this.dateSubmitted = dateSubmitted;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setDateSubmitted(Date dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public Date getDateSubmitted() {
        return this.dateSubmitted;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }



    public void add( NoteRepository noteRepository ) {
        if ( this.description.equals( "" ) )
            return;  // No need to add an empty query to the database..
        noteRepository.insertQuery( this );
    }

    public void delete( NoteRepository noteRepository ) {
        noteRepository.deleteQuery( this.getId() );
    }

}
