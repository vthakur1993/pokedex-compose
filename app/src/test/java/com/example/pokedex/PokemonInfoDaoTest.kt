package com.example.pokedex

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.pokedex.database.PokemonDatabase
import com.example.pokedex.database.PokemonInfoDao
import com.example.pokedex.database.converters.PokemonStatsConverter
import com.example.pokedex.database.converters.PokemonTypeConverter
import com.example.pokedex.utils.Mocks
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class) // Use Robolectric for local Android environment
class PokemonInfoDaoTest {

    private lateinit var db: PokemonDatabase
    private lateinit var pokemonDao: PokemonInfoDao
    @Before
    fun setup() {
        val gson = Gson()
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, PokemonDatabase::class.java)
            .addTypeConverter(PokemonStatsConverter(gson)) // Add your custom type converter
            .addTypeConverter(PokemonTypeConverter(gson)) // Add your custom type converter
            .allowMainThreadQueries() // Allow database operations on the main thread for simplicity in tests
            .build()
        pokemonDao = db.pokemonInfoDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertAndGetPokemonInfo() = runBlocking {
        val info = Mocks.getPokemonInfo()
        pokemonDao.insertPokemonInfo(info)

        val retrievedInfo = pokemonDao.getPokemonInfo(info.nameField)
        assert(retrievedInfo != null)
        assert(retrievedInfo == info)
    }
    @Test
    fun updatePokemonDetails() = runBlocking {
        var info = Mocks.getPokemonInfo()
        pokemonDao.insertPokemonInfo(info)

        info = info.copy(
            id = 5
        )
        pokemonDao.insertPokemonInfo(info)
        val retrievedInfo = pokemonDao.getPokemonInfo(info.nameField)
        assert(retrievedInfo != null)
        assert(retrievedInfo?.id == 5)
    }

    @Test
    fun getPokemonDetails_nonExistent() = runBlocking {
        val retrievedInfo = pokemonDao.getPokemonInfo("Vipul")
        assert(retrievedInfo == null)
    }
}