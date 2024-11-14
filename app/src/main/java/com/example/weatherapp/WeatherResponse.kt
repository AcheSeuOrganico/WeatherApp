package com.example.weatherapp

data class CurrentWeatherResponse (
    val current: WeatherResponse
)

data class WeatherResponse (
    val time: String,
    val interval: Int,
    val temperature_2m: Float,
    val wind_speed_10m: Float
)