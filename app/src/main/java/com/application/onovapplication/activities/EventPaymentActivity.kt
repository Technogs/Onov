package com.application.onovapplication.activities

import android.os.Bundle
import android.view.View
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.adapters.EventAttendeesAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.view_people_going.*

class EventPaymentActivity : BaseAppCompatActivity(), View.OnClickListener {

    private val mBottomSheetDialog by lazy {
        BottomSheetDialog(this, R.style.SheetDialog)
    }

    private var followersAdapter: EventAttendeesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_payment)

        //     lav_main.repeatCount = LottieDrawable.RESTART


    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.peopleGoingBtn -> {
                showBottomSheet()
            }


        }
    }

    private fun showBottomSheet() {

        val sheetView: View =
            layoutInflater.inflate(R.layout.view_people_going, null)
        mBottomSheetDialog.setContentView(sheetView)
        mBottomSheetDialog.show()

        mBottomSheetDialog.rv_people_going.let {
            followersAdapter =
                EventAttendeesAdapter(
                    this
                )

            it.adapter = followersAdapter
        }

        mBottomSheetDialog.ivCancel.setOnClickListener {
            mBottomSheetDialog.dismiss()
        }
    }
}