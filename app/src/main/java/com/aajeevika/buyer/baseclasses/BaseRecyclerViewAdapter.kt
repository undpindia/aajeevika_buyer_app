package com.aajeevika.buyer.baseclasses

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter : RecyclerView.Adapter<BaseViewHolder>() {
    private lateinit var layoutInference: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        if(!::layoutInference.isInitialized) layoutInference = LayoutInflater.from(parent.context)

        createViewHolder(layoutInference, parent, viewType)
            ?: object : BaseViewHolder(DataBindingUtil.inflate(layoutInference, 0, parent, false)) {
                override fun bindData(context: Context) {}
            }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) = holder.bindData(holder.itemView.context)

    protected abstract fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder?
}