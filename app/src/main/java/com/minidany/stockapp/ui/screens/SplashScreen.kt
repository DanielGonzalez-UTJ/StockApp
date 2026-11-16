package com.minidany.stockapp.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minidany.stockapp.R
import com.minidany.stockapp.data.local.SessionManager
import com.minidany.stockapp.data.remote.RetrofitClient
import com.minidany.stockapp.ui.theme.StockAppTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSessionValid: () -> Unit,
    onSessionInvalid: () -> Unit
) {
    var targetScale by remember {
        mutableFloatStateOf(0.7f)
    }

    val logoScale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = tween(durationMillis = 700),
        label = "logoScale"
    )

    val context = LocalContext.current
    val sessionManager = remember(context) {
        SessionManager(context)
    }

    LaunchedEffect(Unit) {
        targetScale = 1f
        delay(1500)

        val token = sessionManager.obtenerToken()

        if (token.isNullOrBlank()) {
            onSessionInvalid()
            return@LaunchedEffect
        }

        try {
            val response = RetrofitClient.api.obtenerPerfil(
                token = "Bearer $token"
            )

            val resultado = response.body()

            if (
                response.isSuccessful &&
                resultado?.success == true &&
                resultado.usuario != null
            ) {
                onSessionValid()
            } else {
                // El servidor rechazó la sesión.
                sessionManager.cerrarSesion()
                onSessionInvalid()
            }

        } catch (e: Exception) {
            e.printStackTrace()

            // Si no hay conexión, conservamos el token.
            // No obligamos al usuario a iniciar sesión otra vez.
            onSessionValid()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.logo_sweetmanager
                ),
                contentDescription = "Logo de SweetManager",
                modifier = Modifier
                    .size(320.dp)
                    .scale(logoScale)
            )

            CircularProgressIndicator(
                modifier = Modifier.size(28.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    StockAppTheme {
        SplashScreen(
            onSessionValid = {},
            onSessionInvalid = {}
        )
    }
}
