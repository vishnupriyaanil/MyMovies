package com.circuithouse.mymovies.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.rememberNavController
import com.circuithouse.mymovies.ui.main.view.LocalNavController
import com.circuithouse.mymovies.ui.main.view.MainContent
import com.circuithouse.mymovies.ui.theme.MyMoviesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            MyMoviesTheme {
                CompositionLocalProvider(
                    LocalNavController provides navController,
                ) {
                    MainContent()
                }
            }
        }
    }
}