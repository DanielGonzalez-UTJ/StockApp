package com.minidany.stockapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minidany.stockapp.ui.theme.StockAppTheme

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onLogOutClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.offset(x = (-16).dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Regresar"
                )
            }

            Text(
                text = "Perfil",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            modifier = Modifier.size(88.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Foto de perfil",
                modifier = Modifier.padding(22.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Daniel González",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Almacenista",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Información de la cuenta",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.size(16.dp))

                    Column {
                        Text(
                            text = "Correo electrónico",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = "dannyglezhdzo@gmail.com",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.size(16.dp))

                    Column {
                        Text(
                            text = "Rol",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = "Almacenista",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Sobre StockApp",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(52.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = null,
                        modifier = Modifier.padding(12.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.size(16.dp))

                Column{
                    Text(
                        text = "Sistema de gestión de inventario",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "para Dulcería",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Versión 1.0.0",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onLogOutClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = "Cerrar sesión"
            )

            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = "Cerrar Sesión",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    StockAppTheme {
        ProfileScreen()
    }
}