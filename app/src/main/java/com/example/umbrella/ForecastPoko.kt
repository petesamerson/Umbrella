package com.example.umbrella

data class ForecastPoko(
    val dt_txt : String,
    val main :  TempPoko,
    val weather : List<DescripPoko>
)