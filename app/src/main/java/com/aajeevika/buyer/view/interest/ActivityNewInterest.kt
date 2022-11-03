package com.aajeevika.buyer.view.interest

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.core.content.ContextCompat
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivityVM
import com.aajeevika.buyer.databinding.ActivityNewInterestBinding
import com.aajeevika.buyer.model.data_model.ProductBasicData
import com.aajeevika.buyer.model.data_model.UserData
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.UtilityActions.showMessage
import com.aajeevika.buyer.utility.extension.goto
import com.aajeevika.buyer.view.dialog.AlertDialog
import com.aajeevika.buyer.view.interest.adapter.InterestDetailsProductRecyclerViewAdapter
import com.aajeevika.buyer.view.interest.viewmodel.MyInterestsViewModel
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject

class ActivityNewInterest : BaseActivityVM<ActivityNewInterestBinding, MyInterestsViewModel>(
    R.layout.activity_new_interest,
    MyInterestsViewModel::class
) {
   private var number = ""
    private val mAdapter =
        InterestDetailsProductRecyclerViewAdapter(arrayListOf())

    private val artisan: UserData by lazy {
        Gson().fromJson(
            intent.getStringExtra(Constant.ARTISAN_ID) ?: "{}", UserData::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.recyclerView.adapter = mAdapter
        viewDataBinding.seller = artisan
        number = artisan.mobile?:""
    }

    override fun observeData() {
        super.observeData()
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            callNowBtn.setOnClickListener {
                if (TextUtils.isEmpty(number)) {
                    viewDataBinding.root.showMessage(getString(R.string.unable_to_make_call))
                    return@setOnClickListener
                }

                if (ContextCompat.checkSelfPermission(
                        this@ActivityNewInterest,
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

            addProductsBtn.setOnClickListener {
                val intent = Intent(this@ActivityNewInterest, ActivityAddInterestProducts::class.java)
                intent.putParcelableArrayListExtra(Constant.PRODUCT_ID,mAdapter.dataList)
                intent.putExtra(Constant.ARTISAN_ID, artisan.id)
                goto(intent, 1000)
            }

            tvDone.setOnClickListener {
                if (mAdapter.dataList.isEmpty()) {
                    root.showMessage(getString(R.string.add_at_least_one_product))
                    return@setOnClickListener
                } else if(etMessage.text.toString().trim().isEmpty()) {
                    root.showMessage(getString(R.string.enter_your_message))
                    return@setOnClickListener
                }

                val jsonObject = JsonObject()
                val products = JsonArray()
                mAdapter.dataList.forEach {
                    val product = JsonObject()
                    product.addProperty("product_id", it.id)
                    product.addProperty("quantity", it.selectedQuantity)
                    products.add(product)
                }
                jsonObject.addProperty("message", viewDataBinding.etMessage.text.toString())
                jsonObject.addProperty("seller_id", artisan.id)
                jsonObject.add("products", products)

                viewModel.addInterest(jsonObject) {
                    AlertDialog(
                        context = this@ActivityNewInterest,
                        message = it,
                        positive = getString(R.string.ok),
                        positiveClick = { onBackPressed() }
                    ).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            val list = data?.getParcelableArrayListExtra<ProductBasicData>(Constant.PRODUCT_ID)
                ?: arrayListOf()

            mAdapter.dataList.clear()
            mAdapter.dataList.addAll(list)
            mAdapter.notifyDataSetChanged()
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