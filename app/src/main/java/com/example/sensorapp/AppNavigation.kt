package com.example.sensorapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {

    // El controlador que maneja el estado de la navegación
    val navController = rememberNavController()

    // Define el NavHost: el contenedor de todas las pantallas
    NavHost(
        navController = navController,
        startDestination = "login", // La primera pantalla que se muestra
        modifier = Modifier.fillMaxSize()
    ) {

        // Define la ruta 'login'
        composable("login") {
            // Llama a LoginScreen, pasándole el controlador para que pueda navegar
            LoginScreen(navController = navController)
        }

        // Define la ruta 'dashboard'
        composable("dashboard") {
            // Llama a DashboardScreen
            DashboardScreen()
        }
    }
}
