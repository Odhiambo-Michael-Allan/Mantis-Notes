package com.mantis.TakeNotes.data.source.local.NoteReferences;

public class NoteReference {

    private int noteReferenceId;
    private int id;

    public NoteReference( int noteReferenceId ) {
        this.noteReferenceId = noteReferenceId;
    }

    public int getNoteReferenceId() {
        return this.noteReferenceId;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
