package com.example.kolo2assignment.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kolo2assignment.R

class ComicsViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {

    val comicName: TextView =itemView.findViewById(R.id.comic_name)
      val comicWriter:TextView =itemView.findViewById(R.id.comic_writer)
      val comicImage:ImageView =itemView.findViewById(R.id.comic_image)
}