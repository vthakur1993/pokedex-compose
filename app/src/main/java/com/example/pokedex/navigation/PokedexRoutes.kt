package com.example.pokedex.navigation

import com.example.pokedex.R
import com.example.pokedex.database.entity.PokemonAsset
import com.example.pokedex.utils.Constants.FAVOURITE_POKEMON_SCREEN_NAME
import com.example.pokedex.utils.Constants.POKEDEX_SCREEN_NAME
import kotlinx.serialization.Serializable

@Serializable
sealed class PokedexRoutes(
    val name: String = "",
    val icon: Int = R.drawable.ditto,
    val selectedIcon: Int = R.drawable.ditto
) {

    @Serializable
    data class PokedexScreenRoute(
        val screeName: String = POKEDEX_SCREEN_NAME,
        val screenIcon: Int = R.drawable.ditto,
        val selectedScreenIcon: Int = R.drawable.ditto,
    ): PokedexRoutes(screeName, screenIcon, selectedScreenIcon)

    @Serializable
    data class PokemonInfoScreenRoute(val pokemonInfo: PokemonAsset): PokedexRoutes()

    @Serializable
    data class FavouritePokemons(
        val screeName: String = FAVOURITE_POKEMON_SCREEN_NAME,
        val screenIcon: Int = R.drawable.star_24px,
        val selectedScreenIcon: Int = R.drawable.star_rate_24px,
    ): PokedexRoutes(screeName, screenIcon, selectedScreenIcon)

}
