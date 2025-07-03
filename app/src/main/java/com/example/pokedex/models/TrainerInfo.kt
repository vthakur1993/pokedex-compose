package com.example.pokedex.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
@JsonClass(generateAdapter = true)
data class TrainerInfo(val isFavourite: Boolean): Parcelable
