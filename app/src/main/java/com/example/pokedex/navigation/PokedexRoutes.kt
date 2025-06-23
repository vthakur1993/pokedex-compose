package com.example.pokedex.navigation

import com.example.pokedex.database.entity.PokemonAsset
import kotlinx.serialization.Serializable

sealed interface PokedexRoutes {
    @Serializable
    data object PokedexScreenRoute: PokedexRoutes

    @Serializable
    data class PokemonInfoScreenRoute(val pokemonInfo: PokemonAsset): PokedexRoutes
}
