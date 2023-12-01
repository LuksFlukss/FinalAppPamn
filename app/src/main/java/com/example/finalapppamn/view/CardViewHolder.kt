package com.example.finalapppamn.view

import android.graphics.Typeface
import android.util.TypedValue
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
    val cardPrice = view.findViewById<TextView>(R.id.price)

    fun render(cardViewModel: CardView) {
        val start = cardViewModel.stars.toString() + "/5"
        cardText.text = cardViewModel.title
        cardText.setTypeface(null, Typeface.BOLD)  // Make the title bold
        cardText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)  // Increase the text size
        cardStart.text = start
        cardPrice.text = "Precio: " + cardViewModel.price.toString() + " €"
        Glide.with(cardImage.context).load(cardViewModel.imageUrl).into(cardImage)
    }
}