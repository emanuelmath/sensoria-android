package com.example.sensoria.presentation.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sensoria.core.theme.glassSurface

private val OnlineGreen = Color(0xFF589445)

private fun Float.format(): String = "%.1f".format(this)

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

    val uiState by viewModel.uiState.collectAsState()

    val headerTitle = when (val state = uiState) {
        is DeviceDetailUiState.Loading -> "Cargando…"
        is DeviceDetailUiState.Error -> "Sensor sin conexión"
        is DeviceDetailUiState.Success -> state.deviceName
    }

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
                    IconButton(onClick = { viewModel.loadTelemetry(deviceId) }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Actualizar datos",
                            tint = MaterialTheme.colorScheme.primary
                        )
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Sensor ID: $deviceId", fontSize = 12.sp, color = Color.Gray)
                    Text(
                        text = headerTitle,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                if (uiState !is DeviceDetailUiState.Loading) {
                    StatusBadge(isOnline = uiState is DeviceDetailUiState.Success)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (val state = uiState) {
                is DeviceDetailUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .glassSurface(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }

                is DeviceDetailUiState.Error -> {
                    StateCard(
                        icon = {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(40.dp)
                            )
                        },
                        title = "No se pudo conectar",
                        message = state.message,
                        actionLabel = "Reintentar",
                        onAction = { viewModel.loadTelemetry(deviceId) }
                    )
                }

                is DeviceDetailUiState.Success -> {
                    val values = state.readings.map { it.value }

                    if (values.isEmpty()) {
                        StateCard(
                            icon = {
                                Icon(
                                    Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(40.dp)
                                )
                            },
                            title = "Aún no hay lecturas",
                            message = "Este sensor todavía no ha enviado datos. Cuando empiece a reportar, verás aquí su historial.",
                            actionLabel = "Actualizar",
                            onAction = { viewModel.loadTelemetry(deviceId) }
                        )
                    } else {
                        val unit = state.readings.last().unit

                        CurrentReadingCard(
                            value = values.last(),
                            unit = unit,
                            isRefreshing = state.isRefreshing
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Telemetría Histórica",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        if (values.size == 1) {
                            SingleReadingCard()
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .glassSurface()
                                    .padding(20.dp)
                            ) {
                                TelemetryChart(
                                    data = values,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                StatChip(
                                    label = "Mínimo",
                                    value = "${values.min().format()} $unit",
                                    modifier = Modifier.weight(1f)
                                )
                                StatChip(
                                    label = "Promedio",
                                    value = "${values.average().toFloat().format()} $unit",
                                    modifier = Modifier.weight(1f)
                                )
                                StatChip(
                                    label = "Máximo",
                                    value = "${values.max().format()} $unit",
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${values.size} lecturas registradas",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
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
private fun StatusBadge(isOnline: Boolean) {
    val color = if (isOnline) OnlineGreen else MaterialTheme.colorScheme.error
    Row(
        modifier = Modifier
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = if (isOnline) "EN LÍNEA" else "SIN CONEXIÓN",
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun CurrentReadingCard(value: Float, unit: String, isRefreshing: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .glassSurface()
            .padding(20.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Última lectura",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    modifier = Modifier.weight(1f)
                )
                if (isRefreshing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value.format(),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 44.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = unit,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun SingleReadingCard() {
    val dotColor = MaterialTheme.colorScheme.primary

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .glassSurface()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
            val cy = size.height / 2f
            drawLine(
                color = dotColor.copy(alpha = 0.3f),
                start = Offset(0f, cy),
                end = Offset(size.width, cy),
                strokeWidth = 3f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 16f))
            )
            val center = Offset(size.width / 2f, cy)
            drawCircle(color = dotColor.copy(alpha = 0.18f), radius = 30f, center = center)
            drawCircle(color = dotColor, radius = 12f, center = center)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Solo hay una lectura",
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "El gráfico de tendencia aparecerá cuando el sensor registre al menos dos lecturas.",
            color = Color.Gray,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StateCard(
    icon: @Composable () -> Unit,
    title: String,
    message: String,
    actionLabel: String,
    onAction: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .glassSurface()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        icon()
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = message,
            color = Color.Gray,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = onAction) {
            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(actionLabel)
        }
    }
}

@Composable
private fun StatChip(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .glassSurface()
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, color = Color.Gray, fontSize = 11.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun TelemetryChart(
    data: List<Float>,
    modifier: Modifier = Modifier
) {
    val lineColor = MaterialTheme.colorScheme.primary
    val gridColor = Color.Gray.copy(alpha = 0.25f)

    Canvas(modifier = modifier) {
        if (data.size < 2) return@Canvas

        val verticalPadding = 12f
        val chartHeight = size.height - verticalPadding * 2

        val max = data.max()
        val min = data.min()
        val range = max - min

        fun yFor(value: Float): Float =
            if (range == 0f) size.height / 2f
            else verticalPadding + chartHeight - ((value - min) / range) * chartHeight

        val stepX = size.width / (data.size - 1)

        val gridLines = 4
        for (i in 0..gridLines) {
            val y = verticalPadding + chartHeight * i / gridLines
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 2f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f))
            )
        }

        val linePath = Path()
        data.forEachIndexed { index, value ->
            val x = index * stepX
            val y = yFor(value)
            if (index == 0) linePath.moveTo(x, y) else linePath.lineTo(x, y)
        }

        val fillPath = Path().apply {
            addPath(linePath)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(lineColor.copy(alpha = 0.30f), Color.Transparent),
                startY = 0f,
                endY = size.height
            )
        )

        drawPath(
            path = linePath,
            color = lineColor,
            style = Stroke(
                width = 8f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        val lastCenter = Offset((data.size - 1) * stepX, yFor(data.last()))
        drawCircle(color = lineColor.copy(alpha = 0.25f), radius = 22f, center = lastCenter)
        drawCircle(color = lineColor, radius = 11f, center = lastCenter)
    }
}