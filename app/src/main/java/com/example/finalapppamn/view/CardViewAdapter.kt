package com.example.finalapppamn.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalapppamn.R
import com.example.finalapppamn.model.CardView

class CardViewAdapter(private val cardViewList: List<CardView>): RecyclerView.Adapter<CardViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CardViewHolder(layoutInflater.inflate(R.layout.item_cardview, parent, false))
    }

    override fun getItemCount(): Int {
            return cardViewList.size
    }
    //
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = cardViewList[position]
        holder.render(item);
    }
}