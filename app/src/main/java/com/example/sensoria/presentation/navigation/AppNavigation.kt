package com.example.sensoria.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.sensoria.presentation.auth.LoginScreen
import com.example.sensoria.presentation.auth.LoginViewModel
import com.example.sensoria.presentation.dashboard.DashboardScreen
import com.example.sensoria.presentation.dashboard.DashboardViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = LoginRoute) {

        composable<LoginRoute> {
            val loginViewModel: LoginViewModel = hiltViewModel()

            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = { companyId ->
                    navController.navigate(DashboardRoute(companyId = companyId)) {
                        popUpTo(LoginRoute) { inclusive = true }
                    }
                }
            )
        }

        composable<DashboardRoute> { backStackEntry ->
            val dashboardRoute = backStackEntry.toRoute<DashboardRoute>()
            val dashboardViewModel: DashboardViewModel = hiltViewModel()

            DashboardScreen(
                companyId = dashboardRoute.companyId,
                viewModel = dashboardViewModel
            )
        }
    }
}