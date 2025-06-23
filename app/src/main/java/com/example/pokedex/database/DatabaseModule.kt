package com.example.pokedex.database

import android.content.Context
import androidx.room.Room
import com.example.pokedex.database.converters.PokemonStatsConverter
import com.example.pokedex.database.converters.PokemonTypeConverter
import com.example.pokedex.database.migrations.MIGRATION_1_2
import com.google.gson.Gson
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
    fun database(
        @ApplicationContext context: Context,
        pokemonStatsConverter: PokemonStatsConverter,
        pokemonTypeConverter: PokemonTypeConverter
    ): PokemonDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PokemonDatabase::class.java,
            "pokedex-database"
        )
            .addTypeConverter(pokemonStatsConverter)
            .addTypeConverter(pokemonTypeConverter)
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Singleton
    @Provides
    fun pokemonDao(database: PokemonDatabase): PokemonDao {
        return database.pokemonDao()
    }

    @Singleton
    @Provides
    fun pokemonTypeConverter(gson: Gson): PokemonTypeConverter {
        return PokemonTypeConverter(gson)
    }

    @Singleton
    @Provides
    fun pokemonStatsConverter(gson: Gson): PokemonStatsConverter {
        return PokemonStatsConverter(gson)
    }

    @Singleton
    @Provides
    fun pokemonInfoDao(database: PokemonDatabase): PokemonInfoDao {
        return database.pokemonInfoDao()
    }
}