package com.aajeevika.buyer.view.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivityVM
import com.aajeevika.buyer.databinding.ActivityProfileBinding
import com.aajeevika.buyer.model.data_model.AddressData
import com.aajeevika.buyer.model.data_model.UserProfileModel
import com.aajeevika.buyer.utility.UtilityActions.formatAddress
import com.aajeevika.buyer.utility.UtilityActions.getPicturesFileUri
import com.aajeevika.buyer.utility.parseRatting
import com.aajeevika.buyer.view.application.ActivityRatingAndReviews
import com.aajeevika.buyer.view.dialog.AlertDialog
import com.aajeevika.buyer.view.profile.viewmodel.ProfileViewModel
import com.google.gson.Gson
import com.yalantis.ucrop.UCrop
import java.io.File

class ActivityProfile : BaseActivityVM<ActivityProfileBinding, ProfileViewModel>(
    R.layout.activity_profile,
    ProfileViewModel::class
) {

    private val galleryRequestCode = 202
    private val cameraRequestCode = 201


    override fun onResume() {
        super.onResume()
        viewModel.requestUserProfile()
    }

    override fun observeData() {
        super.observeData()

        viewModel.requestStatus.observe(this, {
            viewModel.requestUserProfile()
        })

        viewModel.userProfile.observe(this, {
            setupProfile(it)
        })
    }

    private fun setupProfile(userProfileModel: UserProfileModel) {

        preferencesHelper.address = formatAddress(
            userProfileModel.address?.personal ?: userProfileModel.address?.registered
        )
        viewDataBinding.user = userProfileModel.user
        viewDataBinding.address = preferencesHelper.address
        viewDataBinding.rating = userProfileModel.rating
        viewDataBinding.executePendingBindings()
    }

    override fun initListener() {
        viewDataBinding.run {

            editBtn.setOnClickListener {
                val intent = Intent(this@ActivityProfile, ActivityEditProfile::class.java)
                startActivity(intent)
            }

            ratingBtn.setOnClickListener {
                val intent = Intent(this@ActivityProfile, ActivityRatingAndReviews::class.java)
                startActivity(intent)
            }

            ivProfile.setOnClickListener {
                AlertDialog(
                    context = this@ActivityProfile,
                    message = getString(R.string.how_to_add_photo),
                    positive = getString(R.string.camera),
                    negative = getString(R.string.gallery),
                    positiveClick = {
                        if (ContextCompat.checkSelfPermission(
                                this@ActivityProfile,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            initiateCamera()
                        } else {
                            requestPermissions(
                                arrayOf(Manifest.permission.CAMERA),
                                cameraRequestCode
                            )
                        }
                    },
                    negativeClick = {
                        val intent = Intent()
                        intent.type = "image/*"
                        intent.action = Intent.ACTION_GET_CONTENT
                        startActivityForResult(
                            Intent.createChooser(intent, getString(R.string.select_image)),
                            galleryRequestCode
                        )
                    }
                ).show()
            }
        }
    }

    private fun initiateCamera() {
        startActivityForResult(
            Intent.createChooser(
                Intent(MediaStore.ACTION_IMAGE_CAPTURE),
                getString(R.string.capture_image)
            ), cameraRequestCode
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraRequestCode && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            initiateCamera()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            UCrop.REQUEST_CROP -> {
                data?.let { intent ->
                    UCrop.getOutput(intent)?.let {
                        viewModel.requestProfileImageUpload(it.toFile())
                    }
                }
            }
            cameraRequestCode -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.let {
                        val response = it.extras?.get("data") as Bitmap
                        val destination = File(cacheDir, "user_profile.jpg")
                        if (!destination.exists()) destination.createNewFile()
                        val outputStream = destination.outputStream()
                        response.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        UCrop.of(Uri.fromFile(destination), Uri.fromFile(destination))
                            .withAspectRatio(1F, 1F)
                            .withOptions(UCrop.Options().apply {
                                setCircleDimmedLayer(true)
                            })
                            .start(this)
                    }
                }
            }
            galleryRequestCode -> {
                data?.let {
                    val destinationFileName = "${System.currentTimeMillis()}_user_profile.jpg"
                    val file = File(cacheDir, destinationFileName)
                    if (!file.exists()) file.createNewFile()
                    it.data?.let { sUri ->
                        UCrop.of(sUri, Uri.fromFile(file))
                            .withAspectRatio(1F, 1F)
                            .withOptions(UCrop.Options().apply {
                                setCircleDimmedLayer(true)
                            })
                            .start(this)
                    }
                }
            }
        }
    }
}