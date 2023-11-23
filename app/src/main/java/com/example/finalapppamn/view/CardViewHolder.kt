package com.example.finalapppamn.view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalapppamn.R
import com.example.finalapppamn.model.CardView

class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val cardImage = view.findViewById<ImageView>(R.id.imageHome)
    val cardText = view.findViewById<TextView>(R.id.hometitle)
    val cardStart = view.findViewById<TextView>(R.id.cardStart)

    fun render(cardViewModel: CardView) {
        val start = cardViewModel.stars.toString() + "/5"
        cardText.text = cardViewModel.title
        cardStart.text = start
        Glide.with(cardImage.context).load(cardViewModel.imageUrl).into(cardImage)
    }
}