package com.example.finalapppamn

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalapppamn.model.CardView
import com.example.finalapppamn.model.CardViewProvider
import com.example.finalapppamn.view.CardViewAdapter
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore

class HomePage : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recyclerView.setOnContextClickListener(searchEditText)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the RecyclerView
        initRecyclerView()

        // Retrieve and display cards from Firestore
        getCards()
    }

    private fun initRecyclerView() {
        recyclerView = requireView().findViewById(R.id.recycleCard)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = CardViewAdapter(CardViewProvider.cardViewsList)
    }

    private fun getCards() {
        db.collection("Cardview")
            .get()
            .addOnSuccessListener { result ->
                val cardList = mutableListOf<CardView>()

                for (document in result) {
                    // Assuming you have a data class named CardViewData
                    val cardData = CardView(
                        title = document.getString("title") ?: "",
                        imageUrl = document.getString("imageUrl") ?: "",
                        stars = document.getLong("stars")?.toInt() ?: 0,
                        price = document.getLong("precio")?.toInt() ?: 0
                    )
                    cardList.add(cardData)
                }

                // Update RecyclerView adapter with the retrieved data
                updateRecyclerView(cardList)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
    private fun updateRecyclerView(cardList: List<CardView>) {
        // Clear the existing data
        CardViewProvider.cardViewsList.clear()

        // Add the new data to the provider
        CardViewProvider.cardViewsList.addAll(cardList)

        // Notify the adapter that the data has changed
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private val searchEditText = getView().findViewById<EditText>(R.id.search_bar_text)
    searchEditText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
            // Not needed for this case, but this method is triggered before text changes.
        }

        override fun onTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
            // This method is triggered when text changes.
            val searchText = charSequence.toString()
            // Perform actions with searchText (e.g., filter data, update UI, etc.).
            // Call your method or function here using searchText.
            getCardsByTitle(searchText)
        }

        override fun afterTextChanged(editable: Editable?) {
            // Not needed for this case, but this method is triggered after text changes.
        }
    })

    private fun getCardsByTitle(searchText: String) {
        db.collection("Cardview").whereEqualTo("title", searchText)
            .get()
            .addOnSuccessListener { result ->
                val cardList = mutableListOf<CardView>()

                for (document in result) {
                    // Assuming you have a data class named CardViewData
                    val cardData = CardView(
                        title = document.getString("title") ?: "",
                        imageUrl = document.getString("imageUrl") ?: "",
                        stars = document.getLong("stars")?.toInt() ?: 0,
                        price = document.getLong("precio")?.toInt() ?: 0
                    )
                    cardList.add(cardData)
                }

                // Update RecyclerView adapter with the retrieved data
                updateRecyclerView(cardList)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

}
