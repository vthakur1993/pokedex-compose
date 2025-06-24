package com.example.pokedex

import com.example.pokedex.database.PokemonDao
import com.example.pokedex.repository.PokedexNetworkRepository
import com.example.pokedex.utils.Mocks
import com.example.pokedex.viewmodel.FooterUIState
import com.example.pokedex.viewmodel.PokedexState
import com.example.pokedex.viewmodel.PokedexViewmodel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class PokedexViewmodelTest {

    val pokemonDao = mock<PokemonDao>()
    val pokedexNetworkRepository = mock<PokedexNetworkRepository>()

    lateinit var pokedexViewmodel: PokedexViewmodel

    // The TestDispatcher. StandardTestDispatcher gives more control.
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        // Set the main dispatcher to your test dispatcher BEFORE the ViewModel is created
        // This ensures viewModelScope uses your testDispatcher.
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        // Reset the main dispatcher after the test
        Dispatchers.resetMain()
    }

    @Test
    fun `Viewmodel init returns list from database`() = runTest(testDispatcher) {
        whenever(pokemonDao.getPokemonList(0)).thenReturn(flow { emit(Mocks.getPokemonAssetList()) })
        //whenever(pokemonDao.getPokemonList(0).first()).thenReturn(Mocks.getPokemonAssetList())

        pokedexViewmodel = PokedexViewmodel(
            pokedexNetworkRepository,
            pokemonDao
        )

        advanceUntilIdle()

        val first = withTimeoutOrNull(2000) {
            pokedexViewmodel.state.first {
                it is PokedexState.Success
            }
        }
        val list = withTimeoutOrNull(2000) {
            pokedexViewmodel.pokemonListItems.first()
        }
        val footerState = withTimeoutOrNull(2000) {
            pokedexViewmodel.footerUIState.first() {
                it is FooterUIState.Loading
            }
        }
        Assert.assertEquals(PokedexState.Success, first)
        Assert.assertTrue(list?.isNotEmpty() == true)
        Assert.assertEquals(footerState, FooterUIState.Loading)
    }

    @Test
    fun `Viewmodel init database returns empty list`() = runTest(testDispatcher) {
        whenever(pokemonDao.getPokemonList(0))
            .thenReturn(flow { emit(emptyList()) })
            .thenReturn(flow { emit(Mocks.getPokemonAssetList()) })

        whenever(pokedexNetworkRepository.fetchPokemonList(
            page = eq(0),
            onStart = any(),
            onCompleted = any(),
            onError = any()
        )).thenReturn(
            flow { emit(Mocks.getPokemonListItem()) }
        )

        pokedexViewmodel = PokedexViewmodel(
            pokedexNetworkRepository,
            pokemonDao
        )

        advanceUntilIdle()

        val first = withTimeoutOrNull(2000) {
            pokedexViewmodel.state.first {
                it is PokedexState.Success
            }
        }
        val list = withTimeoutOrNull(2000) {
            pokedexViewmodel.pokemonListItems.first()
        }
        Assert.assertEquals(PokedexState.Success, first)
        Assert.assertTrue(list?.isNotEmpty() == true)

        verify(pokemonDao, times(2)).getPokemonList(eq(0))
        verify(pokedexNetworkRepository, times(1)).fetchPokemonList(
            page = eq(0),
            onStart = any(),
            onCompleted = any(),
            onError = any()
        )
        verify(pokemonDao, times(1)).insertPokemonList(eq(list!!))
    }

    @Test
    fun `viewmodel init returns error`() = runTest {
        whenever(pokemonDao.getPokemonList(0))
            .thenReturn(flow { emit(emptyList()) })

        pokedexViewmodel = PokedexViewmodel(
            Mocks.getMockPokedexNetworkRepository(false),
            pokemonDao
        )

        advanceUntilIdle()
        val first = withTimeoutOrNull(2000) {
            pokedexViewmodel.state.first {
                it is PokedexState.Error
            }
        }
        val list = withTimeoutOrNull(2000) {
            pokedexViewmodel.pokemonListItems.first()
        }
        val footerState = withTimeoutOrNull(2000) {
            pokedexViewmodel.footerUIState.first() {
                it is FooterUIState.Error
            }
        }
        Assert.assertEquals(PokedexState.Error("Custom Error"), first)
        Assert.assertEquals((footerState as FooterUIState.Error).msg, FooterUIState.Error("Custom Error").msg)
        Assert.assertTrue(list?.isEmpty() == true)

    }

    @Test
    fun `viewmodel fetchMoreItems`() = runTest {
        whenever(pokemonDao.getPokemonList(0))
            .thenReturn(flow { emit(Mocks.getPokemonAssetList()) })
        whenever(pokemonDao.getPokemonList(1))
            .thenReturn(flow { emit(Mocks.getPokemonAssetList()) })

        pokedexViewmodel = PokedexViewmodel(
            pokedexNetworkRepository,
            pokemonDao
        )

        advanceUntilIdle()
        pokedexViewmodel.fetchMoreItems()
        advanceUntilIdle()

        val first = withTimeoutOrNull(2000) {
            pokedexViewmodel.state.first {
                it is PokedexState.Success
            }
        }
        val list = withTimeoutOrNull(2000) {
            pokedexViewmodel.pokemonListItems.first()
        }
        Assert.assertEquals(PokedexState.Success, first)
        Assert.assertTrue(list?.isNotEmpty() == true)
        Assert.assertTrue(list?.size == 2)

    }

}