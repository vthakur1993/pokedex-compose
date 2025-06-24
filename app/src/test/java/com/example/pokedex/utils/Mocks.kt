package com.example.pokedex.utils

import com.example.pokedex.database.entity.PokemonAsset
import com.example.pokedex.models.PokemonInfo
import com.example.pokedex.models.PokemonListItem
import com.example.pokedex.models.PokemonListResponse
import com.example.pokedex.network.PokedexApiService
import com.example.pokedex.repository.PokedexNetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Response

object Mocks {
    fun getPokemonAssetList(count: Int = 1): List<PokemonAsset> = buildList {
        repeat(count) {
            add(
                PokemonAsset(
                    page = it,
                    nameField = "PokemonListItem$it",
                    url = "https://pokeapi.co/api/v2/pokemon/$it"
                )
            )
        }
    }

    fun getPokemonInfo(): PokemonInfo {
        return PokemonInfo(
            id = 1,
            nameField = "Bulbasaur",
            height = 7,
            weight = 69,
            exp = 100,
            types = listOf(
                com.example.pokedex.models.PokemonType(
                    type = com.example.pokedex.models.Type(
                        nameField = "grass"
                    )
                ),
            ),
            stats = listOf(
                com.example.pokedex.models.PokemonStats(
                    baseStat = 45,
                    stat = com.example.pokedex.models.Stat(
                        nameField = "hp"
                    )
                )
            )
        )
    }

    fun getPokemonListItem(count: Int = 1): List<PokemonListItem> = buildList {
        repeat(count) {
            add(
                PokemonListItem(
                    nameField = "PokemonListItem$it",
                    url = "https://pokeapi.co/api/v2/pokemon/$it"
                )
            )
        }
    }

    fun getMockPokedexApiService(isValid: Boolean, shouldGiveEmptyList: Boolean) = object : PokedexApiService {
        override suspend fun fetchPokemonList(
            limit: Int,
            offset: Int
        ): Response<PokemonListResponse> {
            return if (isValid) {
                Response.success(PokemonListResponse(10, "next", "previous", if (shouldGiveEmptyList) emptyList() else getPokemonListItem()))
            } else {
                Response.error(404, ResponseBody.create(null, "Not Found"))
            }
        }

        override suspend fun fetchPokemonInfo(name: String): Response<PokemonInfo> {
            return if (isValid) {
                Response.success(getPokemonInfo())
            } else {
                Response.error(404, ResponseBody.create(null, "Not Found"))
            }
        }
    }

    fun getMockPokedexNetworkRepository(isValid: Boolean) = object : PokedexNetworkRepository {
        override suspend fun fetchPokemonList(
            page: Int,
            onStart: () -> Unit,
            onCompleted: () -> Unit,
            onError: (String) -> Unit
        ): Flow<List<PokemonListItem>> {
            return if (isValid) {
                flow { emit(getPokemonListItem()) }
            } else {
                onError("Custom Error")
                flow { emit(throw Exception("Custom Error")) }
                    .catch {
                        println("Do nothing")
                    }
            }
        }

        override suspend fun fetchPokemonInfo(
            name: String,
            onStart: () -> Unit,
            onCompleted: () -> Unit,
            onError: (String) -> Unit
        ): Flow<PokemonInfo> {
            return if (isValid) {
                flow { emit(getPokemonInfo()) }
            } else {
                onError("Custom Error")
                flow { emit(throw RuntimeException("Custom Error")) }
            }
        }

    }
}