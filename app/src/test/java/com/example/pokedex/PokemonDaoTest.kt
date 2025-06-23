package com.example.pokedex

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.pokedex.database.PokemonDao
import com.example.pokedex.database.PokemonDatabase
import com.example.pokedex.database.converters.PokemonStatsConverter
import com.example.pokedex.database.converters.PokemonTypeConverter
import com.example.pokedex.database.entity.PokemonAsset
import com.example.pokedex.utils.Mocks
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class) // Use Robolectric for local Android environment
class PokemonDaoTest {

    private lateinit var db: PokemonDatabase
    private lateinit var pokemonDao: PokemonDao

    @Before
    fun setup() {
        val gson = Gson()
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, PokemonDatabase::class.java)
            .addTypeConverter(PokemonStatsConverter(gson)) // Add your custom type converter
            .addTypeConverter(PokemonTypeConverter(gson)) // Add your custom type converter
            .allowMainThreadQueries() // Allow database operations on the main thread for simplicity in tests
            .build()
        pokemonDao = db.pokemonDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertAndGetPokemonAssets() = runBlocking {
        val pokemonInfoList = Mocks.getPokemonAssetList(2)
        pokemonDao.insertPokemonList(pokemonInfoList)

        var pokemonAssetsFromDb = pokemonDao.getPokemonList(0).first()
        assert(pokemonAssetsFromDb.size == 1)
        assert(pokemonAssetsFromDb[0].nameField == "PokemonAsset0")

        pokemonAssetsFromDb = pokemonDao.getPokemonList(1).first()
        assert(pokemonAssetsFromDb.size == 1)
        assert(pokemonAssetsFromDb[0].nameField == "PokemonAsset1")
    }

    @Test
    fun updatePokemonDetails() = runBlocking {
        val pokemonInfoList = Mocks.getPokemonAssetList()
        pokemonDao.insertPokemonList(pokemonInfoList)

        val updatedItem = listOf<PokemonAsset>(pokemonInfoList.first().copy(
            page = 12
        ))
        pokemonDao.insertPokemonList(updatedItem)

        var pokemonAssetsFromDb = pokemonDao.getPokemonList(0).first()
        assert(pokemonAssetsFromDb.isEmpty())

        pokemonAssetsFromDb = pokemonDao.getPokemonList(12).first()
        assert(pokemonAssetsFromDb.size == 1)
        assert(pokemonAssetsFromDb.first().page == 12)
        assert(pokemonAssetsFromDb.first().name == "PokemonAsset0")
    }

    @Test
    fun getPokemonDetails_nonExistent() = runBlocking {
        val retrievedPokemon = pokemonDao.getPokemonList(999).first()
        assert(retrievedPokemon.isEmpty())
    }

}