package com.example.pokedex.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.pokedex.database.PokemonDao
import com.example.pokedex.database.PokemonInfoDao
import com.example.pokedex.database.entity.PokemonAsset
import com.example.pokedex.models.PokemonInfo
import com.example.pokedex.models.TrainerInfo
import com.example.pokedex.navigation.PokedexRoutes.PokemonInfoScreenRoute
import com.example.pokedex.navigation.navtypes.PokemonListItemNavType
import com.example.pokedex.repository.PokedexNetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PokemonInfoViewmodel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val pokedexNetworkRepository: PokedexNetworkRepository,
    private val pokemonListItemNavType: PokemonListItemNavType,
    private val pokemonInfoDao: PokemonInfoDao,
    private val pokemonDao: PokemonDao,
): ViewModel() {

    private val _uiState = mutableStateOf<PokemonInfoState>(PokemonInfoState.Loading)
    val uiState: State<PokemonInfoState> = _uiState
    private var pokemonAsset: PokemonAsset = savedStateHandle.toRoute<PokemonInfoScreenRoute>(typeMap = PokemonAsset.getType(pokemonListItemNavType)).pokemonInfo

    init {
        viewModelScope.launch {
            val pokemonInfo = pokemonInfoDao.getPokemonInfo(pokemonAsset.nameField)
            pokemonInfo.flatMapLatest {
                if (it == null) {
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
                        emit(pokemonInfoDao.getPokemonInfo(name = it.nameField).first()!!)
                    }
                } else {
                    flow<PokemonInfo> {
                        emit(it)
                    }
                }
            }.collect { it ->
                _uiState.value = PokemonInfoState.Success(it, pokemonAsset.imageUrl)
            }
        }
    }

    fun isFavouriteClicked(value: Boolean, saveToBackStack: (PokemonAsset) -> Unit) {
        viewModelScope.launch {
            val stateValue = _uiState.value
            if (stateValue is PokemonInfoState.Success) {
                pokemonInfoDao.insertPokemonInfo(stateValue.pokemonInfo.copy(trainerInfo = stateValue.pokemonInfo.trainerInfo.copy(isFavourite = value)))
                val updatedPokemonAsset = pokemonAsset.copy(trainerInfo = TrainerInfo(value))
                pokemonDao.updatePokemonAsset(updatedPokemonAsset)
                saveToBackStack(updatedPokemonAsset)
            }
        }
    }

}

sealed interface PokemonInfoState {
    object Loading: PokemonInfoState
    data class Success(val pokemonInfo: PokemonInfo,val imageUrl: String): PokemonInfoState
    data class Error(val message: String): PokemonInfoState
}