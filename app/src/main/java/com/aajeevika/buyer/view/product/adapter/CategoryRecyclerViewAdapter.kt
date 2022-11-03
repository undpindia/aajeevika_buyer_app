package com.aajeevika.buyer.view.product.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.buyer.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.buyer.baseclasses.BaseViewHolder
import com.aajeevika.buyer.databinding.ListItemCategoryBigBinding
import com.aajeevika.buyer.model.data_model.CategoryData
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.view.product.ActivityCategorizedProductList

class CategoryRecyclerViewAdapter : BaseRecyclerViewAdapter() {
    private var dataList: ArrayList<CategoryData> = arrayListOf()

    fun setData(dataList: ArrayList<CategoryData>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder = run {
        SubCategoryViewHolder(ListItemCategoryBigBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    private inner class SubCategoryViewHolder(private val viewDataBinding: ListItemCategoryBigBinding) :
        BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            val product = dataList[adapterPosition]
            viewDataBinding.image = product.image
            viewDataBinding.title = product.name

            viewDataBinding.root.setOnClickListener { view ->
                val intent = Intent(context, ActivityCategorizedProductList::class.java)
                intent.putExtra(Constant.TITLE, product.name)
                intent.putExtra(Constant.CATEGORY_ID, product.id)
                intent.putExtra(Constant.WEB_URL, product.image)
                context.startActivity(intent)
            }
            viewDataBinding.executePendingBindings()
        }
    }
}