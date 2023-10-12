package com.codespacepro.wallpapercompose.screen

import android.app.DownloadManager
import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.sharp.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.codespacepro.wallpapercompose.R
import com.codespacepro.wallpapercompose.navigation.navgraph.Screen
import com.codespacepro.wallpapercompose.shared.SharedPreferenceManager
import com.codespacepro.wallpapercompose.ui.theme.WallpaperComposeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FullScreen(navController: NavHostController, imageUrl: String?, photographer: String?) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isLiked by rememberSaveable {
        mutableStateOf(false)
    }
    val uri = imageUrl
    val uriHandler = LocalUriHandler.current

    var choseWallpaper by remember {
        mutableStateOf(false)
    }
    val sharedPreferences = SharedPreferenceManager(context)
    var isDarkTheme by remember { mutableStateOf(false) }

    isDarkTheme = sharedPreferences.getData("myMode", isDarkTheme)

    WallpaperComposeTheme(darkTheme = isDarkTheme) {

        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 12.dp, start = 10.dp)
                    .size(50.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
                    .background(color = MaterialTheme.colorScheme.primary)

            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "")
            }
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 70.dp)
            ) {
                IconButton(
                    onClick = { isLiked = !isLiked }
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "",
                        tint = if (isLiked) Color.Red else Color.White
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                IconButton(
                    onClick = {
                        val downloadManager =
                            context.getSystemService(DownloadManager::class.java)
                        val request =
                            DownloadManager
                                .Request(imageUrl?.toUri())
                                .setMimeType("image/jpeg")
                                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                .setTitle("image.jpeg")
                                .setDestinationInExternalPublicDir(
                                    Environment.DIRECTORY_DOWNLOADS,
                                    "image/jpeg"
                                )
                        downloadManager.enqueue(request)
                    },
                    modifier = Modifier

                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.download),
                        contentDescription = "",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                IconButton(
                    onClick = {
                        navController.navigate(
                            Screen.Detail.passData(
                                Uri.encode(imageUrl),
                                Uri.encode(photographer)
                            )
                        )
                    },
                    modifier = Modifier

                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.linear_scale),
                        contentDescription = "",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                IconButton(
                    onClick = {
                        uri?.let { uriHandler.openUri(it) }
                    },
                    modifier = Modifier

                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.open),
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            }

            FilledTonalIconButton(
                onClick = {
                    choseWallpaper = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                    .toggleable(
                        indication = null,
                        interactionSource = MutableInteractionSource(),
                        value = false,
                        onValueChange = {},
                        role = Role.Button,
                    ),
                interactionSource = MutableInteractionSource(),
                enabled = true,
                colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = MaterialTheme.colorScheme.primary),
                content = {
                    Text(text = "Apply")
                }
            )

        }
        if (choseWallpaper) {
            var wallpaperBitmap by remember { mutableStateOf<Bitmap?>(null) }
            val wallpaperManager = WallpaperManager.getInstance(context)

            LaunchedEffect(key1 = true) {
                val loadBitmap = imageUrl?.let { loadBitmapFromUrl(it) }
                wallpaperBitmap = loadBitmap
            }

            AlertDialog(
                dismissButton = {
                    TextButton(onClick = {
                        scope.launch {
                            if (wallpaperBitmap != null) {
                                wallpaperManager.setBitmap(
                                    wallpaperBitmap,
                                    null,
                                    true,
                                    WallpaperManager.FLAG_LOCK
                                )
                                Toast.makeText(
                                    context,
                                    "Lock Screen Wallpaper Updated Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                delay(200)
                                choseWallpaper = false
                            } else {
                                // Handle the case where wallpaperBitmap is null
                                // You can show an error message or take appropriate action here
                            }
                        }
                    }) {
                        Text(text = "Lock Screen")
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        scope.launch {
                            if (wallpaperBitmap != null) {
                                wallpaperManager.setBitmap(
                                    wallpaperBitmap,
                                    null,
                                    true,
                                    WallpaperManager.FLAG_SYSTEM
                                )
                                Toast.makeText(
                                    context,
                                    "Home Screen Wallpaper Updated Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                delay(200)
                                choseWallpaper = false
                            } else {
                                // Handle the case where wallpaperBitmap is null
                                // You can show an error message or take appropriate action here
                            }
                        }

                    }) {
                        Text(text = "Home Screen")
                    }
                },
                tonalElevation = 8.dp,
                shape = AlertDialogDefaults.shape,
                onDismissRequest = { choseWallpaper = false },
                icon = {
                    Icon(imageVector = Icons.Sharp.Warning, contentDescription = "")
                },
                text = {
                    Text(text = "Choose where to set the wallpaper:")
                },
                title = {
                    Text(text = "Set Wallpaper")
                }

            )
        }
    }
}


suspend fun loadBitmapFromUrl(imageUrl: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.connect()

            val inputStream = BufferedInputStream(connection.inputStream)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            connection.disconnect()
            inputStream.close()

            return@withContext bitmap
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}

