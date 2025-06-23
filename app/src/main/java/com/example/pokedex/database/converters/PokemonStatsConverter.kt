package com.example.pokedex.database.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.pokedex.models.PokemonStats
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class PokemonStatsConverter(private val gson: Gson) {
    @TypeConverter
    fun fromPokemonStats(stats: List<PokemonStats>?): String? {
        return gson.toJson(stats)
    }

    @TypeConverter
    fun toPokemonStats(statsJson: String?): List<PokemonStats>? {
        // Handle nulls if your JSON can be null
        if (statsJson == null) {
            return null
        }
        val type = object : TypeToken<List<PokemonStats>>() {}.type
        return gson.fromJson(statsJson, type)
    }
}