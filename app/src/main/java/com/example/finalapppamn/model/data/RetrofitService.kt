package com.example.finalapppamn.model.data

import android.widget.RemoteViews.RemoteResponse
import com.example.finalapppamn.BuildConfig
import com.example.finalapppamn.model.data.ServiceModel.Geometry
import com.example.finalapppamn.model.data.ServiceModel.Location
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("maps/api/geocode/json")
    suspend fun locationSearch(
        @Query("address") location: String,
        @Query("key") apiKey: String
    ): Response<Location>

}

object RetrofitServiceFactory{
    fun makeRetrofitService(): RetrofitService{
        return Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RetrofitService::class.java)
    }

}
