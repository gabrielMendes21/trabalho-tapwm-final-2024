package com.example.gabitaskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.gabitaskapp.ui.theme.RomaFerrariAppTheme
import com.example.gabitaskapp.ui.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RomaFerrariAppTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}