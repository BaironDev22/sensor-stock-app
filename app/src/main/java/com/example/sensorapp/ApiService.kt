package com.example.sensorapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

// Usamos un 'object' para crear un Singleton.
object ApiService {

    // Cliente OkHttp original (MANTENIDO para la versión REAL)
    private val client = OkHttpClient()

    /**
     * Función que simula la obtención de datos del ESP32.
     * * NOTA: Cuando el ESP32 esté funcionando en la red,
     * descomenta la sección 'CÓDIGO REAL' y comenta la sección 'CÓDIGO SIMULADO'.
     */
    suspend fun fetchEsp32Data(url: String): String = withContext(Dispatchers.IO) {

        // =======================================================
        // CÓDIGO SIMULADO (para pruebas de UI y parsing)
        // =======================================================

        // 1. Simula la latencia de red.
        delay(2000)

        // 2. Devuelve los datos de inventario simulados.
        // Los nombres de las claves deben coincidir con los de parseSimulatedData()
        return@withContext """
ID de Producto Monitorizado: A4789
Unidades Restantes: 145
Última Acción: SALIDA (-1)
Hora de Último Movimiento: 2025-10-28 16:35:12
        """.trimIndent()

        // =======================================================
        // CÓDIGO REAL (OkHttp para conexión física)
        // =======================================================

        /* val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) {
            throw IOException("Código de respuesta inesperado: ${response.code}")
        }

        response.body?.string() ?: throw IOException("Cuerpo de respuesta vacío")
        */
    }
}



