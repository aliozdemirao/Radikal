package com.aliozdemir.radikal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.aliozdemir.radikal.domain.model.Article
import com.aliozdemir.radikal.navigation.Screen.Bookmark
import com.aliozdemir.radikal.navigation.Screen.Detail
import com.aliozdemir.radikal.navigation.Screen.Home
import com.aliozdemir.radikal.navigation.Screen.Search
import com.aliozdemir.radikal.ui.bookmark.BookmarkScreen
import com.aliozdemir.radikal.ui.detail.DetailScreen
import com.aliozdemir.radikal.ui.detail.DetailViewModel
import com.aliozdemir.radikal.ui.home.HomeScreen
import com.aliozdemir.radikal.ui.home.HomeViewModel
import com.aliozdemir.radikal.ui.search.SearchScreen
import kotlin.reflect.typeOf

@Composable
fun NavigationGraph(
    navController: NavHostController,
    startDestination: Screen,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable<Home> {
            val viewModel: HomeViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            HomeScreen(
                uiState = uiState,
                onAction = viewModel::handleUiAction,
                uiEffect = uiEffect,
                onArticleClick = { article -> navController.navigate(Detail(article)) }
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
        composable<Detail> (
            typeMap = mapOf(
                typeOf<Article>() to CustomNavType.ArticleType
            )
        ) { backStackEntry ->
            val args = requireNotNull(backStackEntry.toRoute<Detail>())
            val viewModel: DetailViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            DetailScreen(
                article = args.article,
                uiState = uiState,
                onAction = viewModel::handleUiAction,
                uiEffect = uiEffect,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}