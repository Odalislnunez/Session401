package es.usj.mastertsa.onunez.session401

import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*

class MapActivity : FragmentActivity(), OnMapReadyCallback {
    private var longitude: String? = null
    private var latitude: String? = null
    private var extra: Bundle? = null
    private var mapFragment: SupportMapFragment? = null
    private var map: GoogleMap? = null
    private var address: Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.fragment_map_activity)
        extra = intent.extras
        longitude = extra!!.getString("Longitud")
        latitude = extra!!.getString("Latitud")
        mapAvailable()
    }

    private fun mapAvailable() {
        if (mapFragment == null) {
            mapFragment =
                supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment!!.getMapAsync(this@MapActivity)
        }
        if (mapFragment != null) {
            Toast.makeText(this, "MapActivity de Google disponible",
                Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val dLongitude = if(!longitude.isNullOrEmpty())
            longitude?.toDouble() else 0.0
        val dLatitude = if(!latitude.isNullOrEmpty())
            latitude?.toDouble() else 0.0
        try {
            val geoCoder = Geocoder(this, Locale.getDefault())
            val addresses = geoCoder.getFromLocation(dLatitude!!,
                dLongitude!!, 5)
            map = googleMap
            map!!.mapType = GoogleMap.MAP_TYPE_NORMAL
            val nuevaPosicion = LatLng(dLatitude, dLongitude)
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
                return
            }
            map!!.isMyLocationEnabled = true
            map!!.moveCamera(
                CameraUpdateFactory.newLatLngZoom(nuevaPosicion,
                    20f))
            var addressCoordinates = "Sin Datos"
            if (addresses.size > 0) {
                address = addresses[0]
                addressCoordinates = (address!!.getAddressLine(0)
                        + " " + address!!.postalCode
                        + " " + address!!.locality
                        + ", " + address!!.countryName)
            }
            map!!.addMarker(
                MarkerOptions()
                    .title(addressCoordinates)
                    .position(nuevaPosicion))
            val cameraPosition = CameraPosition.builder()
                .target(nuevaPosicion)
                .zoom(16.0f)
                .tilt(45.0f)
                .bearing(45.0f)
                .build()
            map!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                2000, null)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}