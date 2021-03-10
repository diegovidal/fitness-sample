package com.example.fitnesssample.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.fitnesssample.repositories.MainRepository

class StatisticsViewModel @ViewModelInject constructor(
    private val repository: MainRepository
): ViewModel() {

    val totalTimeRun = repository.fetchTotalTimeInMillis()
    val totalDistance = repository.fetchTotalDistance()
    val totalCaloriesBurned = repository.fetchTotalCaloriesBurned()
    val totalAvgSpeed = repository.fetchTotalAvgSpeed()

    val runsSortedByDate = repository.fetchRunsSortedByDate()
}