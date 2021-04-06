package toma.razvan.weatherapp.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import toma.razvan.weatherapp.R
import toma.razvan.weatherapp.ui.viewmodels.WeatherViewModel
import java.util.*

class MapFragment : Fragment(R.layout.fragment_map), GoogleMap.OnMarkerDragListener {

    private val viewModel: WeatherViewModel by sharedViewModel()
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var marker: Marker
    private lateinit var backTV: TextView
    private lateinit var updateLocation: TextView
    private lateinit var geocoder: Geocoder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        geocoder = Geocoder(activity, Locale.getDefault())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
        view.doOnLayout {
            refreshMap()
        }

        backTV = view.findViewById(R.id.map_backTV)
        backTV.setOnClickListener{
            findNavController().popBackStack()
        }

        updateLocation = view.findViewById(R.id.update_locationBTN)
        updateLocation.setOnClickListener {
            viewModel.position = (marker.position)
            findNavController().popBackStack()
        }
    }

    override fun onMarkerDragStart(p0: Marker?) {
    }

    override fun onMarkerDragEnd(p0: Marker?) {
        marker.position = p0?.position
        val locations = geocoder.getFromLocation(viewModel.position.latitude, viewModel.position.longitude, 1)
        if (locations.size > 0) {
            marker.title = locations[0].locality
        } else {
            marker.title = "Unknown"
        }
    }

    override fun onMarkerDrag(p0: Marker?) {
    }

    private fun refreshMap() {
        mapFragment.getMapAsync { map ->
            val location = viewModel.position
            map.setOnMarkerDragListener(this)
            marker = map.addMarker(
                MarkerOptions()
                    .position(location)
                    .title("Cluj-Napoca")
                    .icon(activity?.let { bitmapDescriptorFromVector(it, R.drawable.ic_pin) })
                    .draggable(true)
            )
            val builder = LatLngBounds.Builder()
            builder.include(location)
            val bounds = builder.build()
            map.animateCamera(
                CameraUpdateFactory.newLatLngBounds(bounds, 100)
            )
        }
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?. run{
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }
}