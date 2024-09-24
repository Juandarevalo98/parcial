package com.example.parcial

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViajeAdapter(private val viajes: List<Viaje>) : RecyclerView.Adapter<ViajeAdapter.ViajeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViajeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_viaje, parent, false)
        return ViajeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViajeViewHolder, position: Int) {
        val viaje = viajes[position]
        holder.bind(viaje)

        // Configuramos el click listener para abrir DetallesViaje y pasar los datos
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetallesViaje::class.java)

            intent.putExtra("viaje", viaje.toJsonString())
            intent.putExtra("indice", position)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = viajes.size

    class ViajeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvFechaInicio: TextView = itemView.findViewById(R.id.tvFechaInicio)
        private val tvFechaFin: TextView = itemView.findViewById(R.id.tvFechaFin)
        private val tvDestino: TextView = itemView.findViewById(R.id.tvDestino)

        fun bind(viaje: Viaje) {
            tvFechaInicio.text = "Fecha inicio: ${viaje.fechaInicio}"
            tvFechaFin.text = "Fecha fin: ${viaje.fechaFin}"
            tvDestino.text = "Destino: ${viaje.destino}"
        }
    }
}