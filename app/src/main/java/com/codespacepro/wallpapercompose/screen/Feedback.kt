package com.codespacepro.wallpapercompose.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.codespacepro.wallpapercompose.shared.SharedPreferenceManager
import com.codespacepro.wallpapercompose.ui.theme.WallpaperComposeTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(navController: NavHostController) {
    var text by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val sharedPreferences = SharedPreferenceManager(context)
    var isDarkTheme by remember { mutableStateOf(false) }

    isDarkTheme = sharedPreferences.getData("myMode", isDarkTheme)

    WallpaperComposeTheme(darkTheme = isDarkTheme) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Feedback",
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = if (isDarkTheme) Color.White else Color.Black
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = if (isDarkTheme) Color.DarkGray else Color.White) // Customize the top app bar color
                )
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
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header
                    Text(
                        text = "Give Us Your Feedback",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp,
                            color = if (isDarkTheme) Color.White else Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Feedback Form
                    BasicTextField(
                        value = text,
                        onValueChange = { text = it },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                // Handle keyboard done action
                            }
                        ),
                        textStyle = TextStyle(
                            fontSize = 18.sp,
                            color = if (isDarkTheme) Color.White else Color.DarkGray
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(if (isDarkTheme) Color.DarkGray else Color.White)
                            .border(
                                1.dp,
                                if (isDarkTheme) Color.LightGray else Color.White,
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            if (text.isBlank()) {
                                Toast.makeText(
                                    context,
                                    "Please Write the Feedback...",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // Implement feedback submission logic
                                val emailIntent = Intent(Intent.ACTION_SENDTO)
                                emailIntent.data = "mailto:18.bscs@gmail.com.com".toUri()
                                emailIntent.putExtra(
                                    Intent.EXTRA_SUBJECT,
                                    "Feedback Regarding Wallpaper Compose"
                                )
                                emailIntent.putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Hello, I want to give you the feedback related to the Wallpaper Compose App:\n$text"
                                )
                                context.startActivity(emailIntent)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Submit Feedback")
                    }
                }
            }
        )
    }
}