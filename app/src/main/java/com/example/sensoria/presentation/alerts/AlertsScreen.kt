package com.example.sensoria.presentation.alerts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sensoria.core.theme.PrimaryGreen
import com.example.sensoria.core.theme.components.LocalToastController
import com.example.sensoria.core.theme.components.ToastType
import com.example.sensoria.domain.models.Alert

@Composable
fun AlertsScreen(viewModel: AlertsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val showToast = LocalToastController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Alertas IOT", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = PrimaryGreen)
                Text(text = "Monitoreo de red de sensores", fontSize = 12.sp, color = Color.Gray)
            }
            IconButton(onClick = { viewModel.loadAlerts() }) {
                Icon(Icons.Default.Refresh, contentDescription = "Refrescar", tint = PrimaryGreen)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        when (val state = uiState) {
            is AlertsUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryGreen)
                }
            }
            is AlertsUiState.Error -> {
                Text(text = state.message, color = MaterialTheme.colorScheme.error)
            }
            is AlertsUiState.Success -> {
                if (state.alerts.isEmpty()) {
                    Text("No hay alertas activas.", color = Color.Gray, modifier = Modifier.padding(16.dp))
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(state.alerts) { alert ->
                            AlertCard(alert = alert, onResolve = {
                                viewModel.resolveAlert(alert.id) { msg ->
                                    showToast(msg, ToastType.SUCCESS)
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AlertCard(alert: Alert, onResolve: () -> Unit) {
    val (icon, color) = when (alert.severity) {
        "CRITICA" -> Icons.Default.Warning to Color(0xFFEF4444)
        "ADVERTENCIA" -> Icons.Default.Info to Color(0xFFF59E0B)
        else -> Icons.Default.Info to Color(0xFF3B82F6)
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = alert.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = alert.message,
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Origen: ${alert.source}",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!alert.isResolved) {
                    TextButton(onClick = onResolve) {
                        Text(
                            text = "Resolver",
                            color = PrimaryGreen,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                } else {
                    ResolvedBadge()
                }
            }
        }
    }
}

@Composable
private fun ResolvedBadge() {
    Surface(
        shape = RoundedCornerShape(50),
        color = PrimaryGreen.copy(alpha = 0.12f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = PrimaryGreen,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Resuelta",
                color = PrimaryGreen,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                softWrap = false
            )
        }
    }
}