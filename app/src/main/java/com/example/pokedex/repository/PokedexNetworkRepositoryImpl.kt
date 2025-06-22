package com.example.pokedex.repository

import com.example.pokedex.di.CoroutineDispatchers
import com.example.pokedex.di.PokedexAppDispatchers
import com.example.pokedex.models.PokemonInfo
import com.example.pokedex.network.Constants.PAGE_SIZE
import com.example.pokedex.network.PokedexApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class PokedexNetworkRepositoryImpl @Inject constructor(
    val pokedexApi: PokedexApiService,
    @CoroutineDispatchers(PokedexAppDispatchers.IO) val coroutineDispatcher: CoroutineDispatcher
): PokedexNetworkRepository {

    override suspend fun fetchPokemonList(
        page: Int,
        onStart: () -> Unit,
        onCompleted: () -> Unit,
        onError: (String) -> Unit,
    ) = flow {
        val fetchPokemonList = pokedexApi.fetchPokemonList(offset = page * PAGE_SIZE)
        if (fetchPokemonList.isSuccessful) {
            emit(fetchPokemonList.body()!!.results)
        }
    }
        .catch {
            it.printStackTrace()
        onError(it.message ?: "Something went wrong")
        }
        .onStart {
            onStart()
        }
        .onCompletion {
            onCompleted()
        }
        .flowOn(coroutineDispatcher)

    override suspend fun fetchPokemonInfo(
        name: String,
        onStart: () -> Unit,
        onCompleted: () -> Unit,
        onError: (String) -> Unit
    ): Flow<PokemonInfo> = flow {
        val response = pokedexApi.fetchPokemonInfo(name)
        if (response.isSuccessful) {
            emit(response.body()!!)
        }
    }.catch {
        it.printStackTrace()
        onError(it.message ?: "Something went wrong")
    }.onStart {
        onStart()
    }.onCompletion {
        onCompleted()
    }.flowOn(coroutineDispatcher)

}