package com.application.onovapplication.activities.common

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.adapters.NotificationsAdapter
import com.application.onovapplication.databinding.ActivityLoginBinding
import com.application.onovapplication.databinding.ActivityNotificationsBinding
import com.application.onovapplication.model.NotificationList
import com.application.onovapplication.viewModels.NotificationsViewModel

class NotificationsActivity : BaseAppCompatActivity() {
    private var notificationsAdapter: NotificationsAdapter? = null

    private val notificationsViewModel by lazy {
        ViewModelProvider(this).get(NotificationsViewModel::class.java)
    }

    private val notificationsList: ArrayList<NotificationList> = ArrayList()
    private lateinit var binding: ActivityNotificationsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        observeViewModel()

        notificationsViewModel.getNotifications(this, userPreferences.getUserREf())
        showDialog()

        notificationsAdapter = NotificationsAdapter(this, notificationsList)
     binding.rvNotifications.adapter = notificationsAdapter
    }


    private fun observeViewModel() {

        notificationsViewModel.successful.observe(this, Observer {
            dismissDialog()

            if (it) {
                if (notificationsViewModel.status == "success") {

                    notificationsList.addAll(notificationsViewModel.notificationsResponse.notificationList!!)

                    if (notificationsList.isEmpty()) {
                        binding.noNotificationData.visibility = View.VISIBLE
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