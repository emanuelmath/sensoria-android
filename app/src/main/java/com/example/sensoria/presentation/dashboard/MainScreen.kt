package com.example.sensoria.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sensoria.core.theme.VeryDarkGreen
import com.example.sensoria.core.theme.PrimaryGreen
import com.example.sensoria.core.theme.BackgroundLight
import com.example.sensoria.presentation.profile.ProfileScreen
import com.example.sensoria.presentation.profile.ProfileViewModel
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import com.example.sensoria.core.theme.components.LocalToastController
import com.example.sensoria.core.theme.components.ToastType
import com.example.sensoria.presentation.support.SupportScreen
import com.example.sensoria.presentation.support.SupportViewModel

enum class MainTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    DEVICES("Dispositivos", Icons.Filled.Home, Icons.Outlined.Home),
    ALERTS("Alertas", Icons.Filled.Notifications, Icons.Outlined.Notifications),
    SUPPORT("Soporte", Icons.Filled.Email, Icons.Outlined.Email),
    PROFILE("Perfil", Icons.Filled.Person, Icons.Outlined.Person)
}
@Composable
fun MainScreen(
    companyId: String,
    onNavigateToDeviceDetail: (String) -> Unit,
    onNavigateToTicketDetail: (Int) -> Unit,
    onCreateTicket: () -> Unit,
    onChangeCompanyClick: () -> Unit = {},
    onLogout: () -> Unit
) {
    val showToast = LocalToastController.current
    var selectedTab by remember { mutableStateOf(MainTab.DEVICES) }
    var showToastWelcome by remember { mutableStateOf(true) }

//    LaunchedEffect(Unit) {
//        if(showToastWelcome) {
//            showToast("¡Bienvenido de nuevo!", ToastType.SUCCESS)
//        }
//    }

    Scaffold(
        containerColor = BackgroundLight,
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundLight)
                    .navigationBarsPadding()
                    .padding(top = 8.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MainTab.entries.forEach { tab ->
                    val isSelected = selectedTab == tab

                    val containerColor by animateColorAsState(
                        targetValue = if (isSelected) PrimaryGreen else VeryDarkGreen,
                        label = "tabContainer"
                    )
                    val iconColor by animateColorAsState(
                        targetValue = if (isSelected) VeryDarkGreen else Color.White.copy(alpha = 0.75f),
                        label = "tabIcon"
                    )

                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .shadow(6.dp, RoundedCornerShape(18.dp))
                            .clip(RoundedCornerShape(18.dp))
                            .background(containerColor)
                            .clickable { selectedTab = tab },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                            contentDescription = tab.title,
                            tint = iconColor,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                MainTab.DEVICES -> {
                    val viewModel: DashboardViewModel = hiltViewModel()
                    DevicesScreen(companyId, viewModel, onNavigateToDeviceDetail)
                }
                MainTab.ALERTS -> Text("Alertas", Modifier.padding(16.dp))
                MainTab.SUPPORT -> {
                    val supportViewModel: SupportViewModel = hiltViewModel()
                    SupportScreen(
                        viewModel = supportViewModel,
                        onNavigateToDetail = onNavigateToTicketDetail,
                        onCreateTicket = onCreateTicket
                    )
                }
                MainTab.PROFILE -> {
                    val profileViewModel: ProfileViewModel = hiltViewModel()
                    ProfileScreen(
                        viewModel = profileViewModel,
                        onLogout = onLogout,
                        onChangeCompanyClick = onChangeCompanyClick
                    )
                }
            }
        }
    }
}