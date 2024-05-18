package com.example.open_weater_kotlin_ui

import android.Manifest
import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.open_weater_kotlin_ui.models.WeatherRepository
import com.example.open_weater_kotlin_ui.models.utils.RetrofitInstance
import com.example.open_weater_kotlin_ui.view.navigation.Navigator
import com.example.open_weater_kotlin_ui.view.theme.MyApplicationTheme
import com.example.open_weater_kotlin_ui.viewModel.WeatherViewModel
import com.example.open_weater_kotlin_ui.viewModel.WeatherViewModelFactory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            checkGPS()
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    private fun checkGPS() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable GPS", Toast.LENGTH_SHORT).show()
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        } else {
            val apiInterface = RetrofitInstance.api
            val weatherRepository = WeatherRepository(apiInterface)
            val factory = WeatherViewModelFactory(weatherRepository)
            val viewModel = ViewModelProvider(this, factory)[WeatherViewModel::class.java]

            requestLocationUpdates(viewModel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleShareIntent(intent)
    }

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

    @OptIn(DelicateCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    private fun resolveShortLink(shortLink: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL(shortLink)
                val connection = url.openConnection() as HttpURLConnection
                connection.instanceFollowRedirects = false
                val originalUrl = connection.getHeaderField("Location")
                extractLatLonFromUrl(originalUrl)
            } catch (e: Exception) {
                Log.e("ResolveShortLink", "Error resolving short link: ${e.message}")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun extractLatLonFromUrl(originalUrl: String?) {
        originalUrl?.let {
            val locationName =
                originalUrl.split("place").lastOrNull()!!.split(",").firstOrNull()!!.split(",").firstOrNull()!!.removePrefix("/")
                    .replace("+", " ")

            Log.i("originalUrl", originalUrl)
            Log.i("locationName", locationName)

            val apiInterface = RetrofitInstance.api
            val weatherRepository = WeatherRepository(apiInterface)
            val factory = WeatherViewModelFactory(weatherRepository)
            val viewModel = ViewModelProvider(this, factory)[WeatherViewModel::class.java]

            if (locationName.matches(Regex("^[a-zA-Z].*")) == true) {
                viewModel.getLocationCoordinates(locationName)
                setContent {
                    MyApplicationTheme {
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
                    MyApplicationTheme {
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

                if (latitude != null && longitude != null) {
                    setContent {
                        MyApplicationTheme {
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

            override fun onProviderDisabled(provider: String) {}

            override fun onProviderEnabled(provider: String) {}

            @Deprecated("Deprecated in Java")
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }
        }, null)
    }
}

@Composable
fun WeatherApp(content: @Composable () -> Unit) {
    MyApplicationTheme {
        Surface {
            content()
        }
    }
}

