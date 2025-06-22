package com.example.pokedex.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.pokedex.models.PokemonListItem
import com.example.pokedex.navigation.PokedexRoutes.PokemonInfoScreenRoute
import com.example.pokedex.navigation.navtypes.PokemonListItemNavType
import com.example.pokedex.screens.PokedexScreen
import com.example.pokedex.screens.PokemonInfoScreen

fun NavGraphBuilder.pokedexNavigation(
    navController: NavController,
    pokemonListItemNavType: PokemonListItemNavType
) {
    composable<PokedexRoutes.PokedexScreenRoute> {
        PokedexScreen(pokemonClicked = {
            navController.navigate(PokemonInfoScreenRoute(it))
        })
    }

    composable<PokemonInfoScreenRoute>(
        typeMap = PokemonListItem.getType(pokemonListItemNavType)
    ) {
        PokemonInfoScreen({
            navController.popBackStack()
        })
    }
}