package com.example.open_weater_kotlin_ui
import android.content.Context
import android.provider.Settings
import android.content.Intent
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.open_weater_kotlin_ui.utils.RetrofitInstance
import com.example.open_weater_kotlin_ui.viewModel.HourlyForecast
import com.example.open_weater_kotlin_ui.viewModel.WeatherRepository
import com.example.open_weater_kotlin_ui.viewModel.WeatherViewModel
import com.example.open_weater_kotlin_ui.viewModel.WeatherViewModelFactory

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            checkGPS()
        } else {
            // todo Permission denied
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherApp {
                val context = LocalContext.current
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
            override fun onLocationChanged(location: Location) {
                val latitude = location.latitude
                val longitude = location.longitude

                viewModel.updateWeatherData(latitude, longitude)

                if (latitude != null && longitude != null) {
                    setContent {
                        WeatherApp {
                            HourlyForecast(viewModel)
                        }
                    }
                }
            }

            override fun onProviderDisabled(provider: String) {}

            override fun onProviderEnabled(provider: String) {}

            @Deprecated("Deprecated in Java")
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        }, null)
    }
}

@Composable
fun WeatherApp(content: @Composable () -> Unit) {
    MaterialTheme {
        Surface {
            content()
        }
    }
}