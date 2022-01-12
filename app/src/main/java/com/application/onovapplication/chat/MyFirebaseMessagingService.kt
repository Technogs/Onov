package com.application.onovapplication.chat

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
//https://www.raywenderlich.com/9227276-firebase-cloud-messaging-for-android-sending-push-notifications
//https://wajahatkarim.com/2019/11/add-push-notifications-to-your-android-chat-app-using-kotlin/
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.application.onovapplication.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService()  {




    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        handleMessage(remoteMessage)
    }

    private fun handleMessage(remoteMessage: RemoteMessage) {
        //1
        val handler = Handler(Looper.getMainLooper())

        //2
        handler.post(Runnable {
            Toast.makeText(baseContext, getString(R.string.new_msg),
                Toast.LENGTH_LONG).show()

//            when(remoteMessage)
//            {
//                is TextMessage -> {
//                    // Convert BaseMessage to TextMessage
//                    val textMessage = remoteMessage
//
//                    // Send notification for this text message here
//                    val notificationId = 124952
//                    Notify.with(this@MyFirebaseMessagingService)
//                        .content { // this: Payload.Content.Default
//                            title = textMessage.senderId
//                            text = textMessage.
//                        }
//                        .alerting("high_priority_notification") {
//                            channelImportance = Notify.IMPORTANCE_HIGH
//                        }
//                        .show(notificationId)
//
//                }
//            }




//            remoteMessage.notification?.let {
//                val intent = Intent("MyData")
//                intent.putExtra("message", it.body);
//                broadcaster?.sendBroadcast(intent);
//            }

        }
        )
    }


//    private var broadcaster: LocalBroadcastManager? = null
//
//    override fun onCreate() {
//        broadcaster = LocalBroadcastManager.getInstance(this)
//    }





}