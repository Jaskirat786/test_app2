package com.example.kolo2assignment.view

import android.content.ContentValues
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kolo2assignment.adapter.ComicsAdapter
import com.example.kolo2assignment.adapter.OnLoadMoreListener
import com.example.kolo2assignment.comic_model.Comics
import com.example.kolo2assignment.comic_model.Result
import com.example.kolo2assignment.databinding.FragmentSecondTabBinding
import com.example.kolo2assignment.network.RetrofitInstance
import com.example.kolo2assignment.utils.Constants
import retrofit2.Call
import retrofit2.Response

class SecondTabFragment : Fragment() {

    private lateinit var binding: FragmentSecondTabBinding
    private lateinit var adapter: ComicsAdapter
    private var list = ArrayList<Result?>()
    private var dataList = ArrayList<Result?>()
    private var isLoading = false
    private var totalPage = 0
    private var currentPage = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Binding the data
        binding = FragmentSecondTabBinding.inflate(inflater, container, false)
        val view = binding.root

        //Setting data in recycler view
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = ComicsAdapter(list, requireContext(), binding.recyclerView)
        binding.recyclerView.adapter = adapter

        //Applying pagination to load the data
        adapter.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                list.add(null)
                isLoading = true
                adapter.notifyItemInserted(list.size - 1)
                Handler(Looper.getMainLooper()).postDelayed({
                    callAPI(currentPage.toString())
                }, 2000)
            }
        })
        callAPI("0")

        return view
    }

    //Calling api implmentation for comics api
    fun callAPI(page: String) {

        RetrofitInstance.instance.getComics(
            Constants.ts,
            Constants.APIKEY,
            Constants.hash,
            page.toInt()
        ).enqueue(
            object : retrofit2.Callback<Comics> {
                override fun onResponse(call: Call<Comics>, response: Response<Comics>) {
                    if (response != null) {
                        dataList.clear()
                        //Loading data in to grid layout
                        if (isLoading) {
                            isLoading = false
                            list.removeAt(list.size - 1)
                            adapter.notifyItemRemoved(list.size)
                        }
                        totalPage = response.body()?.data!!.total
                        dataList.addAll(response.body()!!.data.results)

                        //Adding data for swipe refresh page
                        adapter.addAllList(dataList, currentPage, totalPage)
                        adapter.notifyDataSetChanged()

                        adapter.setLoaded()
                        if (currentPage != totalPage || currentPage < totalPage) {
                            currentPage += 1
                        }
                    }
                }

                override fun onFailure(call: Call<Comics>, t: Throwable) {
                    Log.d(ContentValues.TAG, "onFailure:" + t.message)
                }
            })
    }
}