package toma.razvan.weatherapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import toma.razvan.weatherapp.R
import toma.razvan.weatherapp.data.model.WeatherResponse
import toma.razvan.weatherapp.utils.Constants
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class WeatherHourlyRvAdapter : RecyclerView.Adapter<WeatherHourlyRvAdapter.ListItemViewHolder>() {

    private var hourly = mutableListOf<WeatherResponse>()
    private var timezone: String = ""

    fun setTimezone(timezoneWeather: String) {
        timezone = timezoneWeather
    }


    fun setHourly(hourlyWeather: List<WeatherResponse>) {
        hourly.clear()
        hourly.addAll(hourlyWeather)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        return ListItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.detail_item_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        hourly.getOrNull(position)?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = hourly.size

    inner class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val hourTV = itemView.findViewById<TextView>(R.id.card_hourTV)
        private val temperatureTV = itemView.findViewById<TextView>(R.id.card_temperatureTV)
        private val informationTV = itemView.findViewById<TextView>(R.id.card_informationTV)
        private val formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)

        fun bind(entity: WeatherResponse) {
            val hour = getHour(entity, formatter)
            hourTV.text = hour
            temperatureTV.text = entity.temp.roundToInt().toString()
            informationTV.text = entity.weather.first().main
        }
    }

    private fun getHour(entity: WeatherResponse, formatter: DateTimeFormatter): String {
        val zoneFormat = Instant.ofEpochSecond(entity.dt).atZone(ZoneId.of(timezone))
        val datetime = zoneFormat.format(formatter)
        val split = datetime.split(' ')
        if (split.size > 2) {
            val time = split[1]
            val split1 = time.split(':')
            if (split1.isNotEmpty())
            return split1[0]
        }
        return "0"
    }
}