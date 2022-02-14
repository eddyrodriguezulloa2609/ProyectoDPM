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
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
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

class RegUso : AppCompatActivity() {
    private lateinit var btnLogout: ImageButton
    private lateinit var lblUsuario: TextView
    private lateinit var lblFecha: TextView
    private lateinit var btnRegistrar: Button
    private lateinit var cgSH: ChipGroup
    private lateinit var cgAire: ChipGroup
    private lateinit var cgCabina: ChipGroup
    private lateinit var cgTablero: ChipGroup
    private lateinit var txtObsSH: EditText
    private lateinit var txtObsAire: EditText
    private lateinit var txtObsCabina: EditText
    private lateinit var txtObsTablero: EditText
    private lateinit var txtComentario: EditText

    private val dbF = FirebaseFirestore.getInstance()
    private val db = Firebase.database

    private lateinit var SistemaHidra:String
    private lateinit var Aire:String
    private lateinit var Cabina:String
    private lateinit var Tablero:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_uso)

        var preferencias = getSharedPreferences("sesion", Context.MODE_PRIVATE)
        asignarReferencias()
        Configuracion()
        cargarDatos(preferencias.getString("correo","").toString())
    }

    private fun asignarReferencias() {
        btnLogout = findViewById(R.id.btnLogout)
        lblUsuario = findViewById(R.id.lblUsuario)
        lblFecha = findViewById(R.id.lblFecha)

        cgSH = findViewById(R.id.cgSH)
        cgAire = findViewById(R.id.cgAire)
        cgCabina = findViewById(R.id.cgCabina)
        cgTablero = findViewById(R.id.cgTablero)

        txtObsSH = findViewById(R.id.txtObsSH)
        txtObsAire = findViewById(R.id.txtObsAire)
        txtObsCabina = findViewById(R.id.txtObsCabina)
        txtObsTablero = findViewById(R.id.txtObsTablero)

        txtComentario = findViewById(R.id.txtComentario)

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
                ObtenerEstados()

                val id = intent.getStringExtra("id")
                val referencia = db.getReference("registro").child(id.toString())

                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                val currentDate = sdf.format(Calendar.getInstance().time)

                referencia.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val registro:RegistroBE? = snapshot.getValue(RegistroBE::class.java)

                        val _SistemaHidraObs = txtObsSH.text.toString()
                        val _AireObs = txtObsAire.text.toString()
                        val _CabinaObs = txtObsCabina.text.toString()
                        val _TableroObs = txtObsTablero.text.toString()
                        val _Comentario = txtComentario.text.toString()
                        val _Estado = "US"

                        if (registro != null) {
                            referencia.child("sistemaHidraU").setValue(SistemaHidra)
                            referencia.child("sistemaHidraObsU").setValue(_SistemaHidraObs)
                            referencia.child("aireU").setValue(Aire)
                            referencia.child("aireObsU").setValue(_AireObs)
                            referencia.child("cabinaU").setValue(Cabina)
                            referencia.child("cabinaObsU").setValue(_CabinaObs)
                            referencia.child("tableroU").setValue(Tablero)
                            referencia.child("tableroObsU").setValue(_TableroObs)
                            referencia.child("comentario").setValue(_Comentario)
                            referencia.child("estado").setValue(_Estado)
                            referencia.child("fechaUS").setValue(currentDate)

                            mostrarMensaje("Mensaje", "Uso registrado con éxito")
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

    private fun ObtenerEstados() {
        val idsSH: List<Int> = cgSH.getCheckedChipIds()
        for (id in idsSH) {
            val chip: Chip = cgSH.findViewById(id)
            SistemaHidra = chip.text as String
        }

        val idsAi: List<Int> = cgAire.getCheckedChipIds()
        for (id in idsAi) {
            val chip: Chip = cgAire.findViewById(id)
            Aire = chip.text as String
        }

        val idsCa: List<Int> = cgCabina.getCheckedChipIds()
        for (id in idsCa) {
            val chip: Chip = cgCabina.findViewById(id)
            Cabina = chip.text as String
        }

        val idsTa: List<Int> = cgTablero.getCheckedChipIds()
        for (id in idsTa) {
            val chip: Chip = cgTablero.findViewById(id)
            Tablero = chip.text as String
        }
    }

    private fun cargarDatos(correo:String) {
        title = "Registro de Uso"

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