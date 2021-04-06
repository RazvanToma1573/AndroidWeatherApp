package toma.razvan.weatherapp.data.model

data class ApiResponse (
    var current: WeatherResponse,
    var timezone: String,
    var hourly: List<WeatherResponse>,
    var daily: List<WeatherDayResponse>,
)

data class WeatherResponse (
    var dt: Long,
    var temp: Float,
    var weather: List<Weather>,
    var humidity: Float,
    var wind_speed: Float,
)

data class WeatherDayResponse (
    var dt: Long,
    var temp: WeatherTemperature,
    var weather: List<Weather>,
    var humidity: Float,
    var wind_speed: Float,
)

data class Weather (
    var main: String?,
)

data class WeatherTemperature(
    var day: Double,
)