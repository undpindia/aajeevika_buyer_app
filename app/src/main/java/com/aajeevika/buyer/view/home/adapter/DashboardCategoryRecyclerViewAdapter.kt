package com.aajeevika.buyer.view.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.buyer.baseclasses.BaseViewHolder
import com.aajeevika.buyer.databinding.ListItemCategoryBinding
import com.aajeevika.buyer.model.data_model.CategoryData
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.UtilityActions
import com.aajeevika.buyer.view.product.ActivityCategorizedProductList
import com.aajeevika.buyer.view.product.ActivityCategory
import com.bumptech.glide.Glide

class DashboardCategoryRecyclerViewAdapter(
    private var dataList: ArrayList<CategoryData>
) : BaseRecyclerViewAdapter() {

    fun setData(dataList: ArrayList<CategoryData>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder = run {
        CategoryViewHolder(ListItemCategoryBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = run {
        dataList.size
    }

    private inner class CategoryViewHolder(private val viewDataBinding: ListItemCategoryBinding) :
        BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            with(viewDataBinding) {
                val product = dataList[adapterPosition]
                title = product.name
                image = product.image

                root.setOnClickListener { view ->
                    val intent = Intent(context, ActivityCategorizedProductList::class.java)
                    intent.putExtra(Constant.TITLE, product.name)
                    intent.putExtra(Constant.CATEGORY_ID, product.id)
                    intent.putExtra(Constant.WEB_URL, product.image)
                    context.startActivity(intent)
                }

                executePendingBindings()
            }
        }
    }
}