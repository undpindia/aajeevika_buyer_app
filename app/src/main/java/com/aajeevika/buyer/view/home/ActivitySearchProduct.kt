package com.aajeevika.buyer.view.home

import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivityVM
import com.aajeevika.buyer.databinding.ActivitySearchProductBinding
import com.aajeevika.buyer.model.data_model.SearchDataModel
import com.aajeevika.buyer.utility.RecyclerViewDecoration
import com.aajeevika.buyer.utility.UtilityActions
import com.aajeevika.buyer.view.home.adapter.SearchProductRecyclerViewAdapter
import com.aajeevika.buyer.view.home.viewmodel.SearchViewModel
import java.util.*

class ActivitySearchProduct : BaseActivityVM<ActivitySearchProductBinding, SearchViewModel>(
    R.layout.activity_search_product,
    SearchViewModel::class,
) {
    private lateinit var timer: Timer
    private val searchProductRecyclerViewAdapter = SearchProductRecyclerViewAdapter { page ->
        viewModel.searchProduct(viewDataBinding.inputSearch.text.toString().trim(), page)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.recyclerView.run {
            adapter = searchProductRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(8F,8F,8F,8F))
        }
    }

    override fun onResume() {
        super.onResume()
        UtilityActions.openKeyboard(this, viewDataBinding.inputSearch)
    }

    override fun observeData() {
        super.observeData()

        viewModel.searchResult.observe(this, {
            val searchResults = ArrayList<SearchDataModel>()

            it.searchresult.forEach { searchModel ->
                if(searchModel.type.equals("product", false)) searchModel.data.forEach { data ->
                    searchResults.add(data)
                }
            }

            searchProductRecyclerViewAdapter.addData(searchResults, it.pagination.current_page, it.pagination.last_page)
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            inputSearch.doOnTextChanged { text, _, _, _ ->
                if (::timer.isInitialized) timer.cancel()
                searchProductRecyclerViewAdapter.resetData()

                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        if(!text.isNullOrEmpty() && text.length > 2) runOnUiThread {
                            viewModel.searchProduct(text.toString().trim())
                        }
                    }
                }, 200L)
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.searchProduct(viewDataBinding.inputSearch.text.toString().trim())
            }
        }
    }

    override fun onDestroy() {
        if (::timer.isInitialized) timer.cancel()
        super.onDestroy()
    }
}