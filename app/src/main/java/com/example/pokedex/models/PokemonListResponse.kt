package com.example.pokedex.models

import android.os.Parcelable
import com.example.pokedex.navigation.navtypes.PokemonListItemNavType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@JsonClass(generateAdapter = true)
@Serializable
data class PokemonListResponse(
    val count: Int,
    val next: String? = null,
    val previous: String? = null,
    val results: List<PokemonListItem> = emptyList()
)

@JsonClass(generateAdapter = true)
@Serializable
@Parcelize
data class PokemonListItem(
    @field:Json(name = "name")
    val nameField: String,
    val url: String
): Parcelable {
    val name: String
        get() = nameField.replaceFirstChar { it.uppercase() }

    val imageUrl: String
        inline get() {
            val index = url.split("/".toRegex()).dropLast(1).last()
            return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/" +
                    "pokemon/other/official-artwork/$index.png"
        }

    companion object {
        fun getType(pokemonListItemNavType: PokemonListItemNavType): Map<KType, PokemonListItemNavType> {
            return mapOf(typeOf<PokemonListItem>() to pokemonListItemNavType)
        }
    }
}