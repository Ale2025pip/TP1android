package com.practica.tp1android

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail= findViewById<EditText>(R.id.etLoginEmail)
        val etPassword = findViewById<EditText>(R.id.etLoginPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvGoToRegister = findViewById<TextView>(R.id.tvGoToRegister)

        btnLogin.setOnClickListener{
            val emailInput = etEmail.text.toString().trim()
            val passwordInput = etPassword.text.toString().trim()

            val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val savedEmail = sharedPreferences.getString("USER_EMAIL", "") ?: ""
            val savedPassword = sharedPreferences.getString("USER_PASS", "") ?: ""

            // LOG DE PRUEBA: Mostramos qué está comparando
            if (savedEmail.isEmpty()) {
                Toast.makeText(this, "Error: No hay ningún usuario registrado aún", Toast.LENGTH_LONG).show()
            } else if (emailInput != savedEmail) {
                Toast.makeText(this, "El Email no es correcto", Toast.LENGTH_LONG).show()
            } else if (passwordInput != savedPassword) {
                Toast.makeText(this, "La contraseña es incorrecta", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "¡Bienvenido!", Toast.LENGTH_SHORT).show()
                // Abre HomeActivity
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        tvGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}