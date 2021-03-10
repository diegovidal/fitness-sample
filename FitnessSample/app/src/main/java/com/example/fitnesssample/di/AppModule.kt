package com.example.fitnesssample.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.example.fitnesssample.db.RunningDatabase
import com.example.fitnesssample.model.SetupContent
import com.example.fitnesssample.other.Constants
import com.example.fitnesssample.other.Constants.KEY_FIRST_TIME_TOGGLE
import com.example.fitnesssample.other.Constants.KEY_NAME
import com.example.fitnesssample.other.Constants.KEY_WEIGHT
import com.example.fitnesssample.other.Constants.SHARED_PREFERENCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRunningDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        RunningDatabase::class.java,
        Constants.FITNESS_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun providesRunDao(database: RunningDatabase) = database.fetchRunDao()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app: Context) = app.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideSetupContent(sharedPreferences: SharedPreferences) = SetupContent(
        sharedPreferences.getString(KEY_NAME, "") ?: "",
        sharedPreferences.getFloat(KEY_WEIGHT, 80F)
    )

    @Singleton
    @Provides
    fun provideFirstTimeToggle(sharedPreferences: SharedPreferences) = sharedPreferences.getBoolean(KEY_FIRST_TIME_TOGGLE, true)
}