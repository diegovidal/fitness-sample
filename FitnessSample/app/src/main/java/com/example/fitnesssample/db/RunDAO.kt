package com.example.fitnesssample.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

typealias Runs = List<RunDTO>

@Dao
interface RunDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: RunDTO)

    @Delete
    suspend fun deleteRun(run: RunDTO)

    @Query("SELECT * FROM running_table ORDER BY timestamp DESC")
    fun fetchRunsSortedByDate(): LiveData<Runs>

    @Query("SELECT * FROM running_table ORDER BY timeInMillis DESC")
    fun fetchRunsSortedByTimeInMillis(): LiveData<Runs>

    @Query("SELECT * FROM running_table ORDER BY caloriesBurned DESC")
    fun fetchRunsSortedByCaloriesBurned(): LiveData<Runs>

    @Query("SELECT * FROM running_table ORDER BY avgSpeedInKMH DESC")
    fun fetchRunsSortedByAverageSpeed(): LiveData<Runs>

    @Query("SELECT * FROM running_table ORDER BY distanceInMeters DESC")
    fun fetchRunsSortedByDistance(): LiveData<Runs>

    @Query("SELECT SUM(timeInMillis) FROM running_table")
    fun fetchTotalTimeInMillis(): LiveData<Long>

    @Query("SELECT SUM(caloriesBurned) FROM running_table")
    fun fetchTotalCaloriesBurned(): LiveData<Int>

    @Query("SELECT SUM(distanceInMeters) FROM running_table")
    fun fetchTotalDistance(): LiveData<Int>

    @Query("SELECT AVG(avgSpeedInKMH) FROM running_table")
    fun fetchTotalAvgSpeed(): LiveData<Float>
}