package com.example.open_weater_kotlin_ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.example.open_weater_kotlin_ui.model.broadcast.GPSStatusReceiver
import com.example.open_weater_kotlin_ui.model.repository.WeatherRepositoryImpl
import com.example.open_weater_kotlin_ui.model.utils.RetrofitInstance
import com.example.open_weater_kotlin_ui.view.navigation.Navigator
import com.example.open_weater_kotlin_ui.view.theme.MyApplicationTheme
import com.example.open_weater_kotlin_ui.view_model.WeatherViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale

private var isResolvingShortLink by mutableStateOf(false)
/**
 * MainActivity serves as the entry point of the application and manages the main functionality,
 * including handling permissions, GPS status, and UI rendering.
 */
class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()

    ) { isGranted: Boolean ->

        if (isGranted) {
            checkGPS()
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private lateinit var gpsStatusReceiver: GPSStatusReceiver


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val configuration = resources.configuration
        configuration.setLocale(Locale.ENGLISH)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        gpsStatusReceiver = GPSStatusReceiver()
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        registerReceiver(gpsStatusReceiver, filter)

        val intent = intent
        setContent {
            WeatherApp {
                val context = LocalContext.current
                if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
                    handleShareIntent(intent)
                } else {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        checkGPS()
                    } else {
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(gpsStatusReceiver)
    }

    private fun checkGPS() {
        setContent {
            SplashScreenContent()
        }
        if (isGPSEnabled()) {
            Toast.makeText(this, "Getting information from GPS...", Toast.LENGTH_SHORT).show()
        }

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable GPS", Toast.LENGTH_LONG).show()
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

        } else {
            val apiInterface = RetrofitInstance.api
            val weatherRepository = WeatherRepositoryImpl(apiInterface)
            val viewModel = WeatherViewModel.getInstance(weatherRepository)


            requestLocationUpdates(viewModel)
        }
    }

    @Composable
    fun SplashScreenContent() {
        MyApplicationTheme {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center

            ) {
                // Customize your splash screen UI here
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.app_icon),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.White
                )
            }
        }
    }

    /**
     * Checks if GPS is enabled.
     *
     * @return true if GPS is enabled, false otherwise.
     */
    private fun isGPSEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * Requests location updates for weather data retrieval.
     *
     * @param viewModel the ViewModel for weather data.
     */
    private fun requestLocationUpdates(viewModel: WeatherViewModel) {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, object : android.location.LocationListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onLocationChanged(location: Location) {

                val latitude = location.latitude
                val longitude = location.longitude

                viewModel.setLatLon(latitude, longitude)

                setContent {
                    WeatherApp {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Navigator(viewModel)
                        }
                    }
                }
            }

            override fun onProviderDisabled(provider: String) {}

            override fun onProviderEnabled(provider: String) {}

            @Deprecated("Deprecated in Java")
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }
        }, null)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleShareIntent(intent)
    }

    /**
     * Handles the shared intent, extracts the shared text, and resolves short links if present.
     *
     * @param intent The shared intent.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleShareIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
            sharedText?.let {
                if (it.contains("http")) {
                    resolveShortLink(it)
                }
            }
        }
    }

    /**
     * Resolves a short link to its original URL and extracts latitude and longitude if available.
     *
     * @param shortLink The short link to resolve.
     */
    @OptIn(DelicateCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    private fun resolveShortLink(shortLink: String) {
        isResolvingShortLink = true
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL(shortLink)
                val connection = url.openConnection() as HttpURLConnection
                connection.instanceFollowRedirects = false
                val originalUrl = connection.getHeaderField("Location")
                extractLatLonFromUrl(originalUrl)
            } catch (e: Exception) {
                Log.e("ResolveShortLink", "Error resolving short link: ${e.message}")
            } finally {
                isResolvingShortLink = false
            }
        }
    }


    /**
     * Extracts latitude and longitude from the original URL and initiates weather data retrieval.
     *
     * @param originalUrl The original URL containing latitude and longitude information.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun extractLatLonFromUrl(originalUrl: String?) {
        originalUrl?.let {
            val locationName =
                originalUrl.split("place").lastOrNull()!!.split(",").firstOrNull()!!.split(",").firstOrNull()!!.removePrefix("/")
                    .replace("+", " ")

            Log.i("originalUrl", originalUrl)
            Log.i("locationName", locationName)

            val apiInterface = RetrofitInstance.api
            val weatherRepository = WeatherRepositoryImpl(apiInterface)
            val viewModel = WeatherViewModel.getInstance(weatherRepository)


            if (locationName.matches(Regex("^[a-zA-Z].*")) == true) {
                viewModel.getLocationCoordinates(locationName)
                setContent {
                    WeatherApp {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Navigator(viewModel)
                        }
                    }
                }
            } else {
                val lat = originalUrl.split("place").lastOrNull()?.split(",")?.firstOrNull()?.removePrefix("/")?.toDoubleOrNull()
                val lon = originalUrl.split("place").lastOrNull()?.split(",")?.get(1)?.split("/")?.firstOrNull()?.toDoubleOrNull()

                Log.i("lat", lat!!.toDouble().toString())
                Log.i("lon", lon.toString())

                viewModel.viewModelScope.launch {
                    viewModel.setLatLon(lat, lon!!)
                }

                setContent {
                    WeatherApp {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Navigator(viewModel)
                        }
                    }
                }
            }
        }
    }

}

/**
 * Composable function for the Weather App.
 *
 * @param content The content to display.
 */
@Composable
fun WeatherApp(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        MyApplicationTheme {
            Surface {
                if (isResolvingShortLink) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        colorResource(R.color.customCyan),
                                        colorResource(R.color.customBlue)
                                    )
                                )
                            ), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                } else {
                    content()
                }
            }
        }
    }
}


