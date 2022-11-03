package com.aajeevika.buyer.view.product.adapter

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
import com.aajeevika.buyer.utility.Constant.IS_PATH_VIA_ARTISAN
import com.aajeevika.buyer.utility.Constant.PAGE_TITLE
import com.aajeevika.buyer.utility.Constant.PRODUCT_ID
import com.aajeevika.buyer.utility.UtilityActions
import com.aajeevika.buyer.view.product.ActivityProductDetail

class HorizontalProductsAdapter(private val isPathViaArtisan: Boolean = false) :
    BaseRecyclerViewAdapter() {

    private var dataList: ArrayList<ProductBasicData> = arrayListOf()
    var isBottomTouched: Boolean = false

    fun setData(dataList: ArrayList<ProductBasicData>, isLast: Boolean = true) {
        this.isBottomTouched = isLast
        this.dataList = dataList
        notifyDataSetChanged()
    }

    fun addData(dataList: ArrayList<ProductBasicData>, isLast: Boolean = true) {
        this.isBottomTouched = isLast
        dataList.toList().let { this.dataList.addAll(it) }
        notifyItemRangeInserted(this.dataList.size ?: 0, dataList.size ?: 0)
    }

    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder = run {
        SubCategoryViewHolder(ListItemProductCardBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    private inner class SubCategoryViewHolder(private val viewDataBinding: ListItemProductCardBinding) :
        BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            val parent = viewDataBinding.root
            val layoutParams = parent.layoutParams
            layoutParams.width = UtilityActions.pxRespectToDeviceWidth(parent.context, 2.5F).toInt()

            val product = dataList[adapterPosition]
            viewDataBinding.productId = product.product_id_d
            viewDataBinding.title = "${product.template?.name} (${product.name})"
            viewDataBinding.price = product.price
            viewDataBinding.image = product.image_1
            viewDataBinding.user = product.seller_name ?: product.user?.organization_name

            viewDataBinding.root.setOnClickListener {
                val intent = Intent(context, ActivityProductDetail::class.java)
                intent.putExtra(PRODUCT_ID, product.id)
                intent.putExtra(IS_PATH_VIA_ARTISAN, isPathViaArtisan)
                intent.putExtra(PAGE_TITLE, product.name)
                context.startActivity(intent)
            }
            viewDataBinding.executePendingBindings()
        }
    }
}