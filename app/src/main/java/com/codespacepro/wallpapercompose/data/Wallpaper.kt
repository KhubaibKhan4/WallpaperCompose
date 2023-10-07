package com.codespacepro.wallpapercompose.data

data class Wallpaper(
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val photos: List<Photo>,
    val total_results: Int
)