package com.example.finalapppamn

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
val Context.dataStore by preferencesDataStore(name = "USER_PREFERENCES_NAME")
class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        replaceFragment(HomePage())

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