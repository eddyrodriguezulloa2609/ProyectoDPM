package com.proyectoCM.myapplication

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import java.util.jar.Manifest

class Login : AppCompatActivity() {
    private lateinit var btnIngresar:Button
    private lateinit var txtUsuario:EditText
    private lateinit var txtContrasena:EditText
    private lateinit var btnCall:ImageButton
    private lateinit var lblCall:TextView
    private lateinit var btnNOficinas:ImageButton
    private lateinit var lblNOficinas:TextView

    private val REQUEST_CALL = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        asignarReferencias()
        limpiarCampos()
        Configuracion()
    }

    private fun asignarReferencias() {
        btnIngresar = findViewById(R.id.btnIngresar)
        txtUsuario = findViewById(R.id.txtUsuario)
        txtContrasena = findViewById(R.id.txtContrasena)
        btnNOficinas = findViewById(R.id.btnNOficinas)
        lblNOficinas = findViewById(R.id.lblNOficinas)
        btnCall = findViewById(R.id.btnCall)
        lblCall = findViewById(R.id.lblCall)
    }

    private fun Configuracion() {
        btnIngresar.setOnClickListener {
            if (txtUsuario.text.isNotEmpty() && txtContrasena.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(txtUsuario.text.toString(),
                        txtContrasena.text.toString()).addOnCompleteListener{
                            if (it.isSuccessful) {
                                mostrarInicio(it.result?.user?.email ?: "")
                            } else {
                                mostrarMensaje("Error","Error al autenticar al usuario")
                            }
                        }
            } else {
                mostrarMensaje("Error","Ingrese los datos completos")
            }
        }

        btnCall.setOnClickListener{
            callPhone()
        }

        lblCall.setOnClickListener{
            callPhone()
        }

        btnNOficinas.setOnClickListener{
            mostrarMapa()
        }

        lblNOficinas.setOnClickListener{
            mostrarMapa()
        }
    }

    private fun mostrarMensaje(titulo:String, mensaje:String) {
        val ventana = AlertDialog.Builder(this)
        ventana.setTitle(titulo)
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar", null)
        ventana.create().show()
    }

    private fun limpiarCampos() {
        txtUsuario.setText("")
        txtContrasena.setText("")
    }

    private fun mostrarInicio(correo:String) {
        guardarEnSesion(correo)
        val inicioIntent:Intent = Intent(this, Inicio::class.java)
        startActivity(inicioIntent)
    }

    private fun guardarEnSesion(correo:String) {
        var preferencias = getSharedPreferences("sesion", Context.MODE_PRIVATE)
        var editor = preferencias.edit()
        editor.putString("correo", correo)
        editor.commit()
    }

    private fun mostrarMapa() {
        val mapIntent:Intent = Intent(this, MapsActivity::class.java)
        startActivity(mapIntent)
    }

    private fun callPhone() {
        val tlf:String = "993019390"
        val callIntent:Intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(tlf)))
        startActivity(callIntent)
    }
}