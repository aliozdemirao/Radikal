package com.aliozdemir.radikal.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.aliozdemir.radikal.navigation.NavigationGraph
import com.aliozdemir.radikal.navigation.Screen.Home
import com.aliozdemir.radikal.ui.theme.RadikalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RadikalTheme {
                val navController = rememberNavController()
                val startDestination = Home

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationGraph(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}