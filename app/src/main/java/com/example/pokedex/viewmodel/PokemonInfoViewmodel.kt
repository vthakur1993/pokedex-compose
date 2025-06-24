package com.example.pokedex.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.pokedex.database.PokemonInfoDao
import com.example.pokedex.database.entity.PokemonAsset
import com.example.pokedex.models.PokemonInfo
import com.example.pokedex.navigation.PokedexRoutes.PokemonInfoScreenRoute
import com.example.pokedex.navigation.navtypes.PokemonListItemNavType
import com.example.pokedex.repository.PokedexNetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonInfoViewmodel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val pokedexNetworkRepository: PokedexNetworkRepository,
    val pokemonListItemNavType: PokemonListItemNavType,
    val pokemonInfoDao: PokemonInfoDao
): ViewModel() {
    private val _uiState = mutableStateOf<PokemonInfoState>(PokemonInfoState.Loading)
    val uiState: State<PokemonInfoState> = _uiState

    init {
        viewModelScope.launch {
            val pokemonAsset = savedStateHandle.toRoute<PokemonInfoScreenRoute>(typeMap = PokemonAsset.getType(pokemonListItemNavType)).pokemonInfo
            val pokemonInfo = pokemonInfoDao.getPokemonInfo(pokemonAsset.nameField)
            if (pokemonInfo == null) {
                pokedexNetworkRepository.fetchPokemonInfo(
                    pokemonAsset.name,
                    onStart = {
                        _uiState.value = PokemonInfoState.Loading
                    },
                    onCompleted = {},
                    onError = {
                        _uiState.value = PokemonInfoState.Error(it)
                    }
                ).transform {
                    pokemonInfoDao.insertPokemonInfo(it)
                    emit(pokemonInfoDao.getPokemonInfo(name = it.nameField))
                }.collect {
                    it?.let {
                        _uiState.value = PokemonInfoState.Success(it, pokemonAsset.imageUrl)
                    } ?: run {
                        _uiState.value = PokemonInfoState.Error("Something wrong with Database")
                    }
                }
            } else {
                _uiState.value = PokemonInfoState.Success(pokemonInfo, pokemonAsset.imageUrl)
            }
        }
    }

}

sealed interface PokemonInfoState {
    object Loading: PokemonInfoState
    data class Success(val pokemonInfo: PokemonInfo,val imageUrl: String): PokemonInfoState
    data class Error(val message: String): PokemonInfoState
}