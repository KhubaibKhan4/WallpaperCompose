package com.codespacepro.wallpapercompose.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codespacepro.wallpapercompose.repository.Repository
import javax.inject.Inject

class MainViewModelFactory @Inject constructor(private val repository: Repository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}