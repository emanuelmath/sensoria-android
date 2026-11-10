package com.example.sensoria.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sensoria.core.theme.PrimaryGreen
import com.example.sensoria.core.theme.VeryDarkGreen
import com.example.sensoria.core.theme.components.LocalToastController

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onLogout: () -> Unit,
    onChangeCompanyClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mi Perfil",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (uiState is ProfileUiState.Success) {
            val state = uiState as ProfileUiState.Success

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(PrimaryGreen.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.userName.take(1).uppercase(),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryGreen
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = state.userName, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
                    Text(text = state.email, color = Color.Gray, fontSize = 13.sp)

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = VeryDarkGreen.copy(alpha = 0.1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(14.dp))
                            Text(text = "Rol: ${state.userRole}", color = VeryDarkGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f).padding(end = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Business, contentDescription = null, tint = PrimaryGreen)
                        Column {
                            Text(text = "Empresa Activa", fontSize = 11.sp, color = Color.Gray)
                            Text(
                                text = state.currentCompany,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    TextButton(
                        onClick = onChangeCompanyClick,
                        enabled = state.numberOfCompanies > 1,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = PrimaryGreen,
                            disabledContentColor = Color.LightGray
                        )
                    ) {
                        Text("Cambiar", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        when (val state = uiState) {
            is ProfileUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = PrimaryGreen,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            is ProfileUiState.Error -> {
                Text(text = state.message, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
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
            .height(52.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Salir")
        Spacer(modifier = Modifier.width(8.dp))
        Text("Cerrar Sesión", fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}