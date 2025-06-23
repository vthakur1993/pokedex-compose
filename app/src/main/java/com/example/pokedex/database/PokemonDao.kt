package com.example.pokedex.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.pokedex.database.entity.PokemonAsset
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertPokemonList(pokemonList: List<PokemonAsset>)

    @Query("SELECT * FROM PokemonAsset WHERE page = :page")
    fun getPokemonList(page: Int): Flow<List<PokemonAsset>>

}
