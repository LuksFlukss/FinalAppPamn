package com.example.finalapppamn

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
}
