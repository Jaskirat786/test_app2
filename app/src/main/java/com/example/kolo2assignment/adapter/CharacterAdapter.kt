package com.example.kolo2assignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kolo2assignment.R
import com.example.kolo2assignment.character_model.Results

class CharacterAdapter(val data: List<Results>, val context: Context) :
    RecyclerView.Adapter<CharacterAdapter.CharactersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.first_tab_items, parent, false)
        return CharactersViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {

        val characters = data[position]
        holder.characterName.text = characters.name
        val characterImage = "${characters.thumbnail.path}/portrait_medium.jpg"
        Glide.with(context).load(characterImage).into(holder.characterImage)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class CharactersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val characterName: TextView = itemView.findViewById(R.id.character_name)
        val characterImage: ImageView = itemView.findViewById(R.id.character_image)
    }

}