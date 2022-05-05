package com.application.onovapplication.firebasedata

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.ChatActivity
import com.application.onovapplication.activities.common.SplashScreenActivity
import com.application.onovapplication.repository.BaseUrl
import com.application.onovapplication.utils.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = MyFirebaseMessagingService::class.java.simpleName
    private var pendingIntent: PendingIntent? = null
    var json: JSONObject? = null
    var message: String? = null
    var type: String? = null
    var title: String? = null
    var sender_userRef: String? = null
    var icon: String? = null
    var sender_user_id: String? = null
    var sender_user_name: String? = null

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("EfsasasasasasasasRROR", remoteMessage.data?.toString())
//        Log.e("Efsasasa",  remoteMessage.data?.body.toString())

        if (remoteMessage?.data != null) {
            try {
                val data: String =
                    if (remoteMessage.data?.toString()
                            .contains("=")
                    ) remoteMessage.data?.toString().toString()
                        .replace("=", ":") else
                        remoteMessage.data?.toString()
                            .toString()
                json = JSONObject(data)
                val json2: JSONObject = json!!.getJSONObject("body")

                title = json2.getString("title")
                type = json2.getString("type")
                message = json2.getString("message")
                sender_userRef = json2.getString("sender_userRef")
                icon = json2.getString("icon")
                sender_user_id = json2.getString("sender_user_id")
                sender_user_name = json2.getString("sender_user_name")

                sendNotification(title, message, type)

            } catch (e: Exception) {
                Log.e("ERROR", e.printStackTrace().toString())
            }

        }


    }

    private fun sendNotification(title: String?, body: String?, type: String?) {
        val pendingIntent: PendingIntent
        var intent: Intent? = null

        if (type.equals("chat")){
            intent = Intent(this, ChatActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra(Constants.USER_ID, sender_user_id.toString())
            intent.putExtra(Constants.USER_NAME, sender_user_name.toString())
            intent.putExtra(Constants.PHOTO,BaseUrl.photoUrl + icon.toString())
            intent.putExtra(Constants.FEED, "")
            intent.putExtra(Constants.EVENT, "null")
            intent.putExtra(Constants.FEEDTYPE, "msgType")
            intent.putExtra(Constants.USER_REF, sender_userRef.toString())
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        }else{
            intent = Intent(this, SplashScreenActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)


        }

        val random = Random()
        pendingIntent = PendingIntent.getActivity(this, random.nextInt(10), intent, 0)
        val channelId = getString(R.string.default_notification_channel_id)
        var notification: Uri? = null
        try {
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(applicationContext, notification)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val notificationBuilder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(getNotificationIcon())
                .setColor(resources.getColor(R.color.app_color))
                .setContentTitle(title)
                .setAutoCancel(true)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(body)
                )
                .setVibrate(longArrayOf(500, 1000))
                .setContentText(body)
                .setDefaults(Notification.DEFAULT_ALL)
//                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setWhen(System.currentTimeMillis())
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mChannel: NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
//                    getApplicationContext().getPackageName() + "/" + R.raw.notification);
            mChannel = NotificationChannel(
                channelId,
                "Onov",
                NotificationManager.IMPORTANCE_HIGH
            )
            mChannel.lightColor = Color.BLUE
            mChannel.enableLights(true)
            mChannel.setShowBadge(true)
            mChannel.vibrationPattern = longArrayOf(500, 1000)
            mChannel.enableVibration(true)

            // Allow lockscreen playback control
            mChannel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            //            mChannel.setSound(soundUri, audioAttributes);
            notificationManager?.createNotificationChannel(mChannel)
        }
        assert(notificationManager != null)
        val t = Random()
        val notificationId = t.nextInt(10)
        notificationManager.notify(1, notificationBuilder.build())

    }

    private fun getNotificationIcon(): Int {
        val useWhiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        return if (useWhiteIcon) R.mipmap.ic_launcher else R.mipmap.ic_launcher
    }


}


