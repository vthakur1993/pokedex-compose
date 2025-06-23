package com.example.pokedex.screens.widgets

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.models.PokemonInfo
import com.example.pokedex.models.PokemonStats
import com.example.pokedex.models.PokemonStatsForUI
import com.example.pokedex.models.PokemonType
import com.example.pokedex.models.Stat
import com.example.pokedex.models.Type
import com.example.pokedex.models.getPokemonStatsForUI

@Composable
fun PokemonStatsUI(info: PokemonInfo) {
    Column(Modifier.fillMaxWidth().padding(end = 10.dp, start = 10.dp, top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        info.getPokemonStatsForUI().forEach {
            PokemonStatusItem(it)
        }
    }
}

@Composable
fun PokemonStatusItem(pokemonStatsForUI: PokemonStatsForUI, modifier: Modifier = Modifier) {
    val roundedShape = RoundedCornerShape(8.dp)
    Row(
        modifier
            .fillMaxWidth()
            .height(20.dp),
        verticalAlignment = Alignment.CenterVertically
        ) {
        Text(text = pokemonStatsForUI.label,
            modifier = Modifier
                .width(40.dp),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 18.sp,
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false // Also available here
                ),
                fontWeight = FontWeight.SemiBold
            ),
            color = colorResource(pokemonStatsForUI.labelColor)
        )
        val progressWidth: Float = (pokemonStatsForUI.value.toFloat() / pokemonStatsForUI.maxValue.toFloat())
        val animatableProgress = remember { Animatable(initialValue = 0f) }
        LaunchedEffect(Unit) {
            animatableProgress.animateTo(progressWidth, animationSpec = tween(durationMillis = 950, easing = LinearOutSlowInEasing))
        }
        val isInner = progressWidth > 0.2f

        Box(Modifier
            .padding(start = 10.dp)
            .fillMaxSize()
            .shadow(
                elevation = 6.dp, // Higher elevation for a larger, more visible shadow
                shape = roundedShape, // Match the box's corner radius
                ambientColor = colorResource(pokemonStatsForUI.shadowColor), // Red color for diffused shadow
                spotColor = colorResource(pokemonStatsForUI.shadowColor) // Red color for direct shadow
            )
            .clip(roundedShape) // Clip the entire box to rounded corners
            .background(Color.White) // Set the background color inside the box
            .border(width = 1.dp, color = colorResource(pokemonStatsForUI.shadowColor), shape = roundedShape), // Add the stroke
        ) {
            Row(Modifier
                .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier
                    .fillMaxWidth(animatableProgress.value)
                    .fillMaxHeight()
                    .background(colorResource(pokemonStatsForUI.labelColor), shape = roundedShape),
                    contentAlignment = Alignment.CenterEnd) {
                    if (isInner) {
                        Text(
                            pokemonStatsForUI.progressText,
                            modifier = Modifier
                                .clip(roundedShape)
                                .wrapContentWidth()
                                .padding(start = 5.dp, end = 5.dp),
                            color = Color.White,
                            style = TextStyle(
                                fontSize = 12.sp,
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false // Also available here
                                )
                            )
                        )
                    }
                }
                if (!isInner) {
                    Text(
                        pokemonStatsForUI.progressText,
                        modifier = Modifier
                            .clip(roundedShape)
                            .wrapContentWidth()
                            .padding(start = 5.dp, end = 5.dp),
                        color = Color.Black,
                        style = TextStyle(
                            fontSize = 12.sp,
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false // Also available here
                            )
                        )
                    )
                }
            }

        }
    }
}

@Preview
@Composable
fun ProgressWithAnimatedTextAdvancedPreview() {
    val info = PokemonInfo(1, "Bulbasaur", 20, 20, 300, listOf(
        PokemonType(Type("grass")),
        PokemonType(Type("poison"))
    ), listOf(
        PokemonStats(100, Stat("hp")),
        PokemonStats(100, Stat("attack")),
        PokemonStats(100, Stat("defense")),
    ))
    PokemonStatsUI(info)
}