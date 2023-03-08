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

import androidx.room.Query;
import com.mantis.takenotes.data.source.local.Configuration;

/**
 * @author - Michael Allan Odhiambo
 * @email - odhiambomichaelallan@gmail.com
 * @date - 4th March 2023
 */

@Dao
public interface ConfigurationsDao {

    @Insert
    void insertConfiguration( Configuration configuration );

    @Query( "SELECT ascending FROM configurations_table" )
    LiveData<Integer> getAscending();

    @Query( "SELECT sortingStrategy FROM configurations_table" )
    LiveData<Integer> getSortingStrategy();

    @Query( "SELECT layoutType FROM configurations_table" )
    LiveData<Integer> getLayoutType();

    @Query( "UPDATE configurations_table SET layoutType = :newLayoutTypeConfig" )
    void updateLayoutTypeConfig( int newLayoutTypeConfig );

    @Query( "UPDATE configurations_table SET sortingStrategy = :newSortingStrategyConfig" )
    void updateSortingStrategyConfig( int newSortingStrategyConfig );

    @Query( "UPDATE configurations_table SET ascending = :newAscendingConfig" )
    void updateAscendingConfig( int newAscendingConfig );
}
