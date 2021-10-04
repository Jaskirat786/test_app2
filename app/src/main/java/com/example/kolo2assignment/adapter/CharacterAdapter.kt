package com.example.kolo2assignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kolo2assignment.BindingHolder
import com.example.kolo2assignment.R
import com.example.kolo2assignment.character_model.Results
import com.example.kolo2assignment.databinding.FirstTabItemsBinding
import com.example.kolo2assignment.databinding.PageProgressBarBinding

class CharacterAdapter(
    val data: ArrayList<Results?>,
    val context: Context,
    var recyclerView: RecyclerView
) :
    RecyclerView.Adapter<BindingHolder<*>>() {

    private val linearLayoutManager: LinearLayoutManager
    var lastVisibleItem: Int = 0
    private val viewTypeItem = 0
    private val viewTypeLoading = 1
    var totalItemCount: Int = 0
    private var isLoading: Boolean = false
    private var totalPages: Int = 0
    var currentPages = 0
    lateinit var mOnLoadMoreListener: OnLoadMoreListener

    init {
        linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = linearLayoutManager.childCount
                totalItemCount = linearLayoutManager.itemCount
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                if (!isLoading && totalItemCount <= lastVisibleItem + visibleItemCount) {
                    if (totalPages != currentPages) {
                        mOnLoadMoreListener.onLoadMore()
                        isLoading = true
                    }
                }
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<*> {

        return when (viewType) {
            viewTypeItem -> {
                val bindingView = DataBindingUtil.inflate<FirstTabItemsBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.first_tab_items, parent, false
                )
                return BindingHolder(bindingView)
            }
            viewTypeLoading -> {
                val view = DataBindingUtil.inflate<PageProgressBarBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.page_progress_bar, parent, false
                )
                BindingHolder(view)
            }
            else -> null!!
        }

    }
    override fun onBindViewHolder(holder: BindingHolder<*>, position: Int) {
//showing data from data class
        if (holder.binding is FirstTabItemsBinding) {
            val loadingViewHolder = holder as BindingHolder<FirstTabItemsBinding>
            val characters = data[position]
            loadingViewHolder.binding.characterName.text = characters?.name
            val characterImage = "${characters?.thumbnail?.path}/portrait_medium.jpg"
            Glide.with(context).load(characterImage).into(loadingViewHolder.binding.characterImage)
        }
        //showing progress bar
        else {
            val loadingViewHolder = holder as BindingHolder<PageProgressBarBinding>
            val spinner: ProgressBar =
                loadingViewHolder.binding.progressBar as ProgressBar
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setOnLoadMoreListener(mOnLoadMoreListener: OnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position] == null) viewTypeLoading else viewTypeItem
    }

    fun addAllList(list: ArrayList<Results?>, total: Int, current: Int) {
        this.data.addAll(list)
        this.totalPages = total
        this.currentPages = current
    }

    fun clear(total: Int, current: Int) {
        this.data.clear()
        this.totalPages = total
        this.currentPages = current
    }

    fun setLoaded() {
        isLoading = false
    }
}