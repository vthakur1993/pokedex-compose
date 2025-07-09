package com.example.pokedex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.database.PokemonDao
import com.example.pokedex.database.entity.PokemonAsset
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritePokemonsViewModel@Inject constructor(
    private val pokemonDao: PokemonDao,
): ViewModel() {

    private val _state: MutableStateFlow<FavouritePokemonsState> = MutableStateFlow(FavouritePokemonsState.Empty)
    val state: StateFlow<FavouritePokemonsState> = _state.asStateFlow()

    private val _pokemonListItems = MutableStateFlow<List<PokemonAsset>>(emptyList())
    val pokemonListItems = _pokemonListItems.asStateFlow()


    init {
        viewModelScope.launch {
            pokemonDao.getFavouritePokemonList().collect {
                if (it.isEmpty()) {
                    _state.value = FavouritePokemonsState.Empty
                } else {
                    _pokemonListItems.update { current ->
                        it
                    }
                    _state.value = FavouritePokemonsState.Success
                }
            }
        }
    }

}

sealed interface FavouritePokemonsState {
    object Success: FavouritePokemonsState
    object Empty: FavouritePokemonsState
}