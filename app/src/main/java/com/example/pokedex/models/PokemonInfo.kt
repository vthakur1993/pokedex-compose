package com.example.pokedex.models

import androidx.annotation.ColorRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pokedex.R
import com.example.pokedex.models.PokemonInfo.Companion.MAX_HP
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@Entity
@JsonClass(generateAdapter = true)
data class PokemonInfo(
    val id: Int,
    @PrimaryKey
    @field:Json(name = "name")
    val nameField: String,
    val height: Int,
    val weight: Int,
    @field:Json(name = "base_experience")
    val exp: Int,
    val types: List<PokemonType>,
    val stats: List<PokemonStats>,
) {
    val name: String
        get() = nameField.replaceFirstChar { it.uppercase() }

    val hp: Int
        get() = stats.find { it.stat.nameField == "hp" }?.baseStat ?: 0
    val attack: Int
        get() = stats.find { it.stat.nameField == "attack" }?.baseStat ?: 0
    val speed: Int
        get() = stats.find { it.stat.nameField == "speed" }?.baseStat ?: 0
    val defense: Int
        get() = stats.find { it.stat.nameField == "defense" }?.baseStat ?: 0

    fun getIdString(): String = String.format("#%03d", id)
    fun getWeightString(): String = String.format("%.1f KG", weight.toFloat() / 10)
    fun getHeightString(): String = String.format("%.1f M", height.toFloat() / 10)
    fun getHpString(): String = "$hp/$MAX_HP"
    fun getAttackString(): String = "$attack/$MAX_ATTACK"
    fun getDefenseString(): String = "$defense/$MAX_DEFENSE"
    fun getSpeedString(): String = "$speed/$MAX_SPEED"
    fun getExpString(): String = "$exp/$MAX_EXP"

    companion object {
        const val MAX_HP = 300
        const val MAX_ATTACK = 300
        const val MAX_DEFENSE = 300
        const val MAX_SPEED = 300
        const val MAX_EXP = 1000
    }
}

fun PokemonInfo.getPokemonStatsForUI(): List<PokemonStatsForUI> {
    return listOf(
        PokemonStatsForUI("HP", hp, MAX_HP, getHpString(), R.color.bug, R.color.bug),
        PokemonStatsForUI("ATK", attack, PokemonInfo.MAX_ATTACK, getAttackString(), R.color.fire, R.color.fire),
        PokemonStatsForUI("SPD", speed, PokemonInfo.MAX_SPEED, getSpeedString(), R.color.water, R.color.water),
        PokemonStatsForUI("DEF", defense, PokemonInfo.MAX_DEFENSE, getDefenseString(), R.color.ground, R.color.ground),
        PokemonStatsForUI("EXP", exp, PokemonInfo.MAX_EXP, getExpString(), R.color.teal_700, R.color.teal_200),
    )
}

data class PokemonStatsForUI(
    val label: String,
    val value: Int,
    val maxValue: Int,
    val progressText: String,
    @ColorRes val labelColor: Int = R.color.white,
    @ColorRes val shadowColor: Int = R.color.white,
)


@Serializable
@JsonClass(generateAdapter = true)
data class PokemonType(
    val type: Type,
)

@Serializable
@JsonClass(generateAdapter = true)
data class Type(
    @field:Json(name = "name")
    val nameField: String
) {
    val name: String
        get() = nameField.replaceFirstChar { it.uppercase() }
}

@Serializable
@JsonClass(generateAdapter = true)
data class PokemonStats(
    @field:Json(name = "base_stat")
    val baseStat: Int,
    val stat: Stat,
)

@Serializable
@JsonClass(generateAdapter = true)
data class Stat(
    @field:Json(name = "name")
    val nameField: String
) {
    val name: String
        get() = nameField.replaceFirstChar { it.uppercase() }
}
