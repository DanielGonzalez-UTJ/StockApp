package com.minidany.stockapp.data.remote

import com.minidany.stockapp.data.model.LoginRequest
import com.minidany.stockapp.data.model.LoginResponse
import com.minidany.stockapp.data.model.ProfileResponse
import com.minidany.stockapp.data.model.Producto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

data class ProductosResponse(
    val success: Boolean,
    val total: Int,
    val productos: List<Producto>
)

data class CambiarClaveRequest(
    val nueva_password: String
)

data class MensajeResponse(
    val success: Boolean,
    val mensaje: String
)


data class RegistrarProductoRequest(
    val codigo_barras: String?,
    val nombre: String,
    val categoria_id: Int,
    val descripcion: String?,
    val precio_compra: Double,
    val precio_venta: Double,
    val stock: Int,
    val stock_minimo: Int,
    val stock_maximo: Int?
)

data class RegistrarProductoResponse(
    val success: Boolean,
    val mensaje: String,
    val id: Int?
)


interface ApiService {

    @GET("productos/listar.php")
    suspend fun listarProductos(): Response<ProductosResponse>

    @POST("auth/login.php")
    suspend fun iniciarSesion(
        @Body datos: LoginRequest
    ): Response<LoginResponse>

    @POST("auth/cambiar_clave.php")
    suspend fun cambiarClave(
        @Header("Authorization") token: String,
        @Body datos: CambiarClaveRequest
    ): Response<MensajeResponse>

    @GET("auth/perfil.php")
    suspend fun obtenerPerfil(
        @Header("Authorization") token: String
    ): Response<ProfileResponse>

    @POST("productos/registrar.php")
    suspend fun registrarProducto(
        @Body datos: RegistrarProductoRequest
    ): Response<RegistrarProductoResponse>
}