package com.proyectoCM.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth

class Inicio : AppCompatActivity() {
    private lateinit var btnLogout: ImageButton
    private lateinit var btnRegPreUso:ImageButton
    private lateinit var btnRegUso:ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        AsignarReferencias()
        Configuracion()
    }

    private fun AsignarReferencias() {
        btnLogout = findViewById(R.id.btnLogout)
        btnRegPreUso = findViewById(R.id.btnRegPreUso)
        btnRegUso = findViewById(R.id.btnRegUso)
    }

    private fun Configuracion() {
        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

        btnRegPreUso.setOnClickListener {
            val rpuIntent:Intent = Intent(this, RegPreUso::class.java)
            startActivity(rpuIntent)
        }

        btnRegUso.setOnClickListener {
            val ruIntent:Intent = Intent(this, ListaReg::class.java)
            startActivity(ruIntent)
        }
    }

}