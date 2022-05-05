package com.application.onovapplication.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.activities.common.ChatActivity
import com.application.onovapplication.adapters.ViewChatsAdapter
import com.application.onovapplication.model.EventData
import com.application.onovapplication.model.FeedsData
import com.application.onovapplication.model.Follow
import com.application.onovapplication.repository.BaseUrl
import com.application.onovapplication.viewModels.ChatViewModel
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface

import android.content.Intent
import android.view.KeyEvent
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.application.onovapplication.activities.common.HomeTabActivity
import com.application.onovapplication.databinding.ActivitySignPetitionBinding
import com.application.onovapplication.databinding.ActivityUsersBinding
import com.application.onovapplication.model.ChatModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions



class UsersActivity : BaseAppCompatActivity(), ViewChatsAdapter.OnMessageClickListener,
    ViewChatsAdapter.OnMessageClickLImage {
    var feeds: FeedsData? = null
    var events: EventData? = null
    private var debate: String = ""
    lateinit var chatdata: ChatModel
    private var chatsAdapter: ViewChatsAdapter? = null
    private lateinit var binding: ActivityUsersBinding

    val chatViewModel by lazy { ViewModelProvider(this).get(ChatViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        feeds = intent.getParcelableExtra("feeds")
        events = intent.getParcelableExtra("event")
        chatViewModel.getChatList(this, userPreferences.getUserREf(),"")
      if ( feeds==null)  { binding.shareFeed.visibility=View.GONE}

        showDialog()
        binding.shareFeed.setOnClickListener {
            showDialog()
            when {

                events==null ->  chatViewModel.shareFeed(this,userPreferences.getuserDetails()?.userRef.toString(),feeds?.id.toString())

                feeds==null -> {
                    binding.shareFeed.visibility=View.GONE
//                    chatViewModel.shareFeed(
//                        this,
//                        userPreferences.getuserDetails()?.userRef.toString(),
//                        events?.id.toString()
//                    )
                }            }
        }
        observeViewModel()


    }

    private fun observeViewModel() {

        chatViewModel.successful.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (chatViewModel.status == "success") {
                        if (chatViewModel.chatdata.chatList==null) {
                            binding.rvChatList.visibility = View.GONE
                            binding.noChatData.visibility = View.VISIBLE

                        }else{
                            binding.noChatData.visibility = View.GONE
                            binding.rvChatList.visibility = View.VISIBLE}
                            chatdata = chatViewModel.chatdata
                            chatsAdapter =
                                ViewChatsAdapter(this, chatdata.chatList, this, this, 1)
                            binding.rvChatList.adapter = chatsAdapter
                               chatsAdapter!!.notifyDataSetChanged()

                    } else {
                        setError(chatViewModel.message)

                    }
                }
            } else {
                setError(chatViewModel.message)
            }

        })

        chatViewModel.successfulShareFeed.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (chatViewModel.status == "success") {
                        chatdata=chatViewModel.chatdata
                        startActivity(Intent(this,HomeTabActivity::class.java))


                    } else {
                        setError(chatViewModel.message)

                    }
                }
            } else {
                setError(chatViewModel.message)
            }

        })
    }

    override fun onMessageClickListener(data: Follow) {

        when {
            events==null -> startActivity(
                ChatActivity.getStartIntent(
                    this, data.user_id?.trim().toString(),
                    data.fullName,
                    BaseUrl.photoUrl + "" + data.profilePic.trim(), "feed", feeds,null,
                    data.userRef?.trim().toString()
                )
            )
            feeds==null -> {

                startActivity(
                    ChatActivity.getStartIntent(
                        this, data.user_id?.trim().toString(),
                        data.fullName,
                        BaseUrl.photoUrl + "" + data.profilePic.trim(), "feed", null, events,
                        data.userRef?.trim().toString()
                    )
                )
            }
            else -> startActivity(
                ChatActivity.getStartIntent(
                    this, data.user_id?.trim().toString(),
                    data.fullName,
                    BaseUrl.photoUrl + "" + data.profilePic.trim(), "feed", null,null,
                    data.userRef?.trim().toString()
                )
            )
        }


    }

    override fun onMessageImageClick(data: Follow) {
        previewChatImage(data.profilePic,data.fullName)
    }


    private fun previewChatImage(url: String, userName: String) {
        val ImagePreviewDialog = Dialog(this,
            android.R.style.ThemeOverlay_Material_Dialog
        )
        ImagePreviewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        ImagePreviewDialog.setCancelable(false)
        ImagePreviewDialog.setContentView(R.layout.big_image_preview)
        val btnClose = ImagePreviewDialog.findViewById(R.id.btnIvClose) as ImageView
        val ivPreview = ImagePreviewDialog.findViewById(R.id.iv_preview_image) as ImageView
        val tvUsername = ImagePreviewDialog.findViewById(R.id.tv_username) as TextView
        tvUsername.text = userName
        Glide.with(this).load(BaseUrl.photoUrl +url).apply(RequestOptions().placeholder(R.drawable.ic_baseline_account_circle_24)).into(ivPreview)

        btnClose.setOnClickListener {
            ImagePreviewDialog.dismiss()
        }

        ImagePreviewDialog.setOnKeyListener(DialogInterface.OnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog?.cancel()
                return@OnKeyListener true

            }
            false
        })

        ImagePreviewDialog.show()
        ImagePreviewDialog.setCanceledOnTouchOutside(true)
    }

}