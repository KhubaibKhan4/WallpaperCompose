package com.codespacepro.wallpapercompose.downloader

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri

class AndroidDownloader(private val context: Context) : Downloader {


    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    override fun downloadFil(url: String): Long {
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("image/jpeg")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("image.jpeg")
            .addRequestHeader(
                "Authorization",
                "Bearer psWUxJSiSZbvIQh0qDy6CcKDS4dsBl7fs3L0dzjcxSxe4kYSxAltOS0I"
            )
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "image/jpeg")
        return downloadManager.enqueue(request)
    }
}