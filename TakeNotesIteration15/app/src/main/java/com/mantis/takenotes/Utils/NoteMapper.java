package com.mantis.takenotes.Utils;

import androidx.annotation.NonNull;
import com.mantis.takenotes.Repository.Note;
import com.mantis.takenotes.Repository.NoteRepository;
import com.mantis.takenotes.data.source.local.NoteEntity;

public class NoteMapper {

    public static Note entityToNote( @NonNull NoteEntity noteEntity, NoteRepository noteRepository ) {
        return new Note( noteEntity.id, noteEntity.title, noteEntity.description,
                noteEntity.accessCount, noteEntity.dateCreated, noteEntity.owner,
                noteEntity.timeLeft, noteEntity.dateNoteWasLastDeleted, noteRepository );
    }

    public static NoteEntity noteToEntity( Note note ) {
        return new NoteEntity( note.getTitle(), note.getDescription(), note.getDateCreated(),
                note.getOwner() );
    }
}
