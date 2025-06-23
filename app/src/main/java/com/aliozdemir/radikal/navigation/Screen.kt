package com.aliozdemir.radikal.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Home : Screen

    @Serializable
    data object Search : Screen

    @Serializable
    data object Bookmark : Screen

    @Serializable
    data object Detail : Screen
}