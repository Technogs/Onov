package com.application.onovapplication.activities.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Toast
import com.application.onovapplication.R
import com.application.onovapplication.databinding.ActivityAboutInfoBinding
import com.application.onovapplication.databinding.ActivityDebateRequestsBinding
import com.application.onovapplication.model.UserInfo
import com.application.onovapplication.utils.CustomSpinnerAdapter



class AboutInfoActivity : BaseAppCompatActivity() ,View.OnClickListener{
    private lateinit var binding: ActivityAboutInfoBinding

    private val rolesList =
        arrayOf("Select Support", "Republican", "Democrat", "Independent")
    var role:String=""

   lateinit var userInfo: UserInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        userInfo = intent.getParcelableExtra("userinfo")!!
Log.d("userinfo",""+userInfo)
        // etText.setText("An “About Me,” also known as a blurb, is a short piece of writing that informs your reader about your professional background, key accomplishments, personal values and any brands you may be associated with.")
        setSpinner()

        setData()

    }

    private fun setData() {
        binding.editInfo.setOnClickListener(this)
   binding.updateInfo.setOnClickListener(this)
        binding.etText.setText(userInfo.about)
        binding.websiteLink.setText(userInfo?.webUrl)


        for (i in rolesList.indices) {
            if (rolesList[i] == userInfo?.supporter) {
                binding.spAbout.setSelection(i)
            }
        }

        binding.roleValue.text = userInfo?.role
    }

    private fun setSpinner() {
        val spinnerAdapter = CustomSpinnerAdapter(
            this,  // Use our custom adapter
            R.layout.spinner_text, rolesList
        )


        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown)
        binding.spAbout.adapter = spinnerAdapter


        binding.spAbout.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // selectedRole = parent?.getItemAtPosition(position).toString()
                role=rolesList[position]
            }
        }
    }


    private fun setFocusableFalse(editText: EditText) {
        editText.isFocusable = false
    }

    private fun setFocusableTrue(editText: EditText) {
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
        editText.setSelection(editText.text.length)

        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
          R.id.edit_info->{
              binding.editInfo.visibility=View.GONE
              binding.updateInfo.visibility=View.VISIBLE
//              binding.etText.isFocusable=true
              setFocusableTrue(binding.etText)
              setFocusableTrue(binding.websiteLink)
//              binding.websiteLink.isFocusable=true
          }
            R.id.update_info->{

                val returnIntent = Intent()
                returnIntent.putExtra("about", binding.etText.text.toString())
                returnIntent.putExtra("weburl", binding.websiteLink.text.toString())
                returnIntent.putExtra("role", role)
                setResult(RESULT_OK, returnIntent)
//                startActivity(returnIntent)

                finish()

            }
        }
    }

}
