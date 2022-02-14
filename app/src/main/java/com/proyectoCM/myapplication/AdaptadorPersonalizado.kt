package com.proyectoCM.myapplication

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.proyectoCM.myapplication.entidades.RegistroBE

class AdaptadorPersonalizado(lstRegistro : List<RegistroBE>) : RecyclerView.Adapter<AdaptadorPersonalizado.MyViewHolder>() {

    private var lstRegistro:List<RegistroBE> = lstRegistro

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorPersonalizado.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fila,parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdaptadorPersonalizado.MyViewHolder, position: Int) {
        val reg = lstRegistro[position]

        holder.usuario.text = "Usuario: " + reg.Usuario
        holder.equipo.text = reg.sEquipo
        holder.fecha.text = "Registro Preuso - " + reg.FechaPU
        holder.turno.text = reg.sTurno
        holder.horo.text = "H1: " + reg.HoroIni

        when (reg.Estado) {
            "PU" -> holder.estado.text = "Pre Uso registrado"
            "US" -> holder.estado.text = "Uso registrado"
            else -> { // Note the block
                holder.estado.text = "-"
            }
        }

        holder.btnUso.setOnClickListener { v ->
            
            val iUso = Intent(v.context, RegUso::class.java)
            iUso.putExtra("id", reg.id)
            v.context.startActivity(iUso)
        }

        holder.btnFin.setOnClickListener { v ->
            
            val iFin = Intent(v.context, FinUso::class.java)
            iFin.putExtra("id", reg.id)
            v.context.startActivity(iFin)
        }
    }

    override fun getItemCount(): Int {
        return lstRegistro.size
    }

    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usuario : TextView = itemView.findViewById(R.id.lblUsuario)
        val equipo : TextView = itemView.findViewById(R.id.lblEquipo)
        val fecha : TextView = itemView.findViewById(R.id.lblFecha)
        val turno : TextView = itemView.findViewById(R.id.lblTurno)
        val horo : TextView = itemView.findViewById(R.id.lblHoroIni)
        val estado : TextView = itemView.findViewById(R.id.lblEstado)

        val btnUso : TextView = itemView.findViewById(R.id.btnUso)
        val btnFin : TextView = itemView.findViewById(R.id.btnFinalizar)
    }
}
