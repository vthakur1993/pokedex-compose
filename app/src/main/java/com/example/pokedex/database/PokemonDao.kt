package com.example.pokedex.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.pokedex.database.entity.PokemonAsset
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertPokemonList(pokemonList: List<PokemonAsset>)

    @Query("SELECT * FROM PokemonAsset WHERE page = :page")
    fun getPokemonList(page: Int): Flow<List<PokemonAsset>>

    @Query("SELECT * FROM PokemonAsset WHERE isFavourite = 1")
    fun getFavouritePokemonList(): Flow<List<PokemonAsset>>

    @Update(onConflict = REPLACE)
    suspend fun updatePokemonAsset(pokemonAsset: PokemonAsset)
}
