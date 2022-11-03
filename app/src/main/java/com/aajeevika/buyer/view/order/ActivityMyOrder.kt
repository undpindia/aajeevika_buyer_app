package com.aajeevika.buyer.view.order

import android.os.Bundle
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivity
import com.aajeevika.buyer.databinding.ActivityMyOrderBinding
import com.aajeevika.buyer.view.order.adapter.MyOrderPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class ActivityMyOrder : BaseActivity<ActivityMyOrderBinding>(R.layout.activity_my_order) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.run {
            viewPager.adapter = MyOrderPagerAdapter(supportFragmentManager, lifecycle)

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.pending)
                    else -> getString(R.string.completed)
                }
            }.attach()
        }
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }
        }
    }
}