package com.application.onovapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.adapters.ViewFollowersAdapter
import com.application.onovapplication.databinding.ActionBarLayout2Binding
import com.application.onovapplication.databinding.ActivityPeopleBinding
import com.application.onovapplication.model.ChatModel
import com.application.onovapplication.model.Follow
import com.application.onovapplication.model.PeopleData
import com.application.onovapplication.viewModels.ChatViewModel


class PeopleActivity : BaseAppCompatActivity(),ViewFollowersAdapter.OnPeopleClick,View.OnClickListener {
    var viewFollowersAdapter: ViewFollowersAdapter? = null
    var people: PeopleData? = null
    var name: String? = null
    var userrefs = arrayListOf<String>()

    val chatViewModel by lazy { ViewModelProvider(this).get(ChatViewModel::class.java) }
    lateinit var chatdata: ChatModel
    private lateinit var binding: ActivityPeopleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeopleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val includedView: ActionBarLayout2Binding = binding.ab
        name=intent.getStringExtra("debate").toString()
        chatViewModel.getChatList(this, userPreferences.getUserREf())
includedView.tvScreenTitle.text="People"
//        viewFollowersAdapter =tvScreenTitle
//            ViewFollowersAdapter(this, type.toString(), people!!.followingList)
//        rv_followers.adapter = viewFollowersAdapter
        observeViewModel()
    }

    private fun observeViewModel() {

        chatViewModel.successful.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (chatViewModel.status == "success") {
                        if (chatViewModel.chatdata.chatList.isNullOrEmpty()) {
//changed
                           // binding.noChatData.visibility = View.VISIBLE
                        }
                        chatdata=chatViewModel.chatdata

        viewFollowersAdapter =
            ViewFollowersAdapter(this, "people", name.toString(),this,chatdata.chatList)
       binding.rvFollowers.adapter = viewFollowersAdapter
                        //   chatsAdapter!!.notifyDataSetChanged()

                    } else {
                        setError(chatViewModel.message)

                    }
                }
            } else {
                setError(chatViewModel.message)
            }

        })
    }



    override fun onPClick(datatem: Follow) {

//            val returnIntent = Intent()
//            returnIntent.putExtra("result", datatem.userRef)
//            setResult(RESULT_OK, returnIntent)
//            finish()

    }

    override fun onCheckboxClick(datatem: Follow) {
        userrefs.add(datatem.userRef)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.submit->{
                val returnIntent = Intent()
                returnIntent.putExtra("result", userrefs)
                setResult(RESULT_OK, returnIntent)
                finish()
            }
        }
    }
}