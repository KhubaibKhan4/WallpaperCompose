package com.codespacepro.wallpapercompose.downloader

interface Downloader {
    fun downloadFil(url: String): Long
}