package com.anderson.fitoconsulta.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.anderson.fitoconsulta.screens.about.AboutScreen
import com.anderson.fitoconsulta.screens.detail.PlantDetailScreen
import com.anderson.fitoconsulta.screens.home.HomeScreen

object Routes {
    const val HOME = "home"
    const val DETAIL = "detail/{plantId}"
    const val ABOUT = "about"
    fun getDetailRoute(plantId: Long) = "detail/$plantId"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onPlantClick = { plantId ->
                    navController.navigate(Routes.getDetailRoute(plantId))
                },
                onAboutClick = {
                    navController.navigate(Routes.ABOUT)
                }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("plantId") { type = NavType.LongType })
        ) {
            PlantDetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Routes.ABOUT) {
            AboutScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}