package com.proyectoCM.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.proyectoCM.myapplication.entidades.RegistroBE
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ListaReg : AppCompatActivity() {
    private lateinit var btnLogout:ImageButton
    private lateinit var lblUsuario:TextView
    private lateinit var lblFecha:TextView
    private lateinit var rvRegistros:RecyclerView
    private val lstRegistros:ArrayList<RegistroBE> = ArrayList()
    private lateinit var listener:ValueEventListener

    private val dbF = FirebaseFirestore.getInstance()
    private val db = Firebase.database
    private val referencia = db.getReference("registro")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_reg)

        var preferencias = getSharedPreferences("sesion", Context.MODE_PRIVATE)
        asignarReferencias()
        Configuracion()
        cargarDatos(preferencias.getString("correo","").toString())
    }

    private fun asignarReferencias() {
        btnLogout = findViewById(R.id.btnLogout)
        lblUsuario = findViewById(R.id.lblUsuario)
        lblFecha = findViewById(R.id.lblFecha)
        rvRegistros = findViewById(R.id.rvRegistros)
    }

    private fun Configuracion() {
        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent: Intent = Intent(this,Login::class.java)
            startActivity(intent)
        }
    }

    private fun cargarDatos(correo:String) {
        title = "Lista de Registros"

        /* DATOS DEL USUARIO */
        dbF.collection("users").document(correo).get().addOnSuccessListener {
            lblUsuario.setText("Bienvenido(a) " + it.get("nombreC") as String?)
        }

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val currentDate = sdf.format(Calendar.getInstance().time)

        lblFecha.setText("Fecha y hora: " + currentDate)

        /* DATOS DEL RV */
        lstRegistros.clear()
        configurarRV(rvRegistros)
    }

    private fun configurarRV(rvRegistros: RecyclerView) {
        listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lstRegistros.clear()

                snapshot.children.forEach { item ->
                    if (item.child("estado").getValue().toString() != "FI")
                    {
                        val registro:RegistroBE = RegistroBE(
                            item.key.toString(),
                            item.child("usuario").getValue().toString(),
                            item.child("iturno").getValue().toString().toInt(),
                            item.child("sturno").getValue().toString(),
                            item.child("itipo").getValue().toString().toInt(),
                            item.child("stipo").getValue().toString(),
                            item.child("iequipo").getValue().toString().toInt(),
                            item.child("sequipo").getValue().toString(),
                            item.child("sistemaHidraPU").getValue().toString(),
                            item.child("sistemaHidraObsPU").getValue().toString(),
                            item.child("airePU").getValue().toString(),
                            item.child("aireObsPU").getValue().toString(),
                            item.child("cabinaPU").getValue().toString(),
                            item.child("cabinaObsPU").getValue().toString(),
                            item.child("tableroPU").getValue().toString(),
                            item.child("tableroObsPU").getValue().toString(),
                            item.child("horoIni").getValue().toString().toInt(),
                            item.child("sistemaHidraU").getValue().toString(),
                            item.child("sistemaHidraObsU").getValue().toString(),
                            item.child("aireU").getValue().toString(),
                            item.child("aireObsU").getValue().toString(),
                            item.child("cabinaU").getValue().toString(),
                            item.child("cabinaObsU").getValue().toString(),
                            item.child("tableroU").getValue().toString(),
                            item.child("tableroObsU").getValue().toString(),
                            item.child("comentario").getValue().toString(),
                            item.child("horoFin").getValue().toString().toInt(),
                            item.child("firma").getValue().toString(),
                            item.child("estado").getValue().toString(),
                            item.child("fechaPU").getValue().toString(),
                            item.child("estado").getValue().toString()
                        )

                        registro.let { lstRegistros.add(it) }
                    }
                }

                Log.d("==>", lstRegistros.size.toString())
                rvRegistros.layoutManager = LinearLayoutManager(this@ListaReg)
                rvRegistros.adapter = AdaptadorPersonalizado(lstRegistros)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("==>", error.message)
            }
        }

        referencia.addValueEventListener(listener)
    }
}