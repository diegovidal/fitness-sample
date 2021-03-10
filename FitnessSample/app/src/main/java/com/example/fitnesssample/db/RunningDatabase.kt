package com.example.fitnesssample.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [RunDTO::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class RunningDatabase: RoomDatabase() {

    abstract fun fetchRunDao(): RunDAO
}