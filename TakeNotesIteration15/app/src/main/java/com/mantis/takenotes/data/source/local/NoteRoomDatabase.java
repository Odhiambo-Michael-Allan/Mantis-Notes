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

@Database( entities = { NoteEntity.class, Configuration.class, Query.class }, version = 1 )
@TypeConverters( { Converters.class } )
public abstract class NoteRoomDatabase extends RoomDatabase {

    private static NoteRoomDatabase INSTANCE;

    public static NoteRoomDatabase getDatabase( final Context context ) {
        if ( INSTANCE == null ) {
            synchronized ( NoteRoomDatabase.class ) {
                if ( INSTANCE == null ) {
                    INSTANCE = Room.databaseBuilder( context.getApplicationContext(),
                             NoteRoomDatabase.class, "Take-Notes-Database" )
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
