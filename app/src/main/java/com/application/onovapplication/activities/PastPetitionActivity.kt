package com.application.onovapplication.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.AskToAddActivity
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.adapters.PetitionsAdapter
import com.application.onovapplication.databinding.ActivityPastPetitionBinding
import com.application.onovapplication.viewModels.PetitionViewModel

class PastPetitionActivity : BaseAppCompatActivity(),View.OnClickListener {

    private val petitionViewModel by lazy { ViewModelProvider(this).get(PetitionViewModel::class.java)}
    private lateinit var binding: ActivityPastPetitionBinding
    var petitionsAdapter: PetitionsAdapter?  = null
var past:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPastPetitionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        petitionViewModel.viewPetition(this,"0")
        showDialog()
        observeViewModel()
    }

    private fun observeViewModel() {

        petitionViewModel.successfulViewPetition.observe(this, Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (petitionViewModel.status == "success") {
                      if (petitionViewModel.viewPetitionResponse.petitionArray.isNullOrEmpty())
                      binding.noEventsLayout.visibility=View.VISIBLE
                      else{
                          binding.noEventsLayout.visibility=View.GONE
                          petitionsAdapter = PetitionsAdapter(
                                this,
                                petitionViewModel.viewPetitionResponse.petitionArray,
                                past
                            )
                            binding.rvPetition.adapter = petitionsAdapter
                        }

                    } else {
                        setError(petitionViewModel.message)
                    }
                }
            } else {
                setError(petitionViewModel.message)
            }

        })}

    override fun onClick(v: View?) {
when(v?.id){
    R.id.allPetitions->{
        past=""
        binding.allPetitions.setBackgroundColor(resources.getColor(R.color.app_color))
        binding.allPetitions.setTextColor(resources.getColor(R.color.white))
        binding.pastPetitions.setTextColor(resources.getColor(R.color.app_color))
        binding.pastPetitions.setBackgroundColor(resources.getColor(R.color.white))
        petitionViewModel.viewPetition(this,"0")
        showDialog()

    }  R.id.pastPetitions->{
    past="past"
    binding.pastPetitions.setBackgroundColor(resources.getColor(R.color.app_color))
    binding.pastPetitions.setTextColor(resources.getColor(R.color.white))
    binding.allPetitions.setTextColor(resources.getColor(R.color.app_color))
    binding.allPetitions.setBackgroundColor(resources.getColor(R.color.white))
    petitionViewModel.viewPetition(this,"1")
showDialog()
} R.id.floatBtn->{
    val intent = Intent(this, AskToAddActivity::class.java)
    intent.putExtra("activity", "petition")
    startActivity(intent)
}
}
    }
}