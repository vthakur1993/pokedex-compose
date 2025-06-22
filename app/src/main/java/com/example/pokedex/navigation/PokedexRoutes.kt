package com.example.pokedex.navigation

import com.example.pokedex.models.PokemonListItem
import kotlinx.serialization.Serializable

sealed interface PokedexRoutes {
    @Serializable
    data object PokedexScreenRoute: PokedexRoutes

    @Serializable
    data class PokemonInfoScreenRoute(val pokemonInfo: PokemonListItem): PokedexRoutes
}
