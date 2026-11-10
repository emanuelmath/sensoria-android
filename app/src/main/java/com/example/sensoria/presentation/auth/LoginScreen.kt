package com.example.sensoria.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensoria.core.theme.glassSurface
import com.example.sensoria.R
import com.example.sensoria.core.theme.components.SensoriaToast
import com.example.sensoria.domain.models.Company

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (String) -> Unit,
    onRequireSelection: (List<Company>) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            val companyId = (uiState as LoginUiState.Success).companyId
            onLoginSuccess(companyId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    )
                )
            )
            .imePadding()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .glassSurface()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_sensoria_logo),
                contentDescription = "Logo de Sensoría",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 2.dp)
            )
            Text(
                text = "Sensoría",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Acceso B2B Workspace",
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo corporativo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            when (uiState) {
                is LoginUiState.Loading -> {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
                is LoginUiState.Success -> {
                    LaunchedEffect(Unit) {
                        onLoginSuccess((uiState as LoginUiState.Success).companyId)
                       // SensoriaToast((uiState as LoginUiState.Success).message)
                    }

                }
                is LoginUiState.RequireCompanySelection -> {
                    LaunchedEffect(Unit) {
                        onRequireSelection((uiState as LoginUiState.RequireCompanySelection).companies)
                        viewModel.resetState()
                    }
                }
                is LoginUiState.Error -> {
                    val errorMsg = (uiState as LoginUiState.Error).message
                    Text(
                        text = errorMsg,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    LoginButton(email, password, viewModel)
                }
                else -> {
                    LoginButton(email, password, viewModel)
                }
            }
        }
    }
}

@Composable
private fun LoginButton(email: String, pass: String, viewModel: LoginViewModel) {
    Button(
        onClick = { viewModel.login(email, pass) },
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Text("Ingresar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}