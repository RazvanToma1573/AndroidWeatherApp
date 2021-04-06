package toma.razvan.weatherapp.data.repository

import toma.razvan.weatherapp.data.api.Api

class ApiRepository(private val apiService: Api) {

    suspend fun getWeather(latitude: Double, longitude: Double) = apiService.getWeather(latitude, longitude)

}