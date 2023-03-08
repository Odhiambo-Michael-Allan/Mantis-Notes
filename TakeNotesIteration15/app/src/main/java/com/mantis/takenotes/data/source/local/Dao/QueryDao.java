package com.mantis.takenotes.data.source.local.Dao;

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

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;

import com.mantis.takenotes.data.source.local.Query;
import java.util.List;

/**
 * @author - Michael Allan Odhiambo
 * @email - odhiambomichaelallan@gmail.com
 * @date - 4th March 2023
 */

@Dao
public interface QueryDao {

    @androidx.room.Query( "SELECT * FROM recent_queries" )
    LiveData<List<Query>> getQueries();

    @Insert
    void insert( Query query );

    @androidx.room.Query( "DELETE FROM recent_queries WHERE id = :queryId" )
    void deleteQuery( int queryId );
}
