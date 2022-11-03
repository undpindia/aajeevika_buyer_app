package com.aajeevika.buyer.koin

import com.aajeevika.buyer.view.application.viewmodel.FaqViewModel
import com.aajeevika.buyer.view.application.viewmodel.NotificationsViewModel
import com.aajeevika.buyer.view.application.viewmodel.RatingAndReviewsViewModel
import com.aajeevika.buyer.view.application.viewmodel.SplashScreenViewModel
import com.aajeevika.buyer.view.auth.viewmodel.*
import com.aajeevika.buyer.view.grievance.viewmodel.CreateGrievanceViewModel
import com.aajeevika.buyer.view.grievance.viewmodel.GrievanceChatViewModel
import com.aajeevika.buyer.view.grievance.viewmodel.GrievanceViewModel
import com.aajeevika.buyer.view.home.viewmodel.DashboardViewModel
import com.aajeevika.buyer.view.home.viewmodel.SearchViewModel
import com.aajeevika.buyer.view.interest.viewmodel.AddInterestProductsViewModel
import com.aajeevika.buyer.view.interest.viewmodel.InterestsViewModel
import com.aajeevika.buyer.view.interest.viewmodel.MyInterestsViewModel
import com.aajeevika.buyer.view.map.viewmodel.MapViewModel
import com.aajeevika.buyer.view.order.viewmodel.MyOrderViewModel
import com.aajeevika.buyer.view.order.viewmodel.OrderDetailsViewModel
import com.aajeevika.buyer.view.product.viewmodel.CategoryViewModel
import com.aajeevika.buyer.view.product.viewmodel.ProductDetailViewModel
import com.aajeevika.buyer.view.product.viewmodel.SubCategoryProductsViewModel
import com.aajeevika.buyer.view.profile.viewmodel.AddressViewModel
import com.aajeevika.buyer.view.profile.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myViewModel = module {
    viewModel { SplashScreenViewModel() }
    viewModel { RegisterViewModel() }
    viewModel { OtpVerificationViewModel() }
    viewModel { LoginViewModel() }
    viewModel { ForgotPasswordViewModel() }
    viewModel { ResetPasswordViewModel() }
    viewModel { DashboardViewModel() }
    viewModel { NotificationsViewModel() }
    viewModel { FaqViewModel() }
    viewModel { RatingAndReviewsViewModel() }
    viewModel { MyInterestsViewModel() }
    viewModel { InterestsViewModel() }
    viewModel { SubCategoryProductsViewModel() }
    viewModel { ProductDetailViewModel() }
    viewModel { ProfileViewModel() }
    viewModel { MyOrderViewModel() }
    viewModel { OrderDetailsViewModel() }
    viewModel { CategoryViewModel() }
    viewModel { AddInterestProductsViewModel() }
    viewModel { MapViewModel() }
    viewModel { AddressViewModel() }
    viewModel { SearchViewModel() }
    viewModel { CreateGrievanceViewModel() }
    viewModel { GrievanceChatViewModel() }
    viewModel { GrievanceViewModel() }
}