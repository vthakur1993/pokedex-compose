package com.example.pokedex.utils

import com.example.pokedex.models.PokemonInfo

object Mocks {
    fun getPokemonInfo(): PokemonInfo {
        return PokemonInfo(
            id = 1,
            nameField = "Bulbasaur",
            height = 7,
            weight = 69,
            exp = 100,
            types = listOf(
                com.example.pokedex.models.PokemonType(
                    type = com.example.pokedex.models.Type(
                        nameField = "grass"
                    )
                ),
            ),
            stats = listOf(
                com.example.pokedex.models.PokemonStats(
                    baseStat = 45,
                    stat = com.example.pokedex.models.Stat(
                        nameField = "hp"
                    )
                )
            )
        )
    }
}