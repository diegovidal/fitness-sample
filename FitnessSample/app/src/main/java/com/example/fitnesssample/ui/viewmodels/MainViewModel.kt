package com.example.fitnesssample.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesssample.db.RunDTO
import com.example.fitnesssample.other.SortType
import com.example.fitnesssample.repositories.MainRepository
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val runsSortedByDate = repository.fetchRunsSortedByDate()
    private val runsSortedByDistance = repository.fetchRunsSortedByDistance()
    private val runsSortedByCaloriesBurned = repository.fetchRunsSortedByCaloriesBurned()
    private val runsSortedByTimeInMillis = repository.fetchRunsSortedByTimeInMillis()
    private val runsSortedByAvgSpeed = repository.fetchRunsSortedByAverageSpeed()

    val runsLiveData = MediatorLiveData<List<RunDTO>>()

    var sortType = SortType.DATE

    init {

        runsLiveData.addSource(runsSortedByDate) { result ->
            if (sortType == SortType.DATE) {
                result?.let { runsLiveData.value = it }
            }
        }

        runsLiveData.addSource(runsSortedByDistance) { result ->
            if (sortType == SortType.DISTANCE) {
                result?.let { runsLiveData.value = it }
            }
        }

        runsLiveData.addSource(runsSortedByCaloriesBurned) { result ->
            if (sortType == SortType.CALORIES_BURNED) {
                result?.let { runsLiveData.value = it }
            }
        }

        runsLiveData.addSource(runsSortedByTimeInMillis) { result ->
            if (sortType == SortType.RUNNING_TIME) {
                result?.let { runsLiveData.value = it }
            }
        }

        runsLiveData.addSource(runsSortedByAvgSpeed) { result ->
            if (sortType == SortType.AVG_SPEED) {
                result?.let { runsLiveData.value = it }
            }
        }
    }

    fun sortRuns(sortType: SortType) {
        when(sortType) {
            SortType.DATE -> runsSortedByDate.value?.let { runsLiveData.value = it }
            SortType.RUNNING_TIME -> runsSortedByTimeInMillis.value?.let { runsLiveData.value = it }
            SortType.AVG_SPEED -> runsSortedByAvgSpeed.value?.let { runsLiveData.value = it }
            SortType.DISTANCE -> runsSortedByDistance.value?.let { runsLiveData.value = it }
            SortType.CALORIES_BURNED -> runsSortedByCaloriesBurned.value?.let { runsLiveData.value = it }
        }.also { this.sortType = sortType }
    }

    fun insertRun(run: RunDTO) = viewModelScope.launch {
        repository.insertRun(run)
    }
}