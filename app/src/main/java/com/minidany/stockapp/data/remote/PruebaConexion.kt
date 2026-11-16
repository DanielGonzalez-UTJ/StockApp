package com.minidany.stockapp.data.remote

import org.jetbrains.annotations.Async

suspend fun probarConexion(): String {
    return try {
        val response = RetrofitClient.api.listarProductos()

        if (response.isSuccessful) {
            val resultado = response.body()

            if (resultado != null && resultado.success) {
                "Conexión exitosa. Productos recibidos: ${resultado.productos.size}"
            } else {
                "La API respondió, pero no se pudieron obtener los productos."
            }
        } else {
            "Error del servidor: ${response.code()}"
        }
    } catch (e: Exception) {
        "Error de conexión: ${e.message}"
    }
}