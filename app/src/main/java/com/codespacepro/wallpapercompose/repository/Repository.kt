package com.codespacepro.wallpapercompose.repository

import com.codespacepro.wallpapercompose.api.RetrofitInstance
import com.codespacepro.wallpapercompose.api.remote.WallpaperApi
import com.codespacepro.wallpapercompose.data.Wallpaper
import retrofit2.Response

class Repository {

    suspend fun getWallpaper(page: Int, per_page: Int): Response<Wallpaper> {
        return RetrofitInstance.api.getWallpaper(page, per_page)
    }

    suspend fun getSearchWallpaper(query: String, page: Int, per_page: Int): Response<Wallpaper> {
        return RetrofitInstance.api.getSearchWallpaper(query = query, page, per_page)
    }


    suspend fun getKWallpaper(page: Int, per_page: Int): Wallpaper {
        return WallpaperApi.getWallpaper(page, per_page)
    }
    suspend fun getKSearchWallpaper(query: String, page: Int, per_page: Int): Wallpaper {
        return WallpaperApi.getSearch(query = query, page, per_page)
    }

}