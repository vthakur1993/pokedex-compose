package com.example.pokedex.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pokedex.R
import com.example.pokedex.database.entity.PokemonAsset
import com.example.pokedex.utils.Constants
import com.example.pokedex.viewmodel.FavouritePokemonsState
import com.example.pokedex.viewmodel.FavouritePokemonsViewModel
import com.example.pokedex.viewmodel.FooterUIState

@Composable
fun FavouritePokemonScreen(
    pokemonClicked: (PokemonAsset) -> Unit,
    viewModel: FavouritePokemonsViewModel = hiltViewModel(),
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val state = viewModel.state.collectAsStateWithLifecycle()
        val pokemons = viewModel.pokemonListItems.collectAsStateWithLifecycle()

        when (state.value) {
            FavouritePokemonsState.Empty -> {
                EmptyFavouriteScreen(Constants.FAVOURITE_POKEMON_EMPTY_LIST)
            }

            FavouritePokemonsState.Success -> {
                PokedexList(
                    pokemons.value,
                    rememberLazyGridState(),
                    FooterUIState.EndReached,
                    pokemonClicked,
                )
            }
        }
    }
}

@Composable
fun EmptyFavouriteScreen(message: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 50.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ditto), // Replace with your WebP drawable ID
            contentDescription = "Ditto Error image",
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
    }
}