package com.example.kolo2assignment.view

import android.content.ContentValues
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kolo2assignment.R
import com.example.kolo2assignment.adapter.ComicsAdapter
import com.example.kolo2assignment.adapter.OnLoadMoreListener
import com.example.kolo2assignment.comic_model.Comics
import com.example.kolo2assignment.comic_model.Result
import com.example.kolo2assignment.databinding.FragmentSecondTabBinding
import com.example.kolo2assignment.network.RetrofitInstance
import com.example.kolo2assignment.utils.Constants
import retrofit2.Call
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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

        binding.filter.setOnClickListener { showMenu() }


        //Setting data in recycler view
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = ComicsAdapter(list, requireContext(), binding.recyclerView)
        binding.recyclerView.adapter = adapter


        //Applying pagination to load  data
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

    fun showMenu() {
        val popupmenu = PopupMenu(context, binding.filter)
        popupmenu.menuInflater.inflate(R.menu.filter_menu_items, popupmenu.menu)
        popupmenu.setOnMenuItemClickListener {

            if (it.itemId == R.id.this_week) {
                futureEvents("thisWeek")
            } else if (it.itemId == R.id.next_week) {
                futureEvents("nextWeek")
            } else if (it.itemId == R.id.this_month) {
                futureEvents("thisMonth")
            } else {
                futureEvents("lastWeek")
            }
            true
        }
        popupmenu.show()
    }
// filter implementation according to this week, next week,this month and next month
    fun futureEvents(string: String) {

        val futureEvents = ArrayList<Result?>()
        val currentDate = Date()
        for (comics in dataList) {
            var date: Date? = null
            try {
                if (string == "nextWeek") {
                    date = dateFormat(dataList[0]!!.dates[0].date)
                    if (currentDate.after(date)) {
                        futureEvents.add(comics)
                    }

                } else if (string == "thisMonth") {
                    val output = SimpleDateFormat("MM/yyy")
                    val formattedTime = output.format(currentDate)
                    if (formattedTime == currentMonth(dataList[0]!!.dates[0].date)) {
                        futureEvents.add(comics)
                    }

                } else if (string == "lastWeek") {
                    if (lastWeek(dataList[0]!!.dates[0].date).split(" ")[0].toLong() < Calendar.getInstance().timeInMillis &&
                        Calendar.getInstance().timeInMillis < lastWeek(dataList[0]!!.dates[0].date).split(
                            " "
                        )[1].toLong()
                    ) {
                        futureEvents.add(comics)
                    }

                } else if (string == "thisWeek") {
                    if (currentWeek(dataList[0]!!.dates[0].date).split(" ")[0].toLong() < Calendar.getInstance().timeInMillis &&
                        Calendar.getInstance().timeInMillis < currentWeek(dataList[0]!!.dates[0].date).split(
                            " "
                        )[1].toLong()
                    ) {
                        futureEvents.add(comics)
                    }
                }
            } catch (e: ParseException) {
            }
        }
        adapter = ComicsAdapter(futureEvents, requireContext(), binding.recyclerView)
        binding.recyclerView.adapter = adapter
    }

    fun dateFormat(date: String): Date {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-SSSS")
        val output = SimpleDateFormat("dd//MM/yyyy HH:mm")
        val d: Date? = sdf.parse(date)
        val formattedTime = output.format(d)
        return output.parse(formattedTime)!!
    }

    fun currentMonth(date: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-SSSS")
        val output = SimpleDateFormat("MM/yyy")
        val d: Date? = sdf.parse(date)
        val formattedTime = output.format(d)
        return formattedTime
    }

    fun lastWeek(dateString: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-SSSS")
        val output = SimpleDateFormat("dd//MM/yyyy HH:mm")
        val startDate: Date = dateFormat.parse(dateString)
        val calendar = Calendar.getInstance()
        calendar.time = startDate
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val enddate: Date = dateFormat.parse(dateString)
        val calendarend = Calendar.getInstance()
        calendarend.time = startDate
        calendarend.add(Calendar.DAY_OF_YEAR, 0)
        return calendar.timeInMillis.toString() + " " + calendarend.timeInMillis
    }

    fun currentWeek(dateString: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-SSSS")
        val output = SimpleDateFormat("dd//MM/yyyy HH:mm")
        val startDate: Date = dateFormat.parse(dateString)
        val calendar = Calendar.getInstance()
        calendar.time = startDate
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val endDate: Date = dateFormat.parse(dateString)
        val calendarEnd = Calendar.getInstance()
        calendarEnd.time = startDate
        calendarEnd.add(Calendar.DAY_OF_YEAR, 0)
        return calendar.timeInMillis.toString() + " " + calendarEnd.timeInMillis
    }
}