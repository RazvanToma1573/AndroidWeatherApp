package toma.razvan.weatherapp.ui

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import toma.razvan.weatherapp.R
import toma.razvan.weatherapp.data.model.WeatherResponse
import toma.razvan.weatherapp.ui.adapters.WeatherHourlyRvAdapter
import toma.razvan.weatherapp.ui.adapters.WeatherRvAdapter
import toma.razvan.weatherapp.ui.viewmodels.WeatherViewModel
import toma.razvan.weatherapp.utils.Constants.DATE_TIME_FORMAT
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val viewModel: WeatherViewModel by sharedViewModel()
    private lateinit var hourly: List<WeatherResponse>
    private lateinit var current: WeatherResponse
    private lateinit var timezone: String
    private lateinit var adapter: WeatherHourlyRvAdapter
    private lateinit var weatherDetailsCL: ConstraintLayout
    private lateinit var backTV: TextView
    private lateinit var windInfoTV: TextView
    private lateinit var weatherInfoTV: TextView
    private lateinit var humidityInfoTV: TextView
    private lateinit var previousTemperatureTV: TextView
    private lateinit var nowTemperatureTV: TextView
    private lateinit var nextTemperatureTV: TextView
    private lateinit var weatherLocationTV: TextView
    private lateinit var geocoder: Geocoder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = WeatherHourlyRvAdapter()

        geocoder = Geocoder(activity, Locale.getDefault())

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        observeHourlyWeather()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.findViewById<RecyclerView>(R.id.details_itemsRV).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = this@DetailsFragment.adapter
        }

        weatherDetailsCL = view.findViewById(R.id.details_InfoCL)


        backTV = view.findViewById(R.id.details_backTV)
        backTV.setOnClickListener{
            findNavController().popBackStack()
        }

        windInfoTV = view.findViewById(R.id.weather_wind_infoTV)
        weatherInfoTV = view.findViewById(R.id.weather_infoTV)
        humidityInfoTV = view.findViewById(R.id.weather_humidity_infoTV)

        previousTemperatureTV = view.findViewById(R.id.weather_temperature_previousTV)
        nowTemperatureTV = view.findViewById(R.id.weather_temperature_nowTV)
        nextTemperatureTV = view.findViewById(R.id.weather_temperature_nextTV)

        weatherLocationTV = view.findViewById(R.id.weather_info_locationTV)
    }

    private fun observeHourlyWeather() {
        viewModel.hourly.observe(viewLifecycleOwner, {
            hourly = it
            adapter.setHourly(it)
        })

        viewModel.timezone.observe(viewLifecycleOwner, {
            timezone = it
            adapter.setTimezone(it)
        })

        viewModel.current.observe(viewLifecycleOwner, {
            current = it
            setBackground()
            setInfo()
        })
    }


    private fun setBackground() {
        val temp = current.temp.roundToInt()
        when {
            temp < -4 -> weatherDetailsCL.setBackgroundResource(R.color.weather_one)
            temp > -5 && temp < 1 -> weatherDetailsCL.setBackgroundResource(R.color.weather_two)
            temp in 1..5 -> weatherDetailsCL.setBackgroundResource(R.color.weather_three)
            temp in 6..10 -> weatherDetailsCL.setBackgroundResource(R.color.weather_four)
            temp in 11..15 -> weatherDetailsCL.setBackgroundResource(R.color.weather_five)
            temp in 16..20 -> weatherDetailsCL.setBackgroundResource(R.color.weather_six)
            temp in 21..25 -> weatherDetailsCL.setBackgroundResource(R.color.weather_seven)
            temp in 26..30 -> weatherDetailsCL.setBackgroundResource(R.color.weather_eight)
            temp > 30 -> weatherDetailsCL.setBackgroundResource(R.color.weather_nine)
        }
    }

    private fun setInfo() {
        val addresses = geocoder.getFromLocation(viewModel.position.latitude, viewModel.position.longitude, 1)
        if (addresses.size > 0) {
            val address = addresses[0]
            weatherLocationTV.text = "${address.locality}, ${address.countryName}"
            weatherInfoTV.text = current.weather[0].main
            windInfoTV.text = current.wind_speed.roundToInt().toString()
            humidityInfoTV.text = current.humidity.roundToInt().toString()
            nowTemperatureTV.text = current.temp.roundToInt().toString() + "°"
            val hour = getHour(current)
            val index = hourly.indexOfFirst { getHour(it) == hour }

            if (index > 0) {
                previousTemperatureTV.text = hourly[index - 1].temp.roundToInt().toString() + "°"
            } else {
                previousTemperatureTV.text = hourly[hourly.size - 1].temp.roundToInt().toString() + "°"
            }

            if (index < hourly.size - 1) {
                nextTemperatureTV.text = hourly[index + 1].temp.roundToInt().toString() + "°"
            } else {
                nextTemperatureTV.text = hourly[0].temp.roundToInt().toString() + "°"
            }
        }
    }

    private fun getHour(entity: WeatherResponse): String {
        val formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)
        val zoneFormat = Instant.ofEpochSecond(entity.dt).atZone(ZoneId.of(timezone))
        val datetime = zoneFormat.format(formatter)
        val time = datetime.split(' ')[1]
        return time.split(':')[0]
    }

}