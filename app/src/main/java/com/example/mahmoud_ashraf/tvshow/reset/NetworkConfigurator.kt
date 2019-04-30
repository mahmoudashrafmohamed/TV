package com.example.mahmoud_ashraf.tvshow.reset

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class NetworkConfigurator(private val apiKey : String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        val original = chain!!.request()
        val originalHttpUrl = original.url()
        val url = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", apiKey)
                .addQueryParameter("language", Locale.getDefault().language)
                .addQueryParameter("region", Locale.getDefault().country)
                .build()

        val requestBuilder = original.newBuilder()
                .url(url)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }

    companion object {

    fun provideApi(): Api =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(NetworkConfigurator(API_KEY))
                    .build())
            .build()
            .create(Api::class.java)



        private const val BASE_URL = "https://api.themoviedb.org/3/"
        private const val API_KEY = "6fd435cb71362c657e45c6cd8a5c0510"
    }

}

