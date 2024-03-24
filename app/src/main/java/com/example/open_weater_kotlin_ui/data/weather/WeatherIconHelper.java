package com.example.open_weater_kotlin_ui.data.weather;

import java.util.HashMap;
import java.util.Map;

public class WeatherIconHelper {

    private static Map<String, String> weatherIconMap;

    private static Map<String, String> getWeatherIconMap() {
        if (weatherIconMap == null || weatherIconMap.isEmpty()) {
            populateWeatherIconMap();
        }
        return weatherIconMap;
    }

    public static String getIconUrl(String icon, String description) {
        String weatherIcon = (icon != null ? icon : getWeatherIconMap().getOrDefault(description, null));
        if(weatherIcon == null) return "";
        else return "https://openweathermap.org/img/wn/"+weatherIcon+ "@2x.png";
    }

    private static void populateWeatherIconMap() {
        weatherIconMap = new HashMap<>() {{
            put("clear sky", "01d");
            put("few clouds", "02d");
            put("scattered clouds", "03d");
            put("overcast clouds","04n");
            put("broken clouds", "04d");
            put("shower rain", "09d");
            put("rain", "10d");
            put("thunderstorm", "11d");
            put("snow", "13d");
            put("mist", "50d");
            put("thunderstorm with light rain","11d");
            put("thunderstorm with rain","11d");
            put("thunderstorm with heavy rain","11d");
            put("light thunderstorm","11d");
            put("heavy thunderstorm","11d");
            put("ragged thunderstorm","11d");
            put("thunderstorm with light drizzle","11d");
            put("thunderstorm with drizzle","11d");
            put("thunderstorm with heavy drizzle","11d");
            put("light intensity drizzle","09d");
            put("drizzle","09d");
            put("heavy intensity drizzle","09d");
            put("light intensity drizzle rain","09d");
            put("drizzle rain","09d");
            put("heavy intensity drizzle rain","09d");
            put("shower rain and drizzle","09d");
            put("shower drizzle","09d");
            put("light rain","10d");
            put("moderate rain","10d");
            put("heavy intensity rain","10d");
            put("very heavy rain","10d");
            put("extreme rain","10d");
            put("freezing rain","13d");
            put("light intensity shower rain","09d");
            put("heavy intensity shower rain","09d");
            put("ragged shower rain","09d");
            put("light snow","13d");
            put("heavy snow","13d");
            put("sleet","13d");
            put("light shower sleet","13d");
            put("shower sleet","13d");
            put("light rain and snow","13d");
            put("rain and snow","13d");
            put("light shower snow","13d");
            put("shower snow","13d");
            put("heavy shower snow","13d");
            put("smoke","50d");
            put("haze","50d");
            put("sand/dust whirls","50d");
            put("fog","50d");
            put("sand","50d");
            put("dust","50d");
            put("volcanic ash","50d");
            put("squalls","50d");
            put("tornado","50d");
            put("scattered clouds: 25-50%","03n");
            put("broken clouds: 51-84%","04n");
            put("overcast clouds: 85-100%","04n");
        }};
    }
}
