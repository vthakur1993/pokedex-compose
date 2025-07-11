package com.example.pokedex.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokedex.database.converters.PokemonStatsConverter
import com.example.pokedex.database.converters.PokemonTypeConverter
import com.example.pokedex.database.entity.PokemonAsset
import com.example.pokedex.models.PokemonInfo

@Database(entities = [PokemonAsset::class, PokemonInfo::class],
    version = 4,
    autoMigrations = [AutoMigration(from = 2, to = 3)],
    exportSchema = true)
@TypeConverters(PokemonTypeConverter::class, PokemonStatsConverter::class)
abstract class PokemonDatabase: RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun pokemonInfoDao(): PokemonInfoDao
}