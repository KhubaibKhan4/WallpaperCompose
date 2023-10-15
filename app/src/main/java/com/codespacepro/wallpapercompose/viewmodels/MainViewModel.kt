package com.codespacepro.wallpapercompose.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codespacepro.wallpapercompose.data.Wallpaper
import com.codespacepro.wallpapercompose.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val myResponse: MutableLiveData<Response<Wallpaper>> = MutableLiveData()
    val mySearchResponse: MutableLiveData<Response<Wallpaper>> = MutableLiveData()


    fun getWallpaper(page: Int, per_page: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getWallpaper(page, per_page)
                myResponse.value = response
            } catch (e: TimeoutException) {
                Log.d("Main", "getWallpaper: ${e.printStackTrace()}")
            }

        }
    }

    fun getSearchWallpaper(query: String, page: Int, per_page: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getSearchWallpaper(query, page, per_page)
                myResponse.value = response
            }catch (e: TimeoutException) {
                Log.d("Main", "getWallpaper: ${e.printStackTrace()}")
            }

        }
    }


}