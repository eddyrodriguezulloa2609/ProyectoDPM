package com.proyectoCM.myapplication

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.proyectoCM.myapplication.entidades.RegistroBE


class RegPreUso2 : AppCompatActivity() {
    private lateinit var btnLogout: ImageButton
    private lateinit var lblUsuario: TextView
    private lateinit var lblFecha: TextView
    private lateinit var btnRegresar: Button
    private lateinit var btnRegistrar:Button
    private lateinit var cgSH:ChipGroup
    private lateinit var cgAire:ChipGroup
    private lateinit var cgCabina:ChipGroup
    private lateinit var cgTablero:ChipGroup
    private lateinit var txtObsSH:EditText
    private lateinit var txtObsAire:EditText
    private lateinit var txtObsCabina:EditText
    private lateinit var txtObsTablero:EditText

    private val dbF = FirebaseFirestore.getInstance()
    private val db = Firebase.database

    private lateinit var sCorreo:String
    private var iTurno:Int = 0
    private lateinit var sTurno:String
    private var iTipo:Int = 0
    private lateinit var sTipo:String
    private var iEquipo:Int = 0
    private lateinit var sEquipo:String
    private var iHorometroIni:Int = 0
    private lateinit var SistemaHidra:String
    private lateinit var Aire:String
    private lateinit var Cabina:String
    private lateinit var Tablero:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_pre_uso2)

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

        btnRegresar = findViewById(R.id.btnRegresar)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        iTurno = intent.getIntExtra("iTurno",0)
        sTurno = intent.getStringExtra("sTurno").toString()
        iTipo = intent.getIntExtra("iTipo",0)
        sTipo = intent.getStringExtra("sTipo").toString()
        iEquipo = intent.getIntExtra("iEquipo",0)
        sEquipo = intent.getStringExtra("sEquipo").toString()
        iHorometroIni = intent.getIntExtra("iHorometroIni",0)
    }

    private fun Configuracion() {
        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent: Intent = Intent(this,Login::class.java)
            startActivity(intent)
        }

        btnRegresar.setOnClickListener {
            val intent: Intent = Intent(this,RegPreUso::class.java)

            intent.putExtra("iTurno", iTurno)
            intent.putExtra("sTurno", sTurno)
            intent.putExtra("iTipo", iTipo)
            intent.putExtra("sTipo", sTipo)
            intent.putExtra("iEquipo", iEquipo)
            intent.putExtra("sEquipo", sEquipo)
            intent.putExtra("iHorometroIni", iHorometroIni)

            startActivity(intent)
        }

        btnRegistrar.setOnClickListener {
            try {
                ObtenerEstados()

                var llave = UUID.randomUUID().toString()
                val reference = db.getReference("registro")

                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                val currentDate = sdf.format(Calendar.getInstance().time)

                val _iTurno = iTurno
                val _sTurno = sTurno
                val _iTipo = iTipo
                val _sTipo = sTipo
                val _iEquipo = iEquipo
                val _sEquipo = sEquipo
                val _SistemaHidra = SistemaHidra
                val _SistemaHidraObs = txtObsSH.text.toString()
                val _Aire = Aire
                val _AireObs = txtObsAire.text.toString()
                val _Cabina = Cabina
                val _CabinaObs = txtObsCabina.text.toString()
                val _Tablero = Tablero
                val _TableroObs = txtObsTablero.text.toString()
                val _HoroIni = iHorometroIni
                val _Estado = "PU"

                val reg = RegistroBE(null, sCorreo, _iTurno, _sTurno, _iTipo, _sTipo, _iEquipo, _sEquipo, _SistemaHidra, _SistemaHidraObs, _Aire, _AireObs, _Cabina, _CabinaObs,
                    _Tablero, _TableroObs, _HoroIni, null, null, null, null, null, null,
                    null, null, null, 0, null, _Estado, currentDate)

                reference.child(llave).setValue(reg)
                mostrarMensaje("Mensaje", "Preuso registrado con éxito")
            }catch (e:Exception) {
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
        sCorreo = correo
        title = "Registro de Pre Uso"

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