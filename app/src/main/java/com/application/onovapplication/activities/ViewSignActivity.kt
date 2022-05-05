package com.application.onovapplication.activities

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.adapters.PetitionSignAdapter
import com.application.onovapplication.databinding.ActivityViewSignBinding
import com.application.onovapplication.model.Sign
import com.application.onovapplication.viewModels.PetitionViewModel

class ViewSignActivity : BaseAppCompatActivity() {
    private lateinit var binding: ActivityViewSignBinding
    var petitionSignAdapter: PetitionSignAdapter?  = null
    lateinit var signList:List<Sign>
     var pId:String=""
    private val petitionViewModel by lazy { ViewModelProvider(this).get(PetitionViewModel::class.java)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewSignBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        pId=intent.getStringExtra("pId").toString()

        petitionViewModel.getSignPetition(this,pId)
        showDialog()
        observeViewModel()
    }

    private fun observeViewModel() {

        petitionViewModel.successfulSignPetition.observe(this, Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (petitionViewModel.status == "success") {
                        signList=petitionViewModel.petitionSignResponse.signList.SignList
                        binding.signCount.text="Signature Count: "+signList.size
                        if (signList.isNullOrEmpty())binding.noData.visibility=View.VISIBLE
                        else {binding.noData.visibility=View.GONE
                            petitionSignAdapter = PetitionSignAdapter(this, signList)
                            binding.rvSign.adapter = petitionSignAdapter
                        }

                    } else {
                        setError(petitionViewModel.message)

                    }
                }
            } else {
                setError(petitionViewModel.message)
            }

        })}

}