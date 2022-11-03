package com.aajeevika.buyer.location.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aajeevika.buyer.databinding.ListItemPlaceBinding
import com.aajeevika.buyer.location.model.SearchLocationResponse

class AutoCompleteAdapter(
    private val list: ArrayList<SearchLocationResponse.Predictions>,
    private val clickListener: (Int) -> Unit
) : RecyclerView.Adapter<AutoCompleteAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AutoCompleteAdapter.MyViewHolder {
        return MyViewHolder(
            ListItemPlaceBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    inner class MyViewHolder(private val bind: ListItemPlaceBinding) :
        RecyclerView.ViewHolder(bind.root) {
        fun onBind(position: Int) {
            val place = list[position]
            bind.tvPlaceName.text = place.structured_formatting.main_text
            bind.tvPlaceAddress.text = place.description
            bind.root.setOnClickListener {
                clickListener(position)
            }
        }
    }

    override fun onBindViewHolder(
        holder: AutoCompleteAdapter.MyViewHolder,
        position: Int
    ) = holder.onBind(position)
}