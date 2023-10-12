package com.codespacepro.wallpapercompose.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.KeyboardArrowLeft
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.codespacepro.wallpapercompose.R
import com.codespacepro.wallpapercompose.shared.SharedPreferenceManager
import com.codespacepro.wallpapercompose.ui.theme.WallpaperComposeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(navController: NavHostController) {
    val context = LocalContext.current
    var notificationsEnabled by remember { mutableStateOf(true) }
    var autoDownloadEnabled by remember { mutableStateOf(false) }
    val selectedLanguage by remember { mutableStateOf("English") }


    var isDarkTheme by remember { mutableStateOf(false) }
    val sharedPreferences = SharedPreferenceManager(context)

    isDarkTheme = sharedPreferences.getData("myMode", isDarkTheme)


    WallpaperComposeTheme(darkTheme = isDarkTheme) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Settings", textAlign = TextAlign.Center) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Sharp.KeyboardArrowLeft,
                                contentDescription = "",
                            )
                        }
                    }
                )
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = it.calculateTopPadding(), start = 8.dp, end = 8.dp),
                    verticalArrangement = Arrangement.Top
                ) {



                    // Dark Mode Toggle
                    SettingItem(
                        title = "Dark Mode",
                        icon = painterResource(id = R.drawable.night_mode),
                        enabled = isDarkTheme,
                        onToggle = {
                            isDarkTheme = !isDarkTheme
                            sharedPreferences.saveData("myMode", isDarkTheme)
                        }
                    )

                    Divider()

                    // Notifications Toggle
                    SettingItem(
                        title = "Notifications",
                        icon = painterResource(id = R.drawable.ic_notifications),
                        enabled = notificationsEnabled,
                        onToggle = { notificationsEnabled = !notificationsEnabled }
                    )

                    Divider()

                    // Auto Download Toggle
                    SettingItem(
                        title = "Auto Download",
                        icon = painterResource(id = R.drawable.download),
                        enabled = autoDownloadEnabled,
                        onToggle = { autoDownloadEnabled = !autoDownloadEnabled }
                    )

                    Divider()

                    // Language Selector
                    SettingItem(
                        title = "Language",
                        icon = painterResource(id = R.drawable.language),
                        description = selectedLanguage,
                        onClick = {
                            // Show language selection dialog or screen
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                }
            },

            )
    }

}

@Composable
fun SettingItem(
    title: String,
    icon: Painter,
    enabled: Boolean = true,
    description: String? = null,
    onClick: (() -> Unit)? = null,
    onToggle: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = icon, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = title)
        }

        if (description != null) {
            Text(text = description)
        } else if (enabled != null) {
            Switch(
                checked = enabled,
                onCheckedChange = { onToggle?.invoke() },
                modifier = Modifier.clickable { onClick?.invoke() }
            )
        }
    }
}

