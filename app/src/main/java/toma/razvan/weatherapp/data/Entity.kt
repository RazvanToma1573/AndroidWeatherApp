package toma.razvan.weatherapp.data

data class Entity(
        val id: Long,
        val date: String,
        val information: String,
        val temperature: Double
)

val entitiesList: List<Entity> = listOf(
        Entity(1, "March 13", "Partly Cloudy", 17.5),
        Entity(2, "March 14", "Partly Cloudy", 18.0),
        Entity(3, "March 15", "Partly Cloudy", 17.5),
        Entity(4, "March 16", "Partly Cloudy",18.0),
        Entity(5, "March 17", "Partly Cloudy",18.0),
        Entity(6, "March 18", "Partly Cloudy", 17.5),
        Entity(7, "March 19", "Partly Cloudy",18.0),
        Entity(8, "March 20", "Partly Cloudy", 17.5),
        Entity(9, "March 21", "Partly Cloudy",18.0),
        Entity(10, "March 22", "Partly Cloudy", 17.5),
        Entity(11, "March 23", "Partly Cloudy",18.0),
)
