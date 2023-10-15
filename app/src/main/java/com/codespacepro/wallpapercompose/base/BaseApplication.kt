package com.codespacepro.wallpapercompose.base

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.codespacepro.wallpapercompose.R

class BaseApplication : Application(), ImageLoaderFactory {

    override fun newImageLoader(): ImageLoader {
        return ImageLoader(context = this).newBuilder()
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .maxSizePercent(0.03)
                    .directory(cacheDir)
                    .build()
            }
            .networkObserverEnabled(enable = true)
            .networkCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.01)
                    .strongReferencesEnabled(enable = true)
                    .weakReferencesEnabled(enable = true)
                    .build()
            }
            .placeholder(R.drawable.app_logo)
            .error(R.drawable.report)
            .logger(DebugLogger())
            .crossfade(enable = true)
            .build()
    }
}