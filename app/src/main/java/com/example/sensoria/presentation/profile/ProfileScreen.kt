package com.example.sensoria.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensoria.core.theme.glassSurface
import com.example.sensoria.presentation.auth.LoginUiState

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogout: () -> Unit
) {
    val userName by viewModel.userName.collectAsState()
    val companyName by viewModel.companyName.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mi Perfil",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .glassSurface()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userName.take(1).uppercase(),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = userName, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(text = companyName, color = Color.Gray, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        when (uiState) {
            is ProfileUiState.Loading -> {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }

            is ProfileUiState.Error -> {
                val errorMsg = (uiState as LoginUiState.Error).message
                Text(
                    text = errorMsg,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(16.dp))
                LogoutButton(viewModel, onLogout)
            }

            else -> {
               LogoutButton(viewModel, onLogout)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun LogoutButton(viewModel: ProfileViewModel, onLogout: () -> Unit) {
    Button(
        onClick = { viewModel.logout(onLogoutComplete = onLogout) },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Salir")
        Spacer(modifier = Modifier.width(8.dp))
        Text("Cerrar Sesión", fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}