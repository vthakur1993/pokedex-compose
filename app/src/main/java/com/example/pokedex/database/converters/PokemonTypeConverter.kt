package com.example.pokedex.database.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.pokedex.models.PokemonType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class PokemonTypeConverter(val gson: Gson) {

    @TypeConverter
    fun fromPokemonType(type: List<PokemonType>?): String? {
        return gson.toJson(type)
    }

    @TypeConverter
    fun toPokemonType(typeJson: String?): List<PokemonType>? {
        // Handle nulls if your JSON can be null
        if (typeJson == null) {
            return null
        }
        val type = object : TypeToken<List<PokemonType>>() {}.type
        return gson.fromJson(typeJson, type)
    }
}