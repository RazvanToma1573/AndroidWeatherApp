package toma.razvan.weatherapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import toma.razvan.weatherapp.R
import toma.razvan.weatherapp.data.Entity
import toma.razvan.weatherapp.data.model.WeatherDayResponse
import toma.razvan.weatherapp.data.model.WeatherResponse
import toma.razvan.weatherapp.utils.Constants
import java.text.DateFormatSymbols
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class WeatherRvAdapter(var selectedDay: (weatherResponse: WeatherResponse) -> Unit) :
    RecyclerView.Adapter<WeatherRvAdapter.ListItemViewHolder>() {

    private var daily = mutableListOf<WeatherDayResponse>()
    private var timezone: String = ""

    fun setTimezone(timezoneWeather: String) {
        timezone = timezoneWeather
    }

    fun setDaily(dailyWeather: List<WeatherDayResponse>) {
        daily.clear()
        daily.addAll(dailyWeather)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        return ListItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        daily.getOrNull(position)?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = daily.size

    inner class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardDataDateTV = itemView.findViewById<TextView>(R.id.card_data_dateTV)
        private val cardDataInformationTV = itemView.findViewById<TextView>(R.id.card_data_informationTV)
        private val cardDataIconIV = itemView.findViewById<AppCompatImageView>(R.id.card_iconIV)
        private val formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)

        fun bind(entity: WeatherDayResponse) {
            val zoneFormat = Instant.ofEpochSecond(entity.dt).atZone(ZoneId.of(timezone))
            val datetime = zoneFormat.format(formatter)
            val split = datetime.split(' ')
            if (split.isNotEmpty()) {
                val date = split[0]
                val parsedDate = date.split('-')
                if (parsedDate.size > 2) {
                    val month = parsedDate[1].toInt() - 1
                    cardDataDateTV.text = "${DateFormatSymbols().months[month]} ${parsedDate[2]}"
                    cardDataInformationTV.text = entity.weather.first().main
                    cardDataIconIV.setImageResource(R.drawable.ic_cloudy)
                }
            }

        }
    }


}