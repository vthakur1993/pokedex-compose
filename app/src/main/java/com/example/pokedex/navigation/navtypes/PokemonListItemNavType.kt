package com.example.pokedex.navigation.navtypes

import android.net.Uri
import android.os.Build
import androidx.navigation.NavType
import androidx.savedstate.SavedState
import com.example.pokedex.di.AppModule.gson
import com.example.pokedex.models.PokemonListItem
import com.google.gson.Gson
import com.squareup.moshi.Json
import javax.inject.Inject
import kotlin.jvm.java

class PokemonListItemNavType @Inject constructor(val gson: Gson): NavType<PokemonListItem>(
    isNullableAllowed = true
) {
    override fun put(
        bundle: SavedState,
        key: String,
        value: PokemonListItem
    ) {
        bundle.putParcelable(key, value)
    }

    override fun get(
        bundle: SavedState,
        key: String
    ): PokemonListItem? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, PokemonListItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): PokemonListItem {
        return gson.fromJson(value, PokemonListItem::class.java)
    }

    override fun serializeAsValue(value: PokemonListItem): String {
        return Uri.encode(gson.toJson(value))
    }

}