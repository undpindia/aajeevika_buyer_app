package com.aajeevika.buyer.utility

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.aajeevika.buyer.R
import com.aajeevika.buyer.view.application.ActivitySplashScreen
import com.aajeevika.buyer.view.grievance.ActivityGrievance
import com.aajeevika.buyer.view.grievance.ActivityGrievanceChat
import com.aajeevika.buyer.view.home.ActivityDashboard
import com.aajeevika.buyer.view.interest.ActivityInterestDetails
import com.aajeevika.buyer.view.order.ActivityMyOrder
import com.aajeevika.buyer.view.order.ActivityOrderDetailsCompleted
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson

class FireBaseMessage : FirebaseMessagingService() {
    private var mNotificationManager: NotificationManager? = null
    private var builder: NotificationCompat.Builder? = null
    private val channelId by lazy { packageName }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        addNotification(remoteMessage)
    }

    /**
     * Read and format data received from the notification payload.
     */
    private fun addNotification(remoteMessage: RemoteMessage?) {
        println("Notification Data: ${remoteMessage?.data}")

        val data = Gson().fromJson(Gson().toJson(remoteMessage?.data), NotificationData::class.java)
        if (data.type == "custom" && !TextUtils.isEmpty(data.image)) {
            Glide.with(applicationContext)
                .asBitmap()
                .load(data.image)
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        displayNotification(null, data)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        displayNotification(resource, data)
                        return false
                    }
                }).submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
        } else {
            displayNotification(null, data)
        }
    }

    /**
     * Build and display the notification. In case [bitmap] is null displays simple text notification else display large
     * image notification.
     */
    private fun displayNotification(bitmap: Bitmap?, data: NotificationData) {
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val id = generateRandom()


        val contentIntent = when (data.type) {
            "seller_order" -> {
                TaskStackBuilder.create(applicationContext).run {
                    val dashboardIntent = Intent(applicationContext, ActivityDashboard::class.java)
                    val interestDetailsIntent = Intent(applicationContext, ActivityOrderDetailsCompleted::class.java)
                    interestDetailsIntent.putExtra(Constant.ORDER_ID, data.order_id ?: -1)

                    addParentStack(ActivityDashboard::class.java)
                    addNextIntent(dashboardIntent)
                    addNextIntent(interestDetailsIntent)
                }.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
            "individual_order" -> {
                TaskStackBuilder.create(applicationContext).run {
                    val dashboardIntent = Intent(applicationContext, ActivityDashboard::class.java)
                    val interestDetailsIntent = Intent(applicationContext, ActivityOrderDetailsCompleted::class.java)
                    interestDetailsIntent.putExtra(Constant.ORDER_ID, data.order_id ?: -1)

                    addParentStack(ActivityDashboard::class.java)
                    addNextIntent(dashboardIntent)
                    addNextIntent(interestDetailsIntent)
                }.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
            "buyer_interest" -> {
                TaskStackBuilder.create(applicationContext).run {
                    val dashboardIntent = Intent(applicationContext, ActivityDashboard::class.java)
                    val interestDetailsIntent = Intent(applicationContext, ActivityInterestDetails::class.java)
                    interestDetailsIntent.putExtra(Constant.PRODUCT_ID, data.interest_id ?: -1)

                    addParentStack(ActivityDashboard::class.java)
                    addNextIntent(dashboardIntent)
                    addNextIntent(interestDetailsIntent)
                }.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
            "admin_grievance" -> {
                TaskStackBuilder.create(applicationContext).run {
                    val dashboardIntent = Intent(applicationContext, ActivityDashboard::class.java)
                    val grievanceIntent = Intent(applicationContext, ActivityGrievance::class.java)
                    val grievanceChatIntent = Intent(applicationContext, ActivityGrievanceChat::class.java)
                    grievanceChatIntent.putExtra(Constant.TICKET_ID_DISPLAY, data.ticket_id_d)
                    grievanceChatIntent.putExtra(Constant.TICKET_ID, data.id?.toInt())

                    addParentStack(ActivityDashboard::class.java)
                    addNextIntent(dashboardIntent)
                    addNextIntent(grievanceIntent)
                    addNextIntent(grievanceChatIntent)
                }.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
            else -> {
                val intent = Intent(applicationContext, ActivitySplashScreen::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

                PendingIntent.getActivity(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }

        builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setNotificationCompatChanel()
            NotificationCompat.Builder(this, channelId)
        } else {
            NotificationCompat.Builder(this)
        }

        val style = if (bitmap != null) NotificationCompat.BigPictureStyle().bigPicture(bitmap)
        else NotificationCompat.BigTextStyle().bigText(data.message)

        bitmap?.let { builder?.setLargeIcon(it) }
        builder?.setSmallIcon(R.drawable.aajeevika_logo)
        builder?.setLargeIcon(bitmap ?: BitmapFactory.decodeResource(resources, R.drawable.aajeevika_logo))
        builder?.color = ContextCompat.getColor(this, R.color.gray_600)
        builder?.setContentTitle(data.title)
        builder?.setContentText(data.message)
        builder?.setStyle(style)
        builder?.setContentIntent(contentIntent)
        builder?.setAutoCancel(true)
        mNotificationManager?.notify(id, builder?.build())
    }

    private fun generateRandom(): Int {
        return ((Math.random() * 100).toInt())
    }

    /**
     * Build a new notification channel with [NotificationManager.IMPORTANCE_DEFAULT].
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    internal fun setNotificationCompatChanel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val notificationChannel = NotificationChannel(channelId, applicationContext.getString(R.string.app_name), importance)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        mNotificationManager?.createNotificationChannel(notificationChannel)
    }

    data class NotificationData(
        var id: String? = "",
        var image: String? = "",
        var title: String? = "",
        var type: String? = "",
        var message: String? = "",
        var ticket_id_d: String? = "",
        var order_id: Int?,
        var interest_id: Int?,
    )
}


