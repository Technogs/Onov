package com.application.onovapplication.activities.common

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.adapters.NotificationsAdapter
import com.application.onovapplication.model.NotificationList
import com.application.onovapplication.viewModels.NotificationsViewModel
import kotlinx.android.synthetic.main.activity_notifications.*

class NotificationsActivity : BaseAppCompatActivity() {
    private var notificationsAdapter: NotificationsAdapter? = null

    private val notificationsViewModel by lazy {
        ViewModelProvider(this).get(NotificationsViewModel::class.java)
    }

    private val notificationsList: ArrayList<NotificationList> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        observeViewModel()

        notificationsViewModel.getNotifications(this, userPreferences.getUserREf())
        showDialog()

        notificationsAdapter = NotificationsAdapter(this, notificationsList)
        rv_notifications.adapter = notificationsAdapter
    }


    private fun observeViewModel() {

        notificationsViewModel.successful.observe(this, Observer {
            dismissDialog()

            if (it) {
                if (notificationsViewModel.status == "success") {

                    notificationsList.addAll(notificationsViewModel.notificationsResponse.notificationList!!)

                    if (notificationsList.isEmpty()) {
                        noNotificationData.visibility = View.VISIBLE
                    } else {
                        notificationsAdapter?.notifyDataSetChanged()
                    }
                } else {
                    setError(notificationsViewModel.message)
                }
            } else {
                setError(notificationsViewModel.message)
            }
        })
    }
}