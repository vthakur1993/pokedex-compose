package com.example.pokedex.models

import android.os.Parcelable
import com.example.pokedex.database.entity.PokemonAsset
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

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
}

fun PokemonListItem.toAsset(page: Int): PokemonAsset {
    return PokemonAsset(
        page = page,
        nameField = nameField,
        url = url
    )
}