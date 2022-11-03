package com.aajeevika.buyer.utility

import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import com.aajeevika.buyer.BuildConfig
import com.aajeevika.buyer.R
import com.aajeevika.buyer.utility.extension.hideKeyBoard
import com.bumptech.glide.Glide
import java.lang.StringBuilder
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.DataFormatException

@BindingAdapter("app:setRegisterAgreementText")
fun TextView.setRegisterAgreementText(value: Boolean?) {
    if (value == true) {
        val spannableString = SpannableString(context.getString(R.string.register_agreement))
        val termsAndConditionsText = context.getString(R.string.terms_and_conditions)
        val privacyPolicyText = context.getString(R.string.privacy_policy)

        spannableString.setSpan(
            object : ClickableSpan() {
                override fun onClick(p0: View) {

                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = ContextCompat.getColor(this@setRegisterAgreementText.context, R.color.orange)
                }
            },
            spannableString.indexOf(termsAndConditionsText),
            spannableString.indexOf(termsAndConditionsText) + termsAndConditionsText.length,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE,
        )

        spannableString.setSpan(
            object : ClickableSpan() {
                override fun onClick(p0: View) {

                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = ContextCompat.getColor(this@setRegisterAgreementText.context, R.color.orange)
                }
            },
            spannableString.indexOf(privacyPolicyText),
            spannableString.indexOf(privacyPolicyText) + privacyPolicyText.length,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE,
        )

        text = spannableString
        movementMethod = LinkMovementMethod.getInstance()
    }
}

@BindingAdapter("app:disableSpace")
fun EditText.disableSpace(value: Boolean?) {
    if (value == true) {
        this.doOnTextChanged { text, _, _, _ ->
            if(text?.contains(" ") == true)
                this.setText(text.toString().replace(" ", ""))
        }
    }
}


/**
 * Load image from network using [Glide].
 * In case if the [image] do not contains an prefix the attach default server prefix i.e. [BuildConfig.BASE_API_URL].
 */
@BindingAdapter("app:loadImage")
fun ImageView.loadImage(image: String?) {
    image?.let {
        val url = if(Patterns.WEB_URL.matcher(it).matches()) it else {
           BaseUrls.baseUrl+ it}

        Glide.with(this.context).load(url)
            .placeholder(R.color.gray_50)
            .error(R.color.gray_50)
            .into(this)
    }?: kotlin.run {
        setImageResource(R.color.gray_50)
    }
}


@BindingAdapter("app:loadProfileImage")
fun ImageView.loadProfileImage(image: String?) {
    image?.let {
        val url = if(Patterns.WEB_URL.matcher(it).matches()) it else {
            BaseUrls.baseUrl+ it}

        Glide.with(this.context).load(url)
            .placeholder(R.drawable.default_user)
            .error(R.drawable.default_user)
            .into(this)
    }?: kotlin.run {
        setImageResource(R.drawable.default_user)
    }
}

@BindingAdapter(value = ["app:loadImageFromNetwork", "app:placeholder"], requireAll = false)
fun ImageView.loadImageFromNetwork(image: String?, placeholder: Drawable?) {
    if(!image.isNullOrEmpty()) {
        val requestBuilder = Glide.with(this).load(image)
        placeholder?.run { requestBuilder.placeholder(this) }
        requestBuilder.into(this)
    } else {
        setImageDrawable(placeholder)
    }
}

@BindingAdapter("setDate")
fun TextView.setDate(dateStr:String?){
    dateStr?.let {
        text = if(dateStr != "") {
            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
                val date = sdf.parse(dateStr)
                getDate(date.time)
            } catch (e: DataFormatException) {
                ""
            }
        }else{
            ""
        }
    }?: kotlin.run { text = "" }

}


fun getDate(time: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val df = SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.getDefault())
    return df.format(calendar.timeInMillis)
}

/**
 * Build an autocomplete dropdown and set to [AutoCompleteTextView].
 */
@BindingAdapter("app:dropDownMenu")
fun AutoCompleteTextView.dropDownMenu(menu: ArrayList<String>?) {
    menu?.let {
        val adapter = ArrayAdapter(this.context, android.R.layout.simple_list_item_1, it)
        this.setAdapter(adapter)
        this.threshold = 1
    }
    this.setOnClickListener {
        this.context.hideKeyBoard(this.rootView)
        this.showDropDown()
    }
}


@BindingAdapter("setRatting")
fun RatingBar.setRatting(rating: Double) {
    val point = DecimalFormat("##.##").format(rating).toDouble()
    val format = DecimalFormat("0.##")
    setRating(format.format(point).toFloat())
}


@BindingAdapter("setRatting","prefix",requireAll = false)
fun TextView.setRatting(rating: Double,prefix:String?="") {
    val point = DecimalFormat("##.##").format(rating).toDouble()
    val format = DecimalFormat("0.##")
    text = format.format(point)+prefix
}

@BindingAdapter("app:camelCaseText")
fun TextView.camelCaseText(value: String?) {
    value?.let {
        val finalValue = StringBuilder()

        it.trim().split(" ").forEach { splitText ->
            if (splitText.isNotEmpty()) finalValue.append(splitText[0].uppercaseChar() + splitText.substring(1)).append(" ")
        }

        text = finalValue.trim().toString()
    }
}

fun Double?.parseRatting():Double {
    val point = DecimalFormat("##.##").format(this?:0.0).toDouble()
    val format = DecimalFormat("0.##")
   return (format.format(point).toDouble())
}
