package com.example.sensoria.presentation.support.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sensoria.core.theme.PrimaryGreen
import com.example.sensoria.core.theme.components.LocalToastController
import com.example.sensoria.core.theme.components.ToastType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTicketScreen(
    onBack: () -> Unit,
    onSuccessCreation: (Int) -> Unit,
    viewModel: CreateTicketViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var subject by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var expandedCategory by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("TECNICO") }
    val categories = listOf("TECNICO", "FACTURACION", "GENERAL", "DISPOSITIVO")

    var expandedPriority by remember { mutableStateOf(false) }
    var selectedPriority by remember { mutableStateOf("MEDIA") }
    val priorities = listOf("BAJA", "MEDIA", "ALTA", "CRITICA")

    val showToast = LocalToastController.current

    LaunchedEffect(uiState) {
        when (uiState) {
            is CreateTicketUiState.Success -> {
                onSuccessCreation((uiState as CreateTicketUiState.Success).newTicketId)
                viewModel.resetState()
            }
            is CreateTicketUiState.Error -> {
                showToast((uiState as CreateTicketUiState.Error).message, ToastType.ERROR)
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Abrir Nuevo Ticket", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Detalla el inconveniente para que nuestro equipo pueda ayudarte rápidamente.", color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text("Asunto del problema") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = expandedCategory,
                onExpandedChange = { expandedCategory = !expandedCategory }
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen)
                )
                ExposedDropdownMenu(
                    expanded = expandedCategory,
                    onDismissRequest = { expandedCategory = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    categories.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                selectedCategory = selectionOption
                                expandedCategory = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = expandedPriority,
                onExpandedChange = { expandedPriority = !expandedPriority }
            ) {
                OutlinedTextField(
                    value = selectedPriority,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Prioridad") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPriority) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen)
                )
                ExposedDropdownMenu(
                    expanded = expandedPriority,
                    onDismissRequest = { expandedPriority = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    priorities.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                selectedPriority = selectionOption
                                expandedPriority = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción detallada") },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.createTicket(subject, selectedCategory, selectedPriority, description) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                enabled = uiState !is CreateTicketUiState.Loading
            ) {
                if (uiState is CreateTicketUiState.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Registrar Ticket", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}