package com.aajeevika.buyer.view.auth

import android.content.Intent
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivityVM
import com.aajeevika.buyer.databinding.ActivityResetPasswordBinding
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.UtilityActions.showMessage
import com.aajeevika.buyer.view.auth.viewmodel.ResetPasswordViewModel
import com.aajeevika.buyer.view.dialog.AlertDialog

class ActivityResetPassword : BaseActivityVM<ActivityResetPasswordBinding, ResetPasswordViewModel>(
    R.layout.activity_reset_password,
    ResetPasswordViewModel::class
) {
    private val mobileNo: String? by lazy { intent.extras?.getString(Constant.MOBILE_NUMBER) }
    private val otp :Int? by lazy { intent.extras?.getInt(Constant.OTP) }

    override fun observeData() {
        super.observeData()

        viewModel.requestStatus.observe(this, {
            AlertDialog(
                context = this,
                cancelOnOutsideClick = false,
                message = it,
                positive = getString(R.string.ok),
                positiveClick = {
                    val intent = Intent(this, ActivityLogin::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            ).show()

        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            saveBtn.setOnClickListener {
                val password = viewDataBinding.inputPassword.text.toString().trim()
                val confirmPassword = viewDataBinding.inputConfirmPassword.text.toString().trim()

                validateFormData(password, confirmPassword)?.let { error ->
                    viewDataBinding.root.showMessage(error)
                } ?: run {
                    viewModel.requestPasswordChange(mobileNo, password,otp)
                }
            }
        }
    }

    private fun validateFormData(password: String, confirmPassword: String) : String? {
        return when {
            password.isEmpty() -> getString(R.string.enter_new_password)
            password.length < 8 -> getString(R.string.password_length_error)
            password != confirmPassword -> getString(R.string.password_doesnt_match)
            else -> null
        }
    }
}