package nameit.com.androidgrainchaintest.activities

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import kotlinx.android.synthetic.main.layout_header.tv_title
import kotlinx.android.synthetic.main.main_activity.*
import nameit.com.androidgrainchaintest.fragments.BottomSheetRouteListFragment
import nameit.com.androidgrainchaintest.utils.Constants
import nameit.com.androidgrainchaintest.utils.Utils
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.graphics.Bitmap
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import android.graphics.Canvas
import android.graphics.Color
import android.os.SystemClock
import android.view.WindowManager
import android.widget.ProgressBar
import nameit.com.androidgrainchaintest.R
import nameit.com.androidgrainchaintest.models.PointGPSModel
import nameit.com.androidgrainchaintest.utils.Utils.Companion.bitmapDescriptorFromVector


class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mGoogleMap: GoogleMap? = null
    private var vector: Int = 0
    private var tag: Int = 1
    private var addMarker: Boolean = false

    override fun onMapReady(googleMap: GoogleMap?) {
        mGoogleMap = googleMap;
    }

    internal lateinit var mLocationRequest: LocationRequest
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    lateinit var mLastLocation: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }

        checkPermissionGPS()
        setHeader()
        setListeners()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        vector = R.drawable.ic_pin_32_start
    }

    fun setListeners() {
        btn_details_route.setOnClickListener { showBottomSheet() }
        btn_route.setOnClickListener { route(btn_route) }
    }

    fun setHeader() {
        tv_title.text = resources.getString(R.string.main_header_title)
    }

    fun showBottomSheet() {
        BottomSheetRouteListFragment().show(supportFragmentManager, "bottomSheetRouteList")
    }

    fun route(view: View){
        if (checkPermissionGPS()) {
            if (tag == 1) {
                addMarker = true;
                getCurrentLocation()
                vector = R.drawable.ic_pin_32_start
                //get currency location
                tag = 2
                view.setBackgroundResource(R.drawable.button_stop_route)
                progressBar.visibility = View.VISIBLE
                getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
                Constants.START_TIME = SystemClock.elapsedRealtime();
            } else {
                addMarker = true;
                Constants.FINAL_TIME = SystemClock.elapsedRealtime();
                getCurrentLocation()
                Utils.showAlertDialog(this, "Guardar ruta...", R.layout.layout_info_alert)
                vector = R.drawable.ic_pin_32_finish
                //save currency location
                tag = 1
                view.setBackgroundResource(R.drawable.button_start_route)
                mGoogleMap!!.clear()
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        getCurrentLocation()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == Constants.REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Utils.userMessage(this, "Permiso denegado", R.drawable.ic_close)
            }
        }
    }

    fun checkPermissionGPS(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                // Show the permission request
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    Constants.REQUEST_PERMISSION_LOCATION
                )
                false
            }
        } else {
            true
        }
    }

    protected fun getCurrentLocation() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest!!.setInterval(2000)
        mLocationRequest!!.setFastestInterval(1000)

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        mFusedLocationProviderClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // do work here
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun onLocationChanged(location: Location) {
        mLastLocation = location

        if (addMarker) {
            mGoogleMap!!.addMarker(MarkerOptions().position(LatLng(location.latitude, location.longitude)))
                .setIcon(bitmapDescriptorFromVector(this, vector))
            addMarker = false
            progressBar.visibility = View.INVISIBLE
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            Constants.dataListPointGPS.add(PointGPSModel(location.latitude, location.longitude))

        }

        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(location.latitude, location.longitude))
            .zoom(18f).build()
        //Zoom in and animate the camera.
        mGoogleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }
}