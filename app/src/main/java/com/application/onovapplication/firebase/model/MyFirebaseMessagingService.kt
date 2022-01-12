package com.application.onovapplication.firebase.model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.utils.Constants
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage



class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = MyFirebaseMessagingService::class.java.simpleName
    private var pendingIntent: PendingIntent? = null

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e(TAG, "onMessageReceived body ${remoteMessage.data["body"]}")





        Log.e(TAG, "onMessageReceived title ${remoteMessage.notification?.title}")


        Log.e(TAG, "onMessageReceived body ${remoteMessage.notification?.body}")

        if (remoteMessage.notification != null) {

                displayNotification(applicationContext, remoteMessage.notification!!.title.toString(), remoteMessage.notification!!.body.toString())

            }


        }




    fun displayNotification(context: Context, title: String, body: String) {

        val intent = Intent(context, BaseAppCompatActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            100,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        val mBuilder = NotificationCompat.Builder(context, Constants.CHANNE_ID)
            .setSmallIcon(R.drawable.about_app)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val mNotificationMgr = NotificationManagerCompat.from(context)
        mNotificationMgr.notify(1, mBuilder.build())

    }





    override fun onNewToken(token: String) {
        super.onNewToken(token)
        updateToken(token)
    }


    private fun updateToken(refreshToken: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Tokens")
        val token = Token(refreshToken)
        databaseReference.child((applicationContext as BaseAppCompatActivity).userPreferences.getuserDetails()?.id.toString())
                .setValue(token)

        var intent = Intent(getString(R.string.action_device_token))
        intent.putExtra(Constants.DEVICE_TOKEN, refreshToken)
        sendBroadcast(intent)
    }





}


