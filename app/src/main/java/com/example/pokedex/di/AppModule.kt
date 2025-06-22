package com.example.pokedex.di

import com.example.pokedex.navigation.navtypes.PokemonListItemNavType
import com.example.pokedex.repository.PokedexNetworkRepository
import com.example.pokedex.repository.PokedexNetworkRepositoryImpl
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun gson() = Gson()

    @Provides
    fun getPokemonListItemNavType(gson: Gson): PokemonListItemNavType {
        return PokemonListItemNavType(gson)
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface NavTypeEntryPoint {
    fun gson(): Gson
    fun getPokemonListItemNavType(): PokemonListItemNavType
}

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @CoroutineDispatchers(PokedexAppDispatchers.IO)
    @Provides
    fun providePokedexCoroutineDispatcher() = Dispatchers.IO

}

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {
    @Binds
    fun bindPokedexNetworkRepository(pokedexNetworkRepositoryImpl: PokedexNetworkRepositoryImpl): PokedexNetworkRepository
}