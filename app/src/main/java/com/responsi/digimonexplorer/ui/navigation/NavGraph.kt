package com.responsi.digimonexplorer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.responsi.digimonexplorer.ui.screens.detail.DetailScreen
import com.responsi.digimonexplorer.ui.screens.home.HomeScreen

/**
 * Konfigurasi Jetpack Compose Navigation
 * Aplikasi maksimal memiliki 2 screens:
 * 1. Home Screen
 * 2. Digimon Detail Screen
 */
@Composable
fun DigimonNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Screen 1: Home Screen
        composable(route = Screen.Home.route) {
            HomeScreen(
                onDigimonClick = { digimonId ->
                    navController.navigate(Screen.Detail.createRoute(digimonId))
                }
            )
        }

        // Screen 2: Digimon Detail Screen
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("digimonId") {
                    type = NavType.IntType
                    defaultValue = 1
                }
            )
        ) { backStackEntry ->
            val digimonId = backStackEntry.arguments?.getInt("digimonId") ?: 1
            DetailScreen(
                digimonId = digimonId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
