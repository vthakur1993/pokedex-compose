package com.example.pokedex.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.pokedex.R

@Composable
fun getPokemonTypeColor(type: String): Color {
    return when (type) {
        "fighting" -> colorResource(R.color.fighting)
        "flying" -> colorResource(R.color.flying)
        "poison" -> colorResource(R.color.poison)
        "ground" -> colorResource(R.color.ground)
        "rock" -> colorResource(R.color.rock)
        "bug" -> colorResource(R.color.bug)
        "ghost" -> colorResource(R.color.ghost)
        "steel" -> colorResource(R.color.steel)
        "fire" -> colorResource(R.color.fire)
        "water" -> colorResource(R.color.water)
        "grass" -> colorResource(R.color.grass)
        "electric" -> colorResource(R.color.electric)
        "psychic" -> colorResource(R.color.psychic)
        "ice" -> colorResource(R.color.ice)
        "dragon" -> colorResource(R.color.dragon)
        "fairy" -> colorResource(R.color.fairy)
        "dark" -> colorResource(R.color.dark)
        else -> colorResource(R.color.gray_21)
    }
}