package com.aajeevika.buyer.view.application

import android.os.Bundle
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivityVM
import com.aajeevika.buyer.databinding.ActivityNotificationsBinding
import com.aajeevika.buyer.view.application.adapter.NotificationsRecyclerViewAdapter
import com.aajeevika.buyer.view.application.viewmodel.NotificationsViewModel

class ActivityNotifications : BaseActivityVM<ActivityNotificationsBinding, NotificationsViewModel>(
    R.layout.activity_notifications,
    NotificationsViewModel::class
) {
    private val notificationsRecyclerViewAdapter = NotificationsRecyclerViewAdapter{
        viewModel.requestNotifications(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.recyclerView.adapter = notificationsRecyclerViewAdapter

        viewModel.requestNotifications()
    }

    override fun observeData() {
        super.observeData()
        viewModel.notification.observe(this, {
            viewDataBinding.swipeRefresh.isRefreshing = false
            notificationsRecyclerViewAdapter.addData(it.getnotification, it.pagination.current_page, it.pagination.last_page)
        })
    }

    override fun initListener() {
        viewDataBinding.swipeRefresh.setOnRefreshListener {
            viewModel.requestNotifications()
        }
    }
}