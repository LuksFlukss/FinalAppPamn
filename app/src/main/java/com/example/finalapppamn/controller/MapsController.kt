package com.example.finalapppamn.controller

import com.example.finalapppamn.BuildConfig
import com.example.finalapppamn.MapsActivity
import com.example.finalapppamn.model.data.RetrofitServiceFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.finalapppamn.model.data.ServiceModel.AddressComponent



class MapsController(mapsActivity: MapsActivity) {
    private lateinit var googleMap: GoogleMap

    fun setGoogleMap(googleMap: GoogleMap) {
        this.googleMap = googleMap
        // Configuración adicional del mapa si es necesario
    }
    fun searchLocationByAddress(sAddress: String, callback: (LatLng?) -> Unit) {
        // Create a coroutine scope using the main dispatcher
        val scope = CoroutineScope(Dispatchers.Main)
        val service = RetrofitServiceFactory.makeRetrofitService()

        // Launch a coroutine that will call the getGeocoding() function
// ...

        scope.launch {
            val response = service.locationSearch(sAddress, BuildConfig.MAPS_API_KEY)

            try {
                val responseBody = response.body()
                if (responseBody != null && responseBody.results.isNotEmpty()) {
                    // Extract the location information
                    val location = responseBody.results[0].geometry.location

                    if (location != null) {
                        // Process the location (latitude and longitude)
                        val latLng = LatLng(location.lat, location.lng)
                        callback(latLng)
                    } else {
                        // Handle the case where location is null

                        callback(null)
                    }
                } else {
                    // Handle the case where there are no results
                    callback(null)
                }
            } catch (e: Exception) {
                // Handle exceptions
                e.printStackTrace()
                callback(null)
            }
        }
    }
}
