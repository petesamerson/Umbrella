package com.example.umbrella

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_weather_settings_page.*

class WeatherSettingsPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_settings_page)
        var settings : SettingsPoko = intent.getParcelableExtra("settings")!!
        et_zip.setText(settings.zip)
        val unitsArrayAdapter : ArrayAdapter<String> = ArrayAdapter(
            this,
            R.layout.units_item,
            resources.getStringArray(R.array.units)
        )
        unitsArrayAdapter.setDropDownViewResource(R.layout.units_item)
        spUnits.adapter = unitsArrayAdapter
        spUnits.setSelection(settings.unit)
        ivBack.setOnClickListener {
            sendSettings()
        }
    }
    fun sendSettings(){
        val sendSettings = Intent()
        sendSettings.setClass(this,MainActivity::class.java )
        val settings = SettingsPoko(et_zip.text.toString(),spUnits.selectedItemPosition)
        sendSettings.putExtra("settings",settings)
        sendSettings.putExtra("firstInit",false)
        setResult(Activity.RESULT_OK,sendSettings)
        finish()
    }
}
