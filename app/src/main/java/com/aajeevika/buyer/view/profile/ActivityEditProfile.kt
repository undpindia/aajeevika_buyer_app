package com.aajeevika.buyer.view.profile

import android.content.Intent
import android.os.Bundle
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivityVM
import com.aajeevika.buyer.databinding.ActivityEditProfileBinding
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.UtilityActions
import com.aajeevika.buyer.utility.UtilityActions.showMessage
import com.aajeevika.buyer.utility.app_enum.OtpVerificationType
import com.aajeevika.buyer.utility.extension.goto
import com.aajeevika.buyer.view.auth.ActivityForgotPassword
import com.aajeevika.buyer.view.dialog.AlertDialog
import com.aajeevika.buyer.view.profile.viewmodel.ProfileViewModel

class ActivityEditProfile : BaseActivityVM<ActivityEditProfileBinding, ProfileViewModel>(
    R.layout.activity_edit_profile,
    ProfileViewModel::class
) {
    private var newName: String? = null
    private var newEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.inputName.setText(preferencesHelper.name)
        viewDataBinding.inputEmail.setText(preferencesHelper.email)
        viewDataBinding.inputMobile.setText(preferencesHelper.mobile)
        viewDataBinding.inputAddress.setText(preferencesHelper.address)
    }

    override fun observeData() {
        super.observeData()
        viewModel.requestStatus.observe(this, {
            preferencesHelper.name = newName?:""
            preferencesHelper.email = newEmail?:""
            AlertDialog(
                context = this,
                message = it,
                positive = getString(R.string.ok),
                positiveClick = {
                    onBackPressed()
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
                val name = viewDataBinding.inputName.text.toString()
                val email = viewDataBinding.inputEmail.text.toString()
                val error = when {
                    name.isEmpty() -> getString(R.string.enter_name)
                    email.isNotEmpty() && !UtilityActions.isValidEmailId(email) -> getString(R.string.enter_valid_email_id)
                    else -> null
                }

                error?.let {
                    viewDataBinding.root.showMessage(error)
                } ?: kotlin.run {
                    newName = name
                    newEmail = email
                    viewModel.requestUpdateUserProfile(name, email)
                }
            }

            changeEmailBtn.setOnClickListener {
                val intent = Intent(this@ActivityEditProfile,ActivityForgotPassword::class.java)
                intent.putExtra(Constant.OTP_VERIFICATION_TYPE,OtpVerificationType.CHANGE_EMAIL_ID.name)
                goto(intent)
            }

            changeMobileBtn.setOnClickListener {
                val intent = Intent(this@ActivityEditProfile,ActivityForgotPassword::class.java)
                intent.putExtra(Constant.OTP_VERIFICATION_TYPE,OtpVerificationType.CHANGE_MOBILE_NUMBER.name)
                goto(intent)
            }

            changeAddressBtn.setOnClickListener {
                goto(ActivityEditAddress::class.java)
            }
        }
    }
}