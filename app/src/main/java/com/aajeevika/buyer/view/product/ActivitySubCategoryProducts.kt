package com.aajeevika.buyer.view.product

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivityVM
import com.aajeevika.buyer.databinding.ActivitySubCategoryProductsBinding
import com.aajeevika.buyer.model.data_model.ProductBasicData
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.RecyclerViewDecoration
import com.aajeevika.buyer.utility.app_enum.ProductType
import com.aajeevika.buyer.view.product.adapter.SubCategoryProductsRecyclerViewAdapter
import com.aajeevika.buyer.view.product.viewmodel.SubCategoryProductsViewModel

class ActivitySubCategoryProducts : BaseActivityVM<ActivitySubCategoryProductsBinding, SubCategoryProductsViewModel>(
    R.layout.activity_sub_category_products,
    SubCategoryProductsViewModel::class
) {
    private val title by lazy { intent.getStringExtra(Constant.TITLE) }
    private val subCategoryId by lazy { intent.getIntExtra(Constant.CATEGORY_ID,0) }
    private val artisanId by lazy { intent.getIntExtra(Constant.ARTISAN_ID,0) }
    private val productType by lazy { intent.getStringExtra(Constant.PRODUCT_TYPE) ?: "" }

    private val mAdapter: SubCategoryProductsRecyclerViewAdapter by lazy {
        SubCategoryProductsRecyclerViewAdapter(productType) {
            when(productType){
                ProductType.RECENTLY_ADDED.name -> viewModel.requestRecentProductList(it)
                ProductType.SUB_CATEGORY.name -> viewModel.requestSubCategoryProductList(subCategoryId, it)
                ProductType.ARTISAN_SUB_CATEGORY.name -> viewModel.requestArtisanSubCategoryProductList(artisanId,subCategoryId, it)
                else -> viewModel.requestPopularProductList(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.title = title

        viewDataBinding.recyclerView.run {
            adapter = mAdapter
            addItemDecoration(RecyclerViewDecoration(8F, 8F, 8F, 8F))
        }

        when(productType){
            ProductType.RECENTLY_ADDED.name ->{
                viewModel.requestRecentProductList()
            }

            ProductType.SUB_CATEGORY.name ->{
                viewModel.requestSubCategoryProductList(subCategoryId)
            }

            ProductType.ARTISAN_SUB_CATEGORY.name ->{
                viewModel.requestArtisanSubCategoryProductList(artisanId,subCategoryId)
            }

            ProductType.POPULAR_SELLER.name ->{
                viewDataBinding.recyclerView.layoutManager = GridLayoutManager(this,3)
                viewModel.requestPopularProductList()
            }

            else ->{
                viewModel.requestPopularProductList()
            }
        }
    }

    override fun observeData() {
        super.observeData()

        viewModel.popularProducts.observe(this, {
            it?.run { addData(popularproduct, it.pagination.current_page, it.pagination.last_page) }
        })

        viewModel.recentProducts.observe(this, {
            it?.run { addData(recentlyproduct, it.pagination.current_page, it.pagination.last_page) }
        })

        viewModel.subCategoryWiseProducts.observe(this, {
            it?.run { addData(products, it.pagination.current_page, it.pagination.last_page) }
        })
    }

    private fun addData(data: ArrayList<ProductBasicData>, current: Int, last: Int) {
        mAdapter.addData(data, current, last)
    }

}