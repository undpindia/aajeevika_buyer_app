package com.aajeevika.buyer.location.koin

import com.aajeevika.buyer.location.viewModel.LocationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val locationViewModelModule = module {
    viewModel { LocationViewModel() }
}