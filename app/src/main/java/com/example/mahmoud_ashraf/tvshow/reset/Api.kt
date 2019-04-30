package com.example.mahmoud_ashraf.tvshow.reset

import com.example.mahmoud_ashraf.tvshow.models.ShowListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("tv/popular")
    fun getPopularShows(@Query("page") page: Int): Call<ShowListResponse>

}