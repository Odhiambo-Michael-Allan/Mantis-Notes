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

@Database( entities = { Note.class, Configuration.class }, version = 5 )
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

    static final Migration MIGRATION_4_5 = new Migration( 4, 5 ) {

        @Override
        public void migrate( @NonNull SupportSQLiteDatabase database ) {
            database.execSQL( "DROP TABLE 'home_notes_table'" );
            database.execSQL( "DROP TABLE 'trash_table'" );
            database.execSQL( "DROP TABLE 'archive_table'" );
            database.execSQL( "DROP TABLE 'frequent_notes'" );
        }
    };

    public static NoteRoomDatabase getDatabase( final Context context ) {
        if ( INSTANCE == null ) {
            synchronized ( NoteRoomDatabase.class ) {
                if ( INSTANCE == null ) {
                    INSTANCE = Room.databaseBuilder( context.getApplicationContext(),
                            NoteRoomDatabase.class, "Notes-Database" )
                            .addMigrations( MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5 )
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract NoteDao noteDao();
    public abstract ConfigurationsDao configurationsDao();
}
