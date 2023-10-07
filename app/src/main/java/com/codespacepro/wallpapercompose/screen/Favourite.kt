import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.sharp.KeyboardArrowLeft
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.codespacepro.wallpapercompose.shared.SharedPreferenceManager
import com.codespacepro.wallpapercompose.ui.theme.WallpaperComposeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteScreen(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences = SharedPreferenceManager(context)
    var isDarkTheme by remember { mutableStateOf(false) }

    isDarkTheme = sharedPreferences.getData("myMode", isDarkTheme)

    WallpaperComposeTheme(isDarkTheme) {
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Text(
                        text = "Favourite",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Sharp.KeyboardArrowLeft,
                                contentDescription = "",
                                tint = if (isDarkTheme) MaterialTheme.colorScheme.primary else Color.Black
                            )
                        }
                    })
            },
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = it.calculateTopPadding()),
                    contentAlignment = Alignment.Center
                ) {
                    Row {
                        Icon(imageVector = Icons.Default.Warning, contentDescription = "")
                        Text(text = "No Favourite Item Found...")
                    }
                }
            })
    }
}
