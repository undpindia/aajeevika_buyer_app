package com.aajeevika.buyer.baseclasses

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.aajeevika.buyer.R
import com.aajeevika.buyer.utility.AppPreferencesHelper
import com.aajeevika.buyer.utility.LocaleHelper
import org.koin.android.ext.android.inject

abstract class BaseActivity<D : ViewDataBinding>(private val resourceId: Int) : AppCompatActivity() {
    protected val preferencesHelper: AppPreferencesHelper by inject()
    protected lateinit var viewDataBinding: D

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, resourceId)
        viewDataBinding.lifecycleOwner = this


        viewDataBinding.root.findViewById<ImageView>(R.id.back_btn)?.setOnClickListener {
            onBackPressed()
        }
        initUi()
        initListener()
    }

    open fun onErrorReturn(message: String?) {}

    protected fun setError(message: String?) {
        viewDataBinding.root.run {
            //findViewById<TextView>(R.id.error_message_txt)?.text = message
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.updateLanguage(newBase, preferencesHelper.appLanguage))
    }

    protected fun stopSwipeToRefreshRefresh() {
        findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)?.isRefreshing = false
        findViewById<TextView>(R.id.tv_no_data)?.text = null
    }

    open fun initUi(){}
    open fun initListener() {}
}
