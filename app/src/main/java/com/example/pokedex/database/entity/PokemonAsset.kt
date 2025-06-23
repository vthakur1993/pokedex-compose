package com.example.pokedex.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pokedex.navigation.navtypes.PokemonListItemNavType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@JsonClass(generateAdapter = true)
@Serializable
@Entity
@Parcelize
data class PokemonAsset(
    var page: Int = 0,
    @field:Json(name = "name")
    @PrimaryKey val nameField: String,
    val url: String,
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
            return mapOf(typeOf<PokemonAsset>() to pokemonListItemNavType)
        }
    }
}