package com.example.pokedex.network

import com.example.pokedex.models.PokemonInfo
import com.example.pokedex.models.PokemonListResponse
import com.example.pokedex.network.Constants.PAGE_SIZE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface PokedexApiService {
    @GET("pokemon")
    suspend fun fetchPokemonList(
        @Query("limit") limit: Int = PAGE_SIZE,
        @Query("offset") offset: Int = 0,
    ): Response<PokemonListResponse>

    @GET("pokemon/{name}")
    suspend fun fetchPokemonInfo(@Path("name") name: String): Response<PokemonInfo>
}