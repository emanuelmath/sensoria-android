package com.example.sensoria.presentation.navigation

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.sensoria.R
import com.example.sensoria.core.theme.components.LocalToastController
import com.example.sensoria.core.theme.components.SensoriaToast
import com.example.sensoria.core.theme.components.ToastEvent
import com.example.sensoria.core.theme.components.ToastType
import com.example.sensoria.data.local.AuthPreferences
import com.example.sensoria.domain.models.Company
import com.example.sensoria.presentation.auth.LoginScreen
import com.example.sensoria.presentation.auth.LoginViewModel
import com.example.sensoria.presentation.auth.CompanySelectionScreen
import com.example.sensoria.presentation.dashboard.MainScreen
import com.example.sensoria.presentation.dashboard.DeviceDetailScreen
import com.example.sensoria.presentation.profile.changecompany.ChangeCompanyScreen
import com.example.sensoria.presentation.support.create.CreateTicketScreen
import com.example.sensoria.presentation.support.detail.TicketDetailScreen
import com.example.sensoria.presentation.support.detail.TicketDetailViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun AppNavigation(authPreferences: AuthPreferences) {
    val navController = rememberNavController()

    val token by authPreferences.getAuthToken.collectAsState(initial = "LOADING")
    val savedCompanyId by authPreferences.getCurrentCompanyId.collectAsState(initial = "LOADING")

    var toastEvent by remember { mutableStateOf<ToastEvent?>(null) }

    val showGlobalToast: (String, ToastType) -> Unit = { message, type ->
        toastEvent = ToastEvent(message, type)
    }

    if (token == "LOADING" || savedCompanyId == "LOADING") {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_sensoria_logo),
                contentDescription = "Logo de Sensoría",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 2.dp)
            )
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    val SESSION_ACTIVE = !token.isNullOrEmpty() && !savedCompanyId.isNullOrEmpty()

    val startDestination = if (SESSION_ACTIVE) {
        MainRoute(companyId = savedCompanyId!!)
    } else {
        LoginRoute
    }
    CompositionLocalProvider(LocalToastController provides showGlobalToast) {
    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = startDestination) {

            composable<LoginRoute> {
                val loginViewModel: LoginViewModel = hiltViewModel()
                LoginScreen(
                    viewModel = loginViewModel,
                    onLoginSuccess = { companyId ->
                        showGlobalToast("¡Bienvenido a Sensoría!", ToastType.SUCCESS)
                        navController.navigate(MainRoute(companyId = companyId)) {
                            popUpTo(LoginRoute) { inclusive = true }
                        }
                    },
                    onRequireSelection = { companies ->
                        val companiesJson = Uri.encode(Json.encodeToString(companies))
                        navController.navigate(CompanySelectionRoute(companiesJson = companiesJson))
                    }
                )
            }

            composable<CompanySelectionRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<CompanySelectionRoute>()
                val decodedJson = Uri.decode(route.companiesJson)
                val companies = Json.decodeFromString<List<Company>>(decodedJson)

                CompanySelectionScreen(
                    companies = companies,
                    authPreferences = authPreferences,
                    onCompanySelected = { companyId ->
                        showGlobalToast("¡Bienvenido a Sensoría!", ToastType.SUCCESS)
                        navController.navigate(MainRoute(companyId = companyId)) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable<MainRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<MainRoute>()
                MainScreen(
                    companyId = route.companyId,
                    onNavigateToDeviceDetail = { deviceId ->
                        navController.navigate(DeviceDetailRoute(deviceId = deviceId))
                    },
                    onNavigateToTicketDetail = { ticketId ->
                        navController.navigate(TicketDetailRoute(id = ticketId))
                    },
                    onCreateTicket = {
                        navController.navigate(CreateTicketRoute)
                    },
                    onChangeCompanyClick = {
                        navController.navigate(ChangeCompanyRoute)
                    },
                    onLogout = {
                        navController.navigate(LoginRoute) {
                            popUpTo(0) { inclusive = true }
                        }
                        showGlobalToast("¡Hasta la próxima!", ToastType.SUCCESS)
                    }
                )
            }

            composable<DeviceDetailRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<DeviceDetailRoute>()
                DeviceDetailScreen(
                    deviceId = route.deviceId,
                    onBack = { navController.popBackStack() }
                )
            }

            composable<TicketDetailRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<TicketDetailRoute>()
                TicketDetailScreen(
                    ticketId = route.id,
                    onBack = { navController.popBackStack() }
                )
            }

            composable<CreateTicketRoute> {
                CreateTicketScreen(
                    onBack = { navController.popBackStack() },
                    onSuccessCreation = { newTicketId ->
                        showGlobalToast("Ticket creado exitosamente", ToastType.SUCCESS)
                        navController.popBackStack()
                        navController.navigate(TicketDetailRoute(id = newTicketId))
                    }
                )
            }

            composable<ChangeCompanyRoute> {
                ChangeCompanyScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }


        SensoriaToast(
            message = toastEvent?.message ?: "",
            type = toastEvent?.type ?: ToastType.INFO,
            isVisible = toastEvent != null,
            onDismiss = { toastEvent = null }
        )
        }
    }
}