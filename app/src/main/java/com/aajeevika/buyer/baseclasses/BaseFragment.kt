package com.aajeevika.buyer.baseclasses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.aajeevika.buyer.R
import com.aajeevika.buyer.utility.AppPreferencesHelper
import org.koin.android.ext.android.get

abstract class BaseFragment<D : ViewDataBinding>(private val resourceId: Int) : Fragment() {
    protected val preferencesHelper: AppPreferencesHelper = get()
    protected lateinit var viewDataBinding: D

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, resourceId, container, false)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    open protected fun onErrorReturn(message: String?) {}

    protected fun setError(message: String?) {
        viewDataBinding.root.run {
            //findViewById<TextView>(R.id.error_message_txt)?.text = message
        }
    }

    open fun initListener() {}
}
