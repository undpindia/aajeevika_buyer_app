package com.aajeevika.buyer.location.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import com.aajeevika.buyer.R
import com.aajeevika.buyer.databinding.ActivityPickLocationBinding
import com.aajeevika.buyer.location.adapter.AutoCompleteAdapter
import com.aajeevika.buyer.location.constants.*
import com.aajeevika.buyer.location.model.SearchLocationResponse
import com.aajeevika.buyer.location.viewModel.LocationViewModel
import com.aajeevika.buyer.utility.app_enum.ErrorType
import com.aajeevika.buyer.utility.app_enum.ProgressAction
import com.aajeevika.buyer.utility.extension.gone
import com.aajeevika.buyer.utility.extension.goto
import com.aajeevika.buyer.utility.extension.visible
import com.aajeevika.buyer.view.dialog.AlertDialog
import org.koin.androidx.viewmodel.ext.android.getViewModel

class PickLocationActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityPickLocationBinding
    private val mViewModel: LocationViewModel by lazy {
        getViewModel(clazz = LocationViewModel::class)
    }
    private val isChangeLocation: Boolean
        get() = intent.getBooleanExtra(EXTRA_BOOLEAN, false)
    private var mHandler: Handler = Handler(Looper.getMainLooper())
    private val mPredictionsList = ArrayList<SearchLocationResponse.Predictions>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pick_location)
        mBinding.lifecycleOwner = this
        mBinding.viewModel = mViewModel

        initAdapter()
        initListeners()
        initObserver()
    }

    private fun initAdapter() {
        mBinding.rvAddress.adapter = AutoCompleteAdapter(mPredictionsList) {
            val map = hashMapOf(
                "key" to getString(R.string.google_api_key),
                "placeid" to mPredictionsList[it].place_id
            )
            mViewModel.hitGetPlaceDetailsApi(map)
        }
    }

    private fun initListeners() {
        mBinding.toolbarLayout.backBtn.setOnClickListener { onBackPressed() }

        mBinding.etSearchLocation.doAfterTextChanged {
            mHandler.removeCallbacksAndMessages(null)
            if (it.isNullOrEmpty()) {
                mPredictionsList.clear()
                mBinding.noDataLayout.visibility = View.VISIBLE
                mBinding.rvAddress.visibility = View.GONE
            } else {
                mBinding.noDataLayout.visibility = View.GONE
                mBinding.rvAddress.visibility = View.VISIBLE
                mHandler.postDelayed({
                    val map = hashMapOf(
                        "key" to getString(R.string.google_api_key),
                        "input" to it.toString(),
                    )
                    mViewModel.hitGetPlaceApi(map)
                }, 1000)
            }
        }

        mBinding.currentLocation.setOnClickListener {
            goto(MapsActivity::class.java, REQ_SELECT_ADDRESS)
        }
    }

    private fun initObserver() {
        mViewModel.resPredictionsList.observe(this) {
            if (it.isNotEmpty()) {
                mPredictionsList.clear()
                mPredictionsList.addAll(it)
                mBinding.rvAddress.adapter?.notifyDataSetChanged()
            }
        }

        mViewModel.resPlaceDetails.observe(this) {
            if (isChangeLocation) {
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(EXTRA_LATITUDE, it.result.geometry.location.lat)
                    putExtra(EXTRA_LONGITUDE, it.result.geometry.location.lng)
                })
                finish()
            } else {
                goto(Intent(this, MapsActivity::class.java).apply {
                    putExtra(EXTRA_LATITUDE, it.result.geometry.location.lat)
                    putExtra(EXTRA_LONGITUDE, it.result.geometry.location.lng)
                }, REQ_SELECT_ADDRESS)
            }
        }

        mViewModel.progressHandler.observe(this, {
            when (it ?: ProgressAction.NONE) {
                ProgressAction.PROGRESS_BAR -> {
                    findViewById<ProgressBar>(R.id.progress_bar)?.visible()
                }
                ProgressAction.DISMISS -> {
                    findViewById<ProgressBar>(R.id.progress_bar)?.gone()
                }
                ProgressAction.NONE -> {
                    findViewById<ProgressBar>(R.id.progress_bar)?.gone()
                }
                ProgressAction.PROGRESS_DIALOG->{}
            }
        })

        mViewModel.errorMessage.observe(this, {
            when (it.errorType) {
                ErrorType.DIALOG -> {
                    AlertDialog(
                        context = this,
                        message = it.message,
                        positive = getString(R.string.ok),
                        cancelOnOutsideClick = false,
                    ).show()
                }
                ErrorType.MESSAGE -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                ErrorType.TOAST -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                ErrorType.NONE -> {
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_SELECT_ADDRESS && resultCode == RESULT_OK) {
            data?.let {
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(EXTRA_ADDRESS, it.getStringExtra(EXTRA_ADDRESS))
                    putExtra(EXTRA_LATITUDE, it.getDoubleExtra(EXTRA_LATITUDE, 0.0))
                    putExtra(EXTRA_LONGITUDE, it.getDoubleExtra(EXTRA_LONGITUDE, 0.0))
                })
                finish()
            }
        }
    }
}