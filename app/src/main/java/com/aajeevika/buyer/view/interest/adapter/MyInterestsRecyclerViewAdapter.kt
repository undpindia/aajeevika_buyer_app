package com.aajeevika.buyer.view.interest.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.buyer.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.buyer.baseclasses.BaseViewHolder
import com.aajeevika.buyer.databinding.ListItemMyInterestsBinding
import com.aajeevika.buyer.model.data_model.UserInterestData
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.view.interest.ActivityInterestDetails

class MyInterestsRecyclerViewAdapter(val dataList: ArrayList<UserInterestData>) : BaseRecyclerViewAdapter() {
    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run{
        MyInterestsViewHolder(ListItemMyInterestsBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    inner class MyInterestsViewHolder(private val viewDataBinding: ListItemMyInterestsBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            viewDataBinding.interest = dataList[layoutPosition]
            viewDataBinding.root.setOnClickListener {
                val intent = Intent(context, ActivityInterestDetails::class.java)
                intent.putExtra(Constant.PRODUCT_ID, dataList[layoutPosition].id)
                context.startActivity(intent)
            }
            viewDataBinding.executePendingBindings()
        }
    }
}