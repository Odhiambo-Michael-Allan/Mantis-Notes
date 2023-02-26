package com.mantis.takenotes.data.source.local;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.mantis.takenotes.data.source.local.Dao.ConfigurationsDao;

import com.mantis.takenotes.data.source.local.Dao.NoteDao;
import com.mantis.takenotes.data.source.local.Dao.QueryDao;

@Database( entities = { Note.class, Configuration.class, Query.class }, version = 3 )
@TypeConverters( {Converters.class} )
public abstract class NoteRoomDatabase extends RoomDatabase {

    private static NoteRoomDatabase INSTANCE;

    static Migration MIGRATION_1_2 = new Migration( 1, 2 ) {
        @Override
        public void migrate( @NonNull SupportSQLiteDatabase database ) {
            database.execSQL( "ALTER TABLE notes_table ADD COLUMN dateNoteWasLastDeleted INTEGER" );
        }
    };

    static Migration MIGRATION_2_3 = new Migration( 2, 3 ) {
        @Override
        public void migrate( @NonNull SupportSQLiteDatabase database ) {
            database.execSQL( "CREATE TABLE 'recent_queries' ( 'id' INTEGER NOT NULL, 'description' TEXT, 'dateSubmitted' INTEGER, PRIMARY KEY( 'id' ) )" );
        }
    };


    public static NoteRoomDatabase getDatabase( final Context context ) {
        if ( INSTANCE == null ) {
            synchronized ( NoteRoomDatabase.class ) {
                if ( INSTANCE == null ) {
                    INSTANCE = Room.databaseBuilder( context.getApplicationContext(),
                             NoteRoomDatabase.class, "Note-Database" )
                            .addMigrations( MIGRATION_1_2, MIGRATION_2_3 )
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract NoteDao noteDao();
    public abstract ConfigurationsDao configurationsDao();
    public abstract QueryDao queryDao();
}
