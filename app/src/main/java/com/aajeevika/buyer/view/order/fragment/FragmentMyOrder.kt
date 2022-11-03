package com.aajeevika.buyer.view.order.fragment

import android.os.Bundle
import android.view.View
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseFragmentVM
import com.aajeevika.buyer.databinding.FragmentMyOrderBinding
import com.aajeevika.buyer.utility.RecyclerViewDecoration
import com.aajeevika.buyer.utility.app_enum.MyOrderType
import com.aajeevika.buyer.view.order.adapter.MyOrderRecyclerViewAdapter
import com.aajeevika.buyer.view.order.viewmodel.MyOrderViewModel

class FragmentMyOrder(private val myOrderType: MyOrderType) : BaseFragmentVM<FragmentMyOrderBinding, MyOrderViewModel>(
    R.layout.fragment_my_order,
    MyOrderViewModel::class
) {
    private val mAdapter = MyOrderRecyclerViewAdapter(myOrderType, arrayListOf())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.recyclerView.run {
            adapter = mAdapter
            addItemDecoration(RecyclerViewDecoration(8F,8F,8F,8F))
        }

        viewModel.orderList(myOrderType)
    }

    override fun observeData() {
        super.observeData()
        viewModel.orderList.observe(viewLifecycleOwner,{
            it.map {or->
                if(or.items?.isNotEmpty() == true) {
                    or.updated_at = or.items[0].updated_at
                    or.image = or.items[0].product?.image_1 ?: ""
                }
            }

            mAdapter.dataList.clear()
            mAdapter.dataList.addAll(it)
            mAdapter.notifyDataSetChanged()
        })
    }
}