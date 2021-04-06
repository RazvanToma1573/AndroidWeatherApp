package toma.razvan.weatherapp.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import toma.razvan.weatherapp.data.model.ApiResponse

interface Api {

    @GET("onecall")
    suspend fun getWeather(@Query("lat") latitude: Double, @Query("lon") longitude: Double): ApiResponse

}

