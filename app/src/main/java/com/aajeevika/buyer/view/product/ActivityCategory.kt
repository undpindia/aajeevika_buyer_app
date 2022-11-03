package com.aajeevika.buyer.view.product

import android.os.Bundle
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivityVM
import com.aajeevika.buyer.databinding.ActivityCategoryBinding
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.RecyclerViewDecoration
import com.aajeevika.buyer.view.product.adapter.CategoryRecyclerViewAdapter
import com.aajeevika.buyer.view.product.viewmodel.CategoryViewModel

class ActivityCategory : BaseActivityVM<ActivityCategoryBinding, CategoryViewModel>(
    R.layout.activity_category,
    CategoryViewModel::class
) {
    private val categoryRecyclerViewAdapter = CategoryRecyclerViewAdapter()
    private val title by lazy { intent.getStringExtra(Constant.TITLE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.title = title

        viewDataBinding.recyclerView.run {
            adapter = categoryRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(16F,16F,16F,16F))
        }
        viewModel.requestCategories()
    }

    override fun observeData() {
        super.observeData()
        viewModel.categories.observe(this, {
            categoryRecyclerViewAdapter.setData(it.category?: arrayListOf())
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }
        }
    }
}