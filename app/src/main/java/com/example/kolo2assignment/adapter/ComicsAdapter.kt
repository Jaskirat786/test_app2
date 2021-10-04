package com.example.kolo2assignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kolo2assignment.R
import com.example.kolo2assignment.comic_model.Result

class ComicsAdapter(val data:List<Result>, val context: Context):RecyclerView.Adapter<ComicsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicsViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.second_tab_items,parent,false)
        return ComicsViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ComicsViewHolder, position: Int) {

        val comics =data[position]
        holder.comicName.text=comics.title
        holder.comicWriter.text= comics.creators.toString()
        val image="${comics.thumbnail.path}/portrait_medium.jpg"
        Glide.with(context).load(image).into(holder.comicImage)

    }

    override fun getItemCount(): Int {
       return  data.size
    }
}