package com.example.parcial

import org.json.JSONObject

data class Viaje(
    val destino: String,
    val presupuesto: Int,
    val fechaInicio: String,
    val fechaFin: String,
    val actividades: String,
    val lugaresVisitar: String
) {
    fun toJsonString(): String {
        return JSONObject().apply {
            put("destino", destino)
            put("presupuesto", presupuesto)
            put("fechaInicio", fechaInicio)
            put("fechaFin", fechaFin)
            put("actividades", actividades)
            put("lugaresVisitar", lugaresVisitar)
        }.toString()
    }
}

