package com.example.finalapppamn
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.menu_item_home -> {
                    replaceFragment(HomePage())
                    true
                }
                R.id.menu_item_profile -> {
                    replaceFragment(ProfilePage())
                    true
                }
                R.id.menu_item_settings -> {
                    replaceFragment(HomePage())
                    true
                }
                else -> false
            }
        }
        replaceFragment(HomePage())
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout,fragment).commit()
    }

}