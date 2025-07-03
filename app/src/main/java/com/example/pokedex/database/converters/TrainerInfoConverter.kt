package com.example.pokedex.database.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.pokedex.models.TrainerInfo
import com.google.gson.Gson

@ProvidedTypeConverter
class TrainerInfoConverter(val gson: Gson) {

    @TypeConverter
    fun fromTrainerInfo(trainerInfo: TrainerInfo?): String? {
        return gson.toJson(trainerInfo)
    }

    @TypeConverter
    fun toTrainerInfo(trainerInfoJson: String?): TrainerInfo? {
        return trainerInfoJson?.let {
            gson.fromJson(it, TrainerInfo::class.java)
        } ?: run {
            null
        }
    }
}