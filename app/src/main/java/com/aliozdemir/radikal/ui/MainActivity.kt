package com.aliozdemir.radikal.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aliozdemir.radikal.navigation.BottomNavigationBar
import com.aliozdemir.radikal.navigation.NavigationGraph
import com.aliozdemir.radikal.navigation.Screen.Bookmark
import com.aliozdemir.radikal.navigation.Screen.Home
import com.aliozdemir.radikal.navigation.Screen.Search
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
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val startDestination = Home

                val bottomBarScreens = listOf(Home, Search, Bookmark)

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val shouldShowBottomBar =
                            currentDestination?.route != null && bottomBarScreens.any { it.routeName == currentDestination.route }

                        if (shouldShowBottomBar) {
                            BottomNavigationBar(
                                navController = navController,
                                currentDestination = currentDestination,
                            )
                        }
                    }) { innerPadding ->
                    NavigationGraph(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding)
                            .safeDrawingPadding(),
                    )
                }
            }
        }
    }
}