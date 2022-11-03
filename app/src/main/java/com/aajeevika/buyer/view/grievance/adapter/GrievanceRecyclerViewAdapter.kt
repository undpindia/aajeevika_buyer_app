package com.aajeevika.buyer.view.grievance.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.buyer.baseclasses.BaseViewHolder
import com.aajeevika.buyer.databinding.ListItemGrievanceBinding
import com.aajeevika.buyer.model.data_model.TicketData
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.UtilityActions
import com.aajeevika.buyer.view.grievance.ActivityGrievanceChat

class GrievanceRecyclerViewAdapter : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<TicketData>()

    fun addData(data: ArrayList<TicketData>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run{
        GrievanceViewHolder(ListItemGrievanceBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    inner class GrievanceViewHolder(private val viewDataBinding: ListItemGrievanceBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            val data = dataList[adapterPosition]

            viewDataBinding.run {
                createDate = UtilityActions.parseInterestDate(data.created_at)?.run { UtilityActions.formatTicketDate(this) }
                status = context.getString(if(data.status == 0) R.string.open else R.string.closed)
                ticketNumber = data.ticket_id
                message = data.message

                root.setOnClickListener {
                    val intent = Intent(context, ActivityGrievanceChat::class.java)
                    intent.putExtra(Constant.TICKET_ID_DISPLAY, data.ticket_id)
                    intent.putExtra(Constant.TICKET_ID, data.id)
                    context.startActivity(intent)
                }
            }
        }
    }
}