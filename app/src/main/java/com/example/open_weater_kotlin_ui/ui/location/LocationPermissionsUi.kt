package com.example.open_weater_kotlin_uiui.location
import android.content.Intent
import android.provider.Settings
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
fun getLocationPermissions(
    onLocationGranted: (location: Location) -> Unit,
    onLocationDenied: () -> Unit = { },
    coroutineScope: CoroutineScope,
    locationPermissionsState: MultiplePermissionsState,
    context: Context
) {
    val locationProviderClient: FusedLocationProviderClient?

    if (locationPermissionsState.allPermissionsGranted) {
        locationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        locationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                onLocationGranted.invoke(location)
            } else {
                // Handle case when location is null
                onLocationDenied.invoke()
            }
        }
    } else {
        if (locationPermissionsState.shouldShowRationale)  {
            onLocationDenied.invoke()
        } else {
            // Request permissions
            coroutineScope.launch {
                locationPermissionsState.launchMultiplePermissionRequest()
            }
        }
    }

    // Check if GPS is enabled, if not, prompt the user to enable it
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }
}
