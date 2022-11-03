package com.aajeevika.buyer.view.application.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.buyer.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.buyer.baseclasses.BaseViewHolder
import com.aajeevika.buyer.databinding.ListItemFaqBinding

class FaqsRecyclerViewAdapter : BaseRecyclerViewAdapter() {
    private val map = HashMap<Int, Boolean>()

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        FaqViewHolder(ListItemFaqBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = 4

    inner class FaqViewHolder(private val viewDataBinding: ListItemFaqBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            viewDataBinding.isExpanded = map[adapterPosition] == true

            viewDataBinding.root.setOnClickListener {
                map[adapterPosition] = map[adapterPosition] != true
                notifyItemChanged(adapterPosition)
            }
        }
    }
}