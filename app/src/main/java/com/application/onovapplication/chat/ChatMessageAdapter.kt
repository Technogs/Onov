package com.application.onovapplication.chat

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.*
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.activities.FeedDetailActivity
import com.application.onovapplication.activities.common.ChatActivity
import com.application.onovapplication.databinding.RvReceiverLayoutBinding
import com.application.onovapplication.databinding.RvSenderLayoutBinding
import com.application.onovapplication.extensions.hide
import com.application.onovapplication.extensions.loadImage
import com.application.onovapplication.extensions.show
import com.application.onovapplication.repository.BaseUrl
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import java.util.*


class ChatMessageAdapter(
    var context: Context,
    var messagesArrayList: ArrayList<Messages>,
    var receiverPhotoUrl: String,
    var chatCallBack: OnChatMessageClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val TAG: String = ChatMessageAdapter::class.java.simpleName

    // creating a variable for exoplayer
    var exoPlayer: SimpleExoPlayer? = null

    companion object {
        var ITEM_SEND = 1
        var ITEM_RECEIVE = 2
        private var mPlayer: MediaPlayer? = null
        var pauseSeekbarTimer = 0.0F
        var pauseAudioTimer = "0"
        var isMediaPlayerPause = false


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.e(TAG, "viewType=" + viewType)

        return if (viewType == ITEM_SEND) {
            val binding = RvSenderLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
            MessageSenderViewHolder(binding)
        } else {
            val binding =
                RvReceiverLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
            MessageReceiverViewHolder(binding)


        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.javaClass == MessageSenderViewHolder::class.java) {
            val viewHolder = holder as MessageSenderViewHolder
            viewHolder.bind(messagesArrayList[position])
            //  holder.audioTrack.setOnTouchListener(View.OnTouchListener { v, event -> true })
            holder.senderImage.setOnClickListener {
                chatCallBack.onImageViewClick(position, 0)

            }


        } else {
            val viewHolder = holder as MessageReceiverViewHolder
            viewHolder.bind(messagesArrayList[position])
            //    holder.audioTrack.setOnTouchListener(View.OnTouchListener { v, event -> true })

            holder.receiverImage.setOnClickListener {
                chatCallBack.onImageViewClick(position, 1)
            }
        }
    }

    override fun getItemCount(): Int {
        return messagesArrayList.size
    }

    override fun getItemViewType(position: Int): Int {
        // return super.getItemViewType(position)
        val message: Messages = messagesArrayList[position]
        return if ((context as ChatActivity).userPreferences.getuserDetails()?.id == message.sender_id) {
            ITEM_SEND
        } else { ITEM_RECEIVE }
    }

    internal inner class MessageSenderViewHolder(private val binding: RvSenderLayoutBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        var senderImage = binding.llImageLayout

        //  var audioTrack = binding.seekbarAudio
        var audioPlay = binding.ivAudioPlay
        //   var audioTimer = binding.tvPlayTimer


        fun bind(messages: Messages) {

            val chatTime = (context as ChatActivity).convertDateFormat(
                messages.message_date.toString(),
                "yyyy-MM-dd hh:mm:ss",
                "hh:mm a"
            ).trim()


            if (messages.message_type.equals("text")) {
                binding.llMessageLayout.show()
                binding.llImageLayout.hide()
                binding.llFeedLayout.hide()
                binding.llAudioLayout.hide()
                binding.llVideoLayout.hide()
                binding.tvTimeSender.text = chatTime
                binding.tvSender.text = messages.message
                binding.ivSenderProfile.loadImage(BaseUrl.photoUrl + "" + (context as ChatActivity).userPreferences.getUserPhoto())

            } else if (messages.message_type.equals("image")) {
                binding.llImageLayout.show()
                binding.llMessageLayout.hide()
                binding.llFeedLayout.hide()
                binding.llAudioLayout.hide()
                binding.llVideoLayout.hide()
                binding.tvImageTime.text = chatTime
                binding.ivPic.loadImage(messages.fileUrl)
                binding.ivSenderProfile.loadImage(BaseUrl.photoUrl + "" + (context as ChatActivity).userPreferences.getUserPhoto())
                binding.ivPic.setOnClickListener { showDialog(messages.fileUrl, "photo") }


            } else if (messages.message_type.equals("video")) {
                binding.llImageLayout.hide()
                binding.llMessageLayout.hide()
                binding.llFeedLayout.hide()
                binding.llAudioLayout.hide()
                binding.llVideoLayout.show()
                binding.tvImageTime.text = chatTime
                exoplayer(BaseUrl.photoUrl + messages.fileUrl, binding.idExoVideoVIew)
                binding.ivSenderProfile.loadImage(BaseUrl.photoUrl + "" + (context as ChatActivity).userPreferences.getUserPhoto())


            } else if (messages.message_type.equals("feed")) {
                binding.llImageLayout.hide()
                binding.llMessageLayout.hide()
                binding.llFeedLayout.show()
                binding.llAudioLayout.hide()
                binding.llVideoLayout.hide()
                binding.tvFeedTime.text = chatTime
                Glide.with(context).load(BaseUrl.photoUrl + messages.feed?.profilePic).apply(
                    RequestOptions().placeholder(R.drawable.ic_baseline_account_circle_24)
                ).into(binding.ivChatProfile)
                binding.userName.text = messages.feed?.Name
                binding.feedType.text = "Feed Type : " + messages.feed?.recordType
                binding.feedTitle.text = messages.feed?.title
                binding.feedDescription.text = messages.feed?.description
                if (messages.feed?.recordType == "petition") {
                    binding.feedTitle.text = messages.feed?.petitionTitle
                    binding.feedDescription.text = messages.feed?.petitionDiscription

                } else if (messages.feed?.recordType == "polling") {
                   var path = messages.feed?.pollImage.toString()
                    if (path.isNullOrEmpty()) {
                        binding.feedMedia.visibility = View.GONE
                    } else {
                        Glide.with(context)
                            .load(BaseUrl.photoUrl + messages.feed?.pollImage)
                            .into(binding.ivFeed)
                    }
                    if (messages.feed?.description.isNullOrEmpty()) {
                        binding.feedDescription.visibility = View.GONE
                    } else {
                        binding.feedDescription.visibility = View.VISIBLE
                    }
                    if (messages.feed?.pollImage.isNullOrEmpty()) {
                        binding.feedMedia.visibility = View.GONE
                    }

                }
//                if (messages.feed?.recordType == "polling") {



//                            Glide.with(context).load(BaseUrl.photoUrl + messages.feed?.pollImage)
//                                .into(binding.ivFeed)
//                }

                binding.senderRlyt.setOnClickListener {
                    val intent = Intent(context, FeedDetailActivity::class.java)
                    intent.putExtra("feed", messages.feed)
                    context.startActivity(intent)
                }

                when (messages.feed?.fileType) {
                    "document" -> {
                        binding.wbFeed.visibility = View.VISIBLE
                        binding.vvFeed.visibility = View.GONE
                        binding.idExoPlayerVIew.visibility = View.GONE

                        binding.ivFeed.visibility = View.GONE

                        binding.wbFeed.settings.javaScriptEnabled = true

                        binding.wbFeed.settings.javaScriptCanOpenWindowsAutomatically = true
                        binding.wbFeed.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
                        if (messages.feed?.recordType == "petition") {
                            binding.wbFeed.loadUrl(
                                "https://docs.google.com/gview?embedded=true&url=".plus(
                                    BaseUrl.photoUrl + messages.feed?.petitionMedia
                                )
                            )
                        } else binding.wbFeed.loadUrl(
                            "https://docs.google.com/gview?embedded=true&url=".plus(
                                BaseUrl.photoUrl + messages.feed?.filePath
                            )
                        )
                        //     Log.e("PRACHI","https://docs.google.com/gview?embedded=true&url=".plus(BaseUrl.photoUrl + feedData.filePath))

                    }

                    "video" -> {
                        binding.wbFeed.visibility = View.GONE
                        //   binding.vvFeed.visibility = View.VISIBLE
                        binding.idExoPlayerVIew.visibility = View.VISIBLE
                        binding.ivFeed.visibility = View.GONE
                        if (messages.feed?.recordType == "petition") {
                            exoplayer(
                                BaseUrl.photoUrl + messages.feed?.petitionMedia,
                                binding.idExoPlayerVIew
                            )
                        } else exoplayer(
                            BaseUrl.photoUrl + messages.feed?.filePath,
                            binding.idExoPlayerVIew
                        )
//                    val mc = MediaController(context)
//                    binding.vvFeed.setMediaController(mc)
//                    binding.vvFeed.setVideoPath(BaseUrl.photoUrl + feedData.filePath)
                        // binding.vvFeed.start()
                    }

                    "photo" -> {
                        binding.wbFeed.visibility = View.GONE
                        binding.vvFeed.visibility = View.GONE
                        binding.idExoPlayerVIew.visibility = View.GONE
                        binding.ivFeed.visibility = View.VISIBLE
                        var path = ""
                         if (messages.feed?.recordType == "petition") {
                            path = messages.feed?.petitionMedia.toString()
                            Glide.with(context)
                                .load(BaseUrl.photoUrl + messages.feed?.petitionMedia)
                                .into(binding.ivFeed)
                        } else {
                            path = messages.feed?.filePath.toString()
                            Glide.with(context)
                                .load(BaseUrl.photoUrl + messages.feed?.filePath)
                                .into(binding.ivFeed)
                        }
                        binding.ivFeed.setOnClickListener {
                            showDialog(
                                BaseUrl.photoUrl + path,
                                "photo"
                            )
                        }


                    }
                }

            } else if (messages.message_type.equals("event")) {
                binding.llImageLayout.hide()
                binding.llMessageLayout.hide()
                binding.llFeedLayout.show()
                binding.llAudioLayout.hide()
                binding.tvFeedTime.text = chatTime
                binding.ivChatProfile.visibility = View.GONE
                binding.userName.visibility = View.GONE
                binding.feedType.visibility = View.GONE
                binding.feedTitle.text = messages.event?.title
                binding.feedDescription.text = messages.event?.description

                binding.wbFeed.visibility = View.GONE
                binding.vvFeed.visibility = View.GONE
                binding.idExoPlayerVIew.visibility = View.GONE
                binding.ivFeed.visibility = View.VISIBLE
                Glide.with(context).load(BaseUrl.photoUrl + messages.event?.cover_image)
                    .into(binding.ivFeed)
                binding.ivFeed.setOnClickListener {
                    showDialog(
                        BaseUrl.photoUrl + messages.event?.cover_image.toString(),
                        "photo"
                    )
                }

                binding.viewFeed.setOnClickListener {
                    val intent = Intent(context, FeedDetailActivity::class.java)
                    intent.putExtra("event", messages.event)
                    context.startActivity(intent)
                }


            }

        }
    }

    private fun showDialog(url: String, type: String) {

        var dialog = Dialog(context)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_layout)
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val ivFeed = dialog.findViewById(R.id.ivFeed) as ImageView
        val cross = dialog.findViewById(R.id.close) as ImageView
        val vvFeed = dialog.findViewById(R.id.vvFeed) as VideoView

        val idExoPlayerVIew = dialog.findViewById(R.id.idExoPlayerVIew) as SimpleExoPlayerView
        val wbFeed = dialog.findViewById(R.id.wbFeed) as WebView
        idExoPlayerVIew.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL)

        cross.setOnClickListener { dialog.dismiss() }

        when (type) {
            "document" -> {
                wbFeed.visibility = View.VISIBLE
                vvFeed.visibility = View.GONE
                idExoPlayerVIew.visibility = View.GONE

                ivFeed.visibility = View.GONE
                wbFeed.settings.javaScriptEnabled = true

                wbFeed.settings.javaScriptCanOpenWindowsAutomatically = true
                wbFeed.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
                wbFeed.loadUrl("https://docs.google.com/gview?embedded=true&url=".plus(BaseUrl.photoUrl + url))

            }

            "video" -> {

                //here position
                wbFeed.visibility = View.GONE
                //   binding.vvFeed.visibility = View.VISIBLE
                idExoPlayerVIew.visibility = View.VISIBLE
                ivFeed.visibility = View.GONE

                exoplayer(BaseUrl.photoUrl + url, idExoPlayerVIew)
//                    exoplayer(BaseUrl.photoUrl + mdData.filePath)
            }

            "photo" -> {
                wbFeed.visibility = View.GONE
                vvFeed.visibility = View.GONE
                idExoPlayerVIew.visibility = View.GONE
                ivFeed.visibility = View.VISIBLE

                Glide.with(context).load(url)
                    .into(ivFeed)
                //   Glide.with(context).load(BaseUrl.photoUrl + mdData.filePath).into(ivFeed)

            }
        }
        dialog.setOnKeyListener(DialogInterface.OnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog?.cancel()
                return@OnKeyListener true

            }
            false
        })
        dialog.show()

    }

    internal inner class MessageReceiverViewHolder(private val binding: RvReceiverLayoutBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        var receiverImage = binding.llImageLayout

        fun bind(messages: Messages) {
            Log.e("timefc ", messages.timeStamp)
            val chatTime = (context as ChatActivity).convertDateFormat(
                messages.message_date.toString(),
                "yyyy-MM-dd hh:mm:ss",
                "hh:mm a"
            ).trim()
            //    var chatTime = (context as ChatActivity).convertDateFormat((context as ChatActivity).getEpochDate(messages.timeStamp.toLong()), "yyyy-MM-dd hh:mm:ss", "hh:mm a").trim()

            if (messages.message_type.equals("text")) {
                binding.llReceiverLayout.show()
                binding.llImageLayout.hide()
                binding.llFeedLayout.hide()
                binding.tvTimeReceiver.text = chatTime
                binding.tvReceiverMessage.text = messages.message
                binding.ivReceiverProfile.loadImage(receiverPhotoUrl)
            }


            if (messages.message_type.equals("image")) {
                binding.llImageLayout.show()
                binding.llReceiverLayout.hide()
                binding.llFeedLayout.hide()
                binding.tvImageTime.text = chatTime
                binding.ivPic.loadImage(messages.fileUrl)
                binding.ivReceiverProfile.loadImage(receiverPhotoUrl)
                binding.ivPic.setOnClickListener { showDialog(messages.fileUrl, "photo") }

            }
            if (messages.message_type.equals("feed")) {
                binding.llImageLayout.hide()
                binding.llReceiverLayout.hide()
                binding.llFeedLayout.show()
                binding.tvFeedTime.text = chatTime
                Glide.with(context).load(BaseUrl.photoUrl + messages.feed?.profilePic).apply(
                    RequestOptions().placeholder(R.drawable.ic_baseline_account_circle_24)
                ).into(binding.ivChatProfile)
                binding.userName.text = messages.feed?.Name
                binding.feedType.text = "Feed Type : " + messages.feed?.recordType
                binding.feedTitle.text = messages.feed?.title
                binding.feedDescription.text = messages.feed?.description
                if (messages.feed?.recordType == "petition") {
                    binding.feedTitle.text = messages.feed?.petitionTitle
                    binding.feedDescription.text = messages.feed?.petitionDiscription

                } else if (messages.feed?.recordType == "polling") {
                    var path = messages.feed?.pollImage.toString()
                    if (path.isNullOrEmpty()) {
                        binding.feedMedia.visibility = View.GONE
                    } else {
                        Glide.with(context)
                            .load(BaseUrl.photoUrl + messages.feed?.pollImage)
                            .into(binding.ivFeed)
                    }
                    if (messages.feed?.description.isNullOrEmpty()) {
                        binding.feedDescription.visibility = View.GONE
                    } else {
                        binding.feedDescription.visibility = View.VISIBLE
                    }
                    if (messages.feed?.pollImage.isNullOrEmpty()) {
                        binding.feedMedia.visibility = View.GONE
                    }

                }
                binding.viewFeed.setOnClickListener {
                    val intent = Intent(context, FeedDetailActivity::class.java)
                    intent.putExtra("feed", messages.feed)
                    context.startActivity(intent)

                }
                when (messages.feed?.fileType) {
                    "document" -> {
                        binding.wbFeed.visibility = View.VISIBLE
                        binding.vvFeed.visibility = View.GONE
                        binding.idExoPlayerVIew.visibility = View.GONE

                        binding.ivFeed.visibility = View.GONE

                        binding.wbFeed.settings.javaScriptEnabled = true

                        binding.wbFeed.settings.javaScriptCanOpenWindowsAutomatically = true
                        binding.wbFeed.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
                        if (messages.feed?.recordType == "petition") {
                            binding.wbFeed.loadUrl(
                                "https://docs.google.com/gview?embedded=true&url=".plus(
                                    BaseUrl.photoUrl + messages.feed?.petitionMedia
                                )
                            )
                        } else binding.wbFeed.loadUrl(
                            "https://docs.google.com/gview?embedded=true&url=".plus(
                                BaseUrl.photoUrl + messages.feed?.filePath
                            )
                        )
                        //     Log.e("PRACHI","https://docs.google.com/gview?embedded=true&url=".plus(BaseUrl.photoUrl + feedData.filePath))

                    }

                    "video" -> {
                        binding.wbFeed.visibility = View.GONE
                        //   binding.vvFeed.visibility = View.VISIBLE
                        binding.idExoPlayerVIew.visibility = View.VISIBLE
                        binding.ivFeed.visibility = View.GONE
                        if (messages.feed?.recordType == "petition") {
                            exoplayer(
                                BaseUrl.photoUrl + messages.feed?.petitionMedia,
                                binding.idExoPlayerVIew
                            )
                        } else exoplayer(
                            BaseUrl.photoUrl + messages.feed?.filePath,
                            binding.idExoPlayerVIew
                        )
//                    val mc = MediaController(context)
//                    binding.vvFeed.setMediaController(mc)
//                    binding.vvFeed.setVideoPath(BaseUrl.photoUrl + feedData.filePath)
                        // binding.vvFeed.start()
                    }

                    "photo" -> {
                        binding.wbFeed.visibility = View.GONE
                        binding.vvFeed.visibility = View.GONE
                        binding.idExoPlayerVIew.visibility = View.GONE
                        binding.ivFeed.visibility = View.VISIBLE
                        var path = ""
                        if (messages.feed?.recordType == "polling") {
                            path = messages.feed?.pollImage.toString()
                            if (path.isNullOrEmpty()) {
                                binding.feedMedia.visibility = View.GONE
                            } else {
                                Glide.with(context)
                                    .load(BaseUrl.photoUrl + messages.feed?.pollImage)
                                    .into(binding.ivFeed)
                            }

                            if (messages.feed?.description.isNullOrEmpty()) {
                                binding.feedDescription.visibility = View.GONE
                            } else {
                                binding.feedDescription.visibility = View.VISIBLE
                            }
//                            Glide.with(context).load(BaseUrl.photoUrl + messages.feed?.pollImage)
//                                .into(binding.ivFeed)
                        } else if (messages.feed?.recordType == "petition") {
                            path = messages.feed?.petitionMedia.toString()
                            Glide.with(context)
                                .load(BaseUrl.photoUrl + messages.feed?.petitionMedia)
                                .into(binding.ivFeed)
                        } else {
                            path = messages.feed?.filePath.toString()
                            Glide.with(context)
                                .load(BaseUrl.photoUrl + messages.feed?.filePath)
                                .into(binding.ivFeed)
                        }

                        binding.ivFeed.setOnClickListener {
                            showDialog(
                                BaseUrl.photoUrl + path,
                                "photo"
                            )
                        }

                    }
                }
            }
            if (messages.message_type.equals("event")) {
                binding.llImageLayout.hide()
                binding.llReceiverLayout.hide()
                binding.llFeedLayout.show()
                binding.tvFeedTime.text = chatTime
                binding.ivChatProfile.visibility = View.GONE
                binding.userName.visibility = View.GONE
                binding.feedType.visibility = View.GONE
                binding.feedTitle.text = messages.event?.title
                binding.feedDescription.text = messages.event?.description
                binding.wbFeed.visibility = View.GONE
                binding.vvFeed.visibility = View.GONE
                binding.idExoPlayerVIew.visibility = View.GONE
                binding.ivFeed.visibility = View.VISIBLE

                Glide.with(context).load(BaseUrl.photoUrl + messages.event?.cover_image)
                    .into(binding.ivFeed)
                binding.ivFeed.setOnClickListener {
                    showDialog(
                        BaseUrl.photoUrl + messages.event?.cover_image,
                        "photo"
                    )
                }

                binding.receiverRlyt.setOnClickListener {
                    val intent = Intent(context, FeedDetailActivity::class.java)
                    intent.putExtra("event", messages.event)
                    context.startActivity(intent)
                }

            }
        }
    }

    interface OnChatMessageClickListener {
        fun onImageViewClick(position: Int, status: Int)
    }


    fun exoplayer(videoURL: String, view: SimpleExoPlayerView) {
        try {

            // bandwisthmeter is used for
            // getting default bandwidth
            val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()

            // track selector is used to navigate between
            // video using a default seekbar.
            val trackSelector: TrackSelector =
                DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))

            // we are adding our track selector to exoplayer.
            exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector)

            // we are parsing a video url
            // and parsing its video uri.
            val videouri: Uri = Uri.parse(videoURL)

            // we are creating a variable for datasource factory
            // and setting its user agent as 'exoplayer_view'
            val dataSourceFactory = DefaultHttpDataSourceFactory("exoplayer_video")

            // we are creating a variable for extractor factory
            // and setting it to default extractor factory.
            val extractorsFactory: ExtractorsFactory = DefaultExtractorsFactory()

            // we are creating a media source with above variables
            // and passing our event handler as null,
            val mediaSource: MediaSource =
                ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null)

            // inside our exoplayer view
            // we are setting our player
            view.player = exoPlayer
            // exoPlayerView.setPlayer(exoPlayer)

            // we are preparing our exoplayer
            // with media source.
            exoPlayer?.prepare(mediaSource)

            // we are setting our exoplayer
            // when it is ready.
            exoPlayer?.playWhenReady = true
        } catch (e: Exception) {
            // below line is used for
            // handling our errors.
            Log.e("TAG", "Error : $e")
        }
    }
}