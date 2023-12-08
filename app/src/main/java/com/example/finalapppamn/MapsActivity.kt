package com.example.finalapppamn

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.finalapppamn.databinding.ActivityMapsBinding
import com.example.finalapppamn.model.CardViewCoor
import com.example.finalapppamn.model.cardCoorProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.firebase.firestore.FirebaseFirestore


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient:FusedLocationProviderClient

    companion object{
        private const val LOCATION_REQUEST_CODE = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        setUpMap()
        fetchLocationsFromFirebase()

    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this){location ->
            if (location != null){
                lastLocation  = location
                val currentLatLong  = LatLng(location.latitude,location.longitude)
                placeMarkerOnMap(currentLatLong)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong,12f))
            }

        }

    }

    private fun placeMarkerOnMap(currentLatLong: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("My location")
        markerOptions.draggable(true)
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mMap.addMarker(markerOptions)
    }

    override fun onMarkerClick(p0: Marker): Boolean = false


    private fun fetchLocationsFromFirebase() {
        db.collection("Cardview")
            .get()
            .addOnSuccessListener { result ->
                val cardList = mutableListOf<CardViewCoor>()

                for (document in result) {
                    // Assuming you have a data class named CardViewData
                    val geoPoint = document.getGeoPoint("geopoint")
                    val latitude = geoPoint?.latitude
                    val longitude = geoPoint?.longitude

                    val cardData = CardViewCoor(
                        title = document.getString("title") ?: "",
                        imageUrl = document.getString("imageUrl") ?: "",
                        stars = document.getLong("stars")?.toInt() ?: 0,
                        price = document.getLong("precio")?.toInt() ?: 0,
                        latitude = latitude,
                        longitude = longitude
                    )

                    val currentLatLong  = LatLng(cardData.latitude!!, cardData.longitude!!)
                    val markerOptions = MarkerOptions().position(currentLatLong)
                    markerOptions.title(cardData.title)
                    mMap.addMarker(markerOptions)
                }

                // Update RecyclerView adapter with the retrieved data
                updateView(cardList)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }

    private fun updateView(coords: List<CardViewCoor>) {
        // Clear the existing data
        cardCoorProvider.cardViewsList.clear()

        // Add the new data to the provider
        cardCoorProvider.cardViewsList.addAll(coords)

    }

    }