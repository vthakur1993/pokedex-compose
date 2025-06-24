package com.example.pokedex

import com.example.pokedex.repository.PokedexNetworkRepositoryImpl
import com.example.pokedex.utils.Mocks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test


class PokedexNetworkRepositoryTest {

    lateinit var pokedexNetworkRepository: PokedexNetworkRepositoryImpl

    @Test
    fun test_fetchPokemonList_valid() = runTest {
        pokedexNetworkRepository = PokedexNetworkRepositoryImpl(
            pokedexApi = Mocks.getMockPokedexApiService(true, false),
            coroutineDispatcher = Dispatchers.IO
        )
        var result = pokedexNetworkRepository.fetchPokemonList(0, {}, {}, {}).first()
        Assert.assertEquals(1, result.size)

        pokedexNetworkRepository = PokedexNetworkRepositoryImpl(
            pokedexApi = Mocks.getMockPokedexApiService(true, true),
            coroutineDispatcher = Dispatchers.IO
        )

        result = pokedexNetworkRepository.fetchPokemonList(0, {}, {}, {}).first()
        Assert.assertEquals(0, result.size)
    }

    @Test
    fun test_fetchPokemonList_invalid() = runTest {
        pokedexNetworkRepository = PokedexNetworkRepositoryImpl(
            pokedexApi = Mocks.getMockPokedexApiService(false, false),
            coroutineDispatcher = Dispatchers.IO
        )
        pokedexNetworkRepository.fetchPokemonList(0, {}, {}, {
            Assert.assertNotNull(it)
        }).firstOrNull()
    }

    @Test
    fun test_fetchPokemonInfo_valid() = runTest {
        pokedexNetworkRepository = PokedexNetworkRepositoryImpl(
            pokedexApi = Mocks.getMockPokedexApiService(true, false),
            coroutineDispatcher = Dispatchers.IO
        )
        var result = pokedexNetworkRepository.fetchPokemonInfo("Bulbasaur", {}, {}, {})
        Assert.assertNotNull(result)
    }

    @Test
    fun test_fetchPokemonInfo_invalid() = runTest {
        pokedexNetworkRepository = PokedexNetworkRepositoryImpl(
            pokedexApi = Mocks.getMockPokedexApiService(false, false),
            coroutineDispatcher = Dispatchers.IO
        )
        pokedexNetworkRepository.fetchPokemonInfo("Bulbasaur", {}, {}, {
            Assert.assertNotNull(it)
        }).firstOrNull()
    }
}