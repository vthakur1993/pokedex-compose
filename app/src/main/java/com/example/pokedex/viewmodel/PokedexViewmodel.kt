package com.example.pokedex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.database.PokemonDao
import com.example.pokedex.database.entity.PokemonAsset
import com.example.pokedex.models.toAsset
import com.example.pokedex.repository.PokedexNetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PokedexViewmodel @Inject constructor(
    private val pokedexNetworkRepository: PokedexNetworkRepository,
    private val pokemonDao: PokemonDao,
): ViewModel() {
    private val _state: MutableStateFlow<PokedexState> = MutableStateFlow(PokedexState.Loading)
    val state: StateFlow<PokedexState> = _state.asStateFlow()

    private val _pokemonListItems = MutableStateFlow<List<PokemonAsset>>(emptyList())
    val pokemonListItems = _pokemonListItems.asStateFlow()

    private val _footerUIState: MutableStateFlow<FooterUIState> = MutableStateFlow(FooterUIState.Loading)
    val footerUIState = _footerUIState.asStateFlow()

    private val _startOperation = MutableStateFlow(0)
    private var currentPageIndex = 0

    init {
        viewModelScope.launch {
            _startOperation.flatMapLatest {
                val pokemonList = pokemonDao.getPokemonList(currentPageIndex).first()
                if (pokemonList.isEmpty()) {
                    pokedexNetworkRepository.fetchPokemonList(
                        page = currentPageIndex,
                        onStart = {
                            _state.value = PokedexState.Loading
                            _footerUIState.value = FooterUIState.Loading
                        },
                        onCompleted = {

                        },
                        onError = {
                             _state.value = PokedexState.Error(it)
                            _footerUIState.value = FooterUIState.Error(it)
                            currentPageIndex--
                        }
                    )
                        .flatMapLatest { list ->
                            val map = list.map { it.toAsset(currentPageIndex) }
                            pokemonDao.insertPokemonList(map)
                            pokemonDao.getPokemonList(page = currentPageIndex)
                        }
                } else {
                    flow { emit(pokemonList) }
                }
            }.collect { it: List<PokemonAsset> ->
                _pokemonListItems.update { pokemonList ->
                    pokemonList + it
                }
                _state.value = PokedexState.Success
            }
        }
    }

    private fun fetchPokemons() {
        _startOperation.value++
    }

    fun fetchMoreItems() {
        if (_state.value == PokedexState.Loading) return
        currentPageIndex++
        fetchPokemons()
    }
}

sealed interface PokedexState {
    object Loading: PokedexState
    object Success: PokedexState
    data class Error(val message: String): PokedexState
}

sealed interface FooterUIState {
    object Loading: FooterUIState
    object EndReached: FooterUIState
    class Error(val msg: String): FooterUIState
}