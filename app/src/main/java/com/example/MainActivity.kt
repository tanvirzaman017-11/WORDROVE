package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.WardrobeViewModel
import com.example.ui.components.AppBottomBar
import com.example.ui.navigation.Screen
import com.example.ui.screens.AddItemScreen
import com.example.ui.screens.EditItemScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ItemDetailsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StyleProfileScreen
import com.example.ui.screens.WardrobeScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ObsidianBlack

class MainActivity : ComponentActivity() {

    private val viewModel: WardrobeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                VanguardWardrobeApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun VanguardWardrobeApp(
    viewModel: WardrobeViewModel,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarRoutes = listOf(
        Screen.Home.route,
        Screen.Wardrobe.route,
        Screen.StyleProfile.route,
        Screen.Settings.route
    )

    val showBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBlack),
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                AppBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.Home.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.fillMaxSize()
        ) {
            // 1. Home Dashboard
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToWardrobe = {
                        navController.navigate(Screen.Wardrobe.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToAddItem = {
                        navController.navigate(Screen.AddItem.route)
                    },
                    onNavigateToProfile = {
                        navController.navigate(Screen.StyleProfile.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToItemDetails = { itemId ->
                        navController.navigate(Screen.ItemDetails.createRoute(itemId))
                    }
                )
            }

            // 2. Wardrobe Screen
            composable(Screen.Wardrobe.route) {
                WardrobeScreen(
                    viewModel = viewModel,
                    onNavigateToAddItem = {
                        navController.navigate(Screen.AddItem.route)
                    },
                    onNavigateToItemDetails = { itemId: Long ->
                        navController.navigate(Screen.ItemDetails.createRoute(itemId))
                    }
                )
            }

            // 3. Add Item Screen
            composable(Screen.AddItem.route) {
                AddItemScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onItemSaved = { savedId ->
                        navController.popBackStack()
                        navController.navigate(Screen.ItemDetails.createRoute(savedId))
                    }
                )
            }

            // 4. Item Details Screen
            composable(
                route = Screen.ItemDetails.route,
                arguments = listOf(navArgument("itemId") { type = NavType.LongType })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getLong("itemId") ?: 0L
                ItemDetailsScreen(
                    itemId = itemId,
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToEdit = { id ->
                        navController.navigate(Screen.EditItem.createRoute(id))
                    }
                )
            }

            // 5. Edit Item Screen
            composable(
                route = Screen.EditItem.route,
                arguments = listOf(navArgument("itemId") { type = NavType.LongType })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getLong("itemId") ?: 0L
                EditItemScreen(
                    itemId = itemId,
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // 6. Style Profile Screen
            composable(Screen.StyleProfile.route) {
                StyleProfileScreen(
                    viewModel = viewModel
                )
            }

            // 7. Settings Screen
            composable(Screen.Settings.route) {
                SettingsScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}
