package com.example.pokedex.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.pokedex.models.PokemonInfo
import com.example.pokedex.models.PokemonListItem
import com.example.pokedex.navigation.PokedexRoutes.PokemonInfoScreenRoute
import com.example.pokedex.navigation.navtypes.PokemonListItemNavType
import com.example.pokedex.repository.PokedexNetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonInfoViewmodel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val pokedexNetworkRepository: PokedexNetworkRepository,
    val pokemonListItemNavType: PokemonListItemNavType
): ViewModel() {
    private val _uiState = mutableStateOf<PokemonInfoState>(PokemonInfoState.Loading)
    val uiState: State<PokemonInfoState> = _uiState

    init {
        viewModelScope.launch {
            val pokemonInfo = savedStateHandle.toRoute<PokemonInfoScreenRoute>(typeMap = PokemonListItem.getType(pokemonListItemNavType)).pokemonInfo
            pokedexNetworkRepository.fetchPokemonInfo(
                pokemonInfo.name,
                onStart = {
                    _uiState.value = PokemonInfoState.Loading
                          },
                onCompleted = {},
                onError = {
                    _uiState.value = PokemonInfoState.Error(it)
                }
            ).collect {
                println("Vipulpre info :: $it")
                _uiState.value = PokemonInfoState.Success(it, pokemonInfo.imageUrl)
            }
        }
    }

}

sealed interface PokemonInfoState {
    object Loading: PokemonInfoState
    data class Success(val pokemonInfo: PokemonInfo,val imageUrl: String): PokemonInfoState
    data class Error(val message: String): PokemonInfoState
}