package com.example.pokedex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.database.PokemonDao
import com.example.pokedex.database.entity.PokemonAsset
import com.example.pokedex.models.PokemonListItem
import com.example.pokedex.repository.PokedexNetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PokedexViewmodel @Inject constructor(
    pokedexNetworkRepository: PokedexNetworkRepository,
    pokemonDao: PokemonDao,
): ViewModel() {
    private val _state: MutableStateFlow<PokedexState> = MutableStateFlow(PokedexState.Loading)
    val state: StateFlow<PokedexState> = _state.asStateFlow()
    private val _pokemonListItems = MutableStateFlow<List<PokemonAsset>>(emptyList())
    val pokemonListItems = _pokemonListItems.asStateFlow()

    var currentPageIndex = MutableStateFlow(0)

    init {
        viewModelScope.launch {
            currentPageIndex.flatMapLatest {
                val pokemonList = pokemonDao.getPokemonList(it)
                if (pokemonList.isEmpty()) {
                    pokedexNetworkRepository.fetchPokemonList(
                        page = it,
                        onStart = {
                            if (it == 0) _state.value = PokedexState.Loading
                        },
                        onCompleted = {
                            println("Vipul onCompleted")
                        },
                        onError = {
                            _state.value = PokedexState.Error(it)
                        }
                    )
                }
            }.collect {
                _pokemonListItems.update { currentList ->
                    currentList + it
                }
                _state.value = PokedexState.Success
            }
        }
    }

    fun fetchMoreItems() {
        currentPageIndex.value += 1
    }
}

sealed interface PokedexState {
    object Loading: PokedexState
    object Success: PokedexState
    data class Error(val message: String): PokedexState
}