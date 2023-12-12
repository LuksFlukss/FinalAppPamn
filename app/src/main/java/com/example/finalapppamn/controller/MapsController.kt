package com.example.finalapppamn.controller

import com.example.finalapppamn.BuildConfig
import com.example.finalapppamn.MapsActivity
import com.example.finalapppamn.model.data.RetrofitServiceFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class MapsController(mapsActivity: MapsActivity) {
    private lateinit var googleMap: GoogleMap

    fun setGoogleMap(googleMap: GoogleMap) {
        this.googleMap = googleMap
        // Configuración adicional del mapa si es necesario
    }
    fun searchLocationByAddress(sAddress: String, callback: (LatLng?) -> Unit) {
        // Create a coroutine scope using the main dispatcher
        val scope = CoroutineScope(Dispatchers.Main)
        // Launch a coroutine that will call the getGeocoding() function
        scope.launch {
            // Call the getGeocoding() function
            val service = RetrofitServiceFactory.makeRetrofitService()

            val response = service.locationSearch(sAddress, BuildConfig.MAPS_API_KEY)
            println(response.body())
        }
    }
}
