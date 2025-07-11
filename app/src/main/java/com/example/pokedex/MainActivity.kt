package com.example.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pokedex.navigation.PokedexNavHost
import com.example.pokedex.navigation.PokedexRoutes
import com.example.pokedex.ui.theme.PokemonTheme
import com.example.pokedex.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

val LocalNavHostController = compositionLocalOf<NavHostController> { error("No NavHostController provided") }

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bottomTabs = listOf<PokedexRoutes>(
            PokedexRoutes.PokedexScreenRoute(),
            PokedexRoutes.FavouritePokemons(),
        )
        enableEdgeToEdge()
        setContent {
            PokemonTheme(dynamicColor = false) {
                val navController: NavHostController = rememberNavController()
                var selectedDest by rememberSaveable { mutableIntStateOf(0) }
                var topBarTitle by rememberSaveable { mutableStateOf(Constants.POKEDEX_SCREEN_NAME) }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(topBarTitle) },
                            modifier = Modifier.background(Color.Red),
                            colors = TopAppBarColors(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.onPrimary,
                                MaterialTheme.colorScheme.primary
                            )
                        )
                    },
                    bottomBar = {
                    NavigationBar(
                        windowInsets = NavigationBarDefaults.windowInsets,
                        containerColor = MaterialTheme.colorScheme.primary,
                        tonalElevation = 8.dp
                    ) {
                        bottomTabs.forEachIndexed { index, destination ->
                            val isSelected = index == selectedDest
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = {
                                    if (!isSelected) {
                                        navController.navigate(destination) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                        selectedDest = index
                                        topBarTitle = destination.name
                                    }
                                },
                                icon = {
                                    Image(
                                        painter = painterResource(if (isSelected) destination.selectedIcon else destination.icon),
                                        contentDescription = "",
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                label = {
                                    Text(destination.name)
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSecondary,
                                    indicatorColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                                )

                            )
                        }
                    }
                }) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        CompositionLocalProvider(LocalNavHostController provides navController) {
                            PokedexNavHost(navController)
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PokemonTheme {
        Greeting("Android")
    }
}