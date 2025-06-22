package com.example.pokedex.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokemonAsset(
    var page: Int = 0,
    @PrimaryKey val name: String,
    val url: String,
)