package com.minidany.stockapp.data.model

import com.google.gson.annotations.SerializedName

data class Producto(
    val id: Int,
    @SerializedName("codigo_barras")
    val codigoBarras: String?,
    val nombre: String,
    @SerializedName("categoria_id")
    val categoriaId: Int,
    val categoria: String?,
    val descripcion: String?,
    @SerializedName("precio_compra")
    val precioCompra: String?,
    @SerializedName("precio_venta")
    val precioVenta: String?,
    val stock: Int,
    @SerializedName("stock_minimo")
    val stockMinimo: Int,
    @SerializedName("stock_maximo")
    val stockMaximo: Int?,
    val imagen: String?,
    val activo: Int
)