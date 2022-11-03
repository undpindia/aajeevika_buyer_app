package com.aajeevika.buyer.view.home

import BaseUrls
import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.aajeevika.buyer.BuildConfig
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivityVM
import com.aajeevika.buyer.databinding.ActivityDashboardBinding
import com.aajeevika.buyer.location.activity.PickLocationActivity
import com.aajeevika.buyer.location.constants.EXTRA_ADDRESS
import com.aajeevika.buyer.location.constants.EXTRA_LATITUDE
import com.aajeevika.buyer.location.constants.EXTRA_LONGITUDE
import com.aajeevika.buyer.location.constants.REQ_SELECT_ADDRESS
import com.aajeevika.buyer.utility.RecyclerViewDecoration
import com.aajeevika.buyer.utility.UtilityActions
import com.aajeevika.buyer.utility.app_enum.HomeDataType
import com.aajeevika.buyer.utility.app_enum.LanguageType
import com.aajeevika.buyer.utility.camelCaseText
import com.aajeevika.buyer.utility.extension.*
import com.aajeevika.buyer.utility.loadProfileImage
import com.aajeevika.buyer.view.application.ActivityAboutUs
import com.aajeevika.buyer.view.application.ActivityFaqs
import com.aajeevika.buyer.view.application.ActivityNotifications
import com.aajeevika.buyer.view.auth.ActivityLogin
import com.aajeevika.buyer.view.dialog.AlertDialog
import com.aajeevika.buyer.view.grievance.ActivityGrievance
import com.aajeevika.buyer.view.home.adapter.DashboardCategoryRecyclerViewAdapter
import com.aajeevika.buyer.view.home.adapter.DashboardRecyclerViewAdapter
import com.aajeevika.buyer.view.home.adapter.HeaderViewPager
import com.aajeevika.buyer.view.home.viewmodel.DashboardViewModel
import com.aajeevika.buyer.view.interest.ActivityMyInterests
import com.aajeevika.buyer.view.order.ActivityMyOrder
import com.aajeevika.buyer.view.profile.ActivityProfile
import kotlin.math.abs

