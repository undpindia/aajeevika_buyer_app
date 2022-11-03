package com.aajeevika.buyer.view.application

import android.os.Bundle
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivityVM
import com.aajeevika.buyer.databinding.ActivityRatingAndReviewsBinding
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.view.application.adapter.RatingAndReviewsRecyclerViewAdapter
import com.aajeevika.buyer.view.application.viewmodel.RatingAndReviewsViewModel

class ActivityRatingAndReviews : BaseActivityVM<ActivityRatingAndReviewsBinding, RatingAndReviewsViewModel>(
    R.layout.activity_rating_and_reviews,
    RatingAndReviewsViewModel::class
) {
    private val mAdapter = RatingAndReviewsRecyclerViewAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.recyclerView.adapter = mAdapter

        viewModel.getReviews(intent.getIntExtra(Constant.ARTISAN_ID,0))
    }

    override fun observeData() {
        super.observeData()
        viewModel.reviews.observe(this,{
            mAdapter.dataList.addAll(it)
            mAdapter.notifyDataSetChanged()
        })
    }

    override fun initListener() {
        viewDataBinding.toolbar.backBtn.setOnClickListener {
            onBackPressed()
        }
    }
}