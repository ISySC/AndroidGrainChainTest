package nameit.com.androidgrainchaintest.activities

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.route_detail_activity.*
import nameit.com.androidgrainchaintest.database.DBHelper
import nameit.com.androidgrainchaintest.utils.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import nameit.com.androidgrainchaintest.R
import nameit.com.androidgrainchaintest.utils.Utils.Companion.bitmapDescriptorFromVector


class RouteDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mGoogleMap: GoogleMap? = null

    override fun onMapReady(googleMap: GoogleMap?) {
        mGoogleMap = googleMap
        getRouteDetail()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.route_detail_activity)

        (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync(this)
    }

    fun getRouteDetail() {
        var cursor = DBHelper(this).showRouteDetails(Constants.ROUTE_ID_SELECT, DBHelper(this).readableDatabase)

        while (cursor.moveToNext()) {
            tv_distance.text = cursor.getString(6) + " mtrs "
            tv_time.text = cursor.getString(7) + " min"
            tv_title.text = resources.getString(R.string.route_detail_header_title) + " - " + cursor.getString(1)

            addMarker(cursor.getString(2).toDouble(), cursor.getString(3).toDouble(), R.drawable.ic_pin_32_start)
            addMarker(cursor.getString(4).toDouble(), cursor.getString(5).toDouble(), R.drawable.ic_pin_32_finish)

            mGoogleMap!!.addPolyline(
                PolylineOptions()
                    .add(LatLng(cursor.getString(2).toDouble(), cursor.getString(3).toDouble()),
                        LatLng(cursor.getString(4).toDouble(), cursor.getString(5).toDouble()))
                    .width(5.0F)
                    .color(Color.RED)
            )
        }
    }

    private fun addMarker(lat: Double, lon: Double, vector: Int) {
        mGoogleMap!!.addMarker(MarkerOptions().position(LatLng(lat, lon)))
            .setIcon(bitmapDescriptorFromVector(this, vector))

        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(lat, lon))
            .zoom(15f).build()
        //Zoom in and animate the camera.
        mGoogleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}