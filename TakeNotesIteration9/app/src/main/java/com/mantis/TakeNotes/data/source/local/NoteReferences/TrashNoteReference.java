package com.mantis.TakeNotes.data.source.local.NoteReferences;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity( tableName = "trash_table" )
public class TrashNoteReference extends NoteReference {

    @PrimaryKey( autoGenerate = true )
    private int id;

    public TrashNoteReference( int noteReferenceId ) {
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
