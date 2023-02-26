package com.mantis.takenotes.data.source.local.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;

import androidx.room.Query;
import com.mantis.takenotes.data.source.local.Configuration;

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
