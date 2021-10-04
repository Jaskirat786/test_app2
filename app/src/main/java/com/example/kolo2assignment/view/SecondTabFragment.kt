package com.example.kolo2assignment.view

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kolo2assignment.adapter.ComicsAdapter
import com.example.kolo2assignment.comic_model.Comics
import com.example.kolo2assignment.databinding.FragmentSecondTabBinding
import com.example.kolo2assignment.network.RetrofitInstance
import com.example.kolo2assignment.utils.Constants
import retrofit2.Call
import retrofit2.Response

class SecondTabFragment : Fragment() {

    private lateinit var binding: FragmentSecondTabBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSecondTabBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)

        RetrofitInstance.instance.getComics(Constants.ts, Constants.APIKEY, Constants.hash)
            .enqueue(object : retrofit2.Callback<Comics> {

                override fun onResponse(call: Call<Comics>, response: Response<Comics>) {

                    if (response != null) {
                        binding.recyclerView.adapter =
                            ComicsAdapter(response.body()!!.data.results, context!!)
                    }
                }

                override fun onFailure(call: Call<Comics>, t: Throwable) {
                    Log.d(ContentValues.TAG, "onFailure:" + t.message)
                }
            })

        return view
    }
}