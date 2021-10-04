package com.example.kolo2assignment.view

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kolo2assignment.R
import com.example.kolo2assignment.adapter.CharacterAdapter
import com.example.kolo2assignment.character_model.Characters
import com.example.kolo2assignment.databinding.FragmentFirstTabBinding
import com.example.kolo2assignment.network.RetrofitInstance
import com.example.kolo2assignment.utils.Constants
import retrofit2.Call
import retrofit2.Response

class FirstTabFragment : Fragment() {

    private lateinit var binding: FragmentFirstTabBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFirstTabBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)

        RetrofitInstance.instance.getCharacter(Constants.ts, Constants.APIKEY, Constants.hash)
            .enqueue(object : retrofit2.Callback<Characters> {

                override fun onResponse(call: Call<Characters>, response: Response<Characters>) {

                    if (response != null) {

                        binding.recyclerView.adapter =
                            CharacterAdapter(
                                response.body()!!.data.results, context!!
                            )
                    }
                }

                override fun onFailure(call: Call<Characters>, t: Throwable) {
                    Log.d(ContentValues.TAG, "onFailure:" + t.message)
                }
            })
        return view
    }
}
