package com.minidany.stockapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minidany.stockapp.data.remote.RetrofitClient
import com.minidany.stockapp.ui.theme.StockAppTheme
import com.minidany.stockapp.ui.utils.scanBarcode
import kotlinx.coroutines.launch

data class Product(
    val id: String,
    val name: String,
    val category: String,
    val stock: Int,
    val minStock: Int,
    val price: Double,
    val barcode: String = ""
)

@Composable
fun InventoryScreen(
    onNavigationHome: () -> Unit = {},
    onNavigateMovimientos: () -> Unit = {},
    onNavigateToProductDetail: (String) -> Unit = {},
    onNavigateToAddProduct: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Todos") }

    // Estados de carga y productos.
    var productList by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var refreshKey by remember { mutableStateOf(0) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Consultar los productos reales de la base de datos.
    LaunchedEffect(refreshKey) {
        isLoading = true
        errorMessage = null

        try {
            val response = RetrofitClient.api.listarProductos()

            if (response.isSuccessful) {
                val resultado = response.body()

                if (resultado?.success == true) {
                    productList = resultado.productos.map { producto ->
                        Product(
                            id = producto.id.toString(),
                            name = producto.nombre,
                            category = producto.categoria ?: "Sin categoría",
                            stock = producto.stock,
                            minStock = producto.stockMinimo,
                            price = producto.precioVenta
                                ?.toDoubleOrNull() ?: 0.0,
                            barcode = producto.codigoBarras ?: ""
                        )
                    }
                } else {
                    errorMessage = "No se pudieron obtener los productos."
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

    // Buscar por nombre, categoría o código de barras.
    val filteredProducts = productList.filter { product ->
        val query = searchQuery.trim()

        val matchesSearch =
            product.name.contains(query, ignoreCase = true) ||
                    product.category.contains(query, ignoreCase = true) ||
                    product.barcode.contains(query, ignoreCase = true)

        val matchesFilter = when (selectedFilter) {
            "Stock bajo" ->
                product.stock > 0 &&
                        product.stock <= product.minStock

            "Agotados" -> product.stock == 0

            else -> true
        }

        matchesSearch && matchesFilter
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                windowInsets = NavigationBarDefaults.windowInsets
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigationHome,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio"
                        )
                    },
                    label = { Text("Inicio") }
                )

                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = "Inventario"
                        )
                    },
                    label = { Text("Inventario") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateMovimientos,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = "Movimientos"
                        )
                    },
                    label = { Text("Movimientos") }
                )
            }
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                horizontal = 24.dp,
                vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Inventario",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "Productos almacenados",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = onNavigateToAddProduct
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Agregar producto",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            item {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }

            item {
                // Buscador y escáner.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        placeholder = {
                            Text("Buscar producto...")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar"
                            )
                        },
                        shape = MaterialTheme.shapes.medium
                    )

                    IconButton(
                        onClick = {
                            scanBarcode(
                                context = context,
                                onSuccess = { scannedValue ->
                                    searchQuery = scannedValue
                                    selectedFilter = "Todos"

                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Código escaneado: $scannedValue"
                                        )
                                    }
                                },
                                onError = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "No se pudo leer el código. Intenta nuevamente."
                                        )
                                    }
                                }
                            )
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Escanear código de barras",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }

            item {
                // Filtros del inventario.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InventoryFilterChip(
                        text = "Todos",
                        selected = selectedFilter == "Todos",
                        onClick = {
                            selectedFilter = "Todos"
                        }
                    )

                    InventoryFilterChip(
                        text = "Stock bajo",
                        selected = selectedFilter == "Stock bajo",
                        onClick = {
                            selectedFilter = "Stock bajo"
                        }
                    )

                    InventoryFilterChip(
                        text = "Agotados",
                        selected = selectedFilter == "Agotados",
                        onClick = {
                            selectedFilter = "Agotados"
                        }
                    )
                }
            }

            item {
                Text(
                    text = if (isLoading) {
                        "Cargando productos..."
                    } else {
                        "${filteredProducts.size} productos"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Indicador de carga.
            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            // Error de conexión.
            else if (errorMessage != null) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.error
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = errorMessage ?: "",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                refreshKey++
                            }
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            // Inventario vacío o resultados.
            else if (filteredProducts.isEmpty()) {
                item {
                    InventoryEmptyState(
                        hasProducts = productList.isNotEmpty(),
                        onAddProduct = onNavigateToAddProduct
                    )
                }
            } else {
                items(
                    items = filteredProducts,
                    key = { it.id }
                ) { product ->
                    InventoryProductCard(
                        product = product,
                        onClick = {
                            onNavigateToProductDetail(product.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun InventoryFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(text)
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor =
                MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor =
                MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
private fun InventoryEmptyState(
    hasProducts: Boolean,
    onAddProduct: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 28.dp,
                bottom = 32.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(92.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (hasProducts) {
                    Icons.Default.SearchOff
                } else {
                    Icons.Default.Inventory2
                },
                contentDescription = null,
                modifier = Modifier.size(42.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = if (hasProducts) {
                "No se encontraron productos"
            } else {
                "Tu inventario está vacío"
            },
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = if (hasProducts) {
                "Prueba con otro nombre o cambia el filtro seleccionado."
            } else {
                "Todavía no tienes productos registrados. Agrega el primero para comenzar a controlar tus existencias."
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        if (!hasProducts) {
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onAddProduct,
                shape = MaterialTheme.shapes.large,
                contentPadding = PaddingValues(
                    horizontal = 24.dp,
                    vertical = 14.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Agregar producto")
            }
        }
    }
}

@Composable
private fun InventoryProductCard(
    product: Product,
    onClick: () -> Unit
) {
    val isOutOfStock = product.stock == 0
    val isLowStock =
        product.stock > 0 && product.stock <= product.minStock

    val statusText = when {
        isOutOfStock -> "Agotado"
        isLowStock -> "Stock bajo"
        else -> "Disponible"
    }

    val statusColor = when {
        isOutOfStock -> MaterialTheme.colorScheme.error
        isLowStock -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.primary
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = product.category,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = statusText,
                    style = MaterialTheme.typography.labelMedium,
                    color = statusColor,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Stock disponible",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = product.stock.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Stock mínimo",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = product.minStock.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun InventoryScreenPreview() {
    StockAppTheme {
        InventoryScreen()
    }
}
