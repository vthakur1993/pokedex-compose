package com.example.pokedex.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dagger.hilt.android.EntryPointAccessors

@Composable
fun PokedexNavHost(navController: NavHostController) {
    val pokemonListItemNavType = EntryPointAccessors.fromApplication(
        LocalContext.current.applicationContext,
        com.example.pokedex.di.NavTypeEntryPoint::class.java
    ).getPokemonListItemNavType()

    NavHost(navController, startDestination = PokedexRoutes.PokedexScreenRoute()) {
        pokedexNavigation(navController = navController, pokemonListItemNavType)
    }
}

