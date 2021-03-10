package com.example.fitnesssample.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.fitnesssample.R
import com.example.fitnesssample.other.Constants.ACTION_PAUSE_SERVICE
import com.example.fitnesssample.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.fitnesssample.other.Constants.ACTION_STOP_SERVICE
import com.example.fitnesssample.other.Constants.FASTEST_LOCATION_UPDATE_INTERVAL
import com.example.fitnesssample.other.Constants.LOCATION_UPDATE_INTERVAL
import com.example.fitnesssample.other.Constants.NOTIFICATION_CHANNEL_ID
import com.example.fitnesssample.other.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.fitnesssample.other.Constants.NOTIFICATION_ID
import com.example.fitnesssample.other.Constants.TIME_UPDATE_INTERVAL
import com.example.fitnesssample.other.TrackingUtility
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

@AndroidEntryPoint
class TrackingService : LifecycleService() {

    private var isFirstRun = true
    private var isServiceKilled = false

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    lateinit var currentNotificationBuilder: NotificationCompat.Builder

    private val timeRunInSeconds = MutableLiveData<Long>()
    private var isTimerEnabled = false
    private var lapTime = 0L
    private var timeRun = 0L
    private var timeStarted = 0L
    private var lastSecondTimestamp = 0L

    @SuppressLint("VisibleForTests")
    override fun onCreate() {
        super.onCreate()
        currentNotificationBuilder = baseNotificationBuilder
        postInitialValues()

        isTracking.observe(this, Observer {
            updateLocationTracking(it)
            updateNotificationTrackingState(it)
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {

                    if (isFirstRun) {
                        Timber.d("Starting service...")
                        startForegroundService()
                        isFirstRun = false

                    } else {
                        Timber.d("Resuming service...")
                        startTimer()
                    }
                }

                ACTION_PAUSE_SERVICE -> {
                    Timber.d("Paused service")
                    pauseService()
                }

                ACTION_STOP_SERVICE -> {
                    Timber.d("Stopped service")
                    killService()
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun killService() {

        isServiceKilled = true
        isFirstRun = true
        pauseService()
        postInitialValues()
        stopForeground(true)
        stopSelf()
    }

    private fun startTimer() {

        addEmptyPolyline()
        isTracking.postValue(true)

        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true

        CoroutineScope(Dispatchers.Main).launch {

            while (isTracking.value!!) {

                // Time difference between now and timeStarted
                lapTime = System.currentTimeMillis() - timeStarted
                // Post the new lapTime
                timeRunInMillis.postValue(timeRun + lapTime)
                if (timeRunInMillis.value!! >= lastSecondTimestamp + 1000L) {
                    timeRunInSeconds.postValue(timeRunInSeconds.value!! + 1)
                    lastSecondTimestamp += 1000L
                }
                delay(TIME_UPDATE_INTERVAL)
            }
            timeRun += lapTime
        }
    }

    private fun pauseService() {
        isTracking.postValue(false)
        isTimerEnabled = false
    }

    private fun postInitialValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        timeRunInSeconds.postValue(0L)
        timeRunInMillis.postValue(0L)
    }

    private fun addEmptyPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    private fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if (isTracking.value!!) {
                for (location in result.locations) {
                    addPathPoint(location)
                    Timber.d("NEW LOCATION: ${location.latitude}, ${location.longitude}")
                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun updateNotificationTrackingState(isTracking: Boolean) {

        val notificationActionText = if (isTracking) "Pause" else "Resume"
        val pendingIntent = if (isTracking) {
            val pauseIntent = Intent(this, TrackingService::class.java).apply {
                action = ACTION_PAUSE_SERVICE
            }
            PendingIntent.getService(this, 1, pauseIntent, FLAG_UPDATE_CURRENT)
        } else {
            val resumeIntent = Intent(this, TrackingService::class.java).apply {
                action = ACTION_START_OR_RESUME_SERVICE
            }
            PendingIntent.getService(this, 2, resumeIntent, FLAG_UPDATE_CURRENT)
        }

        // Only this way to remove all actions in the currently notification
        currentNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(currentNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }

        if (!isServiceKilled) {
            currentNotificationBuilder = baseNotificationBuilder
                .addAction(R.drawable.ic_pause_black_24dp, notificationActionText, pendingIntent)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            notificationManager?.notify(NOTIFICATION_ID, currentNotificationBuilder.build())
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {

        if (isTracking) {
            if (TrackingUtility.hasLocationPermissions(this)) {
                val request = LocationRequest.create().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_LOCATION_UPDATE_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        } else fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun startForegroundService() {

        startTimer()
        isTracking.postValue(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(notificationManager)

        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())

        timeRunInSeconds.observe(this, Observer {

            if (!isServiceKilled) {
                val notificaton = currentNotificationBuilder
                    .setContentText(TrackingUtility.fetchFormattedStopWatchTime(it * 1000L))
                notificationManager?.notify(NOTIFICATION_ID, notificaton.build())
            }
        })
    }

    @SuppressLint("NewApi")
    private fun createNotificationChannel(notificationManager: NotificationManager?) {

        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, IMPORTANCE_LOW)
        notificationManager?.createNotificationChannel(channel)
    }

    companion object {

        val timeRunInMillis = MutableLiveData<Long>()

        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<Polylines>()
    }
}