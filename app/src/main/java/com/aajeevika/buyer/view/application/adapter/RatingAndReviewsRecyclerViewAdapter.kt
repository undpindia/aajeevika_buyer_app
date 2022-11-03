package com.aajeevika.buyer.view.application.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.buyer.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.buyer.baseclasses.BaseViewHolder
import com.aajeevika.buyer.databinding.ListItemRatingAndReviewBinding
import com.aajeevika.buyer.model.data_model.ReviewData

class RatingAndReviewsRecyclerViewAdapter(val dataList:ArrayList<ReviewData>) : BaseRecyclerViewAdapter() {

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        RatingAndReviewViewHolder(ListItemRatingAndReviewBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    inner class RatingAndReviewViewHolder(private val viewDataBinding: ListItemRatingAndReviewBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            viewDataBinding.review = dataList[layoutPosition]
            viewDataBinding.executePendingBindings()
        }
    }
}