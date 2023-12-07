package com.example.finalapppamn

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)

        val firebaseAuth = FirebaseAuth.getInstance()

        // Obtenemos una referencia al TextInputEditText para el nombre de usuario
        val usernameEditText = findViewById<TextInputEditText>(R.id.emailUser)

        // Obtenemos una referencia al TextInputEditText para la contraseña
        val passwordEditText = findViewById<TextInputEditText>(R.id.passwordUser)


        val signUpTextView = findViewById<TextView>(R.id.signUp)

        // Escuchamos el evento de clic del botón de login
        val loginButton = findViewById<MaterialButton>(R.id.loginButton)


        signUpTextView.setOnClickListener {
            val intent = Intent(this, signUpActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            firebaseAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in successful
                        Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                        // Go to the home screen
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("EXTRA_USER", firebaseAuth.currentUser)
                        startActivity(intent)
                        finish()
                    } else {
                        // Sign in failed
                        Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
                    }
                }

        }

    }
    companion object {
        const val EXTRA_USER = "extra_user"
    }

}
