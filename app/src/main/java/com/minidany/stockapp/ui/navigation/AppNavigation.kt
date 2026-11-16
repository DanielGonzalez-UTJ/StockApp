
package com.minidany.stockapp.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.minidany.stockapp.data.local.SessionManager
import com.minidany.stockapp.data.remote.CambiarClaveRequest
import com.minidany.stockapp.data.remote.RetrofitClient
import com.minidany.stockapp.ui.screens.AddProductScreen
import com.minidany.stockapp.ui.screens.ChangePasswordScreen
import com.minidany.stockapp.ui.screens.ForgotPasswordScreen
import com.minidany.stockapp.ui.screens.HomeScreen
import com.minidany.stockapp.ui.screens.InventoryScreen
import com.minidany.stockapp.ui.screens.LoginScreen
import com.minidany.stockapp.ui.screens.MovementsScreen
import com.minidany.stockapp.ui.screens.ProductDetailScreen
import com.minidany.stockapp.ui.screens.ProfileScreen
import com.minidany.stockapp.ui.screens.SplashScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val coroutineScope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = modifier
    ) {

        composable(
            route = "login",
            enterTransition = {
                fadeIn(animationSpec = tween(durationMillis = 900))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(durationMillis = 300))
            }
        ) {
            LoginScreen(
                onForgotPasswordClick = {
                    navController.navigate("forgot_password")
                },
                onLoginSuccess = { debeCambiarClave ->
                    val destino = if (debeCambiarClave) {
                        "change_password"
                    } else {
                        "home"
                    }

                    navController.navigate(destino) {
                        popUpTo("login") {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = "splash",
            enterTransition = {
                fadeIn(animationSpec = tween(durationMillis = 500))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(durationMillis = 900))
            }
        ) {
            SplashScreen(
                onSessionValid = {
                    navController.navigate("home") {
                        popUpTo("splash") {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onSessionInvalid = {
                    navController.navigate("login") {
                        popUpTo("splash") {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
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

        composable("change_password") {
            ChangePasswordScreen(
                onChangePassword = { nuevaPassword ->
                    coroutineScope.launch {
                        try {
                            val token = sessionManager.obtenerToken()

                            if (token.isNullOrBlank()) {
                                sessionManager.cerrarSesion()
                                navController.navigate("login") {
                                    popUpTo("change_password") {
                                        inclusive = true
                                    }
                                }
                                return@launch
                            }

                            val response = RetrofitClient.api.cambiarClave(
                                token = "Bearer $token",
                                datos = CambiarClaveRequest(
                                    nueva_password = nuevaPassword
                                )
                            )

                            val resultado = response.body()

                            if (response.isSuccessful &&
                                resultado?.success == true
                            ) {
                                navController.navigate("home") {
                                    popUpTo("change_password") {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
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

            var nombre = remember { mutableStateOf("Cargando...") }
            var correo = remember { mutableStateOf("") }
            var rol = remember { mutableStateOf("") }

            LaunchedEffect(Unit) {
                try {
                    val token = sessionManager.obtenerToken()

                    if (!token.isNullOrBlank()) {
                        val response = RetrofitClient.api.obtenerPerfil(
                            token = "Bearer $token"
                        )

                        val resultado = response.body()

                        if (response.isSuccessful &&
                            resultado?.success == true &&
                            resultado.usuario != null
                        ) {
                            nombre.value = resultado.usuario.nombre
                            correo.value = resultado.usuario.correo
                            rol.value = resultado.usuario.rol
                        } else {
                            nombre.value = "No disponible"
                            correo.value = "No disponible"
                            rol.value = "No disponible"
                        }
                    } else {
                        nombre.value = "No disponible"
                        correo.value = "No disponible"
                        rol.value = "No disponible"
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    nombre.value = "Error al cargar"
                    correo.value = "No disponible"
                    rol.value = "No disponible"
                }
            }

            ProfileScreen(
                nombre = nombre.value,
                correo = correo.value,
                rol = rol.value,
                onBackClick = {
                    navController.popBackStack()
                },
                onLogOutClick = {
                    sessionManager.cerrarSesion()

                    navController.navigate("login") {
                        popUpTo("home") {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("inventory") {
            InventoryScreen(
                onNavigationHome = {
                    navController.navigate("home") {
                        popUpTo("home") {
                            inclusive = true
                        }
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
            MovementsScreen(
                onNavigationHome = {
                    navController.navigate("home") {
                        popUpTo("home") {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                onNavigateInventory = {
                    navController.navigate("inventory") {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("add_product") {
            AddProductScreen(
                onBack = {
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

            val productId = backStackEntry.arguments
                ?.getString("productId")
                .orEmpty()

            ProductDetailScreen(
                productId = productId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
