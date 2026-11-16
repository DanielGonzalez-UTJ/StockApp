
package com.minidany.stockapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minidany.stockapp.data.remote.RegistrarProductoRequest
import com.minidany.stockapp.data.remote.RetrofitClient
import com.minidany.stockapp.ui.theme.StockAppTheme
import com.minidany.stockapp.ui.utils.*
import kotlinx.coroutines.launch

@Composable
fun AddProductScreen(
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var barcode by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var purchasePrice by remember { mutableStateOf("") }
    var salePrice by remember { mutableStateOf("") }
    var currentStock by remember { mutableStateOf("") }
    var minimumStock by remember { mutableStateOf("") }
    var maximumStock by remember { mutableStateOf("") }

    var categoryExpanded by remember { mutableStateOf(false) }
    var showSavedDialog by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }

    var barcodeError by remember { mutableStateOf<String?>(null) }
    var nameError by remember { mutableStateOf<String?>(null) }
    var categoryError by remember { mutableStateOf<String?>(null) }
    var purchasePriceError by remember { mutableStateOf<String?>(null) }
    var salePriceError by remember { mutableStateOf<String?>(null) }
    var currentStockError by remember { mutableStateOf<String?>(null) }
    var minimumStockError by remember { mutableStateOf<String?>(null) }
    var maximumStockError by remember { mutableStateOf<String?>(null) }
    var stockRangeError by remember { mutableStateOf<String?>(null) }

    val categories = listOf(
        "Paletas" to 1,
        "Chocolates" to 2,
        "Gomitas" to 3,
        "Caramelos" to 4,
        "Chicles" to 5,
        "Mayoreo" to 6
    )

    fun saveProduct() {
        if (isSaving) return

        // Validaciones.
        barcodeError = validateBarcode(barcode)
        nameError = validateProductName(name)
        categoryError = validateProductCategory(category)

        purchasePriceError =
            validateProductPrice(purchasePrice, "compra")

        salePriceError =
            validateProductPrice(salePrice, "venta")

        currentStockError =
            validateProductStock(currentStock, "actual")

        minimumStockError =
            validateProductStock(minimumStock, "mínimo")

        maximumStockError =
            validateProductStock(maximumStock, "máximo")

        stockRangeError =
            validateStockRange(minimumStock, maximumStock)

        val hasErrors = listOf(
            barcodeError,
            nameError,
            categoryError,
            purchasePriceError,
            salePriceError,
            currentStockError,
            minimumStockError,
            maximumStockError,
            stockRangeError
        ).any { it != null }

        if (hasErrors) return

        val categoriaId = categories
            .firstOrNull { it.first == category }
            ?.second

        if (categoriaId == null) {
            categoryError = "Selecciona una categoría válida."
            return
        }

        // Convertir los datos numéricos.
        val precioCompra = purchasePrice.toDoubleOrNull()
        val precioVenta = salePrice.toDoubleOrNull()
        val stockActual = currentStock.toIntOrNull()
        val stockMinimo = minimumStock.toIntOrNull()
        val stockMaximo = maximumStock.toIntOrNull()

        if (
            precioCompra == null ||
            precioVenta == null ||
            stockActual == null ||
            stockMinimo == null ||
            (maximumStock.isNotBlank() && stockMaximo == null)
        ) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    "Revisa los datos numéricos del producto."
                )
            }
            return
        }

        scope.launch {
            isSaving = true

            try {
                val request = RegistrarProductoRequest(
                    codigo_barras = barcode.trim().ifBlank { null },
                    nombre = name.trim(),
                    categoria_id = categoriaId,
                    descripcion = description.trim().ifBlank { null },
                    precio_compra = precioCompra,
                    precio_venta = precioVenta,
                    stock = stockActual,
                    stock_minimo = stockMinimo,
                    stock_maximo = stockMaximo
                )

                val response = RetrofitClient.api.registrarProducto(request)
                val resultado = response.body()

                if (
                    response.isSuccessful &&
                    resultado?.success == true
                ) {
                    showSavedDialog = true
                } else {
                    snackbarHostState.showSnackbar(
                        resultado?.mensaje
                            ?: "No se pudo registrar el producto."
                    )
                }

            } catch (e: Exception) {
                e.printStackTrace()

                snackbarHostState.showSnackbar(
                    "No se pudo conectar con el servidor."
                )
            } finally {
                isSaving = false
            }
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 20.dp,
                    vertical = 4.dp
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar"
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = "Agregar producto",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Registra un producto en tu inventario",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Código de barras",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    OutlinedTextField(
                        value = barcode,
                        onValueChange = { value ->
                            if (value.all { it.isDigit() }) {
                                barcode = value
                                barcodeError = null
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Código numérico *") },
                        placeholder = {
                            Text("Escanea o escribe el código")
                        },
                        singleLine = true,
                        isError = barcodeError != null,
                        supportingText = {
                            barcodeError?.let { Text(it) }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedButton(
                        onClick = {
                            scanBarcode(
                                context = context,
                                onSuccess = { scannedValue ->
                                    if (scannedValue.all { it.isDigit() }) {
                                        barcode = scannedValue
                                        barcodeError = null

                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                "Código de barras leído correctamente"
                                            )
                                        }
                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                "El código de barras es inválido"
                                            )
                                        }
                                    }
                                },
                                onError = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Error al escanear el código de barras"
                                        )
                                    }
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text("Escanear código")
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Información del producto",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    HorizontalDivider()

                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            nameError = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Nombre del producto *") },
                        singleLine = true,
                        isError = nameError != null,
                        supportingText = {
                            nameError?.let { Text(it) }
                        },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = category,
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Categoría *") },
                            placeholder = {
                                Text("Selecciona una categoría")
                            },
                            readOnly = true,
                            singleLine = true,
                            isError = categoryError != null,
                            supportingText = {
                                categoryError?.let { Text(it) }
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        categoryExpanded = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ExpandMore,
                                        contentDescription = "Elegir categoría"
                                    )
                                }
                            },
                            shape = RoundedCornerShape(12.dp)
                        )

                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .padding(end = 52.dp)
                                .clickable {
                                    categoryExpanded = true
                                }
                        )

                        DropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = {
                                categoryExpanded = false
                            }
                        ) {
                            categories.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option.first) },
                                    onClick = {
                                        category = option.first
                                        categoryError = null
                                        categoryExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = description,
                        onValueChange = {
                            description = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Descripción (opcional)") },
                        minLines = 3,
                        maxLines = 4,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Precios",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    HorizontalDivider()

                    OutlinedTextField(
                        value = purchasePrice,
                        onValueChange = {
                            purchasePrice = it
                            purchasePriceError = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Precio de compra *") },
                        prefix = { Text("$ ") },
                        singleLine = true,
                        isError = purchasePriceError != null,
                        supportingText = {
                            purchasePriceError?.let { Text(it) }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = salePrice,
                        onValueChange = {
                            salePrice = it
                            salePriceError = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Precio de venta *") },
                        prefix = { Text("$ ") },
                        singleLine = true,
                        isError = salePriceError != null,
                        supportingText = {
                            salePriceError?.let { Text(it) }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Control de inventario",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    HorizontalDivider()

                    OutlinedTextField(
                        value = currentStock,
                        onValueChange = {
                            currentStock = it
                            currentStockError = null
                            stockRangeError = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Cantidad inicial *") },
                        singleLine = true,
                        isError = currentStockError != null,
                        supportingText = {
                            currentStockError?.let { Text(it) }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = minimumStock,
                        onValueChange = {
                            minimumStock = it
                            minimumStockError = null
                            stockRangeError = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Stock mínimo *") },
                        singleLine = true,
                        isError = minimumStockError != null ||
                                stockRangeError != null,
                        supportingText = {
                            minimumStockError?.let { Text(it) }
                                ?: stockRangeError?.let { Text(it) }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = maximumStock,
                        onValueChange = {
                            maximumStock = it
                            maximumStockError = null
                            stockRangeError = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Stock máximo *") },
                        singleLine = true,
                        isError = maximumStockError != null ||
                                stockRangeError != null,
                        supportingText = {
                            maximumStockError?.let { Text(it) }
                                ?: stockRangeError?.let { Text(it) }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Button(
                onClick = { saveProduct() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Guardando...")
                } else {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Guardar producto",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }

    if (showSavedDialog) {
        AlertDialog(
            onDismissRequest = {
                showSavedDialog = false
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(42.dp)
                )
            },
            title = {
                Text("¡Producto registrado!")
            },
            text = {
                Text(
                    "El producto $name se guardó correctamente " +
                            "en la base de datos."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSavedDialog = false
                        onBack()
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddProductScreenPreview() {
    StockAppTheme {
        AddProductScreen()
    }
}
