package com.example.sensoria.presentation.support

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sensoria.core.theme.PrimaryGreen
import com.example.sensoria.core.theme.glassSurface
import com.example.sensoria.domain.models.Ticket

@Composable
fun SupportScreen(
    viewModel: SupportViewModel = hiltViewModel(),
    onNavigateToDetail: (Int) -> Unit = {},
    onCreateTicket: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateTicket,
                containerColor = PrimaryGreen,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Ticket")
            }
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
                    Text(text = "Soporte Técnico", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = PrimaryGreen)
                    Text(text = "Solicitudes y asistencia B2B", fontSize = 12.sp, color = Color.Gray)
                }
                IconButton(onClick = { viewModel.loadTickets() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refrescar", tint = PrimaryGreen)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            when (val state = uiState) {
                is SupportUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = PrimaryGreen)
                    }
                }

                is SupportUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .glassSurface()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = state.message, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(onClick = { viewModel.loadTickets() }) {
                            Text("Reintentar")
                        }
                    }
                }

                is SupportUiState.Success -> {
                    if (state.tickets.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .glassSurface()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No hay tickets de soporte registrados.", color = Color.Gray, fontSize = 14.sp)
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.tickets) { ticket ->
                                TicketCard(ticket = ticket, onClick = { onNavigateToDetail(ticket.id) })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TicketCard(ticket: Ticket, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = ticket.ticketNumber, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = PrimaryGreen)
                Box(
                    modifier = Modifier
                        .background(Color(0xFF2563EB).copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(ticket.status, color = Color(0xFF2563EB), fontWeight = FontWeight.Bold, fontSize = 10.sp)
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = ticket.subject, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Cat: ${ticket.category}", fontSize = 12.sp, color = Color.Gray)
                Text(text = "Prio: ${ticket.priority}", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}