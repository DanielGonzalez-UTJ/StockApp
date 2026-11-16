package com.minidany.stockapp.data.model

data class ProfileResponse(
    val success: Boolean,
    val mensaje: String,
    val usuario: PerfilUsuario?
)

data class PerfilUsuario(
    val id: Int,
    val nombre: String,
    val correo: String,
    val rol: String
)