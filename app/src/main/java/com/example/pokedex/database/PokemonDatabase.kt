package com.example.pokedex.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pokedex.database.entity.PokemonAsset

@Database(entities = [PokemonAsset::class], version = 1)
abstract class PokemonDatabase: RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}