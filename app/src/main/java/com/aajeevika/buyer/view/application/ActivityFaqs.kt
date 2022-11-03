package com.aajeevika.buyer.view.application

import android.os.Bundle
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivityVM
import com.aajeevika.buyer.databinding.ActivityFaqsBinding
import com.aajeevika.buyer.view.application.adapter.FaqsRecyclerViewAdapter
import com.aajeevika.buyer.view.application.viewmodel.FaqViewModel

class ActivityFaqs : BaseActivityVM<ActivityFaqsBinding, FaqViewModel>(
    R.layout.activity_faqs,
    FaqViewModel::class
) {
    private val faqsRecyclerViewAdapter = FaqsRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.recyclerView.adapter = faqsRecyclerViewAdapter
    }

    override fun observeData() {
        super.observeData()
    }

    override fun initListener() {
        viewDataBinding.toolbar.backBtn.setOnClickListener {
            onBackPressed()
        }
    }
}