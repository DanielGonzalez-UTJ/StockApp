package com.minidany.stockapp.ui.test

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.minidany.stockapp.data.remote.probarConexion
import com.minidany.stockapp.ui.theme.StockAppTheme

@Composable
fun ConexionTestScreen() {

    var resultado by remember { mutableStateOf("Presiona el botón para probar la conexión") }
    var cargando by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Prueba de conexión",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                scope.launch {
                    cargando = true
                    resultado = probarConexion()
                    cargando = false
                }
            },
            enabled = !cargando
        ) {
            Text("Conectar con MySQL")
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (cargando) {
            CircularProgressIndicator()
        }

        Text(text = resultado)
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ConexionTestScreenPreview() {
    StockAppTheme() {
        ConexionTestScreen()
    }
}