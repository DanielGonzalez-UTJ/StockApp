package com.minidany.stockapp.ui.utils

import android.content.Context
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

fun scanBarcode(
    context: Context,
    onSuccess: (String) -> Unit,
    onError: (Exception) -> Unit
) {
    val scanner = GmsBarcodeScanning.getClient(context)

    scanner.startScan()
        .addOnSuccessListener { result ->
            val barcode = result.rawValue

            if (!barcode.isNullOrEmpty()) {
                onSuccess(barcode)
            } else {
                onError(
                    Exception("No se pudo obtener el código de barras")
                )
            }
        }
        .addOnFailureListener { exception ->
            onError(exception)
        }
}