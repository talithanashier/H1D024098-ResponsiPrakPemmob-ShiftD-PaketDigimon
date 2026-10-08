package com.responsi.digimonexplorer.ui.navigation

/**
 * Representasi rute navigasi aplikasi (maksimal 2 screen sesuai spesifikasi)
 */
sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object Detail : Screen("detail_screen/{digimonId}") {
        fun createRoute(digimonId: Int): String = "detail_screen/$digimonId"
    }
}
