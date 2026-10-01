package com.minidany.stockapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.minidany.stockapp.ui.screens.ForgotPasswordScreen
import com.minidany.stockapp.ui.screens.LoginScreen
import com.minidany.stockapp.ui.screens.RegisterScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            LoginScreen(
                onRegisterClick = {
                    navController.navigate("register")
                },
                onForgotPasswordClick = {
                    navController.navigate("forgot_password")
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
    }
}