package com.aajeevika.buyer.view.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.buyer.baseclasses.BaseViewHolder
import com.aajeevika.buyer.databinding.ListItemLoadMoreBinding
import com.aajeevika.buyer.databinding.ListItemProductCardBinding
import com.aajeevika.buyer.model.data_model.NotificationData
import com.aajeevika.buyer.model.data_model.SearchDataModel
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.view.product.ActivityProductDetail

class SearchProductRecyclerViewAdapter(private val requestData: (Int) -> Unit) : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<SearchDataModel>()
    private var isNextPageRequested = false
    private var currentPage: Int = -1
    private var lastPage: Int = -1

    fun addData(data: ArrayList<SearchDataModel>, currentPage: Int, lastPage: Int) {
        isNextPageRequested = false

        this.currentPage = currentPage
        this.lastPage = lastPage

        if(currentPage == 1) dataList.clear()
        val currentDataLisSize = dataList.size
        dataList.addAll(data)

        if(currentPage == 1) notifyDataSetChanged()
        else notifyItemRangeChanged(currentDataLisSize, dataList.size - currentDataLisSize)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lastVisibleItemPosition = (recyclerView.layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition()

                if(lastVisibleItemPosition == dataList.size && !isNextPageRequested) {
                    isNextPageRequested = true
                    requestData(currentPage + 1)
                }
            }
        })
    }

    fun resetData() {
        dataList.clear()
        isNextPageRequested = false
        currentPage = -1
        lastPage = -1

        notifyDataSetChanged()
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder? = run {
        when(viewType) {
            1 -> SubCategoryViewHolder(ListItemProductCardBinding.inflate(inflater, parent, false))
            0 -> LoadMoreViewHolder(ListItemLoadMoreBinding.inflate(inflater, parent, false))
            else -> null
        }
    }

    override fun getItemCount() = dataList.size + if(currentPage < lastPage) 1 else 0

    override fun getItemViewType(position: Int) = if(position < dataList.size) 1 else 0

    private inner class SubCategoryViewHolder(private val viewDataBinding: ListItemProductCardBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            val product = dataList[adapterPosition]

            viewDataBinding.run {
                price = product.price
                user = product.artisanshgName ?: ""
                image = BaseUrls.baseUrl + product.image_1
                title = "${ product.template.name } (${ product.productName })"
                productId = product.product_id_d

                root.setOnClickListener {
                    val intent = Intent(context, ActivityProductDetail::class.java)
                    intent.putExtra(Constant.PRODUCT_ID, product.productId)
                    context.startActivity(intent)
                }
            }
        }
    }

    private inner class LoadMoreViewHolder(viewDataBinding: ListItemLoadMoreBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {}
    }
}