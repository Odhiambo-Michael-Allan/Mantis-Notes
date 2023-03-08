package com.mantis.takenotes.data.source.local;

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

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.mantis.takenotes.Repository.NoteRepository;

import java.util.Date;

/**
 * @author - Michael Allan Odhiambo
 * @email - odhiambomichaelallan@gmail.com
 * @date - 4th March 2023
 */

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
