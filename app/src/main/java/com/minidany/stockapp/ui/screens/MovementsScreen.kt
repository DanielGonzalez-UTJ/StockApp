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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minidany.stockapp.ui.theme.StockAppTheme

private data class InventoryMovement(
    val id: String,
    val productName: String,
    val type: MovementType,
    val quantity: Int,
    val time: String,
    val dateGroup: String,
    val note: String
)

private enum class MovementType(val label: String) {
    ENTRY("Entrada"), EXIT("Salida")
}

@Composable
fun MovementsScreen(
    modifier: Modifier = Modifier,
    onNavigationHome: () -> Unit = {},
    onNavigateInventory: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Todos") }

    val movements = remember {
        listOf(
            InventoryMovement("1", "Paletas de Caramelo Macizo", MovementType.ENTRY, 50, "10:30 a. m.", "Hoy", "Reposición de mercancía"),
            InventoryMovement("2", "Chocolates Rellenos 100g", MovementType.EXIT, 12, "09:15 a. m.", "Hoy", "Venta en punto de venta"),
            InventoryMovement("3", "Gomitas Enchiladas Bote", MovementType.ENTRY, 24, "08:40 a. m.", "Hoy", "Entrada de proveedor"),
            InventoryMovement("4", "Lucas Muecas Sabor Mango", MovementType.EXIT, 5, "06:20 p. m.", "Ayer", "Venta en punto de venta"),
            InventoryMovement("5", "Chocolates Rellenos 100g", MovementType.ENTRY, 30, "02:10 p. m.", "Ayer", "Reposición de mercancía"),
            InventoryMovement("6", "Paletas de Caramelo Macizo", MovementType.EXIT, 10, "11:05 a. m.", "Ayer", "Venta en punto de venta")
        )
    }

    val filteredMovements = movements.filter { movement ->
        val matchesSearch = movement.productName.contains(searchQuery.trim(), ignoreCase = true)
        val matchesFilter = when (selectedFilter) {
            "Entradas" -> movement.type == MovementType.ENTRY
            "Salidas" -> movement.type == MovementType.EXIT
            else -> true
        }
        matchesSearch && matchesFilter
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
                    selected = false,
                    onClick = onNavigationHome,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio"
                        )
                    },
                    label = { Text("Inicio") },
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
                    label = { Text("Inventario") },
                    alwaysShowLabel = true
                )

                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = "Movimientos"
                        )
                    },
                    label = { Text("Movimientos") },
                    alwaysShowLabel = true
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Movimientos",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Historial de entradas y salidas",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MovementSummaryCard(
                        title = "Entradas",
                        value = movements.count { it.type == MovementType.ENTRY }.toString(),
                        icon = Icons.Default.ArrowDownward,
                        tint = Color(0xFF218838),
                        modifier = Modifier.weight(1f)
                    )
                    MovementSummaryCard(
                        title = "Salidas",
                        value = movements.count { it.type == MovementType.EXIT }.toString(),
                        icon = Icons.Default.ArrowUpward,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.weight(1f)
                    )
                    MovementSummaryCard(
                        title = "Total",
                        value = movements.size.toString(),
                        icon = Icons.Default.History,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Buscar producto...") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar")
                    },
                    shape = MaterialTheme.shapes.medium
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Todos", "Entradas", "Salidas").forEach { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            label = { Text(filter) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
            }

            item {
                Text(
                    text = "${filteredMovements.size} movimientos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (filteredMovements.isEmpty()) {
                item { MovementEmptyState() }
            } else {
                val groupedMovements = filteredMovements.groupBy { it.dateGroup }
                groupedMovements.forEach { (dateGroup, dateMovements) ->
                    item(key = "date_$dateGroup") {
                        Text(
                            text = dateGroup,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    items(dateMovements, key = { it.id }) { movement ->
                        MovementHistoryCard(movement = movement)
                    }
                }
            }
        }
    }
}

@Composable
private fun MovementSummaryCard(
    title: String,
    value: String,
    icon: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun MovementHistoryCard(movement: InventoryMovement) {
    val isEntry = movement.type == MovementType.ENTRY
    val movementColor = if (isEntry) Color(0xFF218838) else MaterialTheme.colorScheme.error
    val movementIcon = if (isEntry) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = if (isEntry) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.errorContainer,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = movementIcon,
                    contentDescription = movement.type.label,
                    tint = if (isEntry) Color(0xFF218838) else MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(23.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movement.productName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "${movement.type.label} · ${movement.time}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = movement.note,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (isEntry) "+${movement.quantity}" else "−${movement.quantity}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = movementColor
            )
        }
    }
}

@Composable
private fun MovementEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 28.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.SearchOff,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "No se encontraron movimientos",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Prueba con otro nombre de producto o cambia el filtro seleccionado.",
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
private fun MovementsScreenPreview() {
    StockAppTheme {
        MovementsScreen()
    }
}
