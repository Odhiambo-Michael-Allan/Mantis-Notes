package com.mantis.TakeNotes.data.source.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mantis.TakeNotes.data.source.local.Dao.ConfigurationsDao;
import com.mantis.TakeNotes.data.source.local.Dao.NoteDao;

@Database( entities = { Note.class, Configuration.class }, version = 2 )
@TypeConverters( {Converters.class} )
public abstract class NoteRoomDatabase extends RoomDatabase {

    private static NoteRoomDatabase INSTANCE;

    static Migration MIGRATION_1_2 = new Migration( 1, 2 ) {
        @Override
        public void migrate( @NonNull SupportSQLiteDatabase database ) {
            database.execSQL( "ALTER TABLE notes_table ADD COLUMN dateNoteWasLastDeleted INTEGER" );
        }
    };


    public static NoteRoomDatabase getDatabase( final Context context ) {
        if ( INSTANCE == null ) {
            synchronized ( NoteRoomDatabase.class ) {
                if ( INSTANCE == null ) {
                    INSTANCE = Room.databaseBuilder( context.getApplicationContext(),
                             NoteRoomDatabase.class, "Note-Database" )
                            .addMigrations( MIGRATION_1_2 )
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract NoteDao noteDao();
    public abstract ConfigurationsDao configurationsDao();
}
