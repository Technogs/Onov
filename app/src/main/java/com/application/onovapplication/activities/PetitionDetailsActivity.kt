package com.application.onovapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import kotlinx.android.synthetic.main.activity_petition_details.*

class PetitionDetailsActivity : BaseAppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_petition_details)
    }

    override fun onClick(p0: View?) {
        when(p0?.id)
        {
            R.id.ivClear->{
                signature_view.clearCanvas()
            }
        }
    }
}