package com.example.fitnesssample.repositories

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.fitnesssample.db.RunDAO
import com.example.fitnesssample.db.RunDTO
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val rundDao: RunDAO
) {

    suspend fun insertRun(run: RunDTO) = rundDao.insertRun(run)

    suspend fun removeRun(run: RunDTO) = rundDao.deleteRun(run)

    fun fetchRunsSortedByDate() = rundDao.fetchRunsSortedByDate()

    fun fetchRunsSortedByTimeInMillis() = rundDao.fetchRunsSortedByTimeInMillis()

    fun fetchRunsSortedByCaloriesBurned() = rundDao.fetchRunsSortedByCaloriesBurned()

    fun fetchRunsSortedByAverageSpeed() = rundDao.fetchRunsSortedByAverageSpeed()

    fun fetchRunsSortedByDistance() = rundDao.fetchRunsSortedByDistance()

    fun fetchTotalTimeInMillis() = rundDao.fetchTotalTimeInMillis()

    fun fetchTotalCaloriesBurned() = rundDao.fetchTotalCaloriesBurned()

    fun fetchTotalDistance() = rundDao.fetchTotalDistance()

    fun fetchTotalAvgSpeed() = rundDao.fetchTotalAvgSpeed()
}