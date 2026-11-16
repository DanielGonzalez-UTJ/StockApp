package com.minidany.stockapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.minidany.stockapp.data.model.Producto
import com.minidany.stockapp.data.remote.RetrofitClient

@Composable
fun ProductDetailScreen(
    productId: String,
    onBack: () -> Unit
) {
    var product by remember {
        mutableStateOf<Producto?>(null)
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    var retryKey by remember {
        mutableStateOf(0)
    }

    // Consultar los productos reales y buscar el seleccionado.
    LaunchedEffect(productId, retryKey) {
        isLoading = true
        errorMessage = null
        product = null

        try {
            val response = RetrofitClient.api.listarProductos()

            if (response.isSuccessful) {
                val resultado = response.body()

                if (resultado?.success == true) {
                    product = resultado.productos.find {
                        it.id.toString() == productId
                    }

                    if (product == null) {
                        errorMessage = "No se encontró información de este producto."
                    }
                } else {
                    errorMessage = "No se pudieron obtener los datos del producto."
                }
            } else {
                errorMessage = "Error del servidor: ${response.code()}"
            }
        } catch (e: Exception) {
            errorMessage = "No se pudo conectar con el servidor. Verifica tu conexión."
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->

        when {
            // Estado de carga.
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            // Estado de error o producto no encontrado.
            product == null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "No se pudo cargar el producto",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = errorMessage ?: "Producto no encontrado.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (errorMessage?.contains("No se encontró") == true) {
                                onBack()
                            } else {
                                retryKey++
                            }
                        }
                    ) {
                        Text(
                            if (errorMessage?.contains("No se encontró") == true) {
                                "Volver al inventario"
                            } else {
                                "Reintentar"
                            }
                        )
                    }
                }
            }

            // Información del producto.
            else -> {
                val currentProduct = product!!

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {

                    // Encabezado.
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Regresar"
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Detalles del producto",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Icono del producto.
                    Surface(
                        modifier = Modifier
                            .size(92.dp)
                            .align(Alignment.CenterHorizontally),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = null,
                            modifier = Modifier.padding(22.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = currentProduct.nombre,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = currentProduct.categoria ?: "Sin categoría",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.heightIn(20.dp))

                    // Estado del producto.
                    val status = when {
                        currentProduct.stock == 0 -> "Agotado"
                        currentProduct.stock <= currentProduct.stockMinimo -> "Stock bajo"
                        else -> "Disponible"
                    }

                    val statusColor = when {
                        currentProduct.stock <= currentProduct.stockMinimo ->
                            MaterialTheme.colorScheme.error

                        else -> MaterialTheme.colorScheme.primary
                    }

                    Surface(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        shape = MaterialTheme.shapes.large,
                        color = statusColor.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = status,
                            color = statusColor,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(
                                horizontal = 18.dp,
                                vertical = 8.dp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // Información general.
                    Text(
                        text = "Información general",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            DetailRow(
                                icon = {
                                    Icon(
                                        Icons.Default.QrCode,
                                        contentDescription = null
                                    )
                                },
                                label = "Código de barras",
                                value = currentProduct.codigoBarras
                                    ?.takeIf { it.isNotBlank() }
                                    ?: "No registrado"
                            )

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = MaterialTheme.colorScheme.outlineVariant
                            )

                            DetailRow(
                                icon = {
                                    Icon(
                                        Icons.Default.Category,
                                        contentDescription = null
                                    )
                                },
                                label = "Categoría",
                                value = currentProduct.categoria ?: "Sin categoría"
                            )

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = MaterialTheme.colorScheme.outlineVariant
                            )

                            DetailRow(
                                icon = {
                                    Icon(
                                        Icons.Default.LocalOffer,
                                        contentDescription = null
                                    )
                                },
                                label = "Precio de venta",
                                value = "$${"%.2f".format(
                                    currentProduct.precioVenta?.toDoubleOrNull() ?: 0.0
                                )}"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Control de existencias.
                    Text(
                        text = "Control de existencias",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StockCard(
                            title = "Stock Actual",
                            quantity = currentProduct.stock,
                            modifier = Modifier.weight(1f)
                        )

                        StockCard(
                            title = "Stock Mínimo",
                            quantity = currentProduct.stockMinimo,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text("Volver al inventario")
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    icon: @Composable () -> Unit,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Box(
                modifier = Modifier.size(44.dp),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun StockCard(
    title: String,
    quantity: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.AttachMoney,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = quantity.toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "unidades",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
