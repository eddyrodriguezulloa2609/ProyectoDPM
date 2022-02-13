package com.proyectoCM.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
        holder.fecha.text = reg.Fecha
        holder.turno.text = reg.sTurno
        holder.horo.text = "H1: " + reg.HoroIni
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
    }
}