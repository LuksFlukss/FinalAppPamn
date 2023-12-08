package com.example.finalapppamn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

class ProfilePage : Fragment() {

    private lateinit var uid: String
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        uid = arguments?.getString("uid") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_page, container, false)

        val toolbar: MaterialToolbar = view.findViewById(R.id.materialToolBar)
        val userName = view.findViewById<TextView>(R.id.UserName)
        val emailField = view.findViewById<TextView>(R.id.emailField)
        val bioText = view.findViewById<TextView>(R.id.bioContent)
        db.collection("users").document(uid).get().addOnSuccessListener {document ->
            if (document.exists()){
                userName.text = document.getString("name")
                emailField.text = document.getString("email")
                bioText.text = document.getString("aboutYou")
            }

        }

        return view
    }


}