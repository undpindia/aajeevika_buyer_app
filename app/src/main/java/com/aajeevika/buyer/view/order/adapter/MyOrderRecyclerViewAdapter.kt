package com.aajeevika.buyer.view.order.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.buyer.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.buyer.baseclasses.BaseViewHolder
import com.aajeevika.buyer.databinding.ListItemMyOrderBinding
import com.aajeevika.buyer.model.data_model.OrderData
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.app_enum.MyOrderType
import com.aajeevika.buyer.view.order.ActivityOrderDetailsCompleted

class MyOrderRecyclerViewAdapter(
    private val myOrderType: MyOrderType,
    val dataList: ArrayList<OrderData>
) : BaseRecyclerViewAdapter() {
    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        MyOrderViewHolder(ListItemMyOrderBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    private inner class MyOrderViewHolder(private val viewDataBinding: ListItemMyOrderBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            viewDataBinding.order = dataList[layoutPosition]

            viewDataBinding.root.setOnClickListener {
                val intent = Intent(context, ActivityOrderDetailsCompleted::class.java)
                intent.putExtra(Constant.ORDER_ID, dataList[layoutPosition].id)
                context.startActivity(intent)
            }

            viewDataBinding.executePendingBindings()
        }
    }
}