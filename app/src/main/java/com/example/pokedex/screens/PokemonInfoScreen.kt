package com.example.pokedex.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.palette.graphics.Palette
import coil3.BitmapImage
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import com.example.pokedex.R
import com.example.pokedex.database.entity.PokemonAsset
import com.example.pokedex.models.PokemonInfo
import com.example.pokedex.models.PokemonStats
import com.example.pokedex.models.PokemonType
import com.example.pokedex.models.Stat
import com.example.pokedex.models.Type
import com.example.pokedex.screens.widgets.PokemonStatsUI
import com.example.pokedex.utils.getPokemonTypeColor
import com.example.pokedex.utils.getVerticalGradient
import com.example.pokedex.viewmodel.PokemonInfoState
import com.example.pokedex.viewmodel.PokemonInfoViewmodel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonInfoScreen(backClicked: () -> Unit, pokemonAssetUpdated: (PokemonAsset) -> Unit) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val pokemonInfoViewmodel: PokemonInfoViewmodel = hiltViewModel()
            val uiState = remember { pokemonInfoViewmodel.uiState }
            val scrollState = rememberScrollState()
            val uiStateValue = uiState.value
            val isFavouriteClicked: (Boolean) -> Unit = { it ->  pokemonInfoViewmodel.isFavouriteClicked(it, pokemonAssetUpdated) }
            when (uiStateValue) {
                is PokemonInfoState.Error -> {
                    PokemonInfoErrorScreen(backClicked, uiStateValue)
                }

                PokemonInfoState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center) {
                        PokedexLoadingScreen()
                    }
                }

                is PokemonInfoState.Success -> {
                    PokemonDetail(uiStateValue.pokemonInfo, uiStateValue.imageUrl, scrollState, backClicked, isFavouriteClicked)
                }
            }
        }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PokemonInfoErrorScreen(
    backClicked: () -> Unit,
    uiStateValue: PokemonInfoState.Error
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pokedex") },
                modifier = Modifier.background(MaterialTheme.colorScheme.primary),
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow),
                        tint = Color.White,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable { backClicked() }
                    )
                },
                colors = TopAppBarColors(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.onPrimary,
                    MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            PokedexErrorScreen(uiStateValue.message)
        }
    }
}

@Composable
fun PokemonDetail(
    info: PokemonInfo,
    imageUrl: String,
    scrollState: ScrollState,
    backClicked: () -> Unit,
    isFavouriteClicked: (Boolean) -> Unit
) {
    Column(Modifier
        .fillMaxSize()
        .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        PokemonHeader(imageUrl, info.isFavourite, backClicked, isFavouriteClicked)
        PokemonTypeUIWithName(info)
        PokemonStatsUI(info)
    }
}

@Composable
fun PokemonTypeUIWithName(info: PokemonInfo) {
    Column(Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(text = info.name,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold
            )

        WeightHeightUI(info)

        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.CenterHorizontally),
        ) {
            info.types.forEach {
                Text(
                    modifier = Modifier
                        .background(
                            getPokemonTypeColor(it.type.nameField),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 26.dp, vertical = 4.dp),
                    text = it.type.name,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    maxLines = 1,
                    fontSize = 16.sp,
                )
            }
        }
    }

}

@Composable
fun WeightHeightUI(info: PokemonInfo) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(60.dp, alignment = Alignment.CenterHorizontally),
    ) {
        LabelValueColumn("Weight", info.getWeightString())
        LabelValueColumn("Height", info.getHeightString())
    }
}

@Composable
fun LabelValueColumn(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
        )
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}


@Composable
fun PokemonHeader(imageUrl: String, isFavourite: Boolean, backClicked: () -> Unit, isFavouriteClicked: (Boolean) -> Unit) {
    val shape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 64.dp,
        bottomEnd = 64.dp,
    )
    val coroutineScope = rememberCoroutineScope()
    var dominantColor by remember { mutableStateOf(Brush.linearGradient(colors = listOf(Color.Gray, Color.Gray))) }
    var isAnimatingFavourite by remember { mutableStateOf(false) }

    val iconScale by animateFloatAsState(
        targetValue = if (isAnimatingFavourite) 1.3f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        finishedListener = {
            if (isAnimatingFavourite) isAnimatingFavourite = false
        }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(290.dp)
            .shadow(9.dp, shape)
            .background(dominantColor, shape)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .statusBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Key part
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 6.dp)
                    .clickable { backClicked() },
                painter = painterResource(id = R.drawable.ic_arrow),
                tint = Color.White,
                contentDescription = null,
            )
            Crossfade(
                targetState = isFavourite,
                animationSpec = tween(durationMillis = 300),
                label = "FavoriteIconCrossfade"
            ) { currentlyFavourite ->
                Icon(
                    painter = if (currentlyFavourite) painterResource(R.drawable.star_rate_24px) else painterResource(R.drawable.star_24px),
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .scale(iconScale)
                        .toggleable(
                            value = isFavourite,
                            enabled = true,
                            onValueChange = {
                                isAnimatingFavourite = true
                                isFavouriteClicked(it)
                            }
                        ),
                    tint = colorResource(R.color.electric),
                    contentDescription = null,
                )
            }

        }

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .allowHardware(false)
                .build(),
            contentScale = ContentScale.FillBounds,
            error = painterResource(R.drawable.ic_launcher_background),
            onError = {
                it.result.throwable.printStackTrace()
            },
            onSuccess = { result ->
                // Check if the drawable is a BitmapDrawable
                val bitmap = (result.painter.intrinsicSize.width > 0 && result.painter.intrinsicSize.height > 0)
                    .let {
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
                .align(Alignment.BottomCenter)
                .size(250.dp)
                .padding(bottom = 20.dp)

        )
    }
}

@Preview
@Composable
fun PokemonHeaderPreview() {
    PokemonHeader("", false, {}, {})
}

@Preview
@Composable
fun PokemonDetailPreview() {
    val info = PokemonInfo(1, "Bulbasaur", 20, 20,300, listOf(
        PokemonType(Type("grass")),
        PokemonType(Type("poison"))
    ), listOf(
        PokemonStats(100, Stat("hp")),
        PokemonStats(150, Stat("attack")),
        PokemonStats(50, Stat("defense")),
    ))
    PokemonDetail(info, "", rememberScrollState(), { }, { })
}

@Preview
@Composable
fun PokemonTypeUIPreview() {
    val info = PokemonInfo(1, "Bulbasaur", 20, 20,300, listOf(
        PokemonType(Type("grass")),
        PokemonType(Type("poison"))
    ), listOf(
        PokemonStats(45, Stat("hp")),
        PokemonStats(49, Stat("attack")),
        PokemonStats(49, Stat("defense")),
    ))
    PokemonTypeUIWithName(info)
}