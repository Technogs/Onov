package com.application.onovapplication.activities.common

import android.os.Bundle
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity

class ChatActivity : BaseAppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
    }
}