package com.example.schedule.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.UiThread
import com.example.schedule.R
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*

class WeatherFragment : Fragment() {

    private lateinit var currentWeather : TextView
    private lateinit var weatherCity : TextView
    private lateinit var currentCloudy : TextView
    private lateinit var client : OkHttpClient
    private lateinit var request : Request


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_weather, container, false)
        currentWeather = view.findViewById(R.id.info_weather)
        weatherCity = view.findViewById(R.id.info_city)
        currentCloudy = view.findViewById(R.id.cloudy)
        client = OkHttpClient()

        request = Request.Builder()
            .url("https://yahoo-weather5.p.rapidapi.com/weather?location=krasnoyarsk&format=json&u=c")
            .get()
            .addHeader("x-rapidapi-key", "e9d6182cccmsh661154e1e93fd18p130ea4jsnc3964b8c4715")
            .addHeader("x-rapidapi-host", "yahoo-weather5.p.rapidapi.com")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                             // returns an array of JSONObject
                    /*?.map { val id = JSONObject().optInt("id")
                        val title: String? = JSONObject().optString("title")}*/
                        val json = response?.body()?.string()
                    var temperature = JSONObject(json).getJSONObject("current_observation")
                            .getJSONObject("condition").get("temperature").toString()
                    var city = JSONObject(json).getJSONObject("location").get("city").toString()
                    temperature += " Градусов по Цельсию"
                    var cloudy = JSONObject(json).getJSONObject("current_observation")
                            .getJSONObject("condition").get("text").toString()
                    requireActivity().runOnUiThread{
                        weatherCity.text = city
                        currentWeather.text = temperature
                        currentCloudy.text = cloudy
                    }
                }

            }
        })


        return view
    }

}