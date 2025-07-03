package com.example.pokedex.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokedex.models.PokemonInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonInfo(pokemonInfo: PokemonInfo)

    @Query("Select * from PokemonInfo where nameField = :name")
    fun getPokemonInfo(name: String): Flow<PokemonInfo?>

}