package com.aajeevika.buyer.view.order.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.buyer.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.buyer.baseclasses.BaseViewHolder
import com.aajeevika.buyer.databinding.ListItemInterestsDetailsProductBinding
import com.aajeevika.buyer.databinding.ListItemOrderDetailsProductBinding
import com.aajeevika.buyer.model.data_model.ProductBasicData

class OrderDetailsProductRecyclerViewAdapter(val dataList:ArrayList<ProductBasicData>) : BaseRecyclerViewAdapter() {
    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        ProductViewHolder(ListItemInterestsDetailsProductBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    inner class ProductViewHolder(private val viewDataBinding: ListItemInterestsDetailsProductBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            viewDataBinding.product = dataList[layoutPosition]
            viewDataBinding.price = dataList[layoutPosition].orderPrice
            viewDataBinding.executePendingBindings()
        }
    }
}