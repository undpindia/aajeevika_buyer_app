package com.aajeevika.buyer.view.product

import android.os.Bundle
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivityVM
import com.aajeevika.buyer.databinding.ActivityCategoryProductBinding
import com.aajeevika.buyer.utility.Constant.CATEGORY_ID
import com.aajeevika.buyer.utility.Constant.TITLE
import com.aajeevika.buyer.utility.Constant.WEB_URL
import com.aajeevika.buyer.utility.UtilityActions.toPx
import com.aajeevika.buyer.view.product.adapter.CategorizedProductListAdapter
import com.aajeevika.buyer.view.product.viewmodel.CategoryViewModel

class ActivityCategorizedProductList :
    BaseActivityVM<ActivityCategoryProductBinding, CategoryViewModel>(
        R.layout.activity_category_product,
        CategoryViewModel::class
    ) {
    private var categoryId: Int? = null
    private var categoryImage: String? = null
    private val categorizedProductListAdapter = CategorizedProductListAdapter {
        viewModel.requestCategoryProductList(categoryId, it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.title = intent.extras?.getString(TITLE)
        categoryId = intent.extras?.getInt(CATEGORY_ID)
        categoryImage = intent.getStringExtra(WEB_URL)

        initView()
        observeData()
        initSwipeToRefresh()
        viewModel.requestCategoryProductList(categoryId)
    }

    private fun initView() {
        val padding = 8.toPx(this@ActivityCategorizedProductList).toInt()
        viewDataBinding.recyclerView.setPadding(0, padding, 0, padding)
        viewDataBinding.recyclerView.adapter = categorizedProductListAdapter
    }

    private fun initSwipeToRefresh() {
        viewDataBinding.swipeRefresh.setOnRefreshListener {
            viewModel.requestCategoryProductList(categoryId)
        }
    }

    override fun observeData() {
        super.observeData()
        viewModel.categoryWiseProducts.observe(this, {
            viewDataBinding.swipeRefresh.isRefreshing = false
            categorizedProductListAdapter.addData(it.products, it.pagination.current_page, it.pagination.last_page)
        })
    }
}