package io.github.qobiljon.stressapp.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity
import io.github.qobiljon.stressapp.core.database.DatabaseHelper
import io.github.qobiljon.stressapp.core.database.data.ActivityRecognition


class ActivityRecognitionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {
            val result = ActivityRecognitionResult.extractResult(intent)!!
            val activity = parseActivityType(result.mostProbableActivity.type)
            val confidence = result.mostProbableActivity.confidence // 0 to 100 likelihood
            // Log.e(MainActivity.TAG, "Activity recognition: ${parseActivityType(activity)}, $confidence")

            DatabaseHelper.saveActivityRecognition(
                ActivityRecognition(
                    timestamp = System.currentTimeMillis(),
                    activity = activity,
                    confidence = confidence,
                )
            )
        }
    }

    private fun parseActivityType(activity: Int): String {
        return when (activity) {
            DetectedActivity.IN_VEHICLE -> "IN_VEHICLE"
            DetectedActivity.ON_BICYCLE -> "ON_BICYCLE"
            DetectedActivity.ON_FOOT -> "ON_FOOT"
            DetectedActivity.STILL -> "STILL"
            DetectedActivity.WALKING -> "WALKING"
            DetectedActivity.RUNNING -> "RUNNING"
            else -> "UNKNOWN"
        }
    }
}
