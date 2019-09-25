package com.example.umbrella

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import java.io.InputStream
import java.net.URL


class MainActivity : AppCompatActivity() {

    lateinit var settings : SettingsPoko
    lateinit var dayAdapter: DateAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dayRecycler.layoutManager = LinearLayoutManager(this)
        dayAdapter = DateAdapter(this)
        dayRecycler.adapter = dayAdapter

        settings = SettingsPoko("00000", SettingsPoko.FAHRENHEIT)
        openSettings()
        //api.openweathermap.org/data/2.5/forecast?zip=94040,us
        //302004f8b3b6fc5713dd70835bc66b69
        iv_settings.setOnClickListener {
            openSettings()
        }
    }
    private fun getRetrofitFromSettings(){
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiInterface:ApiInterface = retrofit.create(ApiInterface::class.java)

        var zip = settings.zip +",us"
        apiInterface.getWeather(zip,"45ce497d7e79f72e88b6cda6ab866839")
            .enqueue( object : Callback<WeatherPoko> {

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(call: Call<WeatherPoko>, response: Response<WeatherPoko>){
                    if(response.isSuccessful){
                        //todo pass data to adapter
                        var weather = response.body()
                        tv_city.text = weather?.city?.name
                        var tempRaw = weather?.list?.get(0)?.main?.temp
                        var tempConvert = convertTemp(tempRaw)
                        if(settings.unit == 0){
                           if(tempConvert>60)
                               vHolder.setBackgroundColor(resources.getColor(R.color.hot))
                            else
                               vHolder.setBackgroundColor(resources.getColor(R.color.cool))
                        }
                        else{
                            if(tempConvert>20)
                                vHolder.setBackgroundColor(resources.getColor(R.color.hot))
                            else
                                vHolder.setBackgroundColor(resources.getColor(R.color.cool))
                        }
                        tv_cur_temp.text = tempConvert.toString()+getString(R.string.degree)
                        tv_cur_descript.text = weather?.list?.get(0)?.weather?.get(0)?.main
                        Log.d(TAG,weather?.list?.get(0)?.dt_txt!! )
         //               Picasso.get().load(getIconURL(weather?.list?.get(0)?.weather?.get(0)?.icon)).into(testIcon)
                        var weatherConvert = mutableListOf<WeatherConvertedPoko>()
                        for (item in weather?.list!!){
                            weatherConvert.add(
                               WeatherConvertedPoko(
                                   convertTemp(item.main.temp),
                                   item.weather?.get(0)?.main,
                                   getIconURL(item.weather?.get(0)?.icon),
                                   item.dt_txt
                               )
                            )
                        }
                        displayForecast(weatherConvert)
                        //Log.d(TAG,weatherConvert.toString())

                    }
                    else {
                        tv_city.text = resources.getString(R.string.error_zip)
                        vHolder.setBackgroundColor(ContextCompat
                            .getColor(this@MainActivity,R.color.error))
                    }

                }
                override fun onFailure(call: Call<WeatherPoko>, t: Throwable) {
                    Log.d(TAG, t.message.toString())
//                    Toast.makeText(
//                        this, t.message, Toast.LENGTH_LONG
//                    ).show()
                }
            })
    }
    fun displayForecast (forecast:MutableList<WeatherConvertedPoko>){
        var curDate = '0'
        var forecastDays
                = mutableListOf<MutableList<WeatherConvertedPoko>>()
        forecastDays.add(mutableListOf())
        var dayIndex = 0
        curDate = forecast[0].time[9]
        for (i in 0 until forecast.size){
            var time = forecast[i].time
            if(time[9] != curDate){
                dayIndex++
                forecastDays.add(mutableListOf())
                curDate = time[9]
            }
            forecastDays[dayIndex].add(
                WeatherConvertedPoko(
                    forecast[i].temp,
                    forecast[i].descrip,
                    forecast[i].iconUrl,
                    forecast[i].time
                )
            )
        }
        dayAdapter.daysList = forecastDays

        Log.d(TAG,forecastDays.size.toString())
    }
    fun getIconURL(icon: String): String {
            return "http://openweathermap.org/img/wn/$icon@2x.png"
    }
    private fun openSettings() {
        var openSettings  = Intent()
        openSettings.setClass(this,WeatherSettingsPage::class.java )
        openSettings.putExtra("settings",settings)
        startActivityForResult(openSettings, SETTINGS_REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG,"onActivityResult")
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == SETTINGS_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            settings  = data!!.getParcelableExtra("settings")
            Log.d(TAG,settings.zip)
            getRetrofitFromSettings()

        }
    }
    fun convertTemp(temp:Double?):Int{
        var convertTemp = 0.0
        if (settings.unit == 0) {
            convertTemp = (temp!! - 273.15) * 9 / 5 + 32
        }
        else{
            convertTemp = temp!!-273.15
        }
        return convertTemp.toInt()

    }


    companion object{
        val TAG: String = "MainActivity"
        const val  SETTINGS_REQUEST_CODE = 891
    }
}