class ActivityDashboard : BaseActivityVM<ActivityDashboardBinding, DashboardViewModel>(
    R.layout.activity_dashboard,
    DashboardViewModel::class
) {
    private val dashboardCategoryRecyclerViewAdapter = DashboardCategoryRecyclerViewAdapter(arrayListOf())
    private val headerViewPager by lazy { HeaderViewPager(layoutInflater, arrayListOf()) }
    private val dashboardRecyclerViewAdapter = DashboardRecyclerViewAdapter {
        viewModel.requestLoadMoreProducts(null, it)
    }

    private val callRequestCallback = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if(it == true) {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${BaseUrls.contactUsNumber}"))
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.categoryRecyclerView.run {
            adapter = dashboardCategoryRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(16F, 16F, 16F, 16F))
        }

        viewDataBinding.recyclerView.run {
            adapter = dashboardRecyclerViewAdapter
        }

        viewDataBinding.run {
            version = BuildConfig.VERSION_NAME

            viewPager.adapter = headerViewPager
            viewPager.pageMargin = 16
            viewPager.setPageTransformer(false) { page, position ->
                page.scaleX = 1F - 0.05F * abs(position)
                page.scaleY = 1F - 0.25F * abs(position)
            }

            tabLayout.setupWithViewPager(viewPager)

            if(preferencesHelper.appLanguage == LanguageType.ENGLISH.code) languageSwitch.isChecked = false
            else if(preferencesHelper.appLanguage == LanguageType.HINDI.code) languageSwitch.isChecked = true
        }

        viewModel.getAddressFromLatLng(
            hashMapOf(
                "key" to getString(R.string.google_api_key),
                "latlng" to "${preferencesHelper.latitude},${preferencesHelper.longitude}"
            )
        )
    }

    override fun onResume() {
        super.onResume()

        viewModel.requestHomeData()
        viewModel.requestCategories()
        viewModel.requestBannerList()

        viewDataBinding.run {
            if (preferencesHelper.authToken == "") {
                logoutBtn.gone()
                profileContainer.gone()
                guestContainer.visible()
            } else {
                profileContainer.visible()
                guestContainer.gone()
                logoutBtn.visible()
            }
        }

        setProfile()
    }

    override fun observeData() {
        super.observeData()

        viewModel.address.observe(this) {
            if (it.isNotEmpty()) {
                viewDataBinding.tvLocation.text = it[0].formatted_address
            }
        }

        viewModel.bannerList.observe(this, {
            viewDataBinding.viewPager.visibility = View.VISIBLE
            headerViewPager.setData(it.banner ?: arrayListOf())
        })

        viewModel.categories.observe(this, {
            viewDataBinding.categoryRecyclerView.visibility = View.VISIBLE
            dashboardCategoryRecyclerViewAdapter.setData(it.category ?: arrayListOf())
        })

        viewModel.homeData.observe(this, {
            onErrorReturn(null)
            dashboardRecyclerViewAdapter.addData(it.data, -1, -1)
            val initialArtisans: List<Int?> = it.data.filter { data -> data.type == HomeDataType.SHG_ARTISAN.type }.map { artisan -> artisan.id }

            viewModel.requestLoadMoreProducts(initialArtisans)
        })

        viewModel.artisanHomeProductList.observe(this, {
            it?.let { data ->
                dashboardRecyclerViewAdapter.addData(data.data, data.pagination.currentPage, data.pagination.last_page)
            }
        })
    }

    override fun initListener() {

        viewDataBinding.run {
            swipeRefresh.setOnRefreshListener {
                viewModel.requestHomeData()
            }

            menuBtn.setOnClickListener {
                drawerLayout.openDrawer(GravityCompat.START)
            }

            searchBtn.setOnClickListener {
                val intent = Intent(this@ActivityDashboard, ActivitySearchProduct::class.java)
                startActivity(intent)
            }

            tvLocation.setOnClickListener {
                goto(PickLocationActivity::class.java, REQ_SELECT_ADDRESS)
            }

            notificationBtn.setOnClickListener {
                if (preferencesHelper.authToken == "") {
                    showLoginPopup()
                } else {
                    goto(ActivityNotifications::class.java)
                }
            }

            guestContainer.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                goto(ActivityLogin::class.java)
            }

            profileContainer.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)

                if (preferencesHelper.authToken == "") {
                    showLoginPopup()
                } else {
                    goto(ActivityProfile::class.java)
                }
            }

            myProfileBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)

                if (preferencesHelper.authToken == "") {
                    showLoginPopup()
                } else {
                    goto(ActivityProfile::class.java)
                }
            }

            languageSwitch.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked && preferencesHelper.appLanguage != LanguageType.HINDI.code) {
                    preferencesHelper.appLanguage = LanguageType.HINDI.code

                    startActivity(intent)
                    finish()
                    overridePendingTransition(0, 0)
                } else if(!isChecked && preferencesHelper.appLanguage != LanguageType.ENGLISH.code) {
                    preferencesHelper.appLanguage = LanguageType.ENGLISH.code

                    startActivity(intent)
                    finish()
                    overridePendingTransition(0, 0)
                }
            }

            myInternetBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)

                if (preferencesHelper.authToken == "") {
                    showLoginPopup()
                } else {
                    goto(ActivityMyInterests::class.java)
                }
            }

            myOrderBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)

                if (preferencesHelper.authToken == "") {
                    showLoginPopup()
                } else {
                    goto(ActivityMyOrder::class.java)
                }
            }

            favouriteSellerBtn.setOnClickListener {
                /*viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivitySales::class.java)
                startActivity(intent)*/
            }

            supportBtn.setOnClickListener {
                callRequestCallback.launch(Manifest.permission.CALL_PHONE)
            }

            aboutUsBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivityAboutUs::class.java)
                startActivity(intent)
            }

            grievanceBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)

                if (preferencesHelper.authToken == "") {
                    showLoginPopup()
                } else {
                    val intent = Intent(this@ActivityDashboard, ActivityGrievance::class.java)
                    startActivity(intent)
                }
            }

            faqBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivityFaqs::class.java)
                startActivity(intent)
            }

            logoutBtn.setOnClickListener {
                AlertDialog(
                    context = this@ActivityDashboard,
                    message = getString(R.string.logout_confirmation_message),
                    positive = getString(R.string.cancel),
                    negative = getString(R.string.yes_logout),
                    negativeClick = {
                        preferencesHelper.clear()
                        UtilityActions.updateFCM(preferencesHelper)

                        gotoNewTask(ActivityLogin::class.java)
                    }
                ).show()
            }

            drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
                override fun onDrawerStateChanged(newState: Int) {}
                override fun onDrawerClosed(drawerView: View) {}

                override fun onDrawerOpened(drawerView: View) {
                    UtilityActions.closeKeyboard(this@ActivityDashboard, viewDataBinding.root)
                    setProfile()
                }
            })
        }
    }

    private fun setProfile() {
        viewDataBinding.tvName.camelCaseText(preferencesHelper.name)
        viewDataBinding.ivProfile.loadProfileImage(preferencesHelper.profileImage)
        viewDataBinding.tvEmail.text = if(preferencesHelper.email.isNotEmpty()) preferencesHelper.email else preferencesHelper.mobile
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_SELECT_ADDRESS && resultCode == RESULT_OK) {
            data?.let { it ->
                val address = it.getStringExtra(EXTRA_ADDRESS) ?: "NA"
                val lat = it.getDoubleExtra(EXTRA_LATITUDE, 0.0)
                val lng = it.getDoubleExtra(EXTRA_LONGITUDE, 0.0)

                preferencesHelper.latitude = lat
                preferencesHelper.longitude = lng

                viewDataBinding.tvLocation.text = address
            }
        }
    }
}