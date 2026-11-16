package com.minidany.stockapp.data.model

data class LoginResponse(
    val success: Boolean,
    val mensaje: String,
    val token: String?,
    val usuario: UsuarioResponse?
)

data class UsuarioResponse(
    val id: Int,
    val nombre: String,
    val correo: String,
    val rol: String,
    val debe_cambiar_clave: Int
)

