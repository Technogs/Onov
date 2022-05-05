package com.application.onovapplication.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.adapters.SearchFriendsAdapter
import com.application.onovapplication.adapters.ViewFollowersAdapter
import com.application.onovapplication.databinding.ActionBarLayout2Binding
import com.application.onovapplication.databinding.ActivityPeopleBinding
import com.application.onovapplication.model.ChatModel
import com.application.onovapplication.model.Follow
import com.application.onovapplication.model.PeopleData
import com.application.onovapplication.viewModels.ChatViewModel
import com.application.onovapplication.viewModels.SearchViewModel


class PeopleActivity : BaseAppCompatActivity(),ViewFollowersAdapter.OnPeopleClick,View.OnClickListener,SearchFriendsAdapter.OnViewClick {
    var viewFollowersAdapter: ViewFollowersAdapter? = null
    var people: PeopleData? = null
    var name: String? = null
    var userrefs = arrayListOf<String>()
    var user:MutableList<Follow>?=null

    val chatViewModel by lazy { ViewModelProvider(this).get(ChatViewModel::class.java) }
    val searchViewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java) }

    lateinit var chatdata: ChatModel
    private lateinit var binding: ActivityPeopleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeopleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val includedView: ActionBarLayout2Binding = binding.ab
        name=intent.getStringExtra("debate").toString()
        searchViewModel.searchUser(
            this, userPreferences.getUserREf(),
           ""
        )
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

                            binding.noDataLayout.visibility = View.VISIBLE
                            binding.rvFollowers.visibility = View.GONE
                        }
                     else   {
                         binding.noDataLayout.visibility = View.GONE
                            binding.rvFollowers.visibility = View.VISIBLE


                            chatdata = chatViewModel.chatdata
                            viewFollowersAdapter =
                                ViewFollowersAdapter(
                                    this,
                                    "people",
                                    name.toString(),
                                    "","",
                                    this,
                                    chatdata.chatList
                                )
                            binding.rvFollowers.adapter = viewFollowersAdapter}

                    } else {
                        setError(chatViewModel.message)

                    }
                }
            } else {
                setError(chatViewModel.message)
            }

        })

        searchViewModel.successful.observe(this, Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (searchViewModel.status == "success") {
                        if (searchViewModel.searchList.dataList!=null){
                        binding.rvFollowers.visibility=View.VISIBLE
                        binding.noDataLayout.visibility=View.GONE

                            viewFollowersAdapter =
                                ViewFollowersAdapter(
                                    this,
                                    "people",
                                    name.toString(),
                                    "","",
                                    this,
                                    searchViewModel.searchList.dataList
                                )
                            binding.rvFollowers.adapter = viewFollowersAdapter
                    viewFollowersAdapter!!.notifyDataSetChanged()
                        }else{
                    setError(searchViewModel.message)
                    binding.noDataLayout.visibility=View.VISIBLE
                    binding.rvFollowers.visibility=View.GONE
                        }


                    } else    {
                        setError(searchViewModel.message)
                        binding.noDataLayout.visibility=View.VISIBLE
                        binding.rvFollowers.visibility=View.GONE

                    }
                }
            } else {
                setError(searchViewModel.message)
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
        userrefs.add(datatem.userRef2)

    }

    override fun onRemoveFollow(datatem: Follow) {
        TODO("Not yet implemented")
    }

    override fun onRemoveSupport(datatem: Follow) {
        TODO("Not yet implemented")
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.submit->{
                val returnIntent = Intent()
                returnIntent.putExtra("result", userrefs)
                setResult(RESULT_OK, returnIntent)
                finish()
            }  R.id.searchBtn->{
                if (binding.serchText.text.toString() == "")
                    Toast.makeText(this, "please enter a keyword", Toast.LENGTH_SHORT).show()
                else searchViewModel.searchUser(
                    this, userPreferences.getUserREf(),
                    binding.serchText.text.toString()
                )

            }


        }
    }

    override fun onClick(dataItem: Follow) {
        TODO("Not yet implemented")
    }
}