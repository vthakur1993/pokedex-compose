package com.example.pokedex.di

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class CoroutineDispatchers(val dispatcher: PokedexAppDispatchers)

enum class PokedexAppDispatchers {
    IO,
}
