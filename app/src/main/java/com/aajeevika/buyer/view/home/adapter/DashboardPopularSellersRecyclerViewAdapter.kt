package com.aajeevika.buyer.view.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.buyer.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.buyer.baseclasses.BaseViewHolder
import com.aajeevika.buyer.databinding.ListItemPopularSellersBinding
import com.aajeevika.buyer.model.data_model.ProductBasicData
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.UtilityActions
import com.aajeevika.buyer.view.profile.ActivitySellerProfile

class DashboardPopularSellersRecyclerViewAdapter(private val dataList: ArrayList<ProductBasicData>) : BaseRecyclerViewAdapter() {
    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        PopularSellersViewHolder(ListItemPopularSellersBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    private inner class PopularSellersViewHolder(private val viewDataBinding: ListItemPopularSellersBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            viewDataBinding.root.layoutParams.width = (UtilityActions.windowWidth() * .22).toInt()
            viewDataBinding.seller = dataList[layoutPosition]

            viewDataBinding.root.setOnClickListener {
                val intent = Intent(context, ActivitySellerProfile::class.java)
                intent.putExtra(Constant.ARTISAN_ID,dataList[layoutPosition].id)
                context.startActivity(intent)
            }

            viewDataBinding.executePendingBindings()
        }
    }
}