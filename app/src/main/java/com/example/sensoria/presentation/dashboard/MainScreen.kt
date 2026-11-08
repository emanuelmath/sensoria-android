package com.example.sensoria.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sensoria.presentation.profile.ProfileScreen
import com.example.sensoria.presentation.profile.ProfileViewModel

enum class MainTab(val title: String, val icon: ImageVector) {
    DEVICES("Dispositivos", Icons.Default.Home),
    ALERTS("Alertas", Icons.Default.Notifications),
    SUPPORT("Soporte", Icons.Default.Email),
    PROFILE("Perfil", Icons.Default.Person)
}

@Composable
fun MainScreen(
    companyId: String,
    onNavigateToDetail: (String) -> Unit,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(MainTab.DEVICES) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                MainTab.entries.forEach { tab ->
                    NavigationBarItem(
                        icon = { Icon(tab.icon, contentDescription = tab.title) },
                        label = { Text(tab.title, fontWeight = FontWeight.SemiBold) },
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.surface,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        // Contenedor que aísla el contenido de la barra
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                MainTab.DEVICES -> {
                    val viewModel: DashboardViewModel = hiltViewModel()
                    DevicesScreen(companyId, viewModel, onNavigateToDetail)
                }
                MainTab.ALERTS -> Text("Alertas", Modifier.padding(16.dp))
                MainTab.SUPPORT -> Text("Soporte", Modifier.padding(16.dp))
                MainTab.PROFILE -> {
                    val profileViewModel: ProfileViewModel = hiltViewModel()
                    ProfileScreen(
                        viewModel = profileViewModel,
                        onLogout = onLogout
                    )
                }
            }
        }
    }
}