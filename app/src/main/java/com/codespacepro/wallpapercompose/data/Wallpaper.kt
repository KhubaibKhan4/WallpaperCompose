package com.codespacepro.wallpapercompose.data

import kotlinx.serialization.Serializable

@Serializable
data class Wallpaper(
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val photos: List<Photo>,
    val total_results: Int
)