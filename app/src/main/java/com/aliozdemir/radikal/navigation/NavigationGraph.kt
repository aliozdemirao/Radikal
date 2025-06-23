package com.aliozdemir.radikal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aliozdemir.radikal.navigation.Screen.Bookmark
import com.aliozdemir.radikal.navigation.Screen.Detail
import com.aliozdemir.radikal.navigation.Screen.Home
import com.aliozdemir.radikal.navigation.Screen.Search
import com.aliozdemir.radikal.ui.bookmark.BookmarkScreen
import com.aliozdemir.radikal.ui.detail.DetailScreen
import com.aliozdemir.radikal.ui.home.HomeScreen
import com.aliozdemir.radikal.ui.search.SearchScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    startDestination: Screen,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<Home> {
            HomeScreen(

            )
        }
        composable<Search> {
            SearchScreen(

            )
        }
        composable<Bookmark> {
            BookmarkScreen(

            )
        }
        composable<Detail> {
            DetailScreen(

            )
        }
    }
}