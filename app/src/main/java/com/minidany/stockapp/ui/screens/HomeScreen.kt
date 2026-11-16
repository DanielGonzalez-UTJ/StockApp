
package com.minidany.stockapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MoveToInbox
import androidx.compose.material.icons.filled.Outbox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minidany.stockapp.data.local.SessionManager
import com.minidany.stockapp.data.remote.RetrofitClient
import com.minidany.stockapp.ui.theme.StockAppTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit = {},
    onNavigateInventory: () -> Unit = {},
    onNavigateMovimientos: () -> Unit = {},
    onScanProductClick: (String) -> Unit = {}
) {
    val context = LocalContext.current

    val sessionManager = remember(context) {
        SessionManager(context)
    }

    // Estados del nombre del usuario.
    var userName by remember {
        mutableStateOf("Cargando...")
    }

    // Estados de las estadísticas.
    var totalProducts by remember {
        mutableStateOf<Int?>(null)
    }

    var lowStockProducts by remember {
        mutableStateOf<Int?>(null)
    }

    var outOfStockProducts by remember {
        mutableStateOf<Int?>(null)
    }

    // Consultar perfil y estadísticas reales.
    LaunchedEffect(Unit) {

        // Obtener nombre del usuario.
        try {
            val token = sessionManager.obtenerToken()

            if (!token.isNullOrBlank()) {
                val profileResponse = RetrofitClient.api.obtenerPerfil(
                    token = "Bearer $token"
                )

                val profileResult = profileResponse.body()

                if (
                    profileResponse.isSuccessful &&
                    profileResult?.success == true &&
                    profileResult.usuario != null
                ) {
                    userName = profileResult.usuario.nombre
                } else {
                    userName = "Usuario"
                }
            } else {
                userName = "Usuario"
            }

        } catch (e: Exception) {
            userName = "Usuario"
            e.printStackTrace()
        }

        // Obtener estadísticas del inventario.
        try {
            val response = RetrofitClient.api.listarProductos()

            if (response.isSuccessful) {
                val result = response.body()

                if (result?.success == true) {
                    val products = result.productos

                    totalProducts = products.size

                    lowStockProducts = products.count { product ->
                        product.stock > 0 &&
                                product.stock <= product.stockMinimo
                    }

                    outOfStockProducts = products.count { product ->
                        product.stock == 0
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            NavigationBar(
                modifier = Modifier.height(72.dp),
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                windowInsets = WindowInsets(0.dp)
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio"
                        )
                    },
                    label = {
                        Text("Inicio")
                    },
                    alwaysShowLabel = true
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateInventory,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = "Inventario"
                        )
                    },
                    label = {
                        Text("Inventario")
                    },
                    alwaysShowLabel = true
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
                    label = {
                        Text("Movimientos")
                    },
                    alwaysShowLabel = true
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {

            // Encabezado y saludo.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "SweetManager",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Hola $userName",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                IconButton(
                    onClick = onProfileClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Resumen del inventario.
            Text(
                text = "Resumen del inventario",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Column(
                        modifier = Modifier.padding(start = 12.dp)
                    ) {
                        Text(
                            text = "Productos registrados",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Text(
                            text = totalProducts?.toString() ?: "--",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InventoryStatCard(
                    title = "Stock Bajo",
                    value = lowStockProducts?.toString() ?: "--",
                    icon = Icons.Default.Warning,
                    modifier = Modifier.weight(1f)
                )

                InventoryStatCard(
                    title = "Agotados",
                    value = outOfStockProducts?.toString() ?: "--",
                    icon = Icons.Default.RemoveShoppingCart,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Acciones rápidas.
            Text(
                text = "Acciones rápidas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    title = "Entradas",
                    subtitle = "Registrar entrada",
                    icon = Icons.Default.MoveToInbox,
                    onClick = {},
                    modifier = Modifier.weight(1f)
                )

                QuickActionCard(
                    title = "Salida",
                    subtitle = "Registrar salidas",
                    icon = Icons.Default.Outbox,
                    onClick = {},
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    title = "Proveedores",
                    subtitle = "Administrar",
                    icon = Icons.Default.LocalShipping,
                    onClick = {},
                    modifier = Modifier.weight(1f)
                )

                QuickActionCard(
                    title = "Escáner",
                    subtitle = "Buscar por código",
                    icon = Icons.Default.QrCodeScanner,
                    onClick = {},
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun InventoryStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = false
)
@Composable
fun HomeScreenPreview() {
    StockAppTheme {
        HomeScreen()
    }
}
