package com.aajeevika.buyer.view.product.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.buyer.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.buyer.baseclasses.BaseViewHolder
import com.aajeevika.buyer.databinding.ListItemProductDetailsPreviewBinding
import com.aajeevika.buyer.utility.UtilityActions.getVideoThumbnail

class ProductDetailPreviewRecyclerViewAdapter : BaseRecyclerViewAdapter() {

    private var dataList: ArrayList<String> = arrayListOf()
    private var thumbnailUrl: String = ""
    private var videoUrl: String? = null

    fun setData(dataList: ArrayList<String>, videoUrl: String?) {
        this.videoUrl = videoUrl
        this.dataList = dataList

        this.videoUrl?.let {
            thumbnailUrl = getVideoThumbnail(it)
            this.dataList.add(thumbnailUrl)
        }

        notifyDataSetChanged()
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        ProductDetailPreviewViewHolder(ListItemProductDetailsPreviewBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    private inner class ProductDetailPreviewViewHolder(private val viewDataBinding: ListItemProductDetailsPreviewBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            viewDataBinding.image = dataList[layoutPosition]
            viewDataBinding.root.setOnClickListener {
                val temp = dataList[layoutPosition]
                dataList[layoutPosition] = dataList[0]
                dataList[0] = temp

                notifyItemChanged(0)
                notifyItemChanged(layoutPosition)
            }
            viewDataBinding.executePendingBindings()
        }
    }
}