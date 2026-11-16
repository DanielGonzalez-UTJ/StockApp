package com.minidany.stockapp.data.local

import android.content.Context

class SessionManager(context: Context) {
    private val preferences = context.getSharedPreferences(
        "sweetmanager_session",
        Context.MODE_PRIVATE
    )

    fun guardarToken(token: String) {
        preferences.edit()
            .putString("token", token)
            .apply()
    }

    fun obtenerToken(): String? {
        return preferences.getString("token", null)
    }

    fun guardarUsuarioId(id: Int) {
        preferences.edit()
            .putInt("usuario_id", id)
            .apply()
    }

    fun obtenerUsuarioId(): Int {
        return preferences.getInt("usuario_id", -1)
    }

    fun cerrarSesion() {
        preferences.edit().clear().apply()
    }
}