package com.example.pokedex.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.pokedex.database.entity.PokemonAsset
import com.example.pokedex.navigation.PokedexRoutes.PokemonInfoScreenRoute
import com.example.pokedex.navigation.navtypes.PokemonListItemNavType
import com.example.pokedex.screens.FavouritePokemonScreen
import com.example.pokedex.screens.PokedexScreen
import com.example.pokedex.screens.PokemonInfoScreen
import com.example.pokedex.utils.Constants

fun NavGraphBuilder.pokedexNavigation(
    navController: NavController,
    pokemonListItemNavType: PokemonListItemNavType
) {
    composable<PokedexRoutes.PokedexScreenRoute> {
        PokedexScreen(
            navController,
            pokemonClicked = {
            navController.navigate(PokemonInfoScreenRoute(it))
        })
    }

    composable<PokemonInfoScreenRoute>(
        typeMap = PokemonAsset.getType(pokemonListItemNavType)
    ) {
        PokemonInfoScreen(
            { navController.popBackStack() },
            pokemonAssetUpdated = { updatedAsset ->
                navController.previousBackStackEntry?.savedStateHandle?.set(Constants.UPDATED_POKEMON_ASSET, updatedAsset)
            })
    }

    composable<PokedexRoutes.FavouritePokemons> {
        FavouritePokemonScreen({
            navController.navigate(PokemonInfoScreenRoute(it))
        })
    }
}