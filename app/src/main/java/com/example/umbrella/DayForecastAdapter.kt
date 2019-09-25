package com.example.umbrella

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class DayForecastAdapter internal constructor(context: Context)
    :RecyclerView.Adapter<DayForecastAdapter.DayForecastViewHolder> (){


    private var inflator = LayoutInflater.from(context)
    val context = context
    var forecast = mutableListOf<WeatherConvertedPoko>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class DayForecastViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val tvDay : TextView = itemView.findViewById(R.id.tv_time)
        val ivIcon : ImageView = itemView.findViewById(R.id.iv_icon)
        val tvTemp  : TextView = itemView.findViewById(R.id.tv_temp)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayForecastViewHolder {
        val itemView = inflator.inflate(R.layout.grid_forecast_item,parent ,false)
        return DayForecastViewHolder(itemView)
    }

    override fun getItemCount() = forecast.size


    override fun onBindViewHolder(holder: DayForecastViewHolder, position: Int) {
        holder.tvDay.text = forecast[position].time.substring(10,16)
        Picasso.get().load(forecast[position].iconUrl).into(holder.ivIcon)
        holder.tvTemp.text = forecast[position].temp.toString()+
                context.resources.getString(R.string.degree)

    }

}