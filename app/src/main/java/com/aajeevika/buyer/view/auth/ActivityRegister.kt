package com.aajeevika.buyer.view.auth

import android.content.Intent
import android.os.Bundle
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivityVM
import com.aajeevika.buyer.databinding.ActivityRegisterBinding
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.UtilityActions
import com.aajeevika.buyer.utility.UtilityActions.debugOtp
import com.aajeevika.buyer.utility.UtilityActions.showMessage
import com.aajeevika.buyer.utility.app_enum.OtpVerificationType
import com.aajeevika.buyer.view.auth.viewmodel.RegisterViewModel

class ActivityRegister : BaseActivityVM<ActivityRegisterBinding, RegisterViewModel>(
    R.layout.activity_register,
    RegisterViewModel::class
) {
    private var mobileNo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun observeData() {
        super.observeData()
        viewModel.otpLiveData.observe(this, {
            debugOtp(it.otp)
            val intent = Intent(this, ActivityOtpVerification::class.java)
            intent.putExtra(Constant.OTP_VERIFICATION_TYPE, OtpVerificationType.REGISTER.name)
            intent.putExtra(Constant.MOBILE_NUMBER, mobileNo)
            startActivity(intent)
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            signUpBtn.setOnClickListener {
                if(registerAgreementCheckbox.isChecked) {
                    val name = inputName.text.toString().trim()
                    val email = inputEmail.text.toString().trim()
                    val mobileNumber = inputNumber.text.toString().trim()
                    val password = inputPassword.text.toString().trim()
                    val confirmPassword = inputConfirmPassword.text.toString().trim()
                    mobileNo = mobileNumber

                    validateFormData(name, email, mobileNumber, password, confirmPassword)?.let { error ->
                        root.showMessage(error)
                    } ?: run {
                        viewModel.requestUserRegistration(name, email, mobileNumber, password)
                    }
                } else {
                    root.showMessage(getString(R.string.accept_terms_conditions_and_privacy_policy))
                }
            }

            clickToLoginBtn.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun validateFormData(
        name: String,
        email: String,
        mobileNumber: String,
        password: String,
        confirmPassword: String,
    ) : String? {
        return if(name.isEmpty() || mobileNumber.isEmpty()) getString(R.string.fill_all_the_fields)
        else if(email.isNotEmpty() && !UtilityActions.isValidEmailId(email)) getString(R.string.enter_valid_email_id)
        else if (!UtilityActions.isValidMobileNumber(mobileNumber)) getString(R.string.enter_valid_mobile_number)
        else if(password.isEmpty()) getString(R.string.enter_password)
        else if(password.length < 8) getString(R.string.password_length_error)
        else if(password != confirmPassword) getString(R.string.password_doesnt_match)
        else null
    }
}