package com.aajeevika.buyer.view.product.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.buyer.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.buyer.baseclasses.BaseViewHolder
import com.aajeevika.buyer.databinding.ListItemProductCategoryListBinding
import com.aajeevika.buyer.model.data_model.CategorizedProductList

class ProductCategoryRecyclerViewAdapter(var dataList: ArrayList<CategorizedProductList>) : BaseRecyclerViewAdapter() {

    var artisanId :Int? =0
    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder = run {
        ProductSubCategoryViewHolder(
            ListItemProductCategoryListBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = dataList.size

    inner class ProductSubCategoryViewHolder(private val viewDataBinding: ListItemProductCategoryListBinding) :
        BaseViewHolder(viewDataBinding) {

        override fun bindData(context: Context) {
            viewDataBinding.titleTxt.text = dataList[layoutPosition].categoryName
            viewDataBinding.recyclerView.run {
                adapter = ProductSubCategoryRecyclerViewAdapter(
                    dataList[layoutPosition].subCategories ?: arrayListOf(),
                    artisanId
                )
            }
            viewDataBinding.executePendingBindings()
        }
    }
}