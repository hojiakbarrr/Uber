package com.example.uber.register.customer

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import com.example.uber.App
import com.example.uber.R
import com.example.uber.api.RetrofitBuilder
import com.example.uber.databinding.ActivityCustomerMapsBinding
import com.example.uber.model.CreatUserResponse
import com.example.uber.orderModel.GeoPoint
import com.example.uber.orderModel.Order
import com.example.uber.orderModel.OrderResponse
import com.example.uber.register.RegisterActivity
import com.example.uber.utils.toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class CustomerMapsActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityCustomerMapsBinding
    var locationManager: LocationManager? = null
    var currentAddress: String? = null
    var currentLatitude: Double? = null
    var currentLongitude: Double? = null


    /*
     */
    var currentLocation: Location? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var REQUEST_CODE = 101


    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()
        getMyLocation()

        binding.exitBtn.setOnClickListener {
            val editor = App.pref?.edit()
            editor?.clear()
            editor?.apply()
            toast("Good bye!")
            startActivity(Intent(this@CustomerMapsActivity, RegisterActivity::class.java))
        }

        binding.getTaxi.setOnClickListener {
            val geoPoint = GeoPoint()
            geoPoint.latitude = currentLocation?.latitude
            geoPoint.longitude = currentLocation?.longitude
            val order = Order()
            order.location = geoPoint
            order.address = currentAddress
            order.username = App.pref?.getString("userName", "")
            order.phone = App.pref?.getString("phone", "")
            Log.d("log", order.toString())


            val api = RetrofitBuilder.api.createOrder(order)
4
            api.enqueue(object : Callback<OrderResponse?>{
                override fun onResponse(
                    call: Call<OrderResponse?>,
                    response: Response<OrderResponse?>
                ) {
                    if (response.isSuccessful){
                        toast("Order is create")
                    }
                }

                override fun onFailure(call: Call<OrderResponse?>, t: Throwable) {
                    Log.d("log",t.message.toString())
                }

            })

        }

    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
            return
        }

        val task = fusedLocationProviderClient!!.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null){
                currentLocation = location
                val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)
                supportMapFragment!!.getMapAsync(this)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("I Am Here!").icon(BitmapDescriptorFactory.fromResource(R.drawable.pin3))
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16f))

        googleMap.addMarker(markerOptions)

        googleMap.isMyLocationEnabled = true

//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
    }




    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLocation()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private fun getMyLocation() {
        try {
            locationManager =
                this.applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
                return
            }
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        } catch (e: java.lang.Exception) {
            e.message
        }
    }



    override fun onLocationChanged(location: Location) {
        toast(location.latitude.toString() + "___" + location.longitude.toString())
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addressList: List<Address> =
                geocoder.getFromLocation(location.latitude, location.longitude, 1)

            currentLatitude = location.latitude
            currentLongitude = location.longitude

            currentAddress = addressList[0].getAddressLine(0)
            Log.i("location", currentAddress.toString())
            binding.address.text = currentAddress
        } catch (e: IOException) {
            e.message
        }
    }





}