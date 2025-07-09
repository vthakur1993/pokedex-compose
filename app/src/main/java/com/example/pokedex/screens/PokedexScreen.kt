package com.example.pokedex.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.palette.graphics.Palette
import coil3.BitmapImage
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import com.example.pokedex.R
import com.example.pokedex.database.entity.PokemonAsset
import com.example.pokedex.models.TrainerInfo
import com.example.pokedex.screens.widgets.PokemonListFooter
import com.example.pokedex.ui.theme.PokemonTheme
import com.example.pokedex.utils.Constants
import com.example.pokedex.utils.getVerticalGradient
import com.example.pokedex.viewmodel.FooterUIState
import com.example.pokedex.viewmodel.PokedexState
import com.example.pokedex.viewmodel.PokedexViewmodel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    pokemonClicked: (PokemonAsset) -> Unit,
    pokedexViewmodel: PokedexViewmodel = hiltViewModel()
) {
    val pokemons: State<PokedexState> = pokedexViewmodel.state.collectAsStateWithLifecycle()
    val pokemonsList = pokedexViewmodel.pokemonListItems.collectAsStateWithLifecycle()
    val footerUiState = pokedexViewmodel.footerUIState.collectAsStateWithLifecycle()

    val stateFlow =
        navController.currentBackStackEntry?.savedStateHandle?.getStateFlow<PokemonAsset?>(
            Constants.UPDATED_POKEMON_ASSET, null
        )?.onCompletion {
            navController.currentBackStackEntry?.savedStateHandle?.remove<PokemonAsset>(
                Constants.UPDATED_POKEMON_ASSET
            )
        }?.collectAsStateWithLifecycle(null)

    pokedexViewmodel.updateAsset(stateFlow?.value)

    PokedexScreen(
        modifier,
        innerPadding = PaddingValues(0.dp),
        pokemons.value,
        pokemonsList.value.toImmutableList(),
        footerUiState.value,
        { pokedexViewmodel.fetchMoreItems() },
        pokemonClicked
    )
}

@Composable
private fun PokedexScreen(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    state: PokedexState = PokedexState.Loading,
    pokemonsList: List<PokemonAsset>,
    footerUIState: FooterUIState,
    fetchMoreItems: () -> Unit,
    pokemonClicked: (PokemonAsset) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding), contentAlignment = Alignment.Center
    ) {
        val lazyListState = rememberLazyGridState()
        val reachedBottom by remember {
            derivedStateOf {
                val lastVisibleItem = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()
                val totalItems = lazyListState.layoutInfo.totalItemsCount

                // If there are no items, we can't be at the bottom
                if (totalItems == 0) return@derivedStateOf false

                // Check if the last visible item is close to the total count
                // You can adjust the threshold (e.g., 5 for 5 items before the end)
                val threshold = 5
                val isAtEnd =
                    lastVisibleItem != null && lastVisibleItem.index >= totalItems - 1 - threshold
                isAtEnd
            }
        }
        LaunchedEffect(reachedBottom) {
            if (reachedBottom) fetchMoreItems()
        }
        PokedexList(pokemonsList, lazyListState, footerUIState, pokemonClicked)

        if (state == PokedexState.Loading && pokemonsList.isEmpty()) {
            PokedexLoadingScreen()
        }

        if (state is PokedexState.Error && pokemonsList.isEmpty()) {
            PokedexErrorScreen(state.message)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexList(
    pokemonsList: List<PokemonAsset>,
    lazyListState: LazyGridState,
    footerUIState: FooterUIState,
    pokemonClicked: (PokemonAsset) -> Unit
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val state = rememberPullToRefreshState()

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(2000)
            isRefreshing = false
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            // state.startRefresh() // Usually not needed if isRefreshing drives it
        },
        state = state, // Pass the state
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            state = lazyListState
        ) {
            items(pokemonsList, key = { it.url }) { item ->
                val coroutineScope = rememberCoroutineScope()
                var dominantColor by remember(key1 = item.name) {
                    mutableStateOf(
                        Brush.linearGradient(
                            colors = listOf(Color.Gray, Color.Gray)
                        )
                    )
                }
                val shape = RoundedCornerShape(14.dp)
                Card(
                    modifier = Modifier.padding(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    shape = shape,
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    onClick = { pokemonClicked(item) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(brush = dominantColor, shape = shape)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp), // Padding inside the card content area
                            horizontalAlignment = Alignment.CenterHorizontally, // Center children horizontally
                            verticalArrangement = Arrangement.Center // Center children vertically if space allows
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(item.imageUrl).crossfade(true).allowHardware(false)
                                    .build(),
                                contentScale = ContentScale.Fit,
                                error = painterResource(R.drawable.ic_launcher_background),
                                onError = {
                                    it.result.throwable.printStackTrace()
                                },
                                onSuccess = { result ->
                                    //Check if the drawable is a BitmapDrawable
                                    val bitmap =
                                        (result.painter.intrinsicSize.width > 0 && result.painter.intrinsicSize.height > 0).let {
                                                if (result.result.image is BitmapImage) {
                                                    (result.result.image as BitmapImage).bitmap
                                                } else {
                                                    // Handle other drawable types if necessary, e.g., vector drawables
                                                    // You might need to draw them to a new bitmap
                                                    null
                                                }
                                            }
                                    bitmap?.let {
                                        Palette.from(it).generate { palette ->
                                            palette?.let {
                                                coroutineScope.launch {
                                                    palette.getVerticalGradient().collect {
                                                        dominantColor = it
                                                    }
                                                }
                                            }
                                        }
                                    }
                                },
                                placeholder = painterResource(R.drawable.ditto),
                                contentDescription = "Pokemon image",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape),
                            )
                            Text(
                                item.name,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(top = 8.dp),
                            )
                        }
                        if (item.isFavourite) {
                            Image(
                                painter = painterResource(id = R.drawable.star_rate_24px), // Replace with your WebP drawable ID
                                contentDescription = "Ditto image",
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(7.dp),
                                contentScale = ContentScale.Fit,
                                colorFilter = ColorFilter.tint(colorResource(R.color.electric))
                            )
                        }
                    }
                }

            }

            if (footerUIState != FooterUIState.EndReached && !pokemonsList.isEmpty()) {
                item(
                    span = {
                        // This is the key part: GridItemSpan(maxLineSpan) makes it span all columns
                        GridItemSpan(maxLineSpan)
                    }) {
                    PokemonListFooter(footerUIState)
                }
            }
        }
    }
}

@Composable
fun PokedexErrorScreen(message: String, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 50.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ditto), // Replace with your WebP drawable ID
            contentDescription = "Ditto Error image",
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = "Ditto tried to load this page... and failed!",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Preview
@Composable
fun PokedexScreenPreview() {
    PokemonTheme {
        val list: List<PokemonAsset> = List(20) {
            PokemonAsset(
                0, "Pokemonname: $it", "url: $it", trainerInfo = TrainerInfo(it % 2 == 0)
            )
        }
        PokedexList(list, rememberLazyGridState(), FooterUIState.EndReached, {})
    }
}

@Composable
fun PokedexLoadingScreen(modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator(
            modifier = modifier.padding(top = 32.dp),
            color = MaterialTheme.colorScheme.primary, // or any custom Color
            strokeWidth = 6.dp // Thicker line
        )
        Text(
            text = "Fetching more...",
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
    }
}