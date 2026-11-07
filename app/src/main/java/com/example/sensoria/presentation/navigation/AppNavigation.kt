package com.example.sensoria.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.sensoria.presentation.auth.LoginScreen
import com.example.sensoria.presentation.auth.LoginViewModel
import com.example.sensoria.presentation.dashboard.MainScreen
import com.example.sensoria.presentation.dashboard.DeviceDetailScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = LoginRoute) {

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