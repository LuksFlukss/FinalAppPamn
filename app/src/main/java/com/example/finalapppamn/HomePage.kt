package com.example.finalapppamn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalapppamn.model.CardViewProvider
import com.example.finalapppamn.view.CardViewAdapter

class HomePage : Fragment() {

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

        // Inicializar el RecyclerView
        initRecyclerView()
    }

    fun initRecyclerView() {
        recyclerView = requireView().findViewById(R.id.recycleCard)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = CardViewAdapter(CardViewProvider.cardViewsList)
    }
}
