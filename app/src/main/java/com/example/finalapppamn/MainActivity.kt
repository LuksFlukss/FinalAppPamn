package com.example.finalapppamn
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalapppamn.model.CardViewProvider
import com.example.finalapppamn.view.CardViewAdapter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.homepage)
        initRecyclerView()
    }

    fun initRecyclerView(){
        val recyclerView = findViewById<RecyclerView>(R.id.recycleCard)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CardViewAdapter(CardViewProvider.cardViewsList)
    }


}