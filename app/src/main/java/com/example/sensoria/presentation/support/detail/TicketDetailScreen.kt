package com.example.sensoria.presentation.support.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sensoria.core.theme.PrimaryGreen
import com.example.sensoria.domain.models.TicketMessage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailScreen(
    ticketId: Int,
    onBack: () -> Unit,
    viewModel: TicketDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(ticketId) {
        viewModel.loadTicket(ticketId)
    }

    val uiState by viewModel.uiState.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Hilo de Soporte", fontWeight = FontWeight.Bold)
                        if (uiState is TicketDetailUiState.Success) {
                            Text(
                                text = (uiState as TicketDetailUiState.Success).ticketStatus,
                                fontSize = 12.sp,
                                color = PrimaryGreen
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            if (uiState is TicketDetailUiState.Success && (uiState as TicketDetailUiState.Success).ticketStatus != "CERRADO") {
                BottomReplyBar(
                    inputText = inputText,
                    onTextChange = { inputText = it },
                    isSending = (uiState as TicketDetailUiState.Success).isSending,
                    onSend = {
                        viewModel.sendMessage(inputText)
                        inputText = ""
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is TicketDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = PrimaryGreen)
                }
                is TicketDetailUiState.Error -> {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center))
                }
                is TicketDetailUiState.Success -> {
                    LaunchedEffect(state.messages.size) {
                        if (state.messages.isNotEmpty()) {
                            listState.animateScrollToItem(state.messages.size - 1)
                        }
                    }

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = state.ticketSubject,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }

                        items(state.messages) { message ->
                            MessageBubble(message = message)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(message: TicketMessage) {
    val alignment = if (message.isMine) Alignment.CenterEnd else Alignment.CenterStart
    val bubbleColor = if (message.isMine) PrimaryGreen else Color.White
    val textColor = if (message.isMine) Color.White else Color.Black

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = alignment) {
        Card(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isMine) 16.dp else 0.dp,
                bottomEnd = if (message.isMine) 0.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(containerColor = bubbleColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (!message.isMine) {
                    Text(text = "${message.userName} (${message.userRole})", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = PrimaryGreen)
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Text(text = message.message, color = textColor, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = message.createdAt, fontSize = 9.sp, color = textColor.copy(alpha = 0.7f), modifier = Modifier.align(Alignment.End))
            }
        }
    }
}

@Composable
private fun BottomReplyBar(
    inputText: String,
    onTextChange: (String) -> Unit,
    isSending: Boolean,
    onSend: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .navigationBarsPadding()
            .imePadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = inputText,
            onValueChange = onTextChange,
            placeholder = { Text("Escribe tu respuesta...") },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryGreen,
                unfocusedBorderColor = Color.LightGray
            ),
            maxLines = 3
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = onSend,
            enabled = inputText.isNotBlank() && !isSending,
            modifier = Modifier.background(PrimaryGreen, RoundedCornerShape(50))
        ) {
            if (isSending) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
            } else {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar", tint = Color.White)
            }
        }
    }
}