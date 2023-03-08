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

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author - Michael Allan Odhiambo
 * @email - odhiambomichaelallan@gmail.com
 * @date - 4th March 2023
 */

@Entity( tableName = "configurations_table" )
public class Configuration {

    @PrimaryKey( autoGenerate = true )
    private int id;
    private int ascending;
    private int sortingStrategy;
    private int layoutType;

    public Configuration(int ascending, int sortingStrategy, int layoutType ) {
        this.ascending = ascending;
        this.sortingStrategy = sortingStrategy;
        this.layoutType = layoutType;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setAscending( int ascending ) {
        this.ascending = ascending;
    }

    public int getAscending() {
        return this.ascending;
    }

    public void setSortingStrategy( int sortingStrategy ) {
        this.sortingStrategy = sortingStrategy;
    }

    public int getSortingStrategy() {
        return this.sortingStrategy;
    }

    public void setLayoutType( int layoutType ) {
        this.layoutType = layoutType;
    }

    public int getLayoutType() {
        return this.layoutType;
    }
}
