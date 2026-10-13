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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minidany.stockapp.ui.theme.StockAppTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit = {},
    onNavigateInventory: () -> Unit = {},
    onNavigateMovimientos: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                windowInsets = NavigationBarDefaults.windowInsets,
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { /*TODO */ },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio"
                        )
                    },
                    label = {
                        Text("Inicio")
                    }
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
                    }
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
                    }
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "StockApp",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Hola Daniel",
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

            Text(
                text = "Resumen del inventario",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
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

                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(
                            text = "Productos registrados",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Text(
                            text = "125",
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
                    value = "8",
                    icon = Icons.Default.Warning,
                    modifier = Modifier.weight(1f)
                )

                InventoryStatCard(
                    title = "Agotados",
                    value = "3",
                    icon = Icons.Default.RemoveShoppingCart,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

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
                    onClick = { /*TODO */ },
                    modifier = Modifier.weight(1f)
                )

                QuickActionCard(
                    title = "Salida",
                    subtitle = "Registrar salidas",
                    icon = Icons.Default.Outbox,
                    onClick = { /* TODO */ },
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
                    onClick = { /* TODO */ },
                    modifier = Modifier.weight(1f)
                )

                QuickActionCard(
                    title = "Productos",
                    subtitle = "Administrar",
                    icon = Icons.Default.Inventory2,
                    onClick = { /* TODO */ },
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
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
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
        Column(modifier = Modifier.padding(16.dp)) {
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

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun HomeScreenPreview() {
    StockAppTheme() {
        HomeScreen()
    }
}