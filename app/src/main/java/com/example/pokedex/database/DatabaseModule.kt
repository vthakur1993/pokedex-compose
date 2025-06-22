package com.example.pokedex.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun database(@ApplicationContext context: Context): PokemonDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PokemonDatabase::class.java,
            "pokedex-database"
        ).build()
    }

    @Singleton
    @Provides
    fun pokemonDao(database: PokemonDatabase): PokemonDao {
        return database.pokemonDao()
    }
}