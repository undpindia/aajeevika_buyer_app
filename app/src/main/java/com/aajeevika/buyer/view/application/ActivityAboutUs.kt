package com.aajeevika.buyer.view.application

import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivity
import com.aajeevika.buyer.databinding.ActivityAboutUsBinding

class ActivityAboutUs : BaseActivity<ActivityAboutUsBinding>(R.layout.activity_about_us) {
    override fun initListener() {
        viewDataBinding.toolbar.backBtn.setOnClickListener {
            onBackPressed()
        }
    }
}