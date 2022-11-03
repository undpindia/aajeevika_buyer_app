package com.aajeevika.buyer.view.product.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.buyer.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.buyer.baseclasses.BaseViewHolder
import com.aajeevika.buyer.databinding.ListItemProductCardBinding
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.UtilityActions
import com.aajeevika.buyer.view.product.ActivityProductDetail

class ProductRecyclerViewAdapter : BaseRecyclerViewAdapter() {
    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        ProductViewHolder(ListItemProductCardBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = 6

    inner class ProductViewHolder(private val viewDataBinding: ListItemProductCardBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            viewDataBinding.root.layoutParams.width = (UtilityActions.windowWidth() * .41).toInt()

            viewDataBinding.root.setOnClickListener {
                val intent = Intent(context, ActivityProductDetail::class.java)
                context.startActivity(intent)
            }
        }
    }
}