package com.mantis.takenotes.data.source.local;

/**
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;

import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.mantis.takenotes.data.source.local.Dao.ConfigurationsDao;

import com.mantis.takenotes.data.source.local.Dao.NoteDao;
import com.mantis.takenotes.data.source.local.Dao.QueryDao;

/**
 * @author - Michael Allan Odhiambo
 * @email - odhiambomichaelallan@gmail.com
 * @date - 4th March 2023
 */

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
