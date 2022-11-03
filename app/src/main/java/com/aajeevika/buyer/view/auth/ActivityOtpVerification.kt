package com.aajeevika.buyer.view.auth

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.core.app.TaskStackBuilder
import androidx.core.widget.doOnTextChanged
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivityVM
import com.aajeevika.buyer.databinding.ActivityOtpVerificationBinding
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.UtilityActions
import com.aajeevika.buyer.utility.UtilityActions.showMessage
import com.aajeevika.buyer.utility.app_enum.OtpVerificationType
import com.aajeevika.buyer.utility.extension.gotoNewTask
import com.aajeevika.buyer.view.auth.viewmodel.OtpVerificationViewModel
import com.aajeevika.buyer.view.home.ActivityDashboard
import com.aajeevika.buyer.view.profile.ActivityEditProfile
import com.aajeevika.buyer.view.profile.ActivityProfile

class ActivityOtpVerification :
    BaseActivityVM<ActivityOtpVerificationBinding, OtpVerificationViewModel>(
        R.layout.activity_otp_verification,
        OtpVerificationViewModel::class
    ) {
    private lateinit var countDownTimer: CountDownTimer
    private val mobileNumber: String by lazy { intent.getStringExtra(Constant.MOBILE_NUMBER) ?: "" }
    private val emailIdString: String by lazy { intent.getStringExtra(Constant.EMAIL_ID) ?: "" }
    private val otpVerificationType: OtpVerificationType by lazy {
        intent.getStringExtra(Constant.OTP_VERIFICATION_TYPE)
            ?.let { OtpVerificationType.valueOf(it) } ?: OtpVerificationType.REGISTER
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.title = when (otpVerificationType) {
            OtpVerificationType.REGISTER -> getString(R.string.verify_otp)
            OtpVerificationType.CHANGE_MOBILE_NUMBER -> getString(R.string.verify_contact)
            OtpVerificationType.FORGOT_PASSWORD -> getString(R.string.verify_email)
            OtpVerificationType.CHANGE_EMAIL_ID -> getString(R.string.verify_email)
        }

        viewDataBinding.message = when (otpVerificationType) {
            OtpVerificationType.REGISTER -> String.format(
                getString(R.string.enter_4_digit_otp_sent_to_s),
                mobileNumber
            )
            OtpVerificationType.CHANGE_MOBILE_NUMBER -> String.format(
                getString(R.string.enter_4_digit_otp_sent_to_s),
                mobileNumber
            )
            OtpVerificationType.FORGOT_PASSWORD -> String.format(
                getString(R.string.enter_4_digit_otp_sent_to_s),
                emailIdString
            )
            OtpVerificationType.CHANGE_EMAIL_ID -> String.format(
                getString(R.string.enter_4_digit_otp_sent_to_s),
                emailIdString
            )
        }
        initiateTimer()
    }

    override fun onResume() {
        super.onResume()
        UtilityActions.openKeyboard(this, viewDataBinding.inputOtpView)
    }

    override fun onDestroy() {
        if (::countDownTimer.isInitialized) countDownTimer.cancel()
        super.onDestroy()
    }

    override fun observeData() {
        super.observeData()
        viewModel.userProfileLiveData.observe(this, {
            when (otpVerificationType) {
                OtpVerificationType.FORGOT_PASSWORD -> {
                    val intent = Intent(this, ActivityResetPassword::class.java)
                    intent.putExtra(
                        Constant.OTP,
                        viewDataBinding.inputOtpView.text.toString().toInt()
                    )
                    intent.putExtra(Constant.MOBILE_NUMBER, mobileNumber)
                    startActivity(intent)
                    finish()
                }

                OtpVerificationType.CHANGE_MOBILE_NUMBER -> {
                    viewModel.requestUserProfile()
                }

                OtpVerificationType.CHANGE_EMAIL_ID -> {
                    viewModel.requestUserProfile()
                }

                OtpVerificationType.REGISTER -> {
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

                    gotoNewTask(ActivityDashboard::class.java)
                }
            }
        })

        viewModel.userProfile.observe(this, {
            TaskStackBuilder.create(this@ActivityOtpVerification).apply {
                addNextIntent(Intent(this@ActivityOtpVerification, ActivityDashboard::class.java))
                addNextIntent(Intent(this@ActivityOtpVerification, ActivityProfile::class.java))
                addNextIntent(Intent(this@ActivityOtpVerification, ActivityEditProfile::class.java))
                startActivities()
            }
        })

        viewModel.otpLiveData.observe(this, {
            viewDataBinding.root.showMessage(it.otp.toString())
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            resendTxt.setOnClickListener {
                viewDataBinding.inputOtpView.text = null
                viewModel.requestOtpResend(
                    mobileNumber,
                    otpVerificationType == OtpVerificationType.CHANGE_MOBILE_NUMBER
                )
                initiateTimer()
            }

            verifyBtn.setOnClickListener {
                val pin = viewDataBinding.inputOtpView.text.toString().trim()
                if (pin.length == 4)
                    viewModel.requestOtpVerification(
                        mobileNumber,
                        pin,
                        otpVerificationType == OtpVerificationType.REGISTER,
                        if (otpVerificationType == OtpVerificationType.CHANGE_MOBILE_NUMBER)
                            preferencesHelper.uid
                        else null
                    )
                else
                    viewDataBinding.root.showMessage(getString(R.string.enter_valid_otp))
            }

            inputOtpView.doOnTextChanged { pin, _, _, _ ->
                if ((pin?.length ?: 0) == 4) {
                    UtilityActions.closeKeyboard(this@ActivityOtpVerification, inputOtpView)
                }
            }
        }
    }

    private fun initiateTimer() {
        if (::countDownTimer.isInitialized) countDownTimer.cancel()
        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onFinish() = handleCountdown(-1)
            override fun onTick(millisUntilFinished: Long) =
                handleCountdown((millisUntilFinished / 1000).toInt() + 1)
        }.start()
    }

    private fun handleCountdown(countdown: Int) {
        if (countdown >= 0) {
            viewDataBinding.resendTxt.visibility = View.INVISIBLE
            viewDataBinding.timerTxt.text =
                String.format(getString(R.string.remaining_d_sec), countdown)
        } else {
            viewDataBinding.timerTxt.text =
                String.format(getString(R.string.remaining_d_sec), 0)
            viewDataBinding.resendTxt.visibility = View.VISIBLE
        }
    }
}