package com.codespacepro.wallpapercompose.api

import com.codespacepro.wallpapercompose.data.Wallpaper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface WallpaperApi {



    @GET("curated")
    suspend fun getWallpaper(
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): Response<Wallpaper>


    @GET("search")
    suspend fun getSearchWallpaper(
        @Query("query") query: String,
        @Query("page") page: Int,
    @Query("per_page") per_page: Int
    ): Response<Wallpaper>

}