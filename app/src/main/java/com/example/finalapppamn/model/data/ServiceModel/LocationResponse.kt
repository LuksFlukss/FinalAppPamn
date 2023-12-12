package com.example.finalapppamn.model.data.ServiceModel

data class LocationResponse(
    val results: List<GeocodingResult>,
    val status: String
)
