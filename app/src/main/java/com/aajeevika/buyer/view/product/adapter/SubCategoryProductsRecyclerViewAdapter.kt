package com.aajeevika.buyer.view.product.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.buyer.baseclasses.BaseViewHolder
import com.aajeevika.buyer.databinding.ListItemLoadMoreBinding
import com.aajeevika.buyer.databinding.ListItemPopularSellersBinding
import com.aajeevika.buyer.databinding.ListItemProductCardBinding
import com.aajeevika.buyer.model.data_model.NotificationData
import com.aajeevika.buyer.model.data_model.ProductBasicData
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.Constant.PAGE_TITLE
import com.aajeevika.buyer.utility.Constant.PRODUCT_ID
import com.aajeevika.buyer.utility.UtilityActions
import com.aajeevika.buyer.utility.app_enum.ProductType
import com.aajeevika.buyer.view.product.ActivityProductDetail
import com.aajeevika.buyer.view.profile.ActivitySellerProfile

class SubCategoryProductsRecyclerViewAdapter(val productType: String, private val requestData: (Int) -> Unit) : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<ProductBasicData>()
    private var isNextPageRequested = false
    private var currentPage: Int = -1
    private var lastPage: Int = -1

    fun addData(data: ArrayList<ProductBasicData>, currentPage: Int, lastPage: Int) {
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

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder? = run {
        when (viewType) {
            1 -> PopularSellersViewHolder(ListItemPopularSellersBinding.inflate(inflater, parent, false))
            2 -> SubCategoryViewHolder(ListItemProductCardBinding.inflate(inflater, parent, false))
            0 -> LoadMoreViewHolder(ListItemLoadMoreBinding.inflate(inflater, parent, false))
            else -> null
        }
    }

    override fun getItemCount() = dataList.size + if(currentPage < lastPage) 1 else 0

    override fun getItemViewType(position: Int): Int {
        return if (productType == ProductType.POPULAR_SELLER.name) {if(position < dataList.size) 1/*0*/ else 0}
        else {if(position < dataList.size) 2/*1*/ else 0}
    }

    private inner class SubCategoryViewHolder(private val viewDataBinding: ListItemProductCardBinding) :
        BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            val product = dataList[adapterPosition]
            viewDataBinding.productId = product.product_id_d
            viewDataBinding.title = "${product.template?.name} (${product.name})"
            viewDataBinding.price = product.price
            viewDataBinding.image = product.image_1
            viewDataBinding.user = product.user?.organization_name

            viewDataBinding.root.setOnClickListener {
                val intent = Intent(context, ActivityProductDetail::class.java)
                intent.putExtra(PRODUCT_ID, product.id)
                intent.putExtra(PAGE_TITLE, product.name)
                context.startActivity(intent)
            }
            viewDataBinding.executePendingBindings()
        }
    }

    private inner class PopularSellersViewHolder(private val viewDataBinding: ListItemPopularSellersBinding) :
        BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            viewDataBinding.seller = dataList[layoutPosition]

            viewDataBinding.root.setOnClickListener {
                val intent = Intent(context, ActivitySellerProfile::class.java)
                intent.putExtra(Constant.ARTISAN_ID, dataList[layoutPosition].id)
                context.startActivity(intent)
            }

            viewDataBinding.executePendingBindings()
        }
    }

    private inner class LoadMoreViewHolder(private val viewDataBinding: ListItemLoadMoreBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {}
    }
}