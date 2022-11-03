package com.aajeevika.buyer.model.data_model

data class OtpModel(
    var otp: Int? = null,
)

data class UserProfileModel(
    var user: UserData? = null,
    var address: Address? = null,
    var is_otp_verified: Int? = null,
    var mobile: String? = null,
    var otp: Int? = null,
    var rating: RattingData? = RattingData(),
    )



data class ArtisanProfileModel(
    var user: UserData? = null,
    var rating: RattingData? = RattingData(),
    val favoriteStatus: Int,
    var address: AddressData? = null,
)

data class UserData(
    var id: Int? = null,
    var name: String? = null,
    var organization_name: String? = null,
    var title: String? = null,
    var email: String? = null,
    var mobile: String? = null,
    var profileImage: String? = null,
    var role_id: Int? = null,
    var api_token: String? = null,
    var isActive: Int? = null,
    var is_address_added: Int? = null,
    var is_email_verified: Int? = null,
    var is_promotional_mail: String? = null,
    var lat: Double=0.0,
    var log: Double=0.0,
)

data class Address(
    var personal: AddressData? = null,
    var registered: AddressData? = null,
)

data class AddressData(
    var id: Int? = null,
    var address_line_one: String? = null,
    var address_line_two: String? = null,
    var pincode: String? = null,
    var countryId: Int? = null,
    var stateId: Int? = null,
    var districtId: Int? = null,
    var country: String? = null,
    var state: String? = null,
    var district: String? = null,
)