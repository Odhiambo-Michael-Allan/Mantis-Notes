package com.mantis.TakeNotes.data.source.local.NoteReferences;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Since a note in the frequent_notes table must also be in the home_notes table, the
 * foreign key is set to make sure that when a note is deleted in the home_list table,
 * it is also deleted from the frequent_notes table if it exists..
 */

@Entity( tableName = "frequent_notes" )
public class FrequentNoteReference extends NoteReference {

    @PrimaryKey( autoGenerate = true )
    private int id;

    public FrequentNoteReference( int noteReferenceId) {
        super( noteReferenceId );
    }

    @Override
    public void setId( int id ) {
        this.id = id;
    }

    @Override
    public int getId() {
        return this.id;
    }


}
