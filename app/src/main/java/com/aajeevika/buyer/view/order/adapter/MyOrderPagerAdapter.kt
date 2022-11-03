package com.aajeevika.buyer.view.order.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aajeevika.buyer.utility.app_enum.MyOrderType
import com.aajeevika.buyer.view.order.fragment.FragmentMyOrder

class MyOrderPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = run {
        FragmentMyOrder(
            when(position) {
                0 -> MyOrderType.PENDING
                else -> MyOrderType.COMPLETED
            }
        )
    }
}