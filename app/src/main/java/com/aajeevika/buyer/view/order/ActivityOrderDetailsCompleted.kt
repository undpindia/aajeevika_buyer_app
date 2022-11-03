package com.aajeevika.buyer.view.order

import BaseUrls
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivityVM
import com.aajeevika.buyer.databinding.ActivityOrderDetailsCompletedBinding
import com.aajeevika.buyer.model.data_model.BuyerRating
import com.aajeevika.buyer.model.data_model.ProductBasicData
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.UtilityActions.showMessage
import com.aajeevika.buyer.utility.app_enum.MyOrderType
import com.aajeevika.buyer.view.dialog.AlertDialog
import com.aajeevika.buyer.view.order.adapter.OrderDetailsProductRecyclerViewAdapter
import com.aajeevika.buyer.view.order.viewmodel.OrderDetailsViewModel

class ActivityOrderDetailsCompleted : BaseActivityVM<ActivityOrderDetailsCompletedBinding, OrderDetailsViewModel>(
    R.layout.activity_order_details_completed,
    OrderDetailsViewModel::class
) {
    var number = ""
    var orderId = ""
    var buyerRating: BuyerRating? = null

    private val mAdapter = OrderDetailsProductRecyclerViewAdapter(arrayListOf())

    private val id: Int by lazy { intent.getIntExtra(Constant.ORDER_ID, 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.recyclerView.adapter = mAdapter
        viewModel.orderDetail(id)
    }

    override fun observeData() {
        super.observeData()
        viewModel.orderDetail.observe(this, {
            if (it.isNotEmpty()) {
                val order = it[0]
                orderId = order.id.toString()
                viewDataBinding.order = order

                if(order.order_status == MyOrderType.COMPLETED.type) {
                    buyerRating = order.buyer_rating

                    order.buyer_rating?.let {
                        viewDataBinding.run {
                            saveBtn.visibility = View.GONE
                            ratingBar.visibility = View.GONE
                            etMessage.visibility = View.GONE
                            buyerRatingContainer.visibility = View.VISIBLE

                            buyerProfileImage = if (preferencesHelper.profileImage.isNotEmpty()) BaseUrls.baseUrl + preferencesHelper.profileImage else null
                            buyerName = preferencesHelper.name
                            buyerRating = order.buyer_rating.rating ?: 0F
                            buyerReviewMessage = order.buyer_rating.review_msg
                        }
                    } ?: run {
                        viewDataBinding.run {
                            saveBtn.visibility = View.VISIBLE
                            ratingBar.visibility = View.VISIBLE
                            etMessage.visibility = View.VISIBLE
                            buyerRatingContainer.visibility = View.GONE
                        }
                    }

                    order.seller_rating?.let {
                        viewDataBinding.run {
                            sellerRatingContainer.visibility = View.VISIBLE

                            sellerProfileImage = order.seller?.profileImage?.run { BaseUrls.baseUrl + this }
                            sellerOrganizationName = order.seller?.organization_name
                            sellerRating = order.seller_rating.rating ?: 0F
                            sellerReviewMessage = order.seller_rating.review_msg
                        }
                    }
                } else {
                    viewDataBinding.tvOtp.visibility = View.VISIBLE
                }

                viewDataBinding.executePendingBindings()

                number = order.seller?.mobile ?: ""
                var totalPrice: Long = 0
                val list = order.items?.map { ip ->
                    try { totalPrice += ip.quantity * ip.product_price } catch (e: Exception) { }
                    ProductBasicData(
                        id = ip.product?.id,
                        name = ip.product?.name,
                        image_1 = ip.product?.image_1,
                        selectedQuantity = ip.quantity,
                        orderPrice = ip.product_price,
                        price = ip.product?.price,
                        price_unit = ip.product?.price_unit,
                        product_id_d = ip.product?.product_id_d
                    )
                }?.toList()

                list?.let { it1 -> mAdapter.dataList.addAll(it1) }
                mAdapter.notifyDataSetChanged()
                viewDataBinding.totalAmountValue.text = "₹$totalPrice"
            }
        })
    }

    @SuppressLint("NewApi")
    override fun initListener() {
        super.initListener()
        viewDataBinding.callNowBtn.setOnClickListener {
            if (TextUtils.isEmpty(number)) {
                viewDataBinding.root.showMessage(getString(R.string.unable_to_make_call))
                return@setOnClickListener
            }

            if (ContextCompat.checkSelfPermission(
                    this@ActivityOrderDetailsCompleted,
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

        viewDataBinding.saveBtn.setOnClickListener {
            val rating = viewDataBinding.ratingBar.rating
            val msg = viewDataBinding.etMessage.text.toString().trim()
            val userId = viewDataBinding.order?.seller?.id ?: 0



            if (rating <= 0) {
                viewDataBinding.root.showMessage(getString(R.string.rating_cant_be_zero))
                return@setOnClickListener
            }
            viewModel.addRating(orderId, userId, rating, reviewMsg = msg) {
                viewDataBinding.saveBtn.visibility = View.GONE
                viewDataBinding.etMessage.isEnabled = false

                AlertDialog(
                    context = viewDataBinding.root.context,
                    message = it,
                    positive = getString(R.string.ok),
                    cancelOnOutsideClick = false,
                    positiveClick = {
                        intent.putExtra(Constant.ORDER_ID, id)
                        startActivity(intent)
                        finish()
                        overridePendingTransition(0, 0)
                    }
                ).show()
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