package com.example.finalapppamn

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.widget.SearchView
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.finalapppamn.controller.MapsController
import com.example.finalapppamn.databinding.ActivityMapsBinding
import com.example.finalapppamn.model.CardViewCoor
import com.example.finalapppamn.model.cardCoorProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var mapsController: MapsController
    private val db = FirebaseFirestore.getInstance()
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient:FusedLocationProviderClient
    private lateinit var currentLatLong: LatLng
    private var polylines: MutableList<Polyline> = mutableListOf()
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

        mapsController = MapsController(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val searchView = findViewById<SearchView>(R.id.searchView)

        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != ""){
                    val text = searchView.query.toString()

                    mapsController.searchLocationByAddress(text) { result ->
                        if (result != null) {
                            // Manejar el resultado (LatLng)
                            // Por ejemplo, puedes centrar el mapa en la ubicación obtenida
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(result, 11f))
                        }else{
                            showMarkerNameDialog("No se encontró el lugar")
                        }
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle the search query text change
                // Puedes agregar aquí la lógica para actualizar los resultados de búsqueda en tiempo real.
                return false
            }
        })
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.custom_style)
        mMap.setMapStyle(mapStyleOptions)
        setUpMap()
        fetchLocationsFromFirebase()

    }



    private fun setUpMap() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this){location ->
            if (location != null){
                lastLocation  = location
                this.currentLatLong  = LatLng(location.latitude,location.longitude)
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


    private fun removePath(){
        for (line in this.polylines) {
            line.remove()
        }

        polylines.clear()
    }
    override fun onMarkerClick(p0: Marker): Boolean {
        removePath()
        showMarkerNameDialog(p0.title)

        val locationMark = p0.position
        val path: MutableList<List<LatLng>> = ArrayList()
        val urlDirections = "https://maps.googleapis.com/maps/api/directions/json?origin=${this.currentLatLong.latitude},${this.currentLatLong.longitude}&destination=${locationMark.latitude},${locationMark.longitude}&key=${BuildConfig.MAPS_API_KEY}"
        val directionsRequest = object : StringRequest(Method.GET, urlDirections, Response.Listener<String> {
                response ->
            val jsonResponse = JSONObject(response)
            // Get routes
            val routes = jsonResponse.getJSONArray("routes")
            val legs = routes.getJSONObject(0).getJSONArray("legs")
            val steps = legs.getJSONObject(0).getJSONArray("steps")
            for (i in 0 until steps.length()) {
                val points = steps.getJSONObject(i).getJSONObject("polyline").getString("points")
                path.add(PolyUtil.decode(points))
            }
            for (i in 0 until path.size) {
                val polylineOptions = PolylineOptions().addAll(path[i]).color(Color.RED)
                val polyline = mMap.addPolyline(polylineOptions)
                this.polylines.add(polyline)
            }
        }, Response.ErrorListener {
                _ ->
        }){}
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(directionsRequest)


        return true
    }

    private fun showMarkerNameDialog(markerTitle: String?) {
        Toast.makeText(this, markerTitle, Toast.LENGTH_SHORT).show()


    }


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