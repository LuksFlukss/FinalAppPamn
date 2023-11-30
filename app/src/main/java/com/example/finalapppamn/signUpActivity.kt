package com.example.finalapppamn

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class signUpActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_sign_up)

        val loginBack = findViewById<MaterialButton>(R.id.loginInButton)
        val fullName = findViewById<TextInputEditText>(R.id.fullNameInput)
        val email = findViewById<TextInputEditText>(R.id.emailInputText)
        val password = findViewById<TextInputEditText>(R.id.passwordInputText)
        val confirmPassword = findViewById<TextInputEditText>(R.id.confirmInputText)

        val aboutYouInputText = findViewById<TextInputEditText>(R.id.aboutYouInputText)

        val register = findViewById<MaterialButton>(R.id.registerButton)


        register.setOnClickListener {
            val username = fullName.text.toString().trim()
            val userPassword = password.text.toString().trim()
            val userconfirmPass = confirmPassword.text.toString().trim()
            val useraboutYou = aboutYouInputText.text.toString().trim()
            val useremail= email.text.toString().trim()

            if (username.isBlank() || userPassword.isBlank() || userconfirmPass.isBlank() || useraboutYou.isBlank() || useremail.isBlank() ) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if(userconfirmPass != userPassword){
                Toast.makeText(this, "Passwords aren't equals", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
        }

        loginBack.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

