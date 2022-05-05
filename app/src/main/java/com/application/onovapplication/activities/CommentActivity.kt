package com.application.onovapplication.activities

import android.content.Intent
import android.os.Bundle
//https://www.uscreen.tv/blog/live-streaming-platforms/#uscreen
//https://docs.agora.io/en/Interactive%20Broadcast/co_host_overview?platform=Android
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.activities.common.HomeTabActivity
import com.application.onovapplication.adapters.CommentsAdapter
import com.application.onovapplication.adapters.ReplyAdapter
import com.application.onovapplication.databinding.ActivityCommentBinding
import com.application.onovapplication.model.CommentData
import com.application.onovapplication.model.EventData
import com.application.onovapplication.model.FeedsData
import com.application.onovapplication.viewModels.HomeViewModel


class CommentActivity : BaseAppCompatActivity(), View.OnClickListener,
    CommentsAdapter.OnReplyClick {

    private val homeViewModel by lazy { ViewModelProvider(this).get(HomeViewModel::class.java) }
    var feeds: FeedsData? = null
    private lateinit var binding: ActivityCommentBinding

    var dataevent: EventData? = null
    var cmtdata: CommentData? = null
    var rcView: RecyclerView? = null
    var id: String? = ""
    var count: Int? = 5
    var rply: String? = ""

    var commentList = arrayListOf<CommentData>()
    private val debatesAdapter: CommentsAdapter by lazy {
        CommentsAdapter(this, commentList, this)
    }

    var replyAdapter: ReplyAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        feeds = intent.getParcelableExtra("feeds")
        dataevent = intent.getParcelableExtra("event")
        Log.d("datajhjhhfh", "feeds  " + feeds + "  feeds  " + dataevent)

        binding.rvComment.adapter = debatesAdapter

        if (feeds == null) {
            id = dataevent?.id.toString()
            binding.userName.text = "Event"

            homeViewModel.getComment(
                this,
                id.toString(),
                "event",
                userPreferences.getuserDetails()?.userRef.toString(),
                ""
            )
        } else {
            id = feeds?.id.toString()
            binding.userName.text = feeds?.recordType
            if (feeds?.recordType == "post")
                homeViewModel.getComment(
                    this,
                    feeds?.id.toString(),
                    "post",
                    userPreferences.getuserDetails()?.userRef.toString(),
                    ""
                )
            else if (feeds?.recordType == "petition")
                homeViewModel.getComment(
                    this,
                    feeds?.id.toString(),
                    "petition",
                    userPreferences.getuserDetails()?.userRef.toString(),
                    ""
                )
            else if (feeds?.recordType == "donationRequest")
                homeViewModel.getComment(
                    this,
                    feeds?.id.toString(),
                    "donationRequest",
                    userPreferences.getuserDetails()?.userRef.toString(),
                    ""
                )
            else if (feeds?.recordType == "polling")
                homeViewModel.getComment(
                    this,
                    feeds?.id.toString(),
                    "polling",
                    userPreferences.getuserDetails()?.userRef.toString(),
                    ""
                )
            else if (feeds?.recordType == "law")
                homeViewModel.getComment(
                    this,
                    feeds?.id.toString(),
                    "law",
                    userPreferences.getuserDetails()?.userRef.toString(),
                    ""
                )
        }


        observeViewModel()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.send_cmnt -> {
                if (binding.etCommentBox.text.isNullOrEmpty()) setError("Please add a comment")
                else {
                    id = if (feeds?.isShared == "1") feeds?.id else feeds?.id
                    rply = ""
                    if (feeds != null) {
                        id = feeds?.id
                        if (feeds?.recordType == "post")
                            homeViewModel.addComment(
                                this,
                                userPreferences.getuserDetails()?.userRef.toString(),
                                id.toString(),
                                "post",
                                binding.etCommentBox.text.toString(), ""
                            )
                        else if (feeds?.recordType == "petition")
                            homeViewModel.addComment(
                                this,
                                userPreferences.getuserDetails()?.userRef.toString(),
                                id.toString(),
                                "petition",
                                binding.etCommentBox.text.toString(), ""
                            )
                        else if (feeds?.recordType == "donationRequest")
                            homeViewModel.addComment(
                                this,
                                userPreferences.getuserDetails()?.userRef.toString(),
                                id.toString(),
                                "donationRequest",
                                binding.etCommentBox.text.toString(), ""
                            ) else if (feeds?.recordType == "polling")
                            homeViewModel.addComment(
                                this,
                                userPreferences.getuserDetails()?.userRef.toString(),
                                id.toString(),
                                "polling",
                                binding.etCommentBox.text.toString(), ""
                            )
                        else if (feeds?.recordType == "law")
                            homeViewModel.addComment(
                                this,
                                userPreferences.getuserDetails()?.userRef.toString(),
                                id.toString(),
                                "law",
                                binding.etCommentBox.text.toString(), ""
                            )
                    } else {
                        id = dataevent?.id
                        homeViewModel.addComment(
                            this,
                            userPreferences.getuserDetails()?.userRef.toString(),
                            id.toString(), "event",
                            binding.etCommentBox.text.toString(), ""
                        )
                    }

                    binding.etCommentBox.setText("")
                }
            }
        }
    }

    private fun observeViewModel() {

        homeViewModel.successfulCommentGet.observe(this, androidx.lifecycle.Observer {
            dismissDialog()

            if (it != null) {
                if (it) {
                    if (homeViewModel.status == "success") {
                        if (rply == "") {
                            rcView?.visibility = View.GONE
                            if (!commentList.isNullOrEmpty())
                                commentList.clear()

                            commentList.addAll(homeViewModel.cmntResponse.commentData)
                            debatesAdapter.notifyDataSetChanged()


                        } else if (rply == "reply") {
                            replyAdapter =
                                ReplyAdapter(this, homeViewModel.cmntResponse.commentData)
                            rcView?.adapter = replyAdapter
                        }


                    } else {
                        setError(homeViewModel.message)
                        //finish()
                    }
                }
            } else {
                setError(homeViewModel.message)
            }

        })
        homeViewModel.successfulCommentAdd.observe(this, androidx.lifecycle.Observer {

            dismissDialog()
            if (it != null) {
                if (it) {
                    if (homeViewModel.status == "success") {
                        setError(homeViewModel.message)
                        if (rply == "reply") {
                            cmtdata?.replyCount = (cmtdata?.replyCount?.toInt()?.plus(1)).toString()
                            debatesAdapter.notifyDataSetChanged()
                        }

                        if (feeds != null) {
                            id = feeds?.id.toString()
                            if (feeds?.recordType == "post")
                                homeViewModel.getComment(
                                    this,
                                    id.toString(),
                                    "post",
                                    userPreferences.getuserDetails()?.userRef.toString(),
                                    ""
                                )
                            else if (feeds?.recordType == "petition")
                                homeViewModel.getComment(
                                    this,
                                    id.toString(),
                                    "petition",
                                    userPreferences.getuserDetails()?.userRef.toString(),
                                    ""
                                )
                            else if (feeds?.recordType == "donationRequest")
                                homeViewModel.getComment(
                                    this,
                                    id.toString(),
                                    "donationRequest",
                                    userPreferences.getuserDetails()?.userRef.toString(),
                                    ""
                                ) else if (feeds?.recordType == "polling")
                                homeViewModel.getComment(
                                    this,
                                    id.toString(),
                                    "polling",
                                    userPreferences.getuserDetails()?.userRef.toString(),
                                    ""
                                )
                        } else {
                            id = dataevent?.id
                            homeViewModel.getComment(
                                this,
                                id.toString(),
                                "event",
                                userPreferences.getuserDetails()?.userRef.toString(),
                                ""
                            )
                        }


                    } else {
                        setError(homeViewModel.message)
                        finish()
                    }
                }
            } else {
                setError(homeViewModel.message)
            }

        })
        homeViewModel.successfulCommentLike.observe(this, androidx.lifecycle.Observer {

            dismissDialog()
            if (it != null) {
                if (it) {
                    if (homeViewModel.status == "success") {

                        if (homeViewModel.message == "Disliked successfully") {
                            cmtdata?.dislike = true
                            cmtdata?.like = false
                            if ((cmtdata?.likeCount)!!.toInt() > 0 && (cmtdata?.Liked == "1"))
                                cmtdata?.likeCount =
                                    ((cmtdata?.likeCount)!!.toInt() - 1).toString()
                            cmtdata?.dislikeCount =
                                ((cmtdata?.dislikeCount)!!.toInt() + 1).toString()
                            cmtdata?.Liked = "0"
                            cmtdata?.Disliked = "1"

                            binding.rvComment.adapter!!.notifyDataSetChanged()
                        } else if (homeViewModel.message == "Liked successfully") {
                            cmtdata?.like = true
                            cmtdata?.dislike = false
                            cmtdata?.likeCount = ((cmtdata?.likeCount)!!.toInt() + 1).toString()
                            if ((cmtdata?.dislikeCount)!!.toInt() > 0 && (cmtdata?.Disliked == "1"))
                                cmtdata?.dislikeCount =
                                    ((cmtdata?.dislikeCount)!!.toInt() - 1).toString()
                            cmtdata?.Liked = "1"
                            cmtdata?.Disliked = "0"

                            binding.rvComment.adapter!!.notifyDataSetChanged()

                        }
                    } else {
                        setError(homeViewModel.message)
                        finish()
                    }
                }
            } else {
                setError(homeViewModel.message)
            }

        })

    }


    override fun OnClickReplyPost(
        replyLyt: RelativeLayout,
        replyMsg: EditText,
        commentData: CommentData
    ) {
        cmtdata = commentData
        replyLyt.visibility = View.GONE
        rply = "reply"
        showDialog()
        if (feeds != null) {
            homeViewModel.addComment(
                this,
                userPreferences.getuserDetails()?.userRef.toString(),
                feeds?.recordId.toString(),
                "reply",
                replyMsg.text.toString(), commentData.id
            )
        } else {
            homeViewModel.addComment(
                this,
                userPreferences.getuserDetails()?.userRef.toString(),
                dataevent?.id.toString(),
                "reply",
                replyMsg.text.toString(), commentData.id
            )
        }
        replyMsg.setText("")

    }

    override fun OnViewReplyClick(rview: RecyclerView, commentData: CommentData) {
        cmtdata = commentData
        rply = "reply"
        showDialog()
        if (feeds != null)
            homeViewModel.getComment(
                this,
                feeds!!.recordId.toString(),
                "reply",
                userPreferences.getuserDetails()?.userRef.toString(),
                commentData.id
            )
        else homeViewModel.getComment(
            this,
            dataevent!!.id.toString(),
            "reply",
            userPreferences.getuserDetails()?.userRef.toString(),
            commentData.id
        )

        rview.visibility = View.VISIBLE
        rcView = rview


    }

    override fun OnLikeClick(likeText: ImageView, commentData: CommentData) {
        cmtdata = commentData
        if (commentData.Liked == "0") {
            homeViewModel.commentlike(
                this,
                userPreferences.getuserDetails()?.userRef.toString(), commentData.id, "reply", "1"
            )

            count = 1

        }
    }

    override fun OnDisLikeClick(likeText: ImageView, commentData: CommentData) {
        cmtdata = commentData
        if (commentData.Liked == "1") {
            homeViewModel.commentlike(
                this,
                userPreferences.getuserDetails()?.userRef.toString(),
                commentData.id, "reply", "0"
            )
            count = 0
        }
    }

    override fun onBackPressed() {

        startActivity(Intent(this, HomeTabActivity::class.java))
        super.onBackPressed()
    }
}