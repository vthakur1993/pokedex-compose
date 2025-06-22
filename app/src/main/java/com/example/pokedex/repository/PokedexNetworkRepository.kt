package com.example.pokedex.repository

import com.example.pokedex.models.PokemonInfo
import com.example.pokedex.models.PokemonListItem
import kotlinx.coroutines.flow.Flow

interface PokedexNetworkRepository {
    suspend fun fetchPokemonList(
        page: Int,
        onStart: () -> Unit,
        onCompleted: () -> Unit,
        onError: (String) -> Unit,
    ): Flow<List<PokemonListItem>>

    suspend fun fetchPokemonInfo(
        name: String,
        onStart: () -> Unit,
        onCompleted: () -> Unit,
        onError: (String) -> Unit,
    ): Flow<PokemonInfo>
}