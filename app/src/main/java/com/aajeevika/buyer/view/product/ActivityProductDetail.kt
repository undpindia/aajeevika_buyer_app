package com.aajeevika.buyer.view.product

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivityVM
import com.aajeevika.buyer.databinding.ActivityProductDetailsBinding
import com.aajeevika.buyer.databinding.ListItemCertificateBinding
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.Constant.PRODUCT_ID
import com.aajeevika.buyer.utility.RecyclerViewDecoration
import com.aajeevika.buyer.utility.loadImageFromNetwork
import com.aajeevika.buyer.view.product.adapter.ProductDetailPreviewRecyclerViewAdapter
import com.aajeevika.buyer.view.product.viewmodel.ProductDetailViewModel
import com.aajeevika.buyer.view.profile.ActivitySellerProfile

class ActivityProductDetail : BaseActivityVM<ActivityProductDetailsBinding, ProductDetailViewModel>(
    R.layout.activity_product_details,
    ProductDetailViewModel::class
) {
    private  var previewProductImageAdapter =  ProductDetailPreviewRecyclerViewAdapter()
    private var artisianId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.recyclerView.run {
            adapter = previewProductImageAdapter
            addItemDecoration(RecyclerViewDecoration(4F,4F,4F,4F))
            (layoutManager as? GridLayoutManager)?.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if(position == 0) 4 else 1
                }
            }
        }

        observeData()

        viewModel.requestProductDetails( intent.extras?.getInt(PRODUCT_ID))
    }

    override fun observeData() {
        super.observeData()
        viewModel.productDetails.observe(this, { product ->
            val productImages = ArrayList<String>()
            product.productdetail?.image_1?.let { productImages.add(it) }
            product.productdetail?.image_2?.let { productImages.add(it) }
            product.productdetail?.image_3?.let { productImages.add(it) }
            product.productdetail?.image_4?.let { productImages.add(it) }
            product.productdetail?.image_5?.let { productImages.add(it) }
            previewProductImageAdapter.setData(productImages, product.productdetail?.video_url)

            val subTitleStringBuilder = StringBuilder("${ product.productdetail?.catName } | ${ product.productdetail?.SubCatName }")
            product.productdetail?.width?.run { subTitleStringBuilder.append("\n${getString(R.string.width)}: $this ${product.productdetail?.width_unit}") }
            product.productdetail?.height?.run { subTitleStringBuilder.append("\n${getString(R.string.height)}: $this ${product.productdetail?.height_unit}") }
            product.productdetail?.length?.run { subTitleStringBuilder.append("\n${getString(R.string.length)}: $this ${product.productdetail?.length_unit}") }
            product.productdetail?.vol?.run { subTitleStringBuilder.append("\n${getString(R.string.volume)}: $this ${product.productdetail?.vol_unit}") }
            product.productdetail?.weight?.run { subTitleStringBuilder.append("\n${getString(R.string.weight)}: $this ${product.productdetail?.weight_unit}") }

            viewDataBinding.product = product.productdetail
            viewDataBinding.subTitle = subTitleStringBuilder.toString()

            artisianId = product.productdetail?.artisanid?:0
            viewDataBinding.executePendingBindings()

            var statusCount = 0
            statusCount += product.productdetail?.certificate_data?.certificate_status_1 ?: 0
            statusCount += product.productdetail?.certificate_data?.certificate_status_2 ?: 0
            statusCount += product.productdetail?.certificate_data?.certificate_status_3 ?: 0
            statusCount += product.productdetail?.certificate_data?.certificate_status_4 ?: 0
            statusCount += product.productdetail?.certificate_data?.certificate_status_5 ?: 0
            statusCount += product.productdetail?.certificate_data?.certificate_status_6 ?: 0
            statusCount += product.productdetail?.certificate_data?.certificate_status_7 ?: 0

            var totalAvailableCert = 0
            product.productdetail?.certificate_data?.certificate_image_1?.run { totalAvailableCert++ }
            product.productdetail?.certificate_data?.certificate_image_2?.run { totalAvailableCert++ }
            product.productdetail?.certificate_data?.certificate_image_3?.run { totalAvailableCert++ }
            product.productdetail?.certificate_data?.certificate_image_4?.run { totalAvailableCert++ }
            product.productdetail?.certificate_data?.certificate_image_5?.run { totalAvailableCert++ }
            product.productdetail?.certificate_data?.certificate_image_6?.run { totalAvailableCert++ }
            product.productdetail?.certificate_data?.certificate_image_7?.run { totalAvailableCert++ }

            if(statusCount == totalAvailableCert) {
                viewDataBinding.run {
                    product.productdetail?.certificate_data?.certificate_image_1?.run {
                        certificatesTxt.visibility = View.VISIBLE
                        certificateList.visibility = View.VISIBLE
                        divider.visibility = View.VISIBLE
                    }

                    certificateList.removeAllViews()
                    product.productdetail?.certificate_data?.let { cert ->
                        cert.certificate_image_1?.run { initCert(certificateList, this, cert.certificate_type_name_1) }
                        cert.certificate_image_2?.run { initCert(certificateList, this, cert.certificate_type_name_2) }
                        cert.certificate_image_3?.run { initCert(certificateList, this, cert.certificate_type_name_3) }
                        cert.certificate_image_4?.run { initCert(certificateList, this, cert.certificate_type_name_4) }
                        cert.certificate_image_5?.run { initCert(certificateList, this, cert.certificate_type_name_5) }
                        cert.certificate_image_6?.run { initCert(certificateList, this, cert.certificate_type_name_6) }
                        cert.certificate_image_7?.run { initCert(certificateList, this, cert.certificate_type_name_7) }
                    }
                }
            }
        })
    }

    private fun initCert(list: LinearLayout, image: String, name: String?) {
        val binding = ListItemCertificateBinding.inflate(layoutInflater, list, false)
        binding.run {
            certificateName.text = name
            certificateImage.loadImageFromNetwork(BaseUrls.baseUrl + image, null)
        }
        list.addView(binding.root)
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }
            tvSeller.setOnClickListener {
                val intent = Intent(this@ActivityProductDetail, ActivitySellerProfile::class.java)
                intent.putExtra(Constant.ARTISAN_ID,artisianId)
                startActivity(intent)
            }
        }
    }
}