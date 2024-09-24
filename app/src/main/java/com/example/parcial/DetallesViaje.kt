package com.example.parcial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.text.NumberFormat
import java.util.Locale

class DetallesViaje : AppCompatActivity() {

    private lateinit var tvDestino: TextView
    private lateinit var tvFechaInicio: TextView
    private lateinit var tvFechaFin: TextView
    private lateinit var tvActividades: TextView
    private lateinit var tvLugaresVisitar: TextView
    private lateinit var tvPresupuesto: TextView
    private lateinit var btnEditar: Button
    private lateinit var btnEliminar: Button
    private var indiceViaje: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalles_viaje)

        tvDestino = findViewById(R.id.tvDestino)
        tvFechaInicio = findViewById(R.id.tvFechaInicio)
        tvFechaFin = findViewById(R.id.tvFechaFin)
        tvActividades = findViewById(R.id.tvActividades)
        tvLugaresVisitar = findViewById(R.id.tvLugaresVisitar)
        tvPresupuesto = findViewById(R.id.tvPresupuesto)
        btnEditar = findViewById(R.id.btnEditar)
        btnEliminar = findViewById(R.id.btnEliminar)

        val extras = intent.extras
        if (extras != null) {
            val viajeJson = extras.getString("viaje")
            indiceViaje = extras.getInt("indice")

            cargarDatosViaje(viajeJson)
        }

        btnEditar.setOnClickListener {
            val intent = Intent(this, AddViaje::class.java)
            intent.putExtra("destino", tvDestino.text.toString())
            intent.putExtra("fechaInicio", tvFechaInicio.text.toString())
            intent.putExtra("fechaFin", tvFechaFin.text.toString())
            intent.putExtra("actividades", tvActividades.text.toString())
            intent.putExtra("lugaresVisitar", tvLugaresVisitar.text.toString())
            intent.putExtra("presupuesto", tvPresupuesto.text.toString().toIntOrNull() ?: 0)
            intent.putExtra("indice", indiceViaje)
            startActivityForResult(intent, 1)
        }

        btnEliminar.setOnClickListener {
            mostrarAlertaEliminar()
        }
    }

    private fun cargarDatosViaje(viajeJson: String?) {
        if (viajeJson != null) {
            val viaje = JSONObject(viajeJson)
            val presupuesto = viaje.getInt("presupuesto")

            tvDestino.text = viaje.getString("destino")
            tvFechaInicio.text = "Fecha de Inicio: ${viaje.getString("fechaInicio")}"
            tvFechaFin.text = "Fecha de Finalización: ${viaje.getString("fechaFin")}"
            tvActividades.text = viaje.getString("actividades")
            tvLugaresVisitar.text = viaje.getString("lugaresVisitar")
            tvPresupuesto.text = "Total: $${NumberFormat.getInstance(Locale("es", "ES")).format(presupuesto)}"
        }
    }

    private fun mostrarAlertaEliminar() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar viaje")
        builder.setMessage("¿Está seguro de borrar el viaje?")
        builder.setPositiveButton("Sí") { dialog, _ ->
            eliminarViaje(indiceViaje)
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            data?.let {
                val viajeModificado = JSONObject()
                viajeModificado.put("destino", it.getStringExtra("destino"))
                viajeModificado.put("fechaInicio", it.getStringExtra("fechaInicio"))
                viajeModificado.put("fechaFin", it.getStringExtra("fechaFin"))
                viajeModificado.put("actividades", it.getStringExtra("actividades"))
                viajeModificado.put("lugaresVisitar", it.getStringExtra("lugaresVisitar"))
                viajeModificado.put("presupuesto", it.getIntExtra("presupuesto", 0))

                actualizarViaje(this, viajeModificado, indiceViaje)
                finish()
            }
        }
    }

    private fun actualizarViaje(context: Context, viajeModificado: JSONObject, indice: Int) {
        val sharedPrefs = context.getSharedPreferences("viajes", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        val viajesArray = JSONArray(sharedPrefs.getString("viajes_list", "[]"))

        if (indice >= 0 && indice < viajesArray.length()) {
            viajesArray.put(indice, viajeModificado)
        }

        editor.putString("viajes_list", viajesArray.toString())
        editor.apply()
    }

    private fun eliminarViaje(indice: Int) {
        val sharedPrefs = getSharedPreferences("viajes", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        val viajesArray = JSONArray(sharedPrefs.getString("viajes_list", "[]"))

        if (indice >= 0 && indice < viajesArray.length()) {
            viajesArray.remove(indice)
            editor.putString("viajes_list", viajesArray.toString())
            editor.apply()
            finish()
        }
    }
}