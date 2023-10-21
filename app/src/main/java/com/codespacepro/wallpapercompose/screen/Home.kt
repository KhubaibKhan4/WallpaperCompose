package com.codespacepro.wallpapercompose.screen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.material.icons.sharp.ExitToApp
import androidx.compose.material.icons.sharp.Favorite
import androidx.compose.material.icons.sharp.FavoriteBorder
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material.icons.sharp.Settings
import androidx.compose.material.icons.sharp.Share
import androidx.compose.material.icons.sharp.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.codespacepro.wallpapercompose.R
import com.codespacepro.wallpapercompose.data.Photo
import com.codespacepro.wallpapercompose.data.Wallpaper
import com.codespacepro.wallpapercompose.navigation.navgraph.Screen
import com.codespacepro.wallpapercompose.repository.Repository
import com.codespacepro.wallpapercompose.shared.SharedPreferenceManager
import com.codespacepro.wallpapercompose.ui.theme.WallpaperComposeTheme
import com.codespacepro.wallpapercompose.viewmodels.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {

    val repository = Repository()
    val mainViewModel = MainViewModel(repository)
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current


    //Mutable State for Storing Wallpapers
    val wallpaper = remember {
        mutableStateOf<Wallpaper?>(null)
    }

    //Mutable State for Storing List of Photos
    val wallpapers = remember {
        mutableStateListOf<Photo>()
    }

    //Coroutine
    val scope = rememberCoroutineScope()

    //Alert Dialog
    var alertDialog by remember {
        mutableStateOf(false)
    }

    //Context
    val context = LocalContext.current

    //Search Value
    var searchValue by rememberSaveable {
        mutableStateOf("")
    }

    //Search Visibility
    var searchVisibility by remember {
        mutableStateOf(false)
    }

    //search Active
    var isSearchActive by remember {
        mutableStateOf(false)
    }

    //Per Page Wallpaper Functionality
    var pageS by remember {
        mutableStateOf(1)
    }
    var perPage by remember {
        mutableStateOf(80)
    }

    //Loading Property
    var isLoading by remember {
        mutableStateOf(true)
    }

    val sharedPreferences = SharedPreferenceManager(context)
    var isDarkTheme by remember { mutableStateOf(false) }

    isDarkTheme = sharedPreferences.getData("myMode", isDarkTheme)




    try {
        mainViewModel.getWallpaper(pageS, perPage)

        mainViewModel.myResponse.observe(lifecycleOwner, Observer { response ->
            if (response.isSuccessful) {
                isLoading = false
                wallpaper.value = response.body()

                wallpapers.clear()
                wallpapers.addAll(
                    (response.body()?.photos ?: emptyList()) as Collection<Photo>
                )

                Log.d("Main", wallpaper.toString())
                Log.d("Main", response.body()!!.next_page)
                Log.d("Main", response.body()!!.page.toString())
                Log.d("Main", response.body()!!.photos.toString())

            } else {
                isLoading = false
                Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })

    } catch (e: SocketTimeoutException) {
        Log.d("Exception", "HomeScreen: ${e.localizedMessage}")
    }


    val state = rememberLazyStaggeredGridState()
    val topBarState = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)





    WallpaperComposeTheme(darkTheme = isDarkTheme) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(drawerTonalElevation = 4.dp) {
                    ModalDrawerSheet {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(169.dp)
                                .clip(RoundedCornerShape(topEnd = 12.dp))
                                .background(color = Color.White)
                        ) {
                            if (isDarkTheme)
                                Image(
                                    painter = painterResource(id = R.drawable.nav_splash_dark),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            else {
                                Image(
                                    painter = painterResource(id = R.drawable.nav_splash),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        Divider()
                        NavigationDrawerItem(
                            label = { Text(text = "Search") },
                            selected = false,
                            onClick = {
                                scope.launch {
                                    searchVisibility = true
                                    drawerState.close()
                                }
                            },
                            icon = {
                                Icon(imageVector = Icons.Sharp.Search, contentDescription = "")
                            }
                        )
                        NavigationDrawerItem(
                            label = { Text(text = "Favourite") },
                            selected = false,
                            onClick = {
                                scope.launch {
                                    drawerState.close()
                                    navController.navigate(Screen.Favourite.route)
                                }
                            },
                            icon = {
                                Icon(imageVector = Icons.Sharp.Favorite, contentDescription = "")
                            }
                        )
                        NavigationDrawerItem(
                            label = { Text(text = "About") },
                            selected = false,
                            onClick = {
                                scope.launch {
                                    navController.navigate(Screen.About.route)
                                    drawerState.close()
                                }
                            }, icon = {
                                Icon(
                                    imageVector = Icons.Sharp.AccountCircle,
                                    contentDescription = null
                                )
                            }
                        )

                        NavigationDrawerItem(
                            label = { Text(text = "Setting") },
                            selected = false,
                            onClick = {
                                scope.launch {
                                    navController.navigate(Screen.Setting.route)
                                    drawerState.close()
                                }
                            }, icon = {
                                Icon(
                                    imageVector = Icons.Sharp.Settings, contentDescription = null
                                )
                            }
                        )

                        NavigationDrawerItem(
                            label = { Text(text = "Feedback") },
                            selected = false,
                            onClick = {
                                scope.launch {
                                    navController.navigate(Screen.Feedback.route)
                                    drawerState.close()
                                }
                            },
                            icon = {
                                Icon(
                                    painterResource(id = R.drawable.ic_feedback),
                                    contentDescription = ""
                                )
                            }
                        )
                        NavigationDrawerItem(
                            label = { Text(text = "Share") },
                            selected = false,
                            onClick = {
                                scope.launch {
                                    val shareIntent = Intent(Intent.ACTION_SEND)
                                    shareIntent.type = "text/plain"
                                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My App")
                                    shareIntent.putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Check out My App on the Play Store: https://play.google.com/store/apps/details?id=com.codespacepro.wallpapercompose"
                                    )

                                    // You can customize the chooser title if you want
                                    val chooserTitle = "Share via"
                                    val chooser = Intent.createChooser(shareIntent, chooserTitle)

                                    // Check if there are apps available to handle the intent
                                    if (shareIntent.resolveActivity(context.packageManager) != null) {
                                        context.startActivity(chooser)
                                    }
                                    drawerState.close()
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Sharp.Share,
                                    contentDescription = ""
                                )
                            }
                        )
                        NavigationDrawerItem(
                            label = { Text(text = "Exit") },
                            selected = false,
                            onClick = {
                                scope.launch {
                                    drawerState.close()
                                    alertDialog = true
                                }

                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Sharp.ExitToApp,
                                    contentDescription = "exit"
                                )
                            }
                        )
                        // ...other drawer items
                    }
                }
            },
        ) {
            Scaffold(modifier = Modifier
                .fillMaxWidth()
                .nestedScroll(
                    connection = topBarState.nestedScrollConnection, dispatcher = null
                ), topBar = {
                TopAppBar(
                    title = { Text(text = "Wallpaper") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {

                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "null"
                            )
                        }

                    }, colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White,
                        scrolledContainerColor = Color.DarkGray,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    ), actions = {
                        IconButton(onClick = {
                            searchVisibility = !searchVisibility
                        }) {
                            if (searchVisibility)
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = null
                                )
                            else
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null
                                )

                        }

                        IconButton(onClick = {
                            scope.launch {
                                isLoading = true
                                delay(500)
                                pageS = 1
                                perPage = 80
                                mainViewModel.getWallpaper(pageS, perPage)
                            }

                        }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null
                            )
                        }
                        if (pageS >= 2) {
                            IconButton(onClick = {
                                scope.launch {
                                    isLoading = true
                                    delay(500)
                                    pageS--
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        }
                        IconButton(onClick = {

                            scope.launch {
                                isLoading = true
                                delay(500)
                                pageS++
                            }

                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowForward,
                                contentDescription = null
                            )
                        }

                    }, scrollBehavior = topBarState
                )
            }, content = {

                val categories = listOf(
                    "Nature",
                    "Abstract",
                    "Landscape",
                    "Animals",
                    "Space",
                    "Cityscape",
                    "Minimalistic",
                    "Cars",
                    "Sports",
                    "Music",
                    "Food",
                    "Art",
                    "Technology",
                    "Anime",
                    "Fantasy"
                )
                var selectedCategory by remember { mutableStateOf(categories.first()) }


                Column {
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = it.calculateTopPadding()),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        Column(
                            modifier = Modifier.padding(top = it.calculateTopPadding()),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LazyRow(modifier = Modifier.padding(start = 8.dp)) {
                                item {
                                    categories.forEach { category ->
                                        Box(modifier = Modifier.fillMaxWidth()) {
                                            ElevatedButton(
                                                onClick = {
                                                    isLoading = true
                                                    scope.launch(Dispatchers.IO) {
                                                        mainViewModel.getSearchWallpaper(
                                                            query = category,
                                                            pageS,
                                                            perPage
                                                        )
                                                        selectedCategory = category
                                                        delay(1500)
                                                        isLoading = false
                                                    }

                                                    Log.d("main", "Categories: $category")
                                                },
                                                modifier = Modifier.padding(4.dp),
                                                elevation = ButtonDefaults.buttonElevation(
                                                    defaultElevation = 6.dp,
                                                    pressedElevation = 8.dp,
                                                    hoveredElevation = 10.dp,
                                                    focusedElevation = 10.dp
                                                ),
                                                shape = RoundedCornerShape(12.dp),
                                                colors = ButtonDefaults.elevatedButtonColors(
                                                    containerColor = if (selectedCategory == category) MaterialTheme.colorScheme.primary
                                                    else Color.White
                                                )
                                            ) {
                                                Text(
                                                    text = category.uppercase(Locale.ROOT),
                                                    color = if (selectedCategory == category) Color.White else Color.Black
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            if (searchVisibility) {
                                SearchBar(
                                    query = searchValue,
                                    onQueryChange = { searchValue = it },
                                    onSearch = {
                                        scope.launch {
                                            isLoading = true
                                            delay(500)
                                            pageS = 1
                                            perPage = 80
                                            mainViewModel.getSearchWallpaper(
                                                query = searchValue,
                                                pageS,
                                                perPage
                                            )
                                            isSearchActive = false
                                        }
                                    },
                                    active = isSearchActive,
                                    onActiveChange = { isSearchActive },
                                    placeholder = {
                                        Text(text = "Search...")
                                    },
                                    interactionSource = MutableInteractionSource(),
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            scope.launch {
                                                isLoading = true
                                                delay(500)
                                                pageS = 1
                                                perPage = 80
                                                mainViewModel.getSearchWallpaper(
                                                    query = searchValue,
                                                    pageS,
                                                    perPage
                                                )
                                                isSearchActive = false
                                            }
                                        }) {
                                            Icon(
                                                imageVector = Icons.Sharp.Search,
                                                contentDescription = ""
                                            )
                                        }
                                    },
                                ) {
                                    WallpaperList(
                                        photo = wallpapers,
                                        state = state,
                                        navController,
                                        isDarkTheme
                                    )
                                }
                            }
                            WallpaperList(
                                photo = wallpapers,
                                state = state,
                                navController,
                                isDarkTheme
                            )
                        }

                    }
                }
                if (alertDialog) {
                    AlertDialog(
                        onDismissRequest = { alertDialog = false },
                        confirmButton = {
                            TextButton(onClick = {
                                (context as? Activity)?.finish()
                            }) {
                                Text(text = "Yes, Close App")
                            }
                        },
                        icon = {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Sharp.Warning,
                                    contentDescription = "warning"
                                )
                            }
                        },
                        text = {
                            Text(text = "You app will be close & Current Data will be Removed. Closing the app will exit and close all active tasks. Do you still want to close it...")
                        },
                        title = {
                            Text(text = "Are You Sure?")
                        },
                        dismissButton = {
                            TextButton(onClick = { alertDialog = false }) {
                                Text(text = "No")
                            }
                        },
                        shape = AlertDialogDefaults.shape
                    )
                }

            })
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WallpaperList(
    photo: List<Photo>,
    state: LazyStaggeredGridState,
    navController: NavHostController,
    isDarkTheme: Boolean
) {

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(120.dp),
        state = state,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalItemSpacing = 4.dp
    ) {
        items(photo) { photo ->
            WallpaperItem(photo = photo, navController, isDarkTheme)
        }

    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WallpaperItem(photo: Photo, navController: NavHostController, isDarkTheme: Boolean) {
    var liked by rememberSaveable {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp) // Increased padding for spacing
    ) {
        // Apply a gradient background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFE1E1E1), Color(0xFFBDBDBD))
                    )
                )
        )

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(photo.src.portrait)
                .crossfade(true).build(),
            placeholder = painterResource(R.drawable.baseline_image_24),
            contentDescription = "null",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(300.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(4.dp))
                .clickable {
                    navController.navigate(
                        Screen.FullScreen.passData(
                            Uri.encode(photo.src.portrait),
                            photo.photographer
                        )
                    )
                }
                .shadow(4.dp, RoundedCornerShape(4.dp)) // Add shadow
        )

        Spacer(modifier = Modifier.height(8.dp)) // Increased spacing

        Card(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(2.dp))
                .padding(4.dp) // Increased padding
                .shadow(2.dp, RoundedCornerShape(2.dp)) // Add shadow
        ) {
            Text(
                text = photo.photographer,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp, // Adjust font size
                    fontFamily = FontFamily.Serif, // Use a custom font family
                    color = if (isDarkTheme) Color.White else Color.Black // Customize text color
                ),
                modifier = Modifier.padding(8.dp), // Increased padding
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                textAlign = TextAlign.Start
            )
        }
        IconButton(modifier = Modifier
            .align(Alignment.BottomEnd)
            .toggleable(
                value = true,
                indication = null,
                interactionSource = MutableInteractionSource(),
                onValueChange = {}
            ),
            onClick = {
                liked = !liked
            }
        ) {
            Icon(
                imageVector = if (liked) Icons.Sharp.Favorite else Icons.Sharp.FavoriteBorder,
                contentDescription = "",
                tint = if (liked) Color.Red else Color.White
            )
        }
    }
}
