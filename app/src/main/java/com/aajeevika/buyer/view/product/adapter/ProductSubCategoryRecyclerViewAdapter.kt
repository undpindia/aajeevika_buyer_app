package com.aajeevika.buyer.view.product.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.buyer.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.buyer.baseclasses.BaseViewHolder
import com.aajeevika.buyer.databinding.ListItemProductSubCategoryListBinding
import com.aajeevika.buyer.model.data_model.SubCategorizedProductList
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.RecyclerViewDecoration
import com.aajeevika.buyer.utility.app_enum.ProductType
import com.aajeevika.buyer.view.product.ActivityCategorizedProductList
import com.aajeevika.buyer.view.product.ActivitySubCategoryProducts

class ProductSubCategoryRecyclerViewAdapter(private var dataList: ArrayList<SubCategorizedProductList>,
val artisanId:Int?) :
    BaseRecyclerViewAdapter() {

    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder = run {
        ProductSubCategoryViewHolder(
            ListItemProductSubCategoryListBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = dataList.size

    inner class ProductSubCategoryViewHolder(private val viewDataBinding: ListItemProductSubCategoryListBinding) :
        BaseViewHolder(viewDataBinding) {
        private val productRecyclerViewAdapter = HorizontalProductsAdapter()
        override fun bindData(context: Context) {
            val product = dataList[layoutPosition]
            viewDataBinding.titleTxt.text = dataList[layoutPosition].subCategoryName
            viewDataBinding.recyclerView.adapter = productRecyclerViewAdapter
            if (viewDataBinding.recyclerView.itemDecorationCount == 0)
                viewDataBinding.recyclerView.addItemDecoration(
                    RecyclerViewDecoration(
                        8F,
                        8F,
                        8F,
                        8F
                    )
                )
            productRecyclerViewAdapter.addData(product.products ?: arrayListOf())

            viewDataBinding.viewAllBtn.setOnClickListener {
                val intent = Intent(context, ActivitySubCategoryProducts::class.java)
                intent.putExtra(Constant.CATEGORY_ID, product.subCategoryId)
                intent.putExtra(Constant.TITLE, product.subCategoryName)
                intent.putExtra(Constant.ARTISAN_ID, artisanId)
                intent.putExtra(Constant.PRODUCT_TYPE, ProductType.ARTISAN_SUB_CATEGORY.name)
                context.startActivity(intent)
            }
        }
    }
}