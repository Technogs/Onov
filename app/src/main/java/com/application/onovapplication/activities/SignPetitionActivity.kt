package com.application.onovapplication.activities

import android.os.Bundle
import android.view.View
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.adapters.PetitionsAdapter
import kotlinx.android.synthetic.main.activity_sign_petition.*

class SignPetitionActivity : BaseAppCompatActivity(), View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_petition)


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
