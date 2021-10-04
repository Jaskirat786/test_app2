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
import com.example.kolo2assignment.comic_model.Date
import com.example.kolo2assignment.comic_model.Result
import java.text.SimpleDateFormat

class ComicsAdapter(val data: List<Result>, val context: Context) :
    RecyclerView.Adapter<ComicsAdapter.ComicsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicsViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.second_tab_items, parent, false)
        return ComicsViewHolder(itemView)
    }

    fun dateFormat(date: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-SSSS")
        val output = SimpleDateFormat("yyyy")
        val d: java.util.Date? = sdf.parse(date)
        val formattedTime = output.format(d)
        return formattedTime
    }

    override fun onBindViewHolder(holder: ComicsViewHolder, position: Int) {

        val comics = data[position]
        holder.comicName.text = comics.title
        holder.comicWriter.text = dateFormat(comics.dates[0].date)
        val image = "${comics.thumbnail.path}/portrait_medium.jpg"
        Glide.with(context).load(image).into(holder.comicImage)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ComicsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val comicName: TextView = itemView.findViewById(R.id.comic_name)
        val comicWriter: TextView = itemView.findViewById(R.id.release_date)
        val comicImage: ImageView = itemView.findViewById(R.id.comic_image)
    }
}