package com.aajeevika.buyer.utility

import android.content.SharedPreferences
import com.aajeevika.buyer.utility.app_enum.LanguageType

class AppPreferencesHelper(private val mPrefs: SharedPreferences) {

    fun clear() {
        val lat  = latitude
        val long  = longitude
        val selectedLanguage = appLanguage
        mPrefs.edit().clear().apply()
        latitude = lat
        longitude = long
        appLanguage = selectedLanguage
    }

    var uid: String
        get() = mPrefs.getString("uid", "") ?: ""
        set(value) = mPrefs.edit().putString("uid", value).apply()

    var roleId: Int
        get() = mPrefs.getInt("roleId", -1)
        set(value) = mPrefs.edit().putInt("roleId", value).apply()

    var name: String
        get() = mPrefs.getString("name", "") ?: ""
        set(value) = mPrefs.edit().putString("name", value).apply()

    var title: String
        get() = mPrefs.getString("title", "") ?: ""
        set(value) = mPrefs.edit().putString("title", value).apply()

    var email: String
        get() = mPrefs.getString("email", "") ?: ""
        set(value) = mPrefs.edit().putString("email", value).apply()

    var mobile: String
        get() = mPrefs.getString("mobile", "") ?: ""
        set(value) = mPrefs.edit().putString("mobile", value).apply()

    var profileImage: String
        get() = mPrefs.getString("profileImage", "") ?: ""
        set(value) = mPrefs.edit().putString("profileImage", value).apply()

    var authToken: String
        get() = mPrefs.getString("authToken", "").toString()
        set(value) = mPrefs.edit().putString("authToken", value).apply()

    var fcmToken: String
        get() = mPrefs.getString("fcmToken", "").toString()
        set(value) = mPrefs.edit().putString("fcmToken", value).apply()

    var languageSelected: Boolean
        get() = mPrefs.getBoolean("languageSelected", false)
        set(value) = mPrefs.edit().putBoolean("languageSelected", value).apply()

    var appLanguage: String
        get() = mPrefs.getString("appLanguage", LanguageType.ENGLISH.code).toString()
        set(value) = mPrefs.edit().putString("appLanguage", value).apply()

    var location: String
        get() = mPrefs.getString("location", "Location").toString()
        set(value) = mPrefs.edit().putString("location", value).apply()

    var address: String
        get() = mPrefs.getString("address", "Add your address").toString()
        set(value) = mPrefs.edit().putString("address", value).apply()

    var latitude: Double
        get() = (mPrefs.getString("LATITUDE", "0.0")?: "0.0").toDouble()
        set(value) = mPrefs.edit().putString("LATITUDE", value.toString()).apply()

    var longitude: Double
        get() = (mPrefs.getString("LONGITUDE", "0.0") ?: "0.0").toDouble()
        set(value) = mPrefs.edit().putString("LONGITUDE", value.toString()).apply()

    var addressData: String
        get() = mPrefs.getString("addressData", "{}").toString()
        set(value) = mPrefs.edit().putString("addressData", value).apply()

}