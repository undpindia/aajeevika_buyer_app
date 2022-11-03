package com.aajeevika.buyer.view.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.buyer.baseclasses.BaseViewHolder
import com.aajeevika.buyer.databinding.ListItemProductCardBinding
import com.aajeevika.buyer.model.data_model.ProductBasicData
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.UtilityActions
import com.aajeevika.buyer.view.product.ActivityProductDetail

class DashboardRecentlyAddedRecyclerViewAdapter : BaseRecyclerViewAdapter() {
    var basicDataList: ArrayList<ProductBasicData> = arrayListOf()

    fun setData(basicDataList: ArrayList<ProductBasicData>) {
        this.basicDataList = basicDataList
        notifyDataSetChanged()
    }


    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        PopularSellersViewHolder(ListItemProductCardBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = basicDataList.size

    private inner class PopularSellersViewHolder(private val viewDataBinding: ListItemProductCardBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            val parent = viewDataBinding.root
            val layoutParams = parent.layoutParams
            layoutParams.width = UtilityActions.pxRespectToDeviceWidth(parent.context, 2.5F).toInt()

            basicDataList.let {
                val product = it[adapterPosition]
                viewDataBinding.productId = product.product_id_d
                viewDataBinding.title = "${product.template?.name} (${product.name})"
                viewDataBinding.price = product.price
                viewDataBinding.sellerNameTxt.visibility = View.GONE

                viewDataBinding.image = product.image_1
                viewDataBinding.user = product.user?.organization_name


                viewDataBinding.root.setOnClickListener {
                    val intent = Intent(context, ActivityProductDetail::class.java)
                    intent.putExtra(Constant.PRODUCT_ID, product.id)
                    intent.putExtra(Constant.IS_PATH_VIA_ARTISAN, false)
                    intent.putExtra(Constant.PAGE_TITLE, product.name)
                    context.startActivity(intent)
                }
                viewDataBinding.executePendingBindings()
            }
        }
    }
}