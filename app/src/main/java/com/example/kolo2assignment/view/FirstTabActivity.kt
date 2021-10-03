package com.example.kolo2assignment.view

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.kolo2assignment.databinding.ActivityFirstTabBinding

class FirstTabActivity:AppCompatActivity(){

private   lateinit var binding:ActivityFirstTabBinding
private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityFirstTabBinding.inflate(layoutInflater)
        setContentView(binding.root)





    }
}