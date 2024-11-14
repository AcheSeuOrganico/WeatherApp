package com.example.weatherapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path


class MainActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cepTextView = findViewById<TextView>(R.id.cepTextView)
        val getCepButton = findViewById<Button>(R.id.submitButton)

        getCepButton.setOnClickListener {
            blockCep(cepTextView)
        }
    }

    private fun blockCep(cepTextView: TextView) {
        val cep = cepTextView.text.toString().trim()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://cep.awesomeapi.com.br/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getCepDetails(cep)
        call.enqueue(object : Callback<CepResponse> {
            override fun onResponse(
                call: Call<CepResponse>,
                response: Response<CepResponse>
            ) {
                val address = response.body()!!
                val latitude: String = address.lat
                val longitude: String = address.lng
                val intent = Intent(this@MainActivity, WeatherActivity::class.java).apply {
                    putExtra("LATITUDE", latitude)
                    putExtra("LONGITUDE", longitude)
                }
                startActivity(intent)
                finish()
            }

            override fun onFailure(call: Call<CepResponse>, t: Throwable) {
                Log.e("LoginActivity", "onFailure: ${t.message}")
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }

    interface ApiService {
        @GET("json/{cep}")
        fun getCepDetails(@Path("cep") cep: String): Call<CepResponse>
    }
}
