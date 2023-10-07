package com.codespacepro.wallpapercompose.api

import com.codespacepro.wallpapercompose.util.Constant.Companion.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitInstance {
    private val interceptor = Interceptor { interceptor ->
        val originalRequest = interceptor.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "psWUxJSiSZbvIQh0qDy6CcKDS4dsBl7fs3L0dzjcxSxe4kYSxAltOS0I")
            .build()
        interceptor.proceed(newRequest)

    }

    private var client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .connectTimeout(30, TimeUnit.SECONDS) // Increase the connection timeout
        .readTimeout(30, TimeUnit.SECONDS) // Increase the read timeout
        .writeTimeout(30, TimeUnit.SECONDS) // Increase the write timeout
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: WallpaperApi by lazy {
        retrofit.create(WallpaperApi::class.java)
    }
}