package com.proyectoCM.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class RegPreUso : AppCompatActivity() {
    private lateinit var btnLogout: ImageButton
    private lateinit var lblUsuario:TextView
    private lateinit var lblFecha:TextView
    private lateinit var spTurno:Spinner
    private lateinit var spTipoPreuso:Spinner
    private lateinit var spCodEquipo:Spinner
    private lateinit var txtHorometroIni:EditText
    private lateinit var btnSiguiente:Button

    private val dbF = FirebaseFirestore.getInstance()

    private var iTurno:Int = 0
    private var sTurno:String = ""
    private var iTipo:Int = 0
    private var sTipo:String = ""
    private var iEquipo:Int = 0
    private var sEquipo:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_pre_uso)

        var preferencias = getSharedPreferences("sesion", Context.MODE_PRIVATE)
        asignarReferencias()
        Configuracion()
        cargarDatos(preferencias.getString("correo","").toString())
    }

    private fun asignarReferencias() {
        btnLogout = findViewById(R.id.btnLogout)
        lblUsuario = findViewById(R.id.lblUsuario)
        lblFecha = findViewById(R.id.lblFecha)
        spTurno = findViewById(R.id.spTurno)
        spTipoPreuso = findViewById(R.id.spTipoPreuso)
        spCodEquipo = findViewById(R.id.spCodEquipo)
        txtHorometroIni = findViewById(R.id.txtHorometroIni)
        btnSiguiente = findViewById(R.id.btnRegistrar)
    }

    private fun Configuracion() {
        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent:Intent = Intent(this,Login::class.java)
            startActivity(intent)
        }

        val listaTurno = resources.getStringArray(R.array.opcTurnoPU)
        val adapTurno = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, listaTurno)
        spTurno.adapter = adapTurno
        spTurno.onItemSelectedListener=object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                iTurno = spTurno.selectedItemPosition
                sTurno = spTurno.selectedItem.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val listaTipo = resources.getStringArray(R.array.opcTipoPU)
        val adaptadorTipo = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, listaTipo)

        spTipoPreuso.adapter = adaptadorTipo
        spTipoPreuso.onItemSelectedListener=object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                iTipo = spTipoPreuso.selectedItemPosition
                sTipo = spTipoPreuso.selectedItem.toString()

                cargarEquipos(iTipo)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        spCodEquipo.onItemSelectedListener=object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                iEquipo = spCodEquipo.selectedItemPosition
                sEquipo = spCodEquipo.selectedItem.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        btnSiguiente.setOnClickListener {
            val HoroIni:Int = Integer.parseInt(txtHorometroIni.text.toString())

            val intent:Intent = Intent(this,RegPreUso2::class.java)
            intent.putExtra("iTurno", iTurno)
            intent.putExtra("sTurno", sTurno)
            intent.putExtra("iTipo", iTipo)
            intent.putExtra("sTipo", sTipo)
            intent.putExtra("iEquipo", iEquipo)
            intent.putExtra("sEquipo", sEquipo)
            intent.putExtra("iHorometroIni", HoroIni)
            startActivity(intent)
        }
    }

    private fun cargarEquipos(iTipoPU:Int) {
        val listaCMPU = resources.getStringArray(R.array.opcCamionMinero)
        val listaVAPU = resources.getStringArray(R.array.opcVehiculoAuxiliar)
        val listaPHPU = resources.getStringArray(R.array.opcPalaHidraulica)
        val listaCAPU = resources.getStringArray(R.array.opcCamioneta)
        val listaPEPU = resources.getStringArray(R.array.opcPerforadora)

        val adaptadorCMPU = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, listaCMPU)
        val adaptadorVAPU = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, listaVAPU)
        val adaptadorPHPU = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, listaPHPU)
        val adaptadorCAPU = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, listaCAPU)
        val adaptadorPEPU = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, listaPEPU)

        when(iTipoPU){
            1 -> spCodEquipo.adapter = adaptadorCMPU
            2 -> spCodEquipo.adapter = adaptadorVAPU
            3 -> spCodEquipo.adapter = adaptadorPHPU
            4 -> spCodEquipo.adapter = adaptadorCAPU
            5 -> spCodEquipo.adapter = adaptadorPEPU
            else -> {
                spCodEquipo.adapter = null
            }
        }
    }

    private fun cargarDatos(correo:String) {
        title = "Registro de Pre Uso"

        dbF.collection("users").document(correo).get().addOnSuccessListener {
            lblUsuario.setText("Bienvenido(a) " + it.get("nombreC") as String?)
        }

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val currentDate = sdf.format(Calendar.getInstance().time)

        lblFecha.setText("Fecha y hora: " + currentDate)

        if (intent.hasExtra("iTurno") && intent.hasExtra("iTipo") && intent.hasExtra("iEquipo") && intent.hasExtra("iHorometroIni"))
        {
            spTurno.setSelection(intent.getIntExtra("iTurno",0))
            spTipoPreuso.setSelection(intent.getIntExtra("iTipo",0))
            //cargarEquipos(intent.getIntExtra("iTipo",0))
            spCodEquipo.setSelection(intent.getIntExtra("iEquipo",0))
            txtHorometroIni.setText(intent.getIntExtra("iHorometroIni",0).toString())
        }
    }
}