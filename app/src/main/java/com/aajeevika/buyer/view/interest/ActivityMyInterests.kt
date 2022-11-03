package com.aajeevika.buyer.view.interest

import android.os.Bundle
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivityVM
import com.aajeevika.buyer.databinding.ActivityMyInterestsBinding
import com.aajeevika.buyer.model.data_model.UserInterestData
import com.aajeevika.buyer.utility.RecyclerViewDecoration
import com.aajeevika.buyer.view.interest.adapter.MyInterestsRecyclerViewAdapter
import com.aajeevika.buyer.view.interest.viewmodel.MyInterestsViewModel

class ActivityMyInterests : BaseActivityVM<ActivityMyInterestsBinding, MyInterestsViewModel>(
    R.layout.activity_my_interests,
    MyInterestsViewModel::class
) {
    private val mAdapter = MyInterestsRecyclerViewAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.recyclerView.run {
            adapter = mAdapter
            addItemDecoration(RecyclerViewDecoration(8F,8F,8F,8F))
        }

        viewModel.userInterest()
    }

    override fun observeData() {
        super.observeData()
        viewModel.interest.observe(this,{
            it.map {int->
                int.updated_at = int.items?.get(0)?.updated_at?:""
            }
            mAdapter.dataList.clear()
            mAdapter.dataList.addAll(it)
            mAdapter.notifyDataSetChanged()
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