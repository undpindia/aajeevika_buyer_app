package com.aajeevika.buyer.view.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivityVM
import com.aajeevika.buyer.databinding.ActivityLoginBinding
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.UtilityActions
import com.aajeevika.buyer.utility.UtilityActions.debugOtp
import com.aajeevika.buyer.utility.UtilityActions.showMessage
import com.aajeevika.buyer.view.auth.viewmodel.LoginViewModel
import com.aajeevika.buyer.view.home.ActivityDashboard
import com.aajeevika.buyer.utility.app_enum.OtpVerificationType
import com.aajeevika.buyer.utility.extension.gotoNewTask

class ActivityLogin : BaseActivityVM<ActivityLoginBinding, LoginViewModel>(
    R.layout.activity_login,
    LoginViewModel::class
) {
    private val goBack by lazy { intent.getBooleanExtra(Constant.GO_BACK, false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun observeData() {
        super.observeData()

        viewModel.userProfileLiveData.observe(this, {
            if(it.is_otp_verified == 0) {
                debugOtp(it.otp)
                val intent = Intent(this, ActivityOtpVerification::class.java)
                intent.putExtra(Constant.OTP_VERIFICATION_TYPE, OtpVerificationType.REGISTER.name)
                intent.putExtra(Constant.MOBILE_NUMBER, it.mobile)
                startActivity(intent)
            } else if(it.user?.api_token?.isNotEmpty() == true) {
                it.user?.run {
                    preferencesHelper.uid = "${id ?: -1}"
                    preferencesHelper.name = name ?: ""
                    preferencesHelper.title = title ?: ""
                    preferencesHelper.email = email ?: ""
                    preferencesHelper.mobile = mobile ?: ""
                    preferencesHelper.roleId = role_id ?: -1
                    preferencesHelper.authToken = api_token ?: ""
                    preferencesHelper.profileImage = profileImage ?: ""
                }

                if(goBack) finish()
                else gotoNewTask(ActivityDashboard::class.java)
            }
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            forgotPasswordBtn.setOnClickListener {
                val intent = Intent(this@ActivityLogin, ActivityForgotPassword::class.java)
                startActivity(intent)
            }
            sendOtpBtn.setOnClickListener {
                val phoneOrEmail = viewDataBinding.inputEmail.text.toString().trim()
                val password = viewDataBinding.inputPassword.text.toString().trim()

                validateData(phoneOrEmail, password)?.let {
                    viewDataBinding.root.showMessage(it)
                } ?: run {
                    viewModel.requestUserLogin(phoneOrEmail, password)
                }
            }

            clickToRegisterBtn.setOnClickListener {
                val intent = Intent(this@ActivityLogin, ActivityRegister::class.java)
                startActivity(intent)
            }
        }
    }

    private fun validateData(phoneOrEmail: String, password: String): String? = run {
        when {
            phoneOrEmail.isEmpty() -> getString(R.string.enter_mobile_or_email)
            !Patterns.PHONE.matcher(phoneOrEmail).matches() && !Patterns.EMAIL_ADDRESS.matcher(phoneOrEmail).matches() -> getString(R.string.invalid_mobile_or_email)
            password.isEmpty() -> getString(R.string.enter_password)
            password.length < 8 -> getString(R.string.password_length_error)
            else -> null
        }
    }
}