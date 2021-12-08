package es.usj.mastertsa.onunez.session401

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.core.app.ActivityCompat
import es.usj.mastertsa.onunez.session401.databinding.ActivityFifthBinding

class FifthActivity : AppCompatActivity() {
    private var locManager: LocationManager? = null
    private var locListener: LocationListener? = null
    private val bindings: ActivityFifthBinding by lazy {
        ActivityFifthBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        requestedOrientation =  ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(bindings.root)
        gpsRecord()
        bindings.btnLocalizar.setOnClickListener {
            try {
                val intent = Intent(this@FifthActivity,
                    MapActivity::class.java)
                intent.putExtra("Longitud", bindings.tvLongitude.text.toString())
                intent.putExtra("Latitud", bindings.tvLatitude.text.toString())
                startActivity(intent)
            } catch (ex: Exception) {
                Toast.makeText(this@FifthActivity, ex.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun gpsRecord() {
        locManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            return
        }
        val loc =
            locManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        showPosition(loc)
        locListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                showPosition(location)
            }
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        locManager!!.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 5000, 0f, locListener!!)
    }

    private fun showPosition(loc: Location?): Array<String> {
        val data: Array<String>
        if (loc != null) {
            bindings.tvLatitude.setText(loc.latitude.toString())
            bindings.tvLongitude.setText(loc.longitude.toString())
            data = arrayOf(loc.longitude.toString(), loc.latitude.toString())
        } else {
            data = arrayOf(40.4167754.toString(), (-3.7037901999999576).toString(), "Default location")
            bindings.tvLatitude.setText(40.4167754.toString())
            bindings.tvLongitude.setText((-3.7037901999999576).toString())
        }
        return data
    }
}