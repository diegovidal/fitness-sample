package com.example.fitnesssample.db

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "running_table")
data class RunDTO(
    val img: Bitmap? = null,
    val timestamp: Long = 0L,
    val avgSpeedInKMH: Float = 0F,
    val distanceInMeters: Int = 0,
    val timeInMillis: Long = 0L,
    val caloriesBurned: Int = 0
) {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}