package com.example.pokedex

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.navigation.toRoute
import androidx.test.core.app.ApplicationProvider
import com.example.pokedex.navigation.PokedexNavHost
import com.example.pokedex.navigation.PokedexRoutes
import com.example.pokedex.navigation.PokedexRoutes.PokemonInfoScreenRoute
import com.example.pokedex.utils.Mocks
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
//@RunWith(AndroidJUnit4::class)
@RunWith(RobolectricTestRunner::class)
//@Config(application = HiltTestApplication::class)
@Config(application = HiltTestApplication::class)
class PokedexNavigationTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    // Use TestNavHostController
    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        hiltRule.inject()

        // Initialize TestNavHostController
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        navController.navigatorProvider.addNavigator(ComposeNavigator())

        composeTestRule.activity.setContent {
            PokedexNavHost(navController)
        }
    }

    @Test
    fun pokedexNavHost_verifyStartDestination() {
        // Check that the current route is the pokedex screen
        Assert.assertEquals(
            PokedexRoutes.PokedexScreenRoute::class.qualifiedName, // Assuming PokedexScreenRoute is type-safe
            navController.currentBackStackEntry?.destination?.route
        )
    }

    @Test
    fun pokedexNavHost_verifyPokemonInfoScreenDestination() {
        val pokemonAsset = Mocks.getPokemonAssetList().first()
        navController.navigate(PokemonInfoScreenRoute(pokemonAsset))
        Assert.assertTrue(
            navController.currentDestination?.route?.startsWith(PokemonInfoScreenRoute::class.qualifiedName!!) == true
        )
        val retrievedRouteArgs: PokedexRoutes.PokemonInfoScreenRoute = try {
            navController.currentBackStackEntry!!.toRoute() // KClass is inferred
        } catch (e: Exception) {
            Assert.fail("Failed to convert NavBackStackEntry to PokedexRoutes.PokemonInfoScreenRoute: ${e.message}")
            throw e // Will not be reached
        }
        Assert.assertNotNull(retrievedRouteArgs.pokemonInfo)
        Assert.assertEquals(retrievedRouteArgs.pokemonInfo, pokemonAsset)
    }


}