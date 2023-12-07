package com.example.finalapppamn

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.finalapppamn.LoginActivity.Companion.EXTRA_USER
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    val db = Firebase.firestore
    // Get the user from the intent
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(HomePage())

        //Obtenemos el uid del usuario
        val firebaseUser:String = intent.extras?.getString("EXTRA_USER").orEmpty()

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item_home -> {
                    replaceFragment(HomePage())
                    true
                }

                R.id.menu_item_profile -> {
                    replaceFragment(ProfilePage())
                    true
                }

                R.id.menu_item_settings -> {
                    startActivity(Intent(this, MapsActivity::class.java))
                    true
                }

                else -> {
                    // Replace with HomePage fragment if no option is selected
                    replaceFragment(HomePage())
                    true
                }
            }
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit()
    }
}