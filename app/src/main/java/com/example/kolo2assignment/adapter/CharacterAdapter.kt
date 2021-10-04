package com.example.kolo2assignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kolo2assignment.R
import com.example.kolo2assignment.comic_model.Result

class CharacterAdapter(val data:List<Result>, val context: Context):RecyclerView.Adapter<CharactersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.first_tab_items,parent,false)
        return CharactersViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {

    }
    override fun getItemCount(): Int {
       return  data.size
    }
}