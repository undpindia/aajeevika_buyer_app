package com.aajeevika.buyer.view.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivityVM
import com.aajeevika.buyer.databinding.ActivitySellerProfileBinding
import com.aajeevika.buyer.model.data_model.ArtisanProfileModel
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.UtilityActions.formatAddress
import com.aajeevika.buyer.utility.UtilityActions.showMessage
import com.aajeevika.buyer.utility.extension.goto
import com.aajeevika.buyer.utility.extension.showLoginPopup
import com.aajeevika.buyer.view.application.ActivityNotifications
import com.aajeevika.buyer.view.application.ActivityRatingAndReviews
import com.aajeevika.buyer.view.auth.ActivityLogin
import com.aajeevika.buyer.view.interest.ActivityNewInterest
import com.aajeevika.buyer.view.product.adapter.ProductCategoryRecyclerViewAdapter
import com.aajeevika.buyer.view.profile.viewmodel.ProfileViewModel
import com.google.gson.Gson

class ActivitySellerProfile : BaseActivityVM<ActivitySellerProfileBinding, ProfileViewModel>(
    R.layout.activity_seller_profile,
    ProfileViewModel::class
) {
    private val artisanId :Int by lazy {  intent.getIntExtra(Constant.ARTISAN_ID, 0)}
    private val productCategoryRecyclerViewAdapter = ProductCategoryRecyclerViewAdapter(arrayListOf())
    private lateinit var artisanProfileModel: ArtisanProfileModel

    private val callPermissionCallback = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if(::artisanProfileModel.isInitialized && it==true) {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:+91${artisanProfileModel.user?.mobile}"))
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productCategoryRecyclerViewAdapter.artisanId = artisanId
        viewDataBinding.productsRecyclerView.adapter = productCategoryRecyclerViewAdapter
    }

    override fun onResume() {
        super.onResume()

        viewModel.requestArtisanProfile(artisanId)
        viewModel.requestArtisanProducts(artisanId)
    }

    override fun observeData() {
        super.observeData()

        viewModel.requestStatusLiveData.observe(this, {
            viewDataBinding.isLiked = !viewDataBinding.isLiked
            viewDataBinding.executePendingBindings()

            Toast.makeText(this, getString(
                if(viewDataBinding.isLiked) R.string.favourite_added_successfully
                else R.string.favourite_removed_successfully
            ), Toast.LENGTH_SHORT).show()
        })

        viewModel.artisanProfile.observe(this, {
            artisanProfileModel = it

            viewDataBinding.user = it.user
            viewDataBinding.address = formatAddress(it.address)
            viewDataBinding.rating = it.rating
            viewDataBinding.isLiked = it.favoriteStatus == 1
            viewDataBinding.executePendingBindings()
        })

        viewModel.artisanProduct.observe(this, {
            it?.run {
                productCategoryRecyclerViewAdapter.dataList = it.products?: arrayListOf()
                productCategoryRecyclerViewAdapter.notifyDataSetChanged()
            }
        })
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
        val number =viewDataBinding.inputNumber.text.toString()
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initListener() {

        viewDataBinding.phoneCall.setOnClickListener {
            if (TextUtils.isEmpty(viewDataBinding.inputNumber.text.toString())) {
                viewDataBinding.root.showMessage(getString(R.string.unable_to_make_call))
                return@setOnClickListener
            }

            if (ContextCompat.checkSelfPermission(
                    this,
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

        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            toolbar.likeBtn.setOnClickListener {
                if (preferencesHelper.authToken == "") {
                    showLoginPopup(true)
                } else {
                    if(::artisanProfileModel.isInitialized) {
                        artisanProfileModel.user?.id?.let { viewModel.addFavorite(it, if(isLiked) 0 else 1) }
                    }
                }
            }

            ratingBtn.setOnClickListener {
                val intent = Intent(this@ActivitySellerProfile, ActivityRatingAndReviews::class.java)
                intent.putExtra(Constant.ARTISAN_ID,artisanId)
                startActivity(intent)
            }

            callBtn.setOnClickListener {
                callPermissionCallback.launch(Manifest.permission.CALL_PHONE)
            }

            expressInterestBtn.setOnClickListener {
                if (preferencesHelper.authToken == "") {
                    showLoginPopup(true)
                } else {
                    val intent = Intent(this@ActivitySellerProfile, ActivityNewInterest::class.java)
                    intent.putExtra(Constant.ARTISAN_ID,Gson().toJson(viewDataBinding.user))
                    startActivity(intent)
                }
            }
        }
    }
}