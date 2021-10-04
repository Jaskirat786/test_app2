package com.example.kolo2assignment.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kolo2assignment.adapter.ComicsAdapter
import com.example.kolo2assignment.databinding.ActivitySecondTabBinding
import com.example.kolo2assignment.comic_model.Comics
import com.example.kolo2assignment.network.RetrofitInstance
import com.example.kolo2assignment.utils.Constants
import retrofit2.Call
import retrofit2.Response

class SecondTabActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondTabBinding
    private lateinit var adapter: ComicsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySecondTabBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        RetrofitInstance.instance.getComics(Constants.ts, Constants.APIKEY, Constants.hash)
            .enqueue(object : retrofit2.Callback<Comics> {

                override fun onResponse(call: Call<Comics>, response: Response<Comics>) {

                    if (response != null) {
                        binding.recyclerView.adapter =
                            ComicsAdapter(response.body()!!.data.results, this@SecondTabActivity)
                    }
                }
                override fun onFailure(call: Call<Comics>, t: Throwable) {
                    Log.d(TAG, "onFailure:" + t.message)
                }
            })
    }
}