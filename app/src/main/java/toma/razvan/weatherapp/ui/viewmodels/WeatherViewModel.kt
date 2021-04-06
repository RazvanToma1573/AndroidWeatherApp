package toma.razvan.weatherapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import toma.razvan.weatherapp.data.model.WeatherDayResponse
import toma.razvan.weatherapp.data.model.WeatherResponse
import toma.razvan.weatherapp.data.repository.ApiRepository
import kotlin.math.roundToInt

class WeatherViewModel(private val repository: ApiRepository) : ViewModel() {

    val current = MutableLiveData<WeatherResponse>()
    val temperature = MutableLiveData<String>()
    val information = MutableLiveData<String>()
    val timezone = MutableLiveData<String>()
    val hourly = MutableLiveData<List<WeatherResponse>>()
    val daily = MutableLiveData<List<WeatherDayResponse>>()
    var position = LatLng(46.770439, 23.591423)

    fun getWeather() {
        viewModelScope.launch {
            val weather = repository.getWeather(position.latitude, position.longitude)
            current.value = weather.current
            temperature.value = "${weather.current.temp.roundToInt()}°"
            information.value = weather.current.weather.first().main
            timezone.value = weather.timezone
            hourly.value = weather.hourly
            daily.value = weather.daily
        }
    }

//    fun checkData() {
//        Log.d("sa;dkfjasl;dkfjasdlk;slakfjaslkdjfslkjdfsk;df", "checkData() called")
//        viewModelScope.launch {
//            while(true) {
//                Log.d("**************************************", current.value.toString())
//                delay(5000)
//            }
//        }
//    }
}