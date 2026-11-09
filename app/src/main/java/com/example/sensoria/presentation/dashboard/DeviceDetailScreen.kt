package com.example.sensoria.presentation.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sensoria.core.theme.glassSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceDetailScreen(
    deviceId: String,
    onBack: () -> Unit,
    viewModel: DeviceDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(deviceId) {
        viewModel.loadTelemetry(deviceId)
    }

    val telemetryData by viewModel.telemetry.collectAsState()
    val deviceName by viewModel.deviceName.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val chartValues = telemetryData.map { it.value }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Métricas del Sensor", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    // Botón de actualizar manual.
                    IconButton(onClick = { viewModel.loadTelemetry(deviceId) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Actualizar datos", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Sensor ID: $deviceId", fontSize = 12.sp, color = Color.Gray)
                    Text(text = deviceName, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
                }
                Box(
                    modifier = Modifier
                        .background(Color(0xFF589445).copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("EN LÍNEA", color = Color(0xFF589445), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (deviceName == "Error de conexión") {
                Box(
                    modifier = Modifier.fillMaxWidth().glassSurface().padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No se pudo obtener los datos de telemetría.", color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
                }
            } else {
                Text(text = "Telemetría Histórica", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .glassSurface()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    } else if (chartValues.isEmpty()) {
                        Text("Sin lecturas de telemetría registradas.", color = Color.Gray, fontSize = 14.sp)
                    } else {
                        TelemetryChart(
                            data = chartValues,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                telemetryData.lastOrNull()?.let { lastReading ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Última lectura: ${lastReading.value} ${lastReading.unit}",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Mostrando el histórico reciente. Utiliza el botón superior derecho para refrescar los datos en tiempo real.",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun TelemetryChart(
    data: List<Float>,
    modifier: Modifier = Modifier
) {
    val lineColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier) {
        if (data.isEmpty()) return@Canvas

        val max = data.maxOrNull() ?: 100f
        val min = data.minOrNull() ?: 0f
        val range = (max - min).coerceAtLeast(1f)

        val stepX = size.width / (data.size - 1).coerceAtLeast(1)

        val path = Path()
        data.forEachIndexed { index, value ->
            val x = index * stepX
            val y = size.height - ((value - min) / range) * size.height

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(
                width = 8f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}