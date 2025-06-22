package com.example.pokedex.utils

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun Palette.getVerticalGradient(defaultColor: Color = Color.Gray): Flow<Brush> {
    return flow {
        var brush: Brush = Brush.linearGradient(colors = listOf(defaultColor, defaultColor))

            // Extract a dominant color (or vibrant, muted, etc.)
            val dominantSwatch = this@getVerticalGradient.dominantSwatch?.rgb?.let { colorInt ->
                Color(colorInt)
            }
            val lightSwatch = this@getVerticalGradient.lightVibrantSwatch?.rgb?.let { colorInt ->
                Color(colorInt)
            }
            dominantSwatch?.let { domainColor ->
                lightSwatch?.let { lightColor ->
                    val gradient = arrayOf(
                        0.0f to domainColor,
                        1f to lightColor,
                    )
                    brush = Brush.verticalGradient(colorStops = gradient)
                } ?: run {
                    brush = Brush.verticalGradient(colors = listOf(domainColor, domainColor))
                }
            }
        emit(brush)
    }
}