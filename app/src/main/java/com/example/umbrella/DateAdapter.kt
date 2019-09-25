package com.example.umbrella

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DateAdapter internal constructor(context: Context):
    RecyclerView.Adapter<DateAdapter.DateViewHolder>() {

    lateinit var forecastAdapter: DayForecastAdapter
    var context = context
    private var inflator = LayoutInflater.from(context)
    var daysList
            = mutableListOf<MutableList<WeatherConvertedPoko>>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class DateViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val tvDay : TextView = itemView.findViewById(R.id.tv_day)
        val rvForecast : RecyclerView = itemView.findViewById(R.id.rvDayForecast)
    }
    override fun getItemCount() = daysList.size

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.tvDay.text = daysList[position][0].time.substring(5,10)
        holder.rvForecast.layoutManager = GridLayoutManager(context,4)
        forecastAdapter = DayForecastAdapter(context)
        holder.rvForecast.adapter = forecastAdapter
        forecastAdapter.forecast = daysList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val itemView = inflator.inflate(R.layout.days_item,parent ,false)
        return DateViewHolder(itemView)
    }

}
