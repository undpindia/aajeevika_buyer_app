package com.aajeevika.buyer.view.interest

import android.content.Intent
import android.os.Bundle
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivityVM
import com.aajeevika.buyer.databinding.ActivityAddInterestProductsBinding
import com.aajeevika.buyer.model.data_model.ProductBasicData
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.RecyclerViewDecoration
import com.aajeevika.buyer.view.interest.adapter.InterestAddProductRecyclerViewAdapter
import com.aajeevika.buyer.view.interest.viewmodel.AddInterestProductsViewModel

class ActivityAddInterestProducts : BaseActivityVM<ActivityAddInterestProductsBinding, AddInterestProductsViewModel>(
    R.layout.activity_add_interest_products,
    AddInterestProductsViewModel::class
) {
    private val id: Int by lazy { intent.getIntExtra(Constant.ARTISAN_ID, 0) }
    private val mAdapter = InterestAddProductRecyclerViewAdapter {
        viewModel.requestArtisanProducts(id, it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.recyclerView.run {
            adapter = mAdapter
            addItemDecoration(RecyclerViewDecoration(8F, 8F, 8F, 8F))
        }

        viewModel.requestArtisanProducts(id)
    }

    override fun observeData() {
        super.observeData()
        viewModel.artisanProduct.observe(this, {
            intent?.getParcelableArrayListExtra<ProductBasicData>(Constant.PRODUCT_ID)?.forEach {pro->
                it.seller_data.singleOrNull { pro.id == it.id }?.selectedQuantity = pro.selectedQuantity
            }

            mAdapter.addData(it.seller_data, it.pagination.current_page, it.pagination.last_page)
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            tvDone.setOnClickListener {
                val list = mAdapter.getData().filter { it.selectedQuantity > 0 } as ArrayList<ProductBasicData>
                val intent = Intent()
                intent.putParcelableArrayListExtra(Constant.PRODUCT_ID, list)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
}