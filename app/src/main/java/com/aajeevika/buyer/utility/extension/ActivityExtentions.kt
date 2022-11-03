package com.aajeevika.buyer.utility.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.aajeevika.buyer.R
import com.aajeevika.buyer.utility.Constant
import com.aajeevika.buyer.utility.permission.PermissionCallback
import com.aajeevika.buyer.utility.permission.Permissions
import com.aajeevika.buyer.view.auth.ActivityLogin
import com.aajeevika.buyer.view.dialog.AlertDialog
import java.util.ArrayList


fun Activity.goto(intent: Intent, rq: Int = -1) =
    if (rq == -1) startActivity(intent) else startActivityForResult(intent, rq)

fun Activity.gotoFinish(intent: Intent) {
    startActivity(intent)
    finish()
}

fun Activity.gotoNewTask(intent: Intent) {
    startActivity(intent.apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    })
    finish()
}

fun Activity.goto(cls: Class<*>, rq: Int = -1) =
    if (rq == -1) startActivity(Intent(this, cls)) else startActivityForResult(
        Intent(this, cls),
        rq
    )

fun Activity.gotoFinish(cls: Class<*>) {
    startActivity(Intent(this, cls))
    finish()
}

fun Activity.gotoNewTask(cls: Class<*>) {
    startActivity(Intent(this, cls).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    })
    finish()
}
fun showKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = activity.currentFocus
    if (view == null) {
        view = View(activity)
    }
    imm.showSoftInput(view,0)
}

fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = activity.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.getDisplayMatrix(): DisplayMetrics {
    val displayMatrix = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMatrix)
    return displayMatrix
}

fun Activity.checkUserPermissions(permissions: Array<String>, success: (isAllow: Boolean) -> Unit = {}) {
    val permissionHandler = object : PermissionCallback() {
        override fun onGranted() {
            success(true)
        }

        override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>) {
            super.onDenied(context, deniedPermissions)
            success(false)
        }

        override fun onJustBlocked(
            context: Context?,
            justBlockedList: ArrayList<String>,
            deniedPermissions: ArrayList<String>
        ) {
            super.onJustBlocked(context, justBlockedList, deniedPermissions)
            success(false)
        }
    }

    Permissions.check(
        this,
        permissions,
        null,
        null,
        permissionHandler
    )

}

fun Activity.showLoginPopup(goBack: Boolean = false) {
    AlertDialog(
        context = this,
        message = getString(R.string.please_login_to_continue),
        positive = getString(R.string.login),
        negative = getString(R.string.cancel),
        positiveClick = {
            val intent = Intent(this, ActivityLogin::class.java)
            intent.putExtra(Constant.GO_BACK, goBack)
            startActivity(intent)
        }
    ).show()
}

