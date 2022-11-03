package com.aajeevika.buyer.view.interest

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivityVM
import com.aajeevika.buyer.databinding.ActivityInterestsDetailsBinding
import com.aajeevika.buyer.model.data_model.ProductBasicData
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.UtilityActions.showMessage
import com.aajeevika.buyer.view.interest.adapter.InterestDetailsProductRecyclerViewAdapter
import com.aajeevika.buyer.view.interest.viewmodel.MyInterestsViewModel

class ActivityInterestDetails : BaseActivityVM<ActivityInterestsDetailsBinding, MyInterestsViewModel>(
    R.layout.activity_interests_details,
    MyInterestsViewModel::class
) {

    var number = ""
    val id: Int by lazy { intent.getIntExtra(Constant.PRODUCT_ID, 0) }
    private val mAdapter = InterestDetailsProductRecyclerViewAdapter(
        arrayListOf()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.recyclerView.adapter = mAdapter
        viewModel.interestDetail(id)
    }

    override fun observeData() {
        super.observeData()
        viewModel.interest.observe(this, {
            if (it.isNotEmpty()) {
                val interest = it[0]
                viewDataBinding.interest = interest
                viewDataBinding.executePendingBindings()

                number = interest.seller?.mobile ?: ""
                val list = interest.items?.map { ip ->
                    ProductBasicData(
                        id = ip.product?.id,
                        name = ip.product?.name,
                        image_1 = ip.product?.image_1,
                        selectedQuantity = ip.quantity,
                        price = ip.product?.price,
                        price_unit = ip.product?.price_unit,
                        product_id_d = ip.product?.product_id_d
                    )
                }?.toList()
                list?.let { it1 -> mAdapter.dataList.addAll(it1) }
                mAdapter.notifyDataSetChanged()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initListener() {
        viewDataBinding.callNowBtn.setOnClickListener {
            if (TextUtils.isEmpty(number)) {
                viewDataBinding.root.showMessage(getString(R.string.unable_to_make_call))
                return@setOnClickListener
            }

            if (ContextCompat.checkSelfPermission(
                    this@ActivityInterestDetails,
                    Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                initCall()
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.CALL_PHONE),
                    1010
                )
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1010 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            initCall()
    }

    private fun initCall() {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:91$number"))
        startActivity(intent)
    }
}