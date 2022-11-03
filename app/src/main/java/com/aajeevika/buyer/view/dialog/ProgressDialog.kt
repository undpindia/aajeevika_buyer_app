package com.aajeevika.buyer.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.aajeevika.buyer.R
import com.aajeevika.buyer.databinding.ProgressDialogBinding

class ProgressDialog private constructor(context: Context) : Dialog(context) {
    private var viewDataBinding: ProgressDialogBinding
    private var message: String = context.getString(R.string.loading)

    init {
        val layoutInflater = LayoutInflater.from(context)
        viewDataBinding = ProgressDialogBinding.inflate(layoutInflater, null, false)

        setCanceledOnTouchOutside(false)
        setContentView(viewDataBinding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    companion object {
        fun createDialog(context: Context): ProgressDialog = run {
            ProgressDialog(context)
        }
    }

    override fun show() {
        if(!isShowing) {
            viewDataBinding.message = message
            super.show()
        }
    }

    fun setMessage(message: String) {
        this.message = message
        viewDataBinding.message = message
        viewDataBinding.executePendingBindings()
    }

    override fun dismiss() {
        if(isShowing) super.dismiss()
    }
}