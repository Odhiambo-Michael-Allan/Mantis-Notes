package com.mantis.TakeNotes.data.source.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mantis.TakeNotes.data.source.local.Dao.ArchiveNotesDao;
import com.mantis.TakeNotes.data.source.local.Dao.ConfigurationsDao;
import com.mantis.TakeNotes.data.source.local.Dao.FrequentNotesDao;
import com.mantis.TakeNotes.data.source.local.Dao.HomeNotesDao;
import com.mantis.TakeNotes.data.source.local.Dao.NoteDao;
import com.mantis.TakeNotes.data.source.local.Dao.TrashNotesDao;
import com.mantis.TakeNotes.data.source.local.NoteReferences.ArchiveNoteReference;
import com.mantis.TakeNotes.data.source.local.NoteReferences.FrequentNoteReference;
import com.mantis.TakeNotes.data.source.local.NoteReferences.HomeNoteReference;
import com.mantis.TakeNotes.data.source.local.NoteReferences.TrashNoteReference;

@Database( entities = { Note.class, HomeNoteReference.class, FrequentNoteReference.class,
        ArchiveNoteReference.class, TrashNoteReference.class, Configuration.class }, version = 4 )
@TypeConverters( {Converters.class} )
public abstract class NoteRoomDatabase extends RoomDatabase {

    private static NoteRoomDatabase INSTANCE;

    static final Migration MIGRATION_1_2 = new Migration( 1, 2 ) {
        @Override
        public void migrate( @NonNull SupportSQLiteDatabase database ) {
            database.execSQL( "ALTER TABLE notes_table ADD COLUMN owner INTEGER NOT NULL " +
                    "DEFAULT 0" );
        }
    };

    static final Migration MIGRATION_2_3 = new Migration( 2, 3 ) {
        @Override
        public void migrate( @NonNull SupportSQLiteDatabase database ) {
            database.execSQL( "CREATE TABLE 'configurations' ( 'id' INTEGER, PRIMARY KEY( 'id' ) ) " );
        }
    };


    static final Migration MIGRATION_3_4 = new Migration( 3, 4 ) {

        @Override
        public void migrate( @NonNull SupportSQLiteDatabase database ) {
            database.execSQL( "CREATE TABLE 'configurations_table' ( 'id' INTEGER NOT NULL, " +
                    "'ascending' INTEGER NOT NULL DEFAULT 0, " +
                    "'sortingStrategy' INTEGER NOT NULL DEFAULT 0, " +
                    "'layoutType' INTEGER NOT NULL DEFAULT 0, " +
                    "PRIMARY KEY( 'id' ) )" );
        }
    };

    public static NoteRoomDatabase getDatabase( final Context context ) {
        if ( INSTANCE == null ) {
            synchronized ( NoteRoomDatabase.class ) {
                if ( INSTANCE == null ) {
                    INSTANCE = Room.databaseBuilder( context.getApplicationContext(),
                            NoteRoomDatabase.class, "Notes-Database" )
                            .addMigrations( MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4 )
                            .build();
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
    public abstract ConfigurationsDao configurationsDao();
}
