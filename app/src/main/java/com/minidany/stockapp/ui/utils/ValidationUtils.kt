package com.minidany.stockapp.ui.utils

import android.util.Patterns

fun validateEmail(email: String): String? {
    if (email.isEmpty()) {
        return "Ingresa tu correo electrónico"
    }

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        return "Ingresa un correo electrónico válido"
    }

    return null
}

fun validatePassword(password: String): String? {
    if (password.isEmpty()) {
        return "Ingresa tu contraseña"
    }

    if (password.length < 8) {
        return "La contraseña debe tener al menos 8 caracteres"
    }

    if (!password.any { it.isLetter() }) {
        return "La contraseña debe contener al menos una letra"
    }

    if (!password.any { it.isDigit() }) {
        return "La contraseña debe contener al menos un número"
    }

    return null
}

fun validateName(name: String): String? {
    if (name.isEmpty()) {
        return "Ingresa tu nombre completo"
    }

    if (name.length < 3) {
        return "El nombre debe tener al menos 3 caracteres"
    }

    return null
}

fun validateConfirmPassword(password: String, confirmPassword: String): String? {
    if (confirmPassword.isBlank()) {
        return "Confirma tu contraseña"
    }

    if (password != confirmPassword) {
        return "Las contraseñas no coinciden"
    }

    return null
}