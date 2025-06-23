package com.example.pokedex.navigation.navtypes

import android.net.Uri
import android.os.Build
import androidx.navigation.NavType
import androidx.savedstate.SavedState
import com.example.pokedex.database.entity.PokemonAsset
import com.google.gson.Gson
import javax.inject.Inject

class PokemonListItemNavType @Inject constructor(val gson: Gson): NavType<PokemonAsset>(
    isNullableAllowed = true
) {
    override fun put(
        bundle: SavedState,
        key: String,
        value: PokemonAsset
    ) {
        bundle.putParcelable(key, value)
    }

    override fun get(
        bundle: SavedState,
        key: String
    ): PokemonAsset? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, PokemonAsset::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): PokemonAsset {
        return gson.fromJson(value, PokemonAsset::class.java)
    }

    override fun serializeAsValue(value: PokemonAsset): String {
        return Uri.encode(gson.toJson(value))
    }

}