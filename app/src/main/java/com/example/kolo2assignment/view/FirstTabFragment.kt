package com.example.kolo2assignment.view

import android.app.Dialog
import android.content.ContentValues
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kolo2assignment.R
import com.example.kolo2assignment.adapter.CharacterAdapter
import com.example.kolo2assignment.adapter.OnLoadMoreListener
import com.example.kolo2assignment.character_model.Characters
import com.example.kolo2assignment.character_model.Results
import com.example.kolo2assignment.databinding.FragmentFirstTabBinding
import com.example.kolo2assignment.network.RetrofitInstance
import com.example.kolo2assignment.utils.Constants
import retrofit2.Call
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class FirstTabFragment : Fragment() {

    lateinit var dialog: Dialog
    private lateinit var adapter: CharacterAdapter
    private lateinit var binding: FragmentFirstTabBinding
    private var mainList = ArrayList<Results?>()
    private var mainDataList = ArrayList<Results?>()
    private var allDataList = ArrayList<Results?>()
    private var isLoading = false
    private var totalPage = 0
    private var currentPage = 1
    lateinit var searchBox: EditText
    lateinit var search_list_view: RecyclerView
    lateinit var cancel: ImageView
    var searchData = ArrayList<Results?>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Binding the data
        binding = FragmentFirstTabBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.search.setOnClickListener { startDialog() }

   //Setting data in recycler view
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = CharacterAdapter(mainList, requireContext(), binding.recyclerView)
        binding.recyclerView.adapter = adapter

    //Applying pagination to load the data
        adapter.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                mainList.add(null)
                isLoading = true
                adapter.notifyItemInserted(mainList.size - 1)
                Handler(Looper.getMainLooper()).postDelayed({
                    callAPI(currentPage.toString())
                }, 2000)
            }
        })
        callAPI("0")

        return view
    }

    //Calling api implmentation for characters api
    fun callAPI(page: String) {
        RetrofitInstance.instance.getCharacter(
            Constants.ts,
            Constants.APIKEY,
            Constants.hash,
            page.toInt()
        ).enqueue(object : retrofit2.Callback<Characters> {

            override fun onResponse(call: Call<Characters>, response: Response<Characters>) {
                //Loading data in to grid layout
                if (response != null) {
                    mainDataList.clear()

                    if (isLoading) {
                        isLoading = false
                        mainList.removeAt(mainList.size - 1)
                        adapter.notifyItemRemoved(mainList.size)
                    }
                    totalPage = response.body()?.data!!.total
                    mainDataList.addAll(response.body()!!.data.results)
                    allDataList.addAll(response.body()!!.data.results)

                    //Adding data for swipe refresh page
                    adapter.addAllList(mainDataList, currentPage, totalPage)
                    adapter.notifyDataSetChanged()

                    adapter.setLoaded()
                    if (currentPage != totalPage || currentPage < totalPage) {
                        currentPage += 1
                    }
                }
            }

            override fun onFailure(call: Call<Characters>, t: Throwable) {
                Log.d(ContentValues.TAG, "onFailure:" + t.message)
            }
        })
    }

    //Implementation of search functionality
    private fun startDialog() {
        dialog = Dialog(requireContext(), R.style.myDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.search_layout)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        searchBox = dialog.findViewById(R.id.search_box)
        search_list_view = dialog.findViewById(R.id.search_list_view)
        cancel = dialog.findViewById(R.id.cancel)
        search_list_view.setHasFixedSize(true)
        search_list_view.layoutManager = LinearLayoutManager(requireContext())
        search_list_view.adapter = adapter
        dialog.show()

        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                search_list_view.setHasFixedSize(true)
                search_list_view.layoutManager = LinearLayoutManager(requireContext())
                search_list_view.adapter = adapter
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                val text = searchBox.text.toString().toLowerCase(Locale.getDefault())
                filter(text)
            }
        })
        cancel.setOnClickListener(View.OnClickListener {
            adapter = CharacterAdapter(allDataList, requireContext(), binding.recyclerView)
            binding.recyclerView.adapter = adapter
            dialog.dismiss()
        })

    }

    //Method for filtering text according to search
    private fun filter(text: String) {
        var text = text
        text = text.toLowerCase(Locale.getDefault())
        searchData.clear()

        if (text.isEmpty()) {
            searchData.addAll(allDataList)
        } else {
            for (i in allDataList) {
                if (i?.name?.toLowerCase(Locale.getDefault())
                    !!.contains(text)
                ) {
                    searchData.add(i)
                }
            }
            adapter = CharacterAdapter(searchData, requireContext(), binding.recyclerView)
            search_list_view.adapter = adapter
        }

    }

}
