package com.example.pokedex.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pokedex.database.entity.PokemonAsset

@Dao
interface PokemonDao {

    @Insert
    suspend fun insertPokemonList(pokemonList: List<PokemonAsset>)

    @Query
    ("SELECT * FROM PokemonAsset WHERE page = :page")
    suspend fun getPokemonList(page: Int): List<PokemonAsset>


}
