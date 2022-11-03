package com.aajeevika.buyer.view.profile

import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivityVM
import com.aajeevika.buyer.databinding.ActivityEditAddressBinding
import com.aajeevika.buyer.model.data_model.*
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.UtilityActions.showMessage
import com.aajeevika.buyer.utility.extension.gotoNewTask
import com.aajeevika.buyer.view.dialog.AlertDialog
import com.aajeevika.buyer.view.home.ActivityDashboard
import com.aajeevika.buyer.view.profile.viewmodel.AddressViewModel
import com.google.gson.Gson

class ActivityEditAddress : BaseActivityVM<ActivityEditAddressBinding, AddressViewModel>(
    R.layout.activity_edit_address,
    AddressViewModel::class
) {
    private var selectedCountry: CountryData? = null
    private var selectedState: StateData? = null
    private var selectedCity: CityData? = null
    private var addressData: AddressData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addressData = Gson().fromJson(preferencesHelper.addressData, Address::class.java).personal?:AddressData()

        viewDataBinding.address = addressData
    }

    override fun observeData() {
        super.observeData()

        viewModel.countryList.observe(this, { list ->
            list.firstOrNull { it.id == Constant.DEFAULT_COUNTRY_ID }?.let {
                addressData?.countryId = it.id
                addressData?.country = it.name
            }

            viewDataBinding.address = addressData
            viewDataBinding.executePendingBindings()
        })

        viewModel.stateList.observe(this, { list ->
            list.firstOrNull { it.id == Constant.DEFAULT_STATE_ID }?.let {
                addressData?.stateId = it.id
                addressData?.state = it.name
            }

            viewDataBinding.address = addressData
            viewDataBinding.executePendingBindings()
        })

        viewModel.cityNameList.observe(this, { list ->
            viewDataBinding.districtList = list
            viewDataBinding.executePendingBindings()
        })

        viewModel.requestStatus.observe(this, {
            AlertDialog(
                context = this,
                message = it,
                positive = getString(R.string.ok),
                positiveClick = {
                    gotoNewTask(ActivityDashboard::class.java)
                }
            ).show()
        })
    }

    override fun initListener() {
        super.initListener()
        viewDataBinding.inputCity.doOnTextChanged { text, _, _, _ ->
            selectedCity = viewModel.cityList.value?.find { it.name.equals(text.toString(), true) }
        }

        viewDataBinding.tvDone.setOnClickListener {
            val address = viewDataBinding.address?: AddressData()
            if (!validateData(address)) viewDataBinding.root.showMessage(getString(R.string.fill_all_the_fields))
            else {
                addressData?.let {
                    it.address_line_one = address.address_line_one
                    it.pincode = address.pincode
                    it.country = selectedCountry?.name ?: address.country
                    it.district = selectedCity?.name ?: address.district
                    it.state = selectedState?.name ?: address.state
                    it.countryId = selectedCountry?.id ?: address.countryId
                    it.districtId = selectedCity?.id ?: address.districtId
                    it.stateId = selectedState?.id ?: address.stateId

                    viewModel.requestUpdateAddress(it)
                }
            }
        }
    }

    private fun validateData(address: AddressData): Boolean = run {
        !address.address_line_one.isNullOrEmpty() &&
                !address.pincode.isNullOrEmpty() &&
                !address.country.isNullOrEmpty() &&
                !address.state.isNullOrEmpty() &&
                !address.district.isNullOrEmpty()
    }

}