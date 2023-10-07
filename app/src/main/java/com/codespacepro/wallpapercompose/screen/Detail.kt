package com.codespacepro.wallpapercompose.screen

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.sharp.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.codespacepro.wallpapercompose.R
import com.codespacepro.wallpapercompose.navigation.navgraph.Screen
import com.codespacepro.wallpapercompose.shared.SharedPreferenceManager
import com.codespacepro.wallpapercompose.ui.theme.WallpaperComposeTheme
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("Range", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailScreen(navController: NavHostController, image: String?, photographer: String?) {
    val context = LocalContext.current
    val uriState = remember { mutableStateOf<Uri?>(null) }


    val sharedPreferences = SharedPreferenceManager(context)
    var isDarkTheme by remember { mutableStateOf(false) }

    isDarkTheme = sharedPreferences.getData("myMode", isDarkTheme)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument()
    ) { uri ->
        uriState.value = uri
        shareImage(uri, context)
    }

    WallpaperComposeTheme(darkTheme = isDarkTheme) {


        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Text(
                        text = "Detail",
                        textAlign = TextAlign.Center,
                        color = if (isDarkTheme) MaterialTheme.colorScheme.primary else Color.Red
                    )
                },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Sharp.KeyboardArrowLeft,
                                contentDescription = ""
                            )
                        }
                    })
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = it.calculateTopPadding(),
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        ), // Add padding for spacing
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box {
                        // AsyncImage with rounded corners
                        AsyncImage(
                            model = image ?: "",
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(8.dp)), // Rounded corners
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            onClick = {
                                navController.navigate(
                                    Screen.FullScreen.passData(
                                        Uri.encode(image),
                                        Uri.encode(photographer)
                                    )
                                )
                            },
                            modifier = Modifier.align(Alignment.BottomEnd)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.fullscreen),
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                    }


                    Spacer(modifier = Modifier.height(16.dp)) // Add spacing

                    // Photographer's name
                    Text(
                        text = photographer ?: "",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp, // Customize font size
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp)) // Add spacing

                    // Share and Download buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Share button
                        Button(
                            onClick = {
                                val imageFile = createImageFile(image?.toUri().toString(), context)
                                launcher.launch(imageFile.name)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .height(48.dp), // Customize button height
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF)), // Customize button color
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share, contentDescription = null,
                                tint = if (isDarkTheme) Color.White else Color.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp)) // Add spacing
                            Text(
                                text = "Share",
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp)) // Add spacing

                        // Download button
                        Button(
                            onClick = {
                                val downloadManager =
                                    context.getSystemService(DownloadManager::class.java)
                                val request =
                                    DownloadManager
                                        .Request(image?.toUri())
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
                                .weight(1f)
                                .fillMaxWidth()
                                .height(48.dp)
                                .padding(horizontal = 10.dp), // Customize button height
                            colors = ButtonDefaults.buttonColors(contentColor = Color(0xFF34C759)), // Customize button color
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.download),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp)) // Add spacing
                            Text(
                                text = "Download"
                            )
                        }
                    }
                }
            })

    }
}

private fun createImageFile(imageUrl: String?, context: Context): File {
    // Create a temporary file to store the image
    val cacheDir = File(context.cacheDir, "shared_images")
    cacheDir.mkdirs()
    val fileName = "shared_image.png"
    val file = File(cacheDir, fileName)

    // Download the image and save it to the file
    // You can use a library like Coil or Glide to download the image here

    return file
}

private fun shareImage(uri: Uri?, context: Context) {
    if (uri != null) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
    }
}
