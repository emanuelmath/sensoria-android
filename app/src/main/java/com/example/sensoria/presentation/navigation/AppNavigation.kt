package com.example.sensoria.presentation.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.sensoria.data.local.AuthPreferences
import com.example.sensoria.presentation.auth.LoginScreen
import com.example.sensoria.presentation.auth.LoginViewModel
import com.example.sensoria.presentation.dashboard.MainScreen
import com.example.sensoria.presentation.dashboard.DeviceDetailScreen

@Composable
fun AppNavigation(authPreferences: AuthPreferences) {
    val navController = rememberNavController()

    val token by authPreferences.getAuthToken.collectAsState(initial = "LOADING")
    val savedCompanyId by authPreferences.getCurrentCompanyId.collectAsState(initial = "LOADING")

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

    NavHost(navController = navController, startDestination = startDestination) {

        composable<LoginRoute> {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = { companyId ->
                    navController.navigate(MainRoute(companyId = companyId)) {
                        popUpTo(LoginRoute) { inclusive = true }
                    }
                }
            )
        }

        composable<MainRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<MainRoute>()
            MainScreen(
                companyId = route.companyId,
                onNavigateToDetail = { deviceId ->
                    navController.navigate(DeviceDetailRoute(deviceId = deviceId))
                },
                onLogout = {
                    navController.navigate(LoginRoute) {
                        popUpTo(0) { inclusive = true }
                    }
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
    }
}