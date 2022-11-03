package com.aajeevika.buyer.baseclasses

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.aajeevika.buyer.R
import com.aajeevika.buyer.utility.app_enum.ErrorType
import com.aajeevika.buyer.utility.app_enum.ProgressAction
import com.aajeevika.buyer.utility.extension.gone
import com.aajeevika.buyer.utility.extension.visible
import com.aajeevika.buyer.view.dialog.AlertDialog
import com.aajeevika.buyer.view.dialog.ProgressDialog
import org.koin.androidx.viewmodel.ext.android.getViewModel
import kotlin.reflect.KClass

abstract class BaseActivityVM<D : ViewDataBinding, V : BaseViewModel>(
    resourceId: Int,
    private val viewModelClass: KClass<V>
) : BaseActivity<D>(resourceId) {
    protected val viewModel: V by lazy { getViewModel(clazz = viewModelClass) }
    private val progressDialog by lazy { ProgressDialog.createDialog(this) }
    protected var progressBar: ProgressBar? = null
    protected var swipeRefreshLayout: SwipeRefreshLayout? = null
    protected var noDataMessage: LinearLayout? = null
    protected var noDataMessageTv: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initCommonView()
        observeData()

        onErrorReturn(null)
    }


    private fun initCommonView() {
        progressBar = viewDataBinding.root.findViewById(R.id.progress_bar)
        swipeRefreshLayout = viewDataBinding.root.findViewById(R.id.swipe_refresh)
        noDataMessage = viewDataBinding.root.findViewById(R.id.no_data_root)
        noDataMessageTv = viewDataBinding.root.findViewById(R.id.tv_no_data)
    }

    @CallSuper
    open fun observeData() {
        viewModel.progressHandler.observe(this, {
            when (it ?: ProgressAction.NONE) {
                ProgressAction.PROGRESS_DIALOG -> {
                    progressDialog.setMessage(it.message ?: getString(R.string.loading))
                    progressDialog.show()
                }
                ProgressAction.PROGRESS_BAR -> {
                    progressBar?.visible()
                }
                ProgressAction.DISMISS -> {
                    swipeRefreshLayout?.isRefreshing = false
                    progressBar?.gone()
                    progressDialog.dismiss()
                }
                ProgressAction.NONE -> {
                }
            }
        })

        viewModel.errorMessage.observe(this, {
            when (it.errorType) {
                ErrorType.DIALOG -> {
                    AlertDialog(
                        context = viewDataBinding.root.context,
                        message = it.message,
                        positive = getString(R.string.ok),
                        cancelOnOutsideClick = false,
                    ).show()
                }
                ErrorType.MESSAGE -> onErrorReturn(it.message)
                ErrorType.TOAST -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                ErrorType.NONE -> {
                }
            }
        })
    }

    override fun onErrorReturn(message: String?) {
        super.onErrorReturn(message)
        message?.let {
            noDataMessageTv?.text = it
            noDataMessage?.visible()
        } ?: kotlin.run {
            noDataMessage?.gone()
        }
    }
}
