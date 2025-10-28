package com.example.sensorapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.* // Usamos Material3
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import java.io.IOException

// Importa los iconos necesarios para el inventario
import androidx.compose.material.icons.filled.Analytics // Para métricas de stock
import androidx.compose.material.icons.filled.DevicesOther // Icono por defecto

data class SensorData(val name: String, val value: String, val icon: ImageVector)

@OptIn(ExperimentalMaterial3Api::class) // Anotación necesaria para TopAppBar
@Composable
fun DashboardScreen() {
    // El estado para los datos, ahora como una lista de objetos SensorData
    var sensorDataList by remember { mutableStateOf<List<SensorData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val esp32Url = "http://192.168.1.100/" // La IP

    // LaunchedEffect para cargar los datos
    LaunchedEffect(key1 = Unit) {
        try {
            isLoading = true
            errorMessage = null
            // Nota: Se asume que ApiService.kt está en modo SIMULADO
            val responseBody = ApiService.fetchEsp32Data(esp32Url)

            // Aquí parseamos la respuesta simulada para crear la lista de SensorData
            sensorDataList = parseSimulatedData(responseBody)

        } catch (e: IOException) {
            errorMessage = "Error al conectarse: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            // Barra superior de la aplicación
            TopAppBar(
                title = { Text("Control de Inventario IR") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Color de fondo de la barra
                    titleContentColor = MaterialTheme.colorScheme.onPrimary // Color del título
                )
            )
        },
        floatingActionButton = {
            // Botón flotante para agregar elementos (productos)
            FloatingActionButton(
                onClick = { /* TODO: Implementar navegación o diálogo para agregar producto */ },
                containerColor = MaterialTheme.colorScheme.tertiary // Color del botón flotante
            ) {
                Icon(Icons.Filled.Add, "Agregar nuevo producto")
            }
        },
        // El contenido principal de la pantalla
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // Aplica el padding del Scaffold
                    .padding(horizontal = 16.dp, vertical = 8.dp), // Padding adicional
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                if (isLoading) {
                    // Muestra un indicador de carga mientras se obtienen los datos
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    Text("Conectando al sensor y cargando stock...", modifier = Modifier.padding(top = 8.dp))
                } else if (errorMessage != null) {
                    // Muestra el mensaje de error si la conexión falla
                    Text(
                        text = errorMessage ?: "Error desconocido",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    // Si los datos están cargados, los muestra en un LazyColumn
                    if (sensorDataList.isEmpty()) {
                        Text("No hay datos de inventario disponibles.", modifier = Modifier.padding(16.dp))
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp), // Espacio entre tarjetas
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(sensorDataList) { sensor ->
                                SensorCard(sensor = sensor)
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun SensorCard(sensor: SensorData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp), // Altura fija para las tarjetas
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant) // Color de la tarjeta
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = sensor.icon,
                contentDescription = sensor.name,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary // Color del ícono
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = sensor.name,
                    style = MaterialTheme.typography.titleMedium, // Estilo del nombre
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = sensor.value,
                    style = MaterialTheme.typography.bodyLarge, // Estilo del valor
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Función para parsear los datos simulados en una lista de SensorData
fun parseSimulatedData(data: String): List<SensorData> {
    return data.split("\n").mapNotNull { line ->
        val parts = line.split(":", limit = 2)
        if (parts.size == 2) {
            val name = parts[0].trim()
            val value = parts[1].trim()
            val icon = when (name) {
                "Unidades Restantes" -> Icons.Filled.Inventory // Para el stock total
                "Última Acción" -> Icons.Filled.SwapHoriz // Para el movimiento detectado (Entrada/Salida)
                "Hora de Último Movimiento" -> Icons.Filled.Update // Para el timestamp
                "ID de Producto Monitorizado" -> Icons.Filled.Analytics // Para el ID del producto
                else -> Icons.Filled.DevicesOther // Icono por defecto
            }
            SensorData(name, value, icon)
        } else null
    }
}


