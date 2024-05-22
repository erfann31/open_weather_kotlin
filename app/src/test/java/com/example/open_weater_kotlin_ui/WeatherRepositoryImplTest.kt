package com.example.open_weater_kotlin_ui

import com.example.open_weater_kotlin_ui.model.domain.ApiInterface
import com.example.open_weater_kotlin_ui.model.entities.*
import com.example.open_weater_kotlin_ui.model.repository.WeatherRepositoryImpl
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import retrofit2.Response
import kotlinx.coroutines.CompletableDeferred

@ExperimentalCoroutinesApi
class WeatherRepositoryTest {


    @Test
    fun test_getCoordinatesByLocationName() = runBlocking {
        val apiInterface = mock(ApiInterface::class.java)
        val weatherRepository = WeatherRepositoryImpl(apiInterface)

        val response = listOf(
            LocationCoordinate(
                id = 418863,
                name = "Isfahan",
                localName = null,
                lat = 32.6208,
                lon = 51.665,
                country = "IR",
                state = null
            )
        )
        val deferredResponse: Deferred<Response<List<LocationCoordinate>>> = CompletableDeferred(Response.success(response))
        `when`(apiInterface.getCoordinatesByLocationNameAsync("Isfahan", weatherRepository.appId))
            .thenReturn(deferredResponse)

        val result = weatherRepository.getCoordinatesByLocationName("Isfahan")

        assertEquals(response, result.body())
    }

    @Test
    fun test_reverseGeocoding() = runBlocking {
        val apiInterface = mock(ApiInterface::class.java)
        val weatherRepository = WeatherRepositoryImpl(apiInterface)

        val response = listOf(
            LocationCoordinate(
                id = 418863,
                name = "Isfahan",
                localName = null,
                lat = 32.6208,
                lon = 51.665,
                country = "IR",
                state = null
            )
        )
        val deferredResponse: Deferred<Response<List<LocationCoordinate>>> = CompletableDeferred(Response.success(response))
        `when`(apiInterface.reverseGeocodingAsync(32.6208, 51.665, weatherRepository.appId))
            .thenReturn(deferredResponse)

        val result = weatherRepository.reverseGeocoding(32.6208, 51.665)

        assertEquals(response, result.body())
    }

    @Test
    fun test_getCurrentWeatherData() = runBlocking {
        val apiInterface = mock(ApiInterface::class.java)
        val weatherRepository = WeatherRepositoryImpl(apiInterface)

        val response = CurrentWeather(
            id = 12345,
            coord = Coord(lon = 51.665, lat = 32.6208),
            weather = listOf(Weather(id = 800, main = "Clear", description = "clear sky", icon = "01d")),
            base = "stations",
            main = Main(temp = 298.15, feelsLike = 298.15, tempMin = 298.15, tempMax = 298.15, pressure = 1013, humidity = 50,1013,836),
            visibility = 10000,
            wind = Wind(speed = 1.5, deg = 350, gust = 2.0),
            rain = null,
            snow = null,
            clouds = Clouds(all = 0),
            dt = 1625246761,
            sys = Sys(type = 1, id = 7325, country = "IR", sunrise = 1625212432, sunset = 1625263661),
            timezone = 12600,
            name = "Isfahan",
            cod = 200
        )
        val deferredResponse: Deferred<Response<CurrentWeather>> = CompletableDeferred(Response.success(response))
        `when`(apiInterface.getCurrentWeatherDataAsync(32.6208, 51.665, "metric", weatherRepository.appId))
            .thenReturn(deferredResponse)

        val result = weatherRepository.getCurrentWeatherData(32.6208, 51.665, "metric")

        assertEquals(response, result.body())
    }

    @Test
    fun test_getDailyForecast() = runBlocking {
        val apiInterface = mock(ApiInterface::class.java)
        val weatherRepository = WeatherRepositoryImpl(apiInterface)

        val response = ForecastDaily(
            city = City(id = 418863, name = "Isfahan", coord = Coord(lon = 51.665, lat = 32.6208), country = "IR", population = 196459, timezone = 12600),
            cod = "200",
            message = 0.0,
            cnt = 8,
            list = listOf()
        )
        val deferredResponse: Deferred<Response<ForecastDaily>> = CompletableDeferred(Response.success(response))
        `when`(apiInterface.getDailyForecastAsync(32.6208, 51.665, "metric", weatherRepository.appId))
            .thenReturn(deferredResponse)

        val result = weatherRepository.getDailyForecast(32.6208, 51.665, "metric")

        assertEquals(response, result.body())
    }

    @Test
    fun test_getHourlyForecast() = runBlocking {
        val apiInterface = mock(ApiInterface::class.java)
        val weatherRepository = WeatherRepositoryImpl(apiInterface)

        val response = ForecastHourly(
            city = City(id = 418863, name = "Isfahan", coord = Coord(lon = 51.665, lat = 32.6208), country = "IR", population = 196459, timezone = 12600),
            cod = "200",
            message = 0,
            cnt = 8,
            list = listOf()
        )
        val deferredResponse: Deferred<Response<ForecastHourly>> = CompletableDeferred(Response.success(response))
        `when`(apiInterface.getHourlyForecastAsync(32.6208, 51.665, "metric", weatherRepository.appId))
            .thenReturn(deferredResponse)

        val result = weatherRepository.getHourlyForecast(32.6208, 51.665, "metric")

        assertEquals(response, result.body())
    }
}
