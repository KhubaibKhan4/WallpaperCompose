package com.codespacepro.wallpapercompose.api.remote

import com.codespacepro.wallpapercompose.data.Wallpaper
import com.codespacepro.wallpapercompose.util.Constant.Companion.AUTHORIZATION
import com.codespacepro.wallpapercompose.util.Constant.Companion.BAREER
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object WallpaperApi {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    explicitNulls = false
                }
            )

            install(HttpTimeout) {
                requestTimeoutMillis = 100000
                connectTimeoutMillis = 100000
            }

            defaultRequest {
                header(
                    AUTHORIZATION,
                    BAREER
                )
            }

        }

    }


    suspend fun getWallpaper(page: Int, per_page: Int): Wallpaper {
        val url = "https://api.pexels.com/v1/curated?per_page=${per_page}&page=${page}"
        return client.get(url).body()
    }

    suspend fun getSearch(query: String, page: Int, per_page: Int): Wallpaper {
        val url =
            "https://api.pexels.com/v1/search?query=${query}&per_page=${per_page}&page=${page}"
        return client.get(url).body()
    }
}