package com.example.open_weater_kotlin_ui.model.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager

/**
 * A BroadcastReceiver to listen for changes in GPS status.
 *
 * onReceive method is called when the GPS status changes.
 * @param context The context in which the receiver is running.
 * @param intent The intent being received.
 *
 * @author Erfan Nasri
 */
class GPSStatusReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val restartIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
                restartIntent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivity(restartIntent)
            }
        }
    }
}
