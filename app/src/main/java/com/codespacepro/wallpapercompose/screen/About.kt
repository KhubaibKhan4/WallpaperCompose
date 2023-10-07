import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.KeyboardArrowLeft
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.codespacepro.wallpapercompose.R
import com.codespacepro.wallpapercompose.shared.SharedPreferenceManager
import com.codespacepro.wallpapercompose.ui.theme.WallpaperComposeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences = SharedPreferenceManager(context)
    var isDarkTheme by remember { mutableStateOf(false) }

    isDarkTheme = sharedPreferences.getData("myMode", isDarkTheme)

    WallpaperComposeTheme(darkTheme = isDarkTheme) {
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Text(
                        text = "About Us",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
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
                        ),
                    verticalArrangement = Arrangement.Center
                ) {
                    // App Logo or Image
                    Image(
                        painter = painterResource(id = R.drawable.app_logo), // Replace with your app logo
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(shape = CircleShape)
                            .border(2.dp, Color.Gray, shape = CircleShape)
                            .padding(8.dp)
                            .align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // App Name and Description
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = if (isDarkTheme) Color.White else Color.Black
                        ),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Text(
                        text = "Beautiful Wallpapers for Every Mood",
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.Gray
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // About Us Content
                    Text(
                        text = "About Us",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = if (isDarkTheme) Color.White else Color.Black
                        )
                    )

                    Text(
                        text = "Welcome to Your App Name, your go-to destination for stunning wallpapers. We provide a wide range of high-quality wallpapers to suit every taste and mood. Whether you're looking for nature, cityscapes, animals, or abstract art, we have it all. Explore our collection and beautify your device with the perfect wallpaper today!",
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.Gray
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Contact Information
                    Text(
                        text = "Contact Us",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = if (isDarkTheme) Color.White else Color.Black
                        )
                    )

                    Text(
                        text = "If you have any questions, suggestions, or feedback, please don't hesitate to reach out to us. We'd love to hear from you!",
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.Gray
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    // Contact Button
                    Button(
                        onClick = {
                            // Implement your contact action here (e.g., open an email or contact form)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text(text = "Contact Us")
                    }
                }
            })
    }
}
