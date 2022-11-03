package com.aajeevika.buyer.view.product.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aajeevika.buyer.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.buyer.baseclasses.BaseViewHolder
import com.aajeevika.buyer.databinding.ListItemLoadMoreBinding
import com.aajeevika.buyer.databinding.ListItemNotificationBinding
import com.aajeevika.buyer.databinding.ListItemSubCategoryListBinding
import com.aajeevika.buyer.model.data_model.CategoryWiseProductDataList
import com.aajeevika.buyer.model.data_model.NotificationData
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.RecyclerViewDecoration
import com.aajeevika.buyer.utility.UtilityActions.toPx
import com.aajeevika.buyer.utility.app_enum.ProductType
import com.aajeevika.buyer.view.product.ActivitySubCategoryProducts

class CategorizedProductListAdapter(private val requestData: (Int) -> Unit) : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<CategoryWiseProductDataList>()
    private var isNextPageRequested = false
    private var currentPage: Int = -1
    private var lastPage: Int = -1

    fun addData(data: ArrayList<CategoryWiseProductDataList>, currentPage: Int, lastPage: Int) {
        isNextPageRequested = false

        this.currentPage = currentPage
        this.lastPage = lastPage

        if(currentPage == 1) dataList.clear()
        val currentDataLisSize = dataList.size
        dataList.addAll(data)

        if(currentPage == 1) notifyDataSetChanged()
        else notifyItemRangeChanged(currentDataLisSize, dataList.size - currentDataLisSize)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lastVisibleItemPosition = (recyclerView.layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition()

                if(lastVisibleItemPosition == dataList.size && !isNextPageRequested) {
                    isNextPageRequested = true
                    requestData(currentPage + 1)
                }
            }
        })
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int) = run {
        when(viewType) {
            1 -> ProductListViewHolder(ListItemSubCategoryListBinding.inflate(inflater, parent, false))
            0 -> LoadMoreViewHolder(ListItemLoadMoreBinding.inflate(inflater, parent, false))
            else -> null
        }
    }

    override fun getItemCount() = dataList.size + if(currentPage < lastPage) 1 else 0

    override fun getItemViewType(position: Int) = if(position < dataList.size) 1 else 0

    inner class ProductListViewHolder(private val viewTitledRecyclerviewBinding: ListItemSubCategoryListBinding) :
        BaseViewHolder(viewTitledRecyclerviewBinding) {
        override fun bindData(context: Context) {
            with(viewTitledRecyclerviewBinding) {
                val product = dataList[adapterPosition]
                title = product.subcategory

                val adapter = HorizontalProductsAdapter()
                adapter.setData(product.product ?: arrayListOf())
                recyclerView.adapter = adapter
                if (recyclerView.itemDecorationCount == 0) {
                    recyclerView.addItemDecoration(RecyclerViewDecoration(8F, 8F, 8F, 8F))
                }
                recyclerView.setPadding(8.toPx(root.context).toInt(), 0, 0, 0)

                viewAllBtn.setOnClickListener { view ->
                    val intent = Intent(context, ActivitySubCategoryProducts::class.java)
                    intent.putExtra(Constant.CATEGORY_ID, product.subcategoryId)
                    intent.putExtra(Constant.TITLE, product.subcategory)
                    intent.putExtra(Constant.PRODUCT_TYPE, ProductType.SUB_CATEGORY.name)
                    context.startActivity(intent)
                }
            }
        }
    }

    private inner class LoadMoreViewHolder(private val viewDataBinding: ListItemLoadMoreBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {}
    }
}