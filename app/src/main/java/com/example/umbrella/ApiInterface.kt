package com.example.umbrella

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    //api.openweathermap.org/data/2.5/forecast?zip=94040,us
    @GET("data/2.5/forecast")
    fun getWeather(
        @Query("zip") zip : String,
        @Query("appid") appid : String
    ) : Call<WeatherPoko>


}