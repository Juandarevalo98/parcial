package com.example.parcial

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    private lateinit var etBuscarDestino: EditText
    private lateinit var btnBuscar: Button
    private lateinit var btnAgregar: Button
    private lateinit var rvViajes: RecyclerView
    private lateinit var viajeAdapter: ViajeAdapter
    private var viajesList: MutableList<Viaje> = mutableListOf() // Lista global de viajes


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etBuscarDestino = findViewById(R.id.etBuscarDestino)
        btnBuscar = findViewById(R.id.btnBuscar)
        btnAgregar = findViewById(R.id.btnAgregar)
        rvViajes = findViewById(R.id.rvViajes)

        rvViajes.layoutManager = LinearLayoutManager(this)
        viajeAdapter = ViajeAdapter(viajesList)
        rvViajes.adapter = viajeAdapter

        loadViajes()

        btnBuscar.setOnClickListener {
            val destino = etBuscarDestino.text.toString().trim()
            if (destino.isNotEmpty()) {
                filterViajes(destino)
                Toast.makeText(this, "Buscando: $destino", Toast.LENGTH_SHORT).show()
            } else {
                loadViajes()
            }
        }

        btnAgregar.setOnClickListener {
            val intent = Intent(this, AddViaje::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadViajes()
    }

    private fun loadViajes() {
        val sharedPrefs = getSharedPreferences("viajes", MODE_PRIVATE)
        val viajesString = sharedPrefs.getString("viajes_list", "[]")
        val viajesArray = JSONArray(viajesString)

        viajesList.clear()

        for (i in 0 until viajesArray.length()) {
            val viajeJson = viajesArray.getJSONObject(i)
            val viaje = Viaje(
                destino = viajeJson.getString("destino"),
                presupuesto = viajeJson.getInt("presupuesto"),
                fechaInicio = viajeJson.getString("fechaInicio"),
                fechaFin = viajeJson.getString("fechaFin"),
                actividades = viajeJson.getString("actividades"),
                lugaresVisitar = viajeJson.getString("lugaresVisitar")
            )
            viajesList.add(viaje)
        }

        viajeAdapter.notifyDataSetChanged()
    }

    private fun filterViajes(destino: String) {
        val filteredViajes = viajesList.filter {
            it.destino.contains(destino, ignoreCase = true)
        }
        viajeAdapter = ViajeAdapter(filteredViajes)
        rvViajes.adapter = viajeAdapter
    }
}