package com.mantis.MantisNotesIterationOne.data.source.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.mantis.MantisNotesIterationOne.data.source.local.Dao.ArchiveNotesDao;
import com.mantis.MantisNotesIterationOne.data.source.local.Dao.FrequentNotesDao;
import com.mantis.MantisNotesIterationOne.data.source.local.Dao.HomeNotesDao;
import com.mantis.MantisNotesIterationOne.data.source.local.Dao.NoteDao;
import com.mantis.MantisNotesIterationOne.data.source.local.Dao.TrashNotesDao;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.ArchiveNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.FrequentNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.HomeNoteReference;
import com.mantis.MantisNotesIterationOne.data.source.local.NoteReferences.TrashNoteReference;

@Database( entities = { Note.class, HomeNoteReference.class, FrequentNoteReference.class,
        ArchiveNoteReference.class, TrashNoteReference.class }, version = 1 )
@TypeConverters( {Converters.class} )
public abstract class NoteRoomDatabase extends RoomDatabase {

    private static NoteRoomDatabase INSTANCE;

    public static NoteRoomDatabase getDatabase( final Context context ) {
        if ( INSTANCE == null ) {
            synchronized ( NoteRoomDatabase.class ) {
                if ( INSTANCE == null ) {
                    INSTANCE = Room.databaseBuilder( context.getApplicationContext(),
                            NoteRoomDatabase.class, "Notes-Database" ).build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract NoteDao noteDao();
    public abstract HomeNotesDao homeNotesDao();
    public abstract FrequentNotesDao frequentNotesDao();
    public abstract ArchiveNotesDao archiveNotesDao();
    public abstract TrashNotesDao trashNotesDao();
}
