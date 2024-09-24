package com.example.parcial

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class AddViaje : AppCompatActivity() {

    private lateinit var etNombreDestino: EditText
    private lateinit var sbPresupuesto: SeekBar
    private lateinit var tvPresupuesto: TextView
    private lateinit var etFechaInicio: EditText
    private lateinit var etFechaFin: EditText
    private lateinit var etActividades: EditText
    private lateinit var etLugaresVisitar: EditText
    private lateinit var btnAgregarViaje: Button

    private var indiceViaje: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_viaje)

        etNombreDestino = findViewById(R.id.etNombreDestino)
        sbPresupuesto = findViewById(R.id.sbPresupuesto)
        tvPresupuesto = findViewById(R.id.tvPresupuesto)
        etFechaInicio = findViewById(R.id.etFechaInicio)
        etFechaFin = findViewById(R.id.etFechaFin)
        etActividades = findViewById(R.id.etActividades)
        etLugaresVisitar = findViewById(R.id.etLugaresVisitar)
        btnAgregarViaje = findViewById(R.id.btnAgregarViaje)

        val extras = intent.extras
        if (extras != null) {
            etNombreDestino.setText(extras.getString("destino", ""))
            etFechaInicio.setText(extras.getString("fechaInicio", ""))
            etFechaFin.setText(extras.getString("fechaFin", ""))
            etActividades.setText(extras.getString("actividades", ""))
            etLugaresVisitar.setText(extras.getString("lugaresVisitar", ""))
            sbPresupuesto.progress = extras.getInt("presupuesto", 0) // Configurar el SeekBar
            indiceViaje = extras.getInt("indice", -1) // Obtener el índice
        }

        val numberFormat = NumberFormat.getInstance(Locale("es", "ES"))

        sbPresupuesto.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvPresupuesto.text = "Total de presupuesto: $${numberFormat.format(progress)}"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val calendar = Calendar.getInstance()
        val dateSetListener = { editText: EditText ->
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                editText.setText("$dayOfMonth/${month + 1}/$year")
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        etFechaInicio.setOnClickListener { dateSetListener(etFechaInicio) }
        etFechaFin.setOnClickListener { dateSetListener(etFechaFin) }

        btnAgregarViaje.setOnClickListener {
            guardarViaje()
        }
    }

    private fun guardarViaje() {
        val viajeJson = JSONObject()
        viajeJson.put("destino", etNombreDestino.text.toString())
        viajeJson.put("fechaInicio", etFechaInicio.text.toString())
        viajeJson.put("fechaFin", etFechaFin.text.toString())
        viajeJson.put("actividades", etActividades.text.toString())
        viajeJson.put("lugaresVisitar", etLugaresVisitar.text.toString())
        viajeJson.put("presupuesto", sbPresupuesto.progress)

        if (indiceViaje == -1) {
            agregarViaje(this, viajeJson)
        } else {
            val intent = intent
            intent.putExtra("destino", etNombreDestino.text.toString())
            intent.putExtra("fechaInicio", etFechaInicio.text.toString())
            intent.putExtra("fechaFin", etFechaFin.text.toString())
            intent.putExtra("actividades", etActividades.text.toString())
            intent.putExtra("lugaresVisitar", etLugaresVisitar.text.toString())
            intent.putExtra("presupuesto", sbPresupuesto.progress)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun agregarViaje(context: Context, viajeJson: JSONObject) {
        val sharedPrefs = context.getSharedPreferences("viajes", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        val viajesArray = JSONArray(sharedPrefs.getString("viajes_list", "[]"))
        viajesArray.put(viajeJson)
        editor.putString("viajes_list", viajesArray.toString())
        editor.apply()
        finish()
    }
}