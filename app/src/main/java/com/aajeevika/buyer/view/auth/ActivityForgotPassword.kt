package com.aajeevika.buyer.view.auth

import android.content.Intent
import android.os.Bundle
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivityVM
import com.aajeevika.buyer.databinding.ActivityForgotPasswordBinding
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.UtilityActions
import com.aajeevika.buyer.utility.UtilityActions.debugOtp
import com.aajeevika.buyer.utility.UtilityActions.showMessage
import com.aajeevika.buyer.utility.app_enum.OtpVerificationType
import com.aajeevika.buyer.utility.extension.gone
import com.aajeevika.buyer.utility.extension.visible
import com.aajeevika.buyer.view.auth.viewmodel.ForgotPasswordViewModel

class ActivityForgotPassword : BaseActivityVM<ActivityForgotPasswordBinding, ForgotPasswordViewModel>(
    R.layout.activity_forgot_password,
    ForgotPasswordViewModel::class
) {
    var mobileNumber = ""
    var email = ""
    private val mEntryType: OtpVerificationType by lazy {
        intent.getStringExtra(Constant.OTP_VERIFICATION_TYPE)
            ?.let { OtpVerificationType.valueOf(it) } ?: OtpVerificationType.FORGOT_PASSWORD
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when(mEntryType){
            OtpVerificationType.CHANGE_MOBILE_NUMBER->{
                viewDataBinding.title = getString(R.string.change_mobile)
                viewDataBinding.message = getString(R.string.enter_new_mobile_no)
            }

            OtpVerificationType.CHANGE_EMAIL_ID->{
                viewDataBinding.layoutEmail.visible()
                viewDataBinding.layoutMobile.gone()
                viewDataBinding.title = getString(R.string.change_email)
                viewDataBinding.message = getString(R.string.enter_new_email)
            }

            else ->{
                viewDataBinding.title = getString(R.string.forgot_password)
                viewDataBinding.message = getString(R.string.forgot_password_message)
            }
        }
    }


    override fun observeData() {
        super.observeData()

        viewModel.otpLiveData.observe(this, {
            debugOtp(it.otp)

            val intent = Intent(this@ActivityForgotPassword, ActivityOtpVerification::class.java)
            intent.putExtra(Constant.OTP_VERIFICATION_TYPE, mEntryType.name)
            intent.putExtra(Constant.MOBILE_NUMBER, mobileNumber)
            intent.putExtra(Constant.EMAIL_ID, email)
            startActivity(intent)
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            verifyBtn.setOnClickListener {
                mobileNumber = inputMobileNumber.text.toString()
                email = inputEmail.text.toString()

                validateFormData(mobileNumber,email)?.let { error ->
                    viewDataBinding.root.showMessage(error)
                } ?: run {
                    when(mEntryType){
                        OtpVerificationType.FORGOT_PASSWORD ->{
                            viewModel.requestForgotPassword(mobileNumber)
                        }
                        OtpVerificationType.CHANGE_MOBILE_NUMBER->{
                            viewModel.requestOtpResend(mobileNumber,true)
                        }

                        else ->{

                        }
                    }
                }
            }
        }
    }

    private fun validateFormData(mobileNumber: String,email: String): String? {
        if(mEntryType == OtpVerificationType.CHANGE_EMAIL_ID){
            return if (email.isEmpty())
                getString(R.string.enter_valid_email_id)
            else null
        }else{
            return if (!UtilityActions.isValidMobileNumber(mobileNumber))
                getString(R.string.enter_valid_mobile_number)
            else null
        }
    }
}