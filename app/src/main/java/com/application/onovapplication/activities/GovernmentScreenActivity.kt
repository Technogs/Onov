package com.application.onovapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.api.ApiClient
import com.application.onovapplication.api.ApiInterface
import com.application.onovapplication.fragments.CongressFragment
import com.application.onovapplication.fragments.ConstitutionFragment
import com.application.onovapplication.fragments.ExecutiveFragment
import com.application.onovapplication.fragments.JudicialFragment
import com.application.onovapplication.model.SenateResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class GovernmentScreenActivity : BaseAppCompatActivity(), View.OnClickListener {
    lateinit var tvExecutive: TextView
    lateinit var tvSenate: TextView
    lateinit var tvLegislation: TextView
    lateinit var tvCongress: TextView
    lateinit var tvJudicial: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_government_screen)

        initViews()
        replaceFragment(CongressFragment("Congress"))
        tvCongress.setBackgroundResource(R.drawable.background_gradient_square)
        tvCongress.setTextColor(ContextCompat.getColor(this, R.color.white))

    }

    fun initViews() {
        tvExecutive = findViewById(R.id.tvExecutive)
        tvSenate = findViewById(R.id.tvSenate)
        tvLegislation = findViewById(R.id.tvLegislation)
        tvCongress = findViewById(R.id.tvCongress)
        tvJudicial = findViewById(R.id.tvJudicial)

        //setting click listeners
        tvExecutive.setOnClickListener(this)
        tvSenate.setOnClickListener(this)
        tvLegislation.setOnClickListener(this)
        tvCongress.setOnClickListener(this)
        tvJudicial.setOnClickListener(this)
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.my_container, fragment)
        transaction.commit()
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tvExecutive -> {
                tvExecutive.setBackgroundResource(R.drawable.background_gradient_square)
                tvExecutive.setTextColor(ContextCompat.getColor(this, R.color.white))
                tvSenate.setBackgroundResource(R.color.white)
                tvSenate.setTextColor(ContextCompat.getColor(this, R.color.grey))
                tvLegislation.setBackgroundResource(R.color.white)
                tvLegislation.setTextColor(ContextCompat.getColor(this, R.color.grey))
                tvCongress.setBackgroundResource(R.color.white)
                tvCongress.setTextColor(ContextCompat.getColor(this, R.color.grey))
                tvJudicial.setBackgroundResource(R.color.white)
                tvJudicial.setTextColor(ContextCompat.getColor(this, R.color.grey))
                replaceFragment(ExecutiveFragment())
            }
            R.id.tvSenate -> {
                tvExecutive.setBackgroundResource(R.color.white)
                tvExecutive.setTextColor(ContextCompat.getColor(this, R.color.grey))
                tvSenate.setBackgroundResource(R.drawable.background_gradient_square)
                tvSenate.setTextColor(ContextCompat.getColor(this, R.color.white))
                tvLegislation.setBackgroundResource(R.color.white)
                tvLegislation.setTextColor(ContextCompat.getColor(this, R.color.grey))
                tvCongress.setBackgroundResource(R.color.white)
                tvCongress.setTextColor(ContextCompat.getColor(this, R.color.grey))
                tvJudicial.setBackgroundResource(R.color.white)
                tvJudicial.setTextColor(ContextCompat.getColor(this, R.color.grey))
                replaceFragment(CongressFragment("Senate"))
            }
            R.id.tvLegislation -> {
                tvExecutive.setBackgroundResource(R.color.white)
                tvExecutive.setTextColor(ContextCompat.getColor(this, R.color.grey))
                tvSenate.setBackgroundResource(R.color.white)
                tvSenate.setTextColor(ContextCompat.getColor(this, R.color.grey))
                tvLegislation.setBackgroundResource(R.drawable.background_gradient_square)
                tvLegislation.setTextColor(ContextCompat.getColor(this, R.color.white))
                tvCongress.setBackgroundResource(R.color.white)
                tvCongress.setTextColor(ContextCompat.getColor(this, R.color.grey))
                tvJudicial.setBackgroundResource(R.color.white)
                tvJudicial.setTextColor(ContextCompat.getColor(this, R.color.grey))

                replaceFragment(ConstitutionFragment())
//                replaceFragment(CongressFragment("legislation"))
            }
            R.id.tvCongress -> {
                tvExecutive.setBackgroundResource(R.color.white)
                tvExecutive.setTextColor(ContextCompat.getColor(this, R.color.grey))
                tvSenate.setBackgroundResource(R.color.white)
                tvSenate.setTextColor(ContextCompat.getColor(this, R.color.grey))
                tvLegislation.setBackgroundResource(R.color.white)
                tvLegislation.setTextColor(ContextCompat.getColor(this, R.color.grey))
                tvCongress.setBackgroundResource(R.drawable.background_gradient_square)
                tvCongress.setTextColor(ContextCompat.getColor(this, R.color.white))
                tvJudicial.setBackgroundResource(R.color.white)
                tvJudicial.setTextColor(ContextCompat.getColor(this, R.color.grey))
                replaceFragment(CongressFragment("Congress"))
            }
            R.id.tvJudicial -> {
                tvExecutive.setBackgroundResource(R.color.white)
                tvExecutive.setTextColor(ContextCompat.getColor(this, R.color.grey))
                tvSenate.setBackgroundResource(R.color.white)
                tvSenate.setTextColor(ContextCompat.getColor(this, R.color.grey))
                tvLegislation.setBackgroundResource(R.color.white)
                tvLegislation.setTextColor(ContextCompat.getColor(this, R.color.grey))
                tvCongress.setBackgroundResource(R.color.white)
                tvCongress.setTextColor(ContextCompat.getColor(this, R.color.grey))
                tvJudicial.setBackgroundResource(R.drawable.background_gradient_square)
                tvJudicial.setTextColor(ContextCompat.getColor(this, R.color.white))
                replaceFragment(JudicialFragment())
            }

        }
    }


}