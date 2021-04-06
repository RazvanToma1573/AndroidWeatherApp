package toma.razvan.weatherapp.ui

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import toma.razvan.weatherapp.R
import toma.razvan.weatherapp.data.model.WeatherResponse
import toma.razvan.weatherapp.ui.adapters.WeatherRvAdapter
import toma.razvan.weatherapp.ui.viewmodels.WeatherViewModel
import java.util.*
import kotlin.math.roundToInt

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(R.layout.fragment_first) {

    private val viewModel: WeatherViewModel by sharedViewModel()
    private lateinit var adapter: WeatherRvAdapter
    private var current: WeatherResponse? = null
    private lateinit var todayTemperatureTV: TextView
    private lateinit var todayInformationTV: TextView
    private lateinit var todayIconIV: ImageView
    private lateinit var titleTemperatureTV: TextView
    private lateinit var titleInformationTV: TextView
    private lateinit var titleIconIV: ImageView
    private lateinit var hourlyForecast: TextView
    private lateinit var changeLocationTV: TextView
    private lateinit var locationTV: TextView
    private lateinit var geocoder: Geocoder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        geocoder = Geocoder(activity, Locale.getDefault())
        adapter = WeatherRvAdapter(::onWeatherClicked)

    }
    
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel.getWeather()

        observeWeather()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.itemsRV).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = this@FirstFragment.adapter
        }

        hourlyForecast = view.findViewById(R.id.card_data_noteTV)
        hourlyForecast.setOnClickListener{
            findNavController().navigate(
                    FirstFragmentDirections.actionFirstFragmentToDetailsFragment()
            )
        }

        todayTemperatureTV = view.findViewById(R.id.card_data_temperatureTV)
        todayInformationTV = view.findViewById(R.id.card_data_informationTV)
        todayIconIV = view.findViewById(R.id.card_iconIV)

        titleTemperatureTV = view.findViewById(R.id.title_temperatureTV)
        titleInformationTV = view.findViewById(R.id.title_infoTV)
        titleIconIV = view.findViewById(R.id.title_iconTV)

        locationTV = view.findViewById(R.id.locationTV)

        changeLocationTV = view.findViewById(R.id.change_locationBTN)
        changeLocationTV.setOnClickListener{
            findNavController().navigate(
                FirstFragmentDirections.actionFirstFragmentToMapFragment()
            )
        }
    }

    override fun onResume() {
        super.onResume()
        val locations = geocoder.getFromLocation(viewModel.position.latitude, viewModel.position.longitude, 1)
        if (locations.size > 0) {
            locationTV.text = locations[0].locality
        } else {
            locationTV.text = "Unknown"
        }
    }

    private fun observeWeather() {
        viewModel.daily.observe(viewLifecycleOwner, { value ->
            adapter.setDaily(value)
        })

        viewModel.timezone.observe(viewLifecycleOwner, { value ->
            adapter.setTimezone(value)
        })

        viewModel.current.observe(viewLifecycleOwner, { value ->
            current = value
            updateUi()
        })

        viewModel.temperature.observe(viewLifecycleOwner, { value ->
            todayTemperatureTV.text = value
            titleTemperatureTV.text = value
        })

        viewModel.information.observe(viewLifecycleOwner, { value ->
            todayInformationTV.text = value
            titleInformationTV.text = value
        })
    }

    private fun updateUi() {
        current?.let {
            todayIconIV.setImageResource(R.drawable.ic_cloudy)
            titleIconIV.setImageResource(R.drawable.ic_cloudy_2)
        }
    }

    private fun onWeatherClicked(weather: WeatherResponse) {
        Log.d("RecyclerViewClick", "Element in RecyclerView Clicked")
    }

}