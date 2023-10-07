package com.codespacepro.wallpapercompose.shared

import android.content.Context

class SharedPreferenceManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
    fun saveData(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getData(key: String, value: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, value) ?: true
    }
}
