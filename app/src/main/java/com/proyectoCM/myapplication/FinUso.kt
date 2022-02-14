package com.proyectoCM.myapplication

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
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

class FinUso : AppCompatActivity() {
    private lateinit var btnLogout: ImageButton
    private lateinit var lblUsuario: TextView
    private lateinit var lblFecha: TextView
    private lateinit var txtHoroFin: EditText
    private lateinit var btnRegistrar: Button

    private val dbF = FirebaseFirestore.getInstance()
    private val db = Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fin_uso)

        var preferencias = getSharedPreferences("sesion", Context.MODE_PRIVATE)
        asignarReferencias()
        Configuracion()
        cargarDatos(preferencias.getString("correo","").toString())
    }

    private fun asignarReferencias() {
        btnLogout = findViewById(R.id.btnLogout)
        lblUsuario = findViewById(R.id.lblUsuario)
        lblFecha = findViewById(R.id.lblFecha)
        txtHoroFin = findViewById(R.id.txtHoroFin)

        btnRegistrar = findViewById(R.id.btnRegistrar)
    }

    private fun Configuracion() {
        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent: Intent = Intent(this,Login::class.java)
            startActivity(intent)
        }

        btnRegistrar.setOnClickListener {
            try {
                val id = intent.getStringExtra("id")
                val referencia = db.getReference("registro").child(id.toString())

                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                val currentDate = sdf.format(Calendar.getInstance().time)

                referencia.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val registro: RegistroBE? = snapshot.getValue(RegistroBE::class.java)

                        val _Estado = "FI"

                        if (registro != null){
                            referencia.child("estado").setValue(_Estado)
                            referencia.child("horoFin").setValue(txtHoroFin.text.toString())
                            referencia.child("fechaFI").setValue(currentDate)

                            mostrarMensaje("Mensaje", "Fin de uso registrado con éxito")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("==>", error.message)
                    }
                })
            } catch (e:Exception) {
                mostrarMensaje("Error", e.toString())
            }
        }
    }

    private fun cargarDatos(correo:String) {
        title = "Reg. Fin de Uso"

        dbF.collection("users").document(correo).get().addOnSuccessListener {
            lblUsuario.setText("Bienvenido(a) " + it.get("nombreC") as String?)
        }

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val currentDate = sdf.format(Calendar.getInstance().time)

        lblFecha.setText("Fecha y hora: " + currentDate)
    }

    private fun mostrarMensaje(titulo:String, mensaje:String) {
        val ventana = AlertDialog.Builder(this)
        ventana.setTitle(titulo)
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar", { dialogInterface: DialogInterface, i:Int ->
            if (titulo == "Mensaje")
            {
                val lrIntent:Intent = Intent(this, ListaReg::class.java)
                startActivity(lrIntent)
            }
        })
        ventana.create().show()
    }
}