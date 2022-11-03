package com.aajeevika.buyer.view.home.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.buyer.baseclasses.BaseViewHolder
import com.aajeevika.buyer.databinding.ListItemLoadMoreBinding
import com.aajeevika.buyer.databinding.ListItemSubCategoryListBinding
import com.aajeevika.buyer.databinding.ViewListLoadingBinding
import com.aajeevika.buyer.model.data_model.NotificationData
import com.aajeevika.buyer.model.data_model.ProductBasicData
import com.aajeevika.buyer.model.data_model.ProductListData
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.RecyclerViewDecoration
import com.aajeevika.buyer.utility.UtilityActions
import com.aajeevika.buyer.utility.UtilityActions.toPx
import com.aajeevika.buyer.utility.app_enum.HomeDataType
import com.aajeevika.buyer.utility.app_enum.ProductType
import com.aajeevika.buyer.view.product.ActivitySubCategoryProducts
import com.aajeevika.buyer.view.profile.ActivitySellerProfile
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class DashboardRecyclerViewAdapter(private val requestData: (Int) -> Unit) : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<ProductListData>()
    private var isNextPageRequested = false
    private var currentPage: Int = -1
    private var lastPage: Int = -1

    fun addData(data: ArrayList<ProductListData>, currentPage: Int, lastPage: Int) {
        isNextPageRequested = false

        this.currentPage = currentPage
        this.lastPage = lastPage

        if(currentPage == -1) dataList.clear()
        val currentDataLisSize = dataList.size
        dataList.addAll(data)

        if(currentPage == -1) notifyDataSetChanged()
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
            HomeDataType.POPULAR.code -> PopularSellersViewHolder(ListItemSubCategoryListBinding.inflate(inflater, parent, false))
            HomeDataType.SHG_ARTISAN.code -> SellersViewHolder(ListItemSubCategoryListBinding.inflate(inflater, parent, false))
            HomeDataType.RECENTLY.code -> RecentlyAddedViewHolder(ListItemSubCategoryListBinding.inflate(inflater, parent, false))
            0 -> LoadMoreViewHolder(ListItemLoadMoreBinding.inflate(inflater, parent, false))
            else -> null
        }
    }

    override fun getItemCount() = dataList.size + if(currentPage < lastPage) 1 else 0

    override fun getItemViewType(position: Int) = run {
        if(position < dataList.size) when (dataList[position].type) {
            HomeDataType.POPULAR.type -> HomeDataType.POPULAR.code
            HomeDataType.SHG_ARTISAN.type -> HomeDataType.SHG_ARTISAN.code
            HomeDataType.RECENTLY.type -> HomeDataType.RECENTLY.code
            else -> -1
        }
        else 0

    }

    private inner class PopularSellersViewHolder(private val viewDataBinding: ListItemSubCategoryListBinding) :
        BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            viewDataBinding.root.setPadding(
                0,
                0,
                0,
                UtilityActions.pxFromDp(context, 8F).toInt()
            )

            viewDataBinding.title = context.getString(R.string.popular_sellers)
            val list = dataList[layoutPosition].data ?: arrayListOf()
            Collections.sort(list,SortSeller())
            viewDataBinding.recyclerView.adapter = DashboardPopularSellersRecyclerViewAdapter(list)
            if (viewDataBinding.recyclerView.itemDecorationCount == 0)
                viewDataBinding.recyclerView.addItemDecoration(
                    RecyclerViewDecoration(
                        16F,
                        16F,
                        16F,
                        0F
                    )
                )

            viewDataBinding.viewAllBtn.setOnClickListener {
                val intent = Intent(context, ActivitySubCategoryProducts::class.java)
                intent.putExtra(Constant.TITLE, context.getString(R.string.popular_sellers))
                intent.putExtra(Constant.PRODUCT_TYPE, ProductType.POPULAR_SELLER.name)
                context.startActivity(intent)
            }
        }
    }

    private inner class SellersViewHolder(private val viewDataBinding: ListItemSubCategoryListBinding) :
        BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {

            val product = dataList[adapterPosition]

            with(viewDataBinding) {
                root.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                root.setPadding(
                    0,
                    UtilityActions.pxFromDp(context, 16F).toInt(),
                    0,
                    UtilityActions.pxFromDp(context, 16F).toInt()
                )

                title = product.name


                val padding = 8.toPx(root.context).toInt()
                root.setPadding(0, padding, 0, padding)
                root.setBackgroundColor(Color.WHITE)

                val adapter = DashboardSellersRecyclerViewAdapter()
                adapter.setData(product.data ?: arrayListOf())

                recyclerView.setPadding(padding, 0, 0, 0)
                recyclerView.adapter = adapter
                if (recyclerView.itemDecorationCount == 0)
                    recyclerView.addItemDecoration(RecyclerViewDecoration(8F, 8F, 8F, 8F))

                viewAllBtn.setOnClickListener {
                    val intent = Intent(context, ActivitySellerProfile::class.java)
                    intent.putExtra(Constant.ARTISAN_ID, product.id)
                    context.startActivity(intent)
                }
                executePendingBindings()
            }
        }
    }

    private inner class RecentlyAddedViewHolder(private val viewDataBinding: ListItemSubCategoryListBinding) :
        BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            val product = dataList[adapterPosition]
            with(viewDataBinding) {
                root.setBackgroundColor(ContextCompat.getColor(context, R.color.orange))
                title = product.title
                viewDataBinding.titleValue.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
                viewDataBinding.viewAllBtn.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
                recyclerView.setPadding(
                    UtilityActions.pxFromDp(context, 8F).toInt(),
                    0,
                    UtilityActions.pxFromDp(context, 8F).toInt(),
                    UtilityActions.pxFromDp(context, 20F).toInt()
                )
                if (recyclerView.itemDecorationCount == 0)
                    recyclerView.addItemDecoration(RecyclerViewDecoration(8F, 8F, 8F, 8F))

                val adapter = DashboardRecentlyAddedRecyclerViewAdapter()
                adapter.setData(product.data ?: arrayListOf())
                recyclerView.adapter = adapter
                viewAllBtn.setOnClickListener {
                    val intent = Intent(context, ActivitySubCategoryProducts::class.java)
                    intent.putExtra(Constant.TITLE, context.getString(R.string.recently_added))
                    intent.putExtra(Constant.PRODUCT_TYPE, ProductType.RECENTLY_ADDED.name)
                    context.startActivity(intent)
                }
                executePendingBindings()
            }
        }
    }

    private inner class LoadMoreViewHolder(private val viewDataBinding: ListItemLoadMoreBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {}
    }

    private inner class SortSeller: Comparator<ProductBasicData> {
        override fun compare(o1: ProductBasicData?, o2: ProductBasicData?): Int {
            val rat1 = o1?.rating?.ratingAvgStar ?: 0.0
            val rat2 = o2?.rating?.ratingAvgStar ?: 0.0

            return rat2.compareTo(rat1)
        }

    }
}