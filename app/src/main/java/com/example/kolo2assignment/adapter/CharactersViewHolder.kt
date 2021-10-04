package com.example.kolo2assignment.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kolo2assignment.R

class CharactersViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {

    val characterName:TextView =itemView.findViewById(R.id.character_name)
  //  val comicName:TextView =itemView.findViewById(R.id.comic_name)
}