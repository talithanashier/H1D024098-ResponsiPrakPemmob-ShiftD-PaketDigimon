package com.responsi.digimonexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.responsi.digimonexplorer.ui.navigation.DigimonNavGraph
import com.responsi.digimonexplorer.ui.theme.DigimonTheme

/**
 * Entry point utama aplikasi Android.
 * Menggunakan 100% Jetpack Compose (NO XML Layout).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DigimonTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    DigimonNavGraph(navController = navController)
                }
            }
        }
    }
}
