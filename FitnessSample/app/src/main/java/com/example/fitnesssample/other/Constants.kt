package com.example.fitnesssample.other

import android.graphics.Color

object Constants {

    const val SHARED_PREFERENCES_NAME = "sharedPref"

    const val KEY_FIRST_TIME_TOGGLE = "KEY_FIRST_TIME_TOGGLE"
    const val KEY_NAME = "KEY_NAME"
    const val KEY_WEIGHT = "KEY_WEIGHT"

    const val FITNESS_DATABASE_NAME = "fitness_db"

    const val REQUEST_CODE_LOCATION_PERMISSION = 100

    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    const val ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT"

    const val NOTIFICATION_CHANNEL_ID = "tracking_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Tracking"
    const val NOTIFICATION_ID = 200

    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_UPDATE_INTERVAL = 2000L

    const val POLYLINE_COLOR = Color.RED
    const val POLYLINE_WIDTH = 8F

    const val MAP_ZOOM = 15F

    const val TIME_UPDATE_INTERVAL = 50L

    const val CANCEL_TRACKING_DIALOG_TAG = "CANCEL_TRACKING_DIALOG_TAG"
}