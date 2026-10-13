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

// VALIDACIONES CODIGO DE BARRAS
fun validateBarcode(barcode: String): String? {
    if (barcode.isBlank()) {
        return "Ingresa o escanea el código de barras"
    }

    if (!barcode.all { it.isDigit()} ) {
        return "El código de barras debe contener solo números"
    }

    return null
}

fun validateProductName(name: String): String? {
    if (name.isBlank()) {
        return "Ingresa el nombre del producto"
    }

    if (name.trim().length < 2) {
        return "El nombre del producto debe tener al menos 2 caracteres"
    }

    return null
}

fun validateProductCategory(category: String): String? {
    if (category.isBlank()) {
        return "Selecciona una categoría"
    }

    return null
}

fun validateProductPrice(
    price: String,
    type: String
): String? {
    if (price.isBlank()) {
        return "Ingresa el precio de $type"
    }

    val parsed = price.toDoubleOrNull() ?: return "Ingresa un precio válido"

    if (!parsed.isFinite()) {
        return "Ingresa un precio válido"
    }

    if (parsed < 0) {
        return "El precio no puede ser negativo"
    }

    return null
}

fun validateProductStock(
    stock: String,
    type: String
): String? {
    if (stock.isBlank()) {
        return "Ingresa el stock de $type"
    }

    val parsed = stock.toIntOrNull() ?: return "Ingresa una cantidad válida"

    if (parsed < 0) {
        return "El stock no puede ser negativo"
    }

    return null
}

fun validateStockRange(
    minimumStock: String,
    maximumStock: String
): String? {
    val minimum = minimumStock.toIntOrNull()
        ?: return null

    val maximum = maximumStock.toIntOrNull()
        ?: return null

    if (minimum > maximum) {
        return "El stock mínimo no puede superar el máximo"
    }

    return null
}