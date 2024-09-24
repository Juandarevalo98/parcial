package com.example.parcial

import android.content.Context
import android.widget.EditText
import android.widget.SeekBar
import org.json.JSONArray
import org.json.JSONObject

object ActualizarViaje {
    fun actualizar(viajeOriginal: JSONObject,
                   etNombreDestino: EditText,
                   sbPresupuesto: SeekBar,
                   etFechaInicio: EditText,
                   etFechaFin: EditText,
                   etActividades: EditText,
                   etLugaresVisitar: EditText,
                   context: Context) {
        val nombreDestino = etNombreDestino.text.toString()
        val presupuesto = sbPresupuesto.progress
        val fechaInicio = etFechaInicio.text.toString()
        val fechaFin = etFechaFin.text.toString()
        val actividades = etActividades.text.toString()
        val lugaresVisitar = etLugaresVisitar.text.toString()

        viajeOriginal.apply {
            put("destino", nombreDestino)
            put("presupuesto", presupuesto)
            put("fechaInicio", fechaInicio)
            put("fechaFin", fechaFin)
            put("actividades", actividades)
            put("lugaresVisitar", lugaresVisitar)
        }

        val sharedPrefs = context.getSharedPreferences("viajes", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        val viajesArray = JSONArray(sharedPrefs.getString("viajes_list", "[]"))

        for (i in 0 until viajesArray.length()) {
            if (viajesArray.getJSONObject(i).getString("destino") == viajeOriginal.getString("destino")) {
                viajesArray.put(i, viajeOriginal) // Actualiza el viaje
                break
            }
        }

        editor.putString("viajes_list", viajesArray.toString())
        editor.apply()
    }
}
