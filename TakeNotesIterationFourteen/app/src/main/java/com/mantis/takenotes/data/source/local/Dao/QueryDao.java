package com.mantis.takenotes.data.source.local.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;

import com.mantis.takenotes.data.source.local.Query;

import java.util.List;

@Dao
public interface QueryDao {

    @androidx.room.Query( "SELECT * FROM recent_queries" )
    LiveData<List<Query>> getQueries();

    @Insert
    void insert( Query query );

    @androidx.room.Query( "DELETE FROM recent_queries WHERE id = :queryId" )
    void deleteQuery( int queryId );
}
