package com.example.finalapppamn

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalapppamn.model.User
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore

//import com.google.firebase.auth.FirebaseAuth

class signUpActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private var userAdded = false
    //private lateinit var auth: FirebaseAuth;
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
            val useremail= email.text.toString()

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

            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(useremail, userPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // User created successfully
                        val user = FirebaseAuth.getInstance().currentUser

                        if (user != null) {
                            // Add the user to the database
                            val newUser = User(username,useremail,useraboutYou, favoritePublicationIds = mutableListOf()
                            )
                            db.collection("users").document(user.uid).set(newUser)
                                .addOnCompleteListener { task2 ->
                                    if (task2.isSuccessful) {
                                        // User added to the database successfully
                                        Toast.makeText(this, "Creado con existo", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this, LoginActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        // Error adding user to the database
                                        Toast.makeText(this, "Error al crear el usuario", Toast.LENGTH_SHORT).show()
                                    }

                                }
                        }
                    } else {
                        // User creation failed
                        if (task.exception is FirebaseAuthUserCollisionException) {
                            Toast.makeText(this, "El correo ya está en uso", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Error al crearse", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

        }

        loginBack.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

