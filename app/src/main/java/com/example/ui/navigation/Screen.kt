package com.example.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Wardrobe : Screen("wardrobe")
    object AddItem : Screen("add_item")
    object EditItem : Screen("edit_item/{itemId}") {
        fun createRoute(itemId: Long) = "edit_item/$itemId"
    }
    object ItemDetails : Screen("item_details/{itemId}") {
        fun createRoute(itemId: Long) = "item_details/$itemId"
    }
    object StyleProfile : Screen("style_profile")
    object Settings : Screen("settings")
}
