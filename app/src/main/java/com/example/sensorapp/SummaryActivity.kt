package com.example.sensorapp

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Activity que muestra de forma ordenada los datos recibidos por Intent desde el formulario.
 * Añade un botón para simular la salida del producto y notificar al usuario con opción a descontar.
 */
class SummaryActivity : ComponentActivity() {

    private val CHANNEL_ID = "sensor_alerts_channel"
    private val NOTIFICATION_ID = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        val tvName = findViewById<TextView>(R.id.tvSummaryName)
        val tvQuantity = findViewById<TextView>(R.id.tvSummaryQuantity)
        val tvSensor = findViewById<TextView>(R.id.tvSummarySensor)
        val tvAuto = findViewById<TextView>(R.id.tvSummaryAuto)
        val btnSimulate = findViewById<Button>(R.id.btnSimulate)

        var name = intent.getStringExtra("product_name") ?: "-"
        var quantity = intent.getStringExtra("quantity") ?: "0"
        val sensor = intent.getStringExtra("sensor_type") ?: "-"
        val auto = intent.getStringExtra("auto_discount") ?: getString(R.string.no)

        // Si la Activity fue lanzada desde la notificación con la acción de descontar
        val action = intent.getStringExtra("action")
        if (action == "discount") {
            // Aplicar descuento de 1 unidad (si es posible)
            val q = quantity.toIntOrNull() ?: 0
            if (q > 0) quantity = (q - 1).toString()
            Toast.makeText(this, getString(R.string.discount_applied), Toast.LENGTH_SHORT).show()
        }

        tvName.text = name
        tvQuantity.text = quantity
        tvSensor.text = sensor
        tvAuto.text = auto

        createNotificationChannelIfNeeded()

        btnSimulate.setOnClickListener {
            // Mostrar diálogo local preguntando si desea descontar
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.simulate_dialog_title))
                .setMessage(getString(R.string.simulate_dialog_message, name))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    // Descontar localmente y actualizar UI
                    val q = tvQuantity.text.toString().toIntOrNull() ?: 0
                    if (q > 0) {
                        val newQ = q - 1
                        tvQuantity.text = newQ.toString()
                        Toast.makeText(this, getString(R.string.discount_applied), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, getString(R.string.no_stock), Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton(getString(R.string.no), null)
                .show()

            // Además, publicar una notificación con acción para descontar
            sendSensorNotification(name)
        }
    }

    private fun createNotificationChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_name)
            val descriptionText = getString(R.string.notification_channel_description)
            val importance = android.app.NotificationManager.IMPORTANCE_DEFAULT
            val channel = android.app.NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: android.app.NotificationManager =
                getSystemService(android.app.NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendSensorNotification(productName: String) {
        // Intent para la acción 'Descontar' que reinicia esta Activity con extra 'action=discount'
        val discountIntent = Intent(this, SummaryActivity::class.java).apply {
            putExtra("action", "discount")
            // Mantener los mismos datos del producto (opcional)
            putExtra("product_name", productName)
            putExtra("quantity", findViewById<TextView>(R.id.tvSummaryQuantity).text.toString())
        }

        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT else PendingIntent.FLAG_UPDATE_CURRENT

        val discountPendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, discountIntent, flags)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_text, productName))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(0, getString(R.string.discount_action), discountPendingIntent)

        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }
}
