package com.minidany.stockapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.minidany.stockapp.ui.screens.AddProductScreen
import com.minidany.stockapp.ui.screens.ForgotPasswordScreen
import com.minidany.stockapp.ui.screens.LoginScreen
import com.minidany.stockapp.ui.screens.RegisterScreen
import com.minidany.stockapp.ui.screens.HomeScreen
import com.minidany.stockapp.ui.screens.InventoryScreen
import com.minidany.stockapp.ui.screens.ProfileScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {

        composable("login") {
            LoginScreen(
                onRegisterClick = {
                    navController.navigate("register")
                },
                onForgotPasswordClick = {
                    navController.navigate("forgot_password")
                },
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("forgot_password") {
            ForgotPasswordScreen(
                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("home") {
            HomeScreen(
                onProfileClick = {
                    navController.navigate("profile")
                },
                onNavigateInventory = {
                    navController.navigate("inventory")
                },
                onNavigateMovimientos = {
                    navController.navigate("movements")
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLogOutClick = {
                    navController.navigate("login") {
                        popUpTo("home") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable("inventory") {
            InventoryScreen (
                onNavigationHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateMovimientos = {
                    navController.navigate("movements")
                },
                onNavigateToProductDetail = { productId ->
                    navController.navigate("product_detail/$productId")
                },
                onNavigateToAddProduct = {
                    navController.navigate("add_product")
                }
            )
        }

        composable("movements") {
            PlaceHolderScreen("Movimientos")
        }

        composable("add_product") {
            AddProductScreen (
                onBack ={
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "product_detail/{productId}",
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val productId =
                backStackEntry.arguments?.getString("productId").orEmpty()

            PlaceHolderScreen("Detalles del producto $productId")
        }
    }
}

@Composable
private fun PlaceHolderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}