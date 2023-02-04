package com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity( tableName = "archive_table" )
public class ArchiveNoteReference extends NoteReference {

    @PrimaryKey( autoGenerate = true )
    private int id;
    public ArchiveNoteReference( int noteReferenceId ) {
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
