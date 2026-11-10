package com.example.sensoria.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensoria.core.theme.glassSurface
import com.example.sensoria.domain.models.Device

@Composable
fun DevicesScreen(
    companyId: String,
    viewModel: DashboardViewModel,
    onDeviceClick: (String) -> Unit
) {
    val devices by viewModel.devices.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val companyName by viewModel.companyName.collectAsState()

    LaunchedEffect(companyId) { viewModel.loadDevicesForCompany(companyId) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Workspace Activo", fontSize = 14.sp, color = MaterialTheme.colorScheme.secondary)
        Text(text = companyName, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Tus Dispositivos IoT", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            if (devices.isEmpty()) {
                Text("No tienes dispositivos aún.")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(devices, key = { it.id }) { device ->
                        DeviceCard(device, onClick = { onDeviceClick(device.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun DeviceCard(device: Device, onClick: () -> Unit) {
    val hasData = device.lastReading != "Sin datos"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .glassSurface()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(RoundedCornerShape(50))
                .background(if (device.isOnline) Color(0xFF589445) else Color.Red)
        )
        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = device.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = device.type, fontSize = 12.sp, color = Color.DarkGray)
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = device.lastReading,
            fontWeight = if (hasData) FontWeight.SemiBold else FontWeight.Normal,
            fontSize = if (hasData) 16.sp else 13.sp,
            color = if (hasData) MaterialTheme.colorScheme.primary else Color.Gray,
            textAlign = TextAlign.End,
            modifier = Modifier.widthIn(max = 140.dp)
        )
    }
}