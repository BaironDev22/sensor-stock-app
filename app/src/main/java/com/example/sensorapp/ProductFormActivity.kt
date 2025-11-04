package com.example.sensorapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.activity.ComponentActivity

/**
 * Activity con formulario (XML) para ingresar los datos del producto a controlar.
 * Requisitos: varios inputs (EditText, Spinner, RadioGroup) y botón para enviar.
 */
class ProductFormActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_form)

        val edtProductName = findViewById<EditText>(R.id.edtProductName)
        val edtQuantity = findViewById<EditText>(R.id.edtQuantity)
        val spinnerSensor = findViewById<Spinner>(R.id.spinnerSensorType)
        val radioGroup = findViewById<RadioGroup>(R.id.radioAutoDiscount)
        val btnSubmit = findViewById<Button>(R.id.btnSubmitProduct)

        // Rellenamos el spinner con opciones simples
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.sensor_types,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSensor.adapter = adapter

        btnSubmit.setOnClickListener {
            val name = edtProductName.text.toString().ifBlank { "-" }
            val quantity = edtQuantity.text.toString().ifBlank { "0" }
            val sensorType = spinnerSensor.selectedItem?.toString() ?: "-"
            val autoDiscount = when (radioGroup.checkedRadioButtonId) {
                R.id.radioYes -> getString(R.string.yes)
                R.id.radioNo -> getString(R.string.no)
                else -> getString(R.string.no)
            }

            // Enviamos los datos a SummaryActivity por Intent
            val intent = Intent(this, SummaryActivity::class.java).apply {
                putExtra("product_name", name)
                putExtra("quantity", quantity)
                putExtra("sensor_type", sensorType)
                putExtra("auto_discount", autoDiscount)
            }
            startActivity(intent)
        }
    }
}
