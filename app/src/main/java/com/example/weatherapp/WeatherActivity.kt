package com.example.weatherapp


import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class WeatherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        fetchWeatherTime()
    }

    private fun fetchWeatherTime() {
        val weatherInfoContainer = findViewById<TextView>(R.id.weatherInfo)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiService::class.java)

        val latitude = intent.getStringExtra("LATITUDE") ?: "-1"
        val longitude = intent.getStringExtra("LONGITUDE") ?: "-1"

        val call = apiService.getWeather(latitude, longitude, "temperature_2m,wind_speed_10m")
        call.enqueue(object : Callback<CurrentWeatherResponse> {
            override fun onResponse(
                call: Call<CurrentWeatherResponse>,
                response: Response<CurrentWeatherResponse>
            ) {
                val weatherInfo = response.body()!!

                val temperature = weatherInfo.current.temperature_2m
                weatherInfoContainer.text = "${temperature}C"
            }

            override fun onFailure(call: Call<CurrentWeatherResponse>, t: Throwable) {

            }

        })
    }

    interface ApiService {
        @GET("forecast/")
        fun getWeather(
            @Query("latitude") latitude: String,
            @Query("longitude") longitude: String,
            @Query("current") current: String,
        ): Call<CurrentWeatherResponse>
    }
}

