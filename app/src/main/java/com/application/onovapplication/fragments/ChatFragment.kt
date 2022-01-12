package com.application.onovapplication.fragments

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseFragment
import com.application.onovapplication.activities.common.ChatActivity
import com.application.onovapplication.adapters.OnlineStatusAdapter
import com.application.onovapplication.adapters.ViewChatsAdapter
import com.application.onovapplication.databinding.ActivityAskToAddBinding
import com.application.onovapplication.databinding.FragmentChatBinding
import com.application.onovapplication.model.FeedsData
import com.application.onovapplication.model.Follow
import com.application.onovapplication.repository.BaseUrl
import com.application.onovapplication.viewModels.ChatViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
//https://objectpartners.com/2017/09/06/real-time-chat-application-with-kotlin-and-firebase/import com.application.onovapplication.viewModels.ChatViewModel


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ChatFragment : BaseFragment(), ViewChatsAdapter.OnMessageClickListener,ViewChatsAdapter.OnMessageClickLImage {

    private var param1: String? = null
    private var param2: String? = null
    var feed: FeedsData? = null

    private var onlineStatusAdapter: OnlineStatusAdapter? = null
    private var chatsAdapter: ViewChatsAdapter? = null
    val chatViewModel by lazy { ViewModelProvider(this).get(ChatViewModel::class.java) }
    lateinit var binding: FragmentChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("userinfo",userPreferences.getuserDetails().toString())
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onlineStatusAdapter = OnlineStatusAdapter(requireContext())
        binding.rvStatus.adapter = onlineStatusAdapter


        chatViewModel.getChatList(requireActivity(),userPreferences.getUserREf())
        showDialog()
        observeViewModel()

    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    private fun observeViewModel() {

        chatViewModel.successful.observe(requireActivity(), androidx.lifecycle.Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (chatViewModel.status == "success") {
                        if (chatViewModel.chatdata.chatList.isNullOrEmpty()){
                            binding.noChatData.visibility=View.VISIBLE
                        }
                        chatsAdapter = ViewChatsAdapter(requireActivity(),chatViewModel.chatdata.chatList,this,this,0)
                        binding.rvChatList.adapter = chatsAdapter
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



    override fun onMessageClickListener(data: Follow) {

        startActivity(ChatActivity.getStartIntent(
            requireActivity(), data.id.trim(),
            data.fullName,
            BaseUrl.photoUrl + "" + data.profilePic.trim(),"",null,null,
            data.userRef.trim()))

    }

    override fun onMessageImageClick(data: Follow) {
    previewChatImage(data.profilePic,data.fullName)
    }

    private fun previewChatImage(url: String, userName: String) {
        val ImagePreviewDialog = Dialog(requireActivity(),
            android.R.style.ThemeOverlay_Material_Dialog
        )
        ImagePreviewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        ImagePreviewDialog.setCancelable(false)
        ImagePreviewDialog.setContentView(R.layout.big_image_preview)
        val btnClose = ImagePreviewDialog.findViewById(R.id.btnIvClose) as ImageView
        val ivPreview = ImagePreviewDialog.findViewById(R.id.iv_preview_image) as ImageView
        val tvUsername = ImagePreviewDialog.findViewById(R.id.tv_username) as TextView
        tvUsername.text = userName
        Glide.with(requireActivity()).load(BaseUrl.photoUrl +url).apply(RequestOptions().placeholder(R.drawable.ic_baseline_account_circle_24)).into(ivPreview)

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