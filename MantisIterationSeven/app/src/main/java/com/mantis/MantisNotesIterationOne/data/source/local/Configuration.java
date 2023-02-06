package com.mantis.MantisNotesIterationOne.data.source.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
