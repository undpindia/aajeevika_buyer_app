package com.aajeevika.buyer.view.interest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aajeevika.buyer.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.buyer.baseclasses.BaseViewHolder
import com.aajeevika.buyer.databinding.ListItemLoadMoreBinding
import com.aajeevika.buyer.databinding.ListItemSaleAddProductBinding
import com.aajeevika.buyer.model.data_model.NotificationData
import com.aajeevika.buyer.model.data_model.ProductBasicData
import com.aajeevika.buyer.utility.UtilityActions

class InterestAddProductRecyclerViewAdapter(private val requestData: (Int) -> Unit) : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<ProductBasicData>()
    private var isNextPageRequested = false
    private var currentPage: Int = -1
    private var lastPage: Int = -1

    fun addData(data: ArrayList<ProductBasicData>, currentPage: Int, lastPage: Int) {
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

    fun getData(): ArrayList<ProductBasicData> = dataList

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder? = run {
        when(viewType) {
            1 -> AddSaleProductViewHolder(ListItemSaleAddProductBinding.inflate(inflater, parent, false))
            0 -> LoadMoreViewHolder(ListItemLoadMoreBinding.inflate(inflater, parent, false))
            else -> null
        }
    }

    override fun getItemCount() = dataList.size + if(currentPage < lastPage) 1 else 0

    override fun getItemViewType(position: Int) = if(position < dataList.size) 1 else 0

    private inner class AddSaleProductViewHolder(private val viewDataBinding: ListItemSaleAddProductBinding) :
        BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            val product = dataList[adapterPosition]
            viewDataBinding.product = product

            viewDataBinding.ivAdd.setOnClickListener {
                if(!viewDataBinding.qtyValue.text.toString().trim().isEmpty()) {
                    if(product.selectedQuantity < product.qty) {
                        UtilityActions.closeKeyboard(context, viewDataBinding.qtyValue)

                        val value = viewDataBinding.qtyValue.text.toString().toInt()
                        product.selectedQuantity = value + 1
                        notifyItemChanged(adapterPosition)
                    }
                } else {
                    product.selectedQuantity = 1
                    notifyItemChanged(adapterPosition)
                }
            }

            viewDataBinding.ivMinus.setOnClickListener {
                if (product.selectedQuantity > 0) {
                    UtilityActions.closeKeyboard(context, viewDataBinding.qtyValue)

                    val value = viewDataBinding.qtyValue.text.toString().toInt()
                    product.selectedQuantity = value - 1
                    notifyItemChanged(adapterPosition)
                }
            }
        }
    }

    private inner class LoadMoreViewHolder(private val viewDataBinding: ListItemLoadMoreBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {}
    }
}