package com.application.onovapplication.adapters

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.activities.AddPollingResultActivity
import com.application.onovapplication.activities.PetitionDetailsActivity
import com.application.onovapplication.activities.PollResultActivity
import com.application.onovapplication.activities.common.AskDonationsActivity
import com.application.onovapplication.activities.common.StartPetition
import com.application.onovapplication.activities.politicians.CreateLawActivity
import com.application.onovapplication.databinding.DonateDialogBinding
import com.application.onovapplication.databinding.RvDebatesBinding
import com.application.onovapplication.fragments.CreatePostFragment
import com.application.onovapplication.model.FeedsData
import com.application.onovapplication.model.LikeDataClass
import com.application.onovapplication.prefs.PreferenceManager
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
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory


class ViewDebatesAdapter(
    val context: Context,
    val feedData: List<FeedsData>,
    val onClickItem: OnClickItem,
    val onClickshare: OnClickShare
) : RecyclerView.Adapter<ViewDebatesAdapter.RVHolder>() {
    // creating a variable for exoplayer
    var exoPlayer: SimpleExoPlayer? = null
    var likeDataClass: LikeDataClass? = null
    var videoPosition = 0
    val userPreferences: PreferenceManager by lazy {
        PreferenceManager(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding = RvDebatesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RVHolder(binding)
    }

    override fun getItemCount(): Int {
        return feedData.size
    }

    override fun onViewRecycled(holder: RVHolder) {
        super.onViewRecycled(holder)

        exoPlayer?.playWhenReady = false


    }

    fun releaseExoplayer() {
        Log.e("ViewDebate", "exoPlayer=" + exoPlayer)

    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
        holder.binding.ivDeletePost.setOnClickListener {
            showPopup(holder.binding.ivDeletePost, feedData[position], position)
        }
        holder.bind(feedData[position])
        //   when (position) {


//            0 -> {
//                holder.binding.feedTitle.text = "This is a petition Title"
//                holder.binding.feedType.text = "Feed Type : Petition"
//                holder.binding.feedDescription.text =
//                    "This is a petition description. You can tap on the image to see the details of the petition."
//            }
//
//
//            4 -> {
//                holder.binding.feedMedia.visibility = View.GONE
//            }
//
//            1 -> {
//                holder.binding.donateLayout.visibility = View.VISIBLE
//                holder.binding.feedTitle.text = "This is a donation Title"
//                holder.binding.feedType.text = "Feed Type : Donation"
//
//            }

        //  }
        if (feedData[position].isShared == "1") {
            holder.binding.sharedPost.visibility = View.VISIBLE
        } else holder.binding.sharedPost.visibility = View.GONE

//        if (feedData[0].profile == true) holder.binding.ivDeletePost.visibility = View.VISIBLE
//        else holder.binding.ivDeletePost.visibility = View.GONE

        holder.binding.likelyt.setOnClickListener {
            onClickshare.onclicklike(
                "1",
                feedData[position]
            )
        }
        holder.binding.cmntlyt.setOnClickListener { onClickshare.onclickCmnt(feedData[position]) }
        holder.binding.dislikeLyt.setOnClickListener {
            onClickshare.onclicklike(
                "0",
                feedData[position]
            )
        }
//        holder.binding.ivDeletePost.setOnClickListener {
//            onClickshare.onDeletePost(
//                position,
//                feedData[position]
//            )
//        }
        holder.binding.feedlyt.setOnClickListener {
            if (feedData[position].recordType == "petition") {
                val intent = Intent(context, PetitionDetailsActivity::class.java)
                intent.putExtra("feed", feedData[position])
                context.startActivity(intent)
            } else if (feedData[position].recordType == "polling") {
                holder.binding.ivDeletePost.visibility = View.GONE
                if (feedData[position].isPoll == "0") {
                    val intent = Intent(context, AddPollingResultActivity::class.java)
//                        intent.putExtra("type",data.fileType)
                    intent.putExtra("feed", feedData[position])
                    context.startActivity(intent)
                } else if (feedData[position].isPoll == "1") {
                    val intent = Intent(context, PollResultActivity::class.java)
//                        intent.putExtra("type",data.fileType)
                    intent.putExtra("feed", feedData[position])
                    context.startActivity(intent)
                }
            }

        }
        holder.binding.shareBtn.setOnClickListener {

            showVideoDialog(feedData[position])
        }

        holder.binding.donateLayout.setOnClickListener {

            openDonationsDialog(feedData[position])
        }
    }

    private fun showDonationDialog(amount: String, feeditem: FeedsData) {
        val pictureDialog: AlertDialog.Builder = AlertDialog.Builder(context)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf(
            "Pay with Stripe",
            "Pay with Google Pay"
        )
        pictureDialog.setItems(
            pictureDialogItems
        ) { _, which ->
            when (which) {
                0 -> {
                    if (amount == "")
                        Toast.makeText(context, "Enter an amount", Toast.LENGTH_SHORT).show()
                    else {
                        onClickItem.onclickdn(feeditem, amount, "stripe")
                    }
                }//chooseVideoFromGallery()
                1 -> {
                    if (amount == "")
                        Toast.makeText(context, "Enter an amount", Toast.LENGTH_SHORT).show()
                    else {
                        onClickItem.onclickdn(feeditem, amount, "gpay")
                    }
                }//takeVideoFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun showPopup(view: View, data: FeedsData, pos: Int) {
        val popup = PopupMenu(context, view)
        popup.inflate(R.menu.feed_menu)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.one -> {
//                    Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show()
                    if (data.recordType == "post") {
                        val bundle = Bundle()
                        bundle.putParcelable("feedData", data)
                        if (data.fileType == "photo") bundle.putString("type", "photo")
                        else if (data.fileType == "video") bundle.putString("type", "video")
                        val activity = view.context as AppCompatActivity
                        val fragment = CreatePostFragment()
                        fragment.arguments = bundle
                        val transaction: FragmentTransaction =
                            activity.supportFragmentManager.beginTransaction()
                        transaction.replace(
                            R.id.homeTabContainer,
                            fragment
                        ) // give your fragment container id in first parameter
                        transaction.addToBackStack(null).commit()
                    } else if (data.recordType == "donationRequest") {
                        val intent = Intent(context, AskDonationsActivity::class.java)
                        intent.putExtra("feed", data)
                        intent.putExtra("type", data.fileType)
                        context.startActivity(intent)
                    } else if (data.recordType == "law") {
                        val intent = Intent(context, CreateLawActivity::class.java)
                        intent.putExtra("feed", data)
                        context.startActivity(intent)
                    } else if (data.recordType == "petition") {
                        val intent = Intent(context, StartPetition::class.java)
                        intent.putExtra("type", data.fileType)
                        intent.putExtra("feed", data)
                        context.startActivity(intent)
                    }
                }
                R.id.two -> {
                    onClickshare.onDeletePost(data, pos)
                }

            }

            true
        })

        popup.show()
    }

    private fun showVideoDialog(data: FeedsData) {
        val pictureDialog: AlertDialog.Builder = AlertDialog.Builder(context)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf(
            "Share within app",
            "Share to other network"
        )
        pictureDialog.setItems(
            pictureDialogItems
        ) { _, which ->
            when (which) {
                0 -> shareToApp(data)
                1 -> shareToOtherNetworks()
            }
        }
        pictureDialog.show()
    }

    fun shareToApp(data: FeedsData) {
        onClickshare.onclickShr(data.userRef.toString(), data)
    }

    fun shareToOtherNetworks() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    inner class RVHolder(val binding: RvDebatesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(feedData: FeedsData) {

            if (feedData.Liked == "1") {
                binding.iconLike.setColorFilter(context.resources.getColor(R.color.red))
                feedData.like = true
                feedData.dislike = false
            } else if (feedData.Disliked == "1") {
                binding.iconDislike.setColorFilter(context.resources.getColor(R.color.red))
                feedData.like = false
                feedData.dislike = true
            }
            if (feedData.like == true) {
                binding.iconLike.setColorFilter(context.resources.getColor(R.color.red))
            } else {
                binding.iconLike.colorFilter = null
            }
            if (feedData.dislike == true) binding.iconDislike.setColorFilter(
                context.resources.getColor(R.color.red)
            )
            else binding.iconDislike.colorFilter = null

            Glide.with(context).load(BaseUrl.photoUrl + feedData.profilePic)
                .apply(RequestOptions().placeholder(R.drawable.ic_baseline_account_circle_24))
                .into(binding.ivChatProfile)
            binding.userName.text = feedData.Name
            binding.tvLikeCount.text = feedData.likeCount
            binding.tvDislikeCount.text = feedData.dislikeCount
            binding.tvCommentCount.text = feedData.commentCount
            binding.feedType.text = "Feed Type : " + feedData.recordType
            binding.feedTitle.text = feedData.title
            binding.feedDescription.text = feedData.description
            if (feedData.recordType == "petition") {
                if (feedData.petitionMedia.isNullOrEmpty()) {
                    binding.feedMedia.visibility = View.GONE
                } else {
                    binding.feedMedia.visibility = View.VISIBLE
                }

                if (feedData.petitionDiscription.isNullOrEmpty()) {
                    binding.feedDescription.visibility = View.GONE
                } else {
                    binding.feedDescription.visibility = View.VISIBLE
                    binding.feedDescription.text = feedData.petitionDiscription
                }
//                if (feedData.petitionTitle.isNullOrEmpty()) {
//                    binding.feedTitle.visibility = View.GONE
//                } else {
//                    binding.feedTitle.visibility = View.VISIBLE
                binding.feedTitle.text = feedData.petitionTitle

                //  }
            } else {
                if (feedData.filePath.isNullOrEmpty()) {
                    binding.feedMedia.visibility = View.GONE
                } else {
                    binding.feedMedia.visibility = View.VISIBLE

                }

                if (feedData.description.isNullOrEmpty()) {
                    binding.feedDescription.visibility = View.GONE
                } else {
                    binding.feedDescription.visibility = View.VISIBLE
                }
//                if (feedData.title.isNullOrEmpty()) {
//                    binding.feedTitle.visibility = View.GONE
//                } else {
//                    binding.feedTitle.visibility = View.VISIBLE
//                }
            }

            if (feedData.recordType == "polling") {
                binding.feedTitle.text = feedData.polTitle
            }
            when (feedData.recordType) {

                "donationRequest" -> {
                    if (feedData.userRef == userPreferences.getuserDetails()?.userRef) {
                        binding.donateLayout.visibility = View.GONE
                    } else {
                        binding.donateLayout.visibility = View.VISIBLE
                    }
                }

//                "petition"->{
//                    val intent = Intent(context, PetitionDetailsActivity::class.java)
//                    context.startActivity(intent)
//                }

                else -> {
                    binding.donateLayout.visibility = View.GONE
                }
            }

            when (feedData.fileType) {
                "document" -> {
                    binding.wbFeed.visibility = View.VISIBLE
                    binding.vvFeed.visibility = View.GONE
                    binding.idExoPlayerVIew.visibility = View.GONE

                    binding.ivFeed.visibility = View.GONE

                    binding.wbFeed.settings.javaScriptEnabled = true

                    binding.wbFeed.settings.javaScriptCanOpenWindowsAutomatically = true
                    binding.wbFeed.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
                    if (feedData.recordType == "petition") {
                        binding.wbFeed.loadUrl(
                            "https://docs.google.com/gview?embedded=true&url=".plus(
                                BaseUrl.photoUrl + feedData.petitionMedia
                            )
                        )
                    } else binding.wbFeed.loadUrl(
                        "https://docs.google.com/gview?embedded=true&url=".plus(
                            BaseUrl.photoUrl + feedData.filePath
                        )
                    )
                    //     Log.e("PRACHI","https://docs.google.com/gview?embedded=true&url=".plus(BaseUrl.photoUrl + feedData.filePath))

                }

                "video" -> {
                    videoPosition = adapterPosition
                    Log.e("ViewDebate", "posiition=" + videoPosition)
                    //here position
                    binding.wbFeed.visibility = View.GONE
                    //   binding.vvFeed.visibility = View.VISIBLE
                    binding.idExoPlayerVIew.visibility = View.VISIBLE
                    binding.ivFeed.visibility = View.GONE
                    if (feedData.recordType == "petition") {
                        exoplayer(BaseUrl.photoUrl + feedData.petitionMedia)
                    } else exoplayer(BaseUrl.photoUrl + feedData.filePath)
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

                    if (feedData.recordType == "petition") {
                        Glide.with(context).load(BaseUrl.photoUrl + feedData.petitionMedia)
                            .into(binding.ivFeed)
                    } else Glide.with(context).load(BaseUrl.photoUrl + feedData.filePath)
                        .into(binding.ivFeed)

                }
            }

        }


        fun exoplayer(videoURL: String) {
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
                binding.idExoPlayerVIew.player = exoPlayer
                // exoPlayerView.setPlayer(exoPlayer)

                // we are preparing our exoplayer
                // with media source.
                exoPlayer?.prepare(mediaSource)

                // we are setting our exoplayer
                // when it is ready.
                //   exoPlayer?.playWhenReady = true
                exoPlayer?.playWhenReady = false
            } catch (e: Exception) {
                // below line is used for
                // handling our errors.
                Log.e("TAG", "Error : $e")
            }
        }

    }

    private fun openDonationsDialog(feedData: FeedsData) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        val binding = DonateDialogBinding.inflate(dialog.layoutInflater)
        val view = binding.root
        dialog.setContentView(view)
        // dialog.setContentView(R.layout.donate_dialog)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        binding.submitDonation.setOnClickListener {
            showDonationDialog(binding.edDonationPrice.text.toString(), feedData)
            dialog.dismiss()
        }

        binding.cancelDonation.setOnClickListener {
            dialog.dismiss()

        }

    }

    interface OnClickItem {
        fun onclickdn(feeditem: FeedsData, amount: String, method: String)
    }

    interface OnClickShare {
        fun onclickShr(toRef: String, feeditem: FeedsData)
        fun onclickCmnt(feeditem: FeedsData)
        fun onclicklike(type: String, feeditem: FeedsData)
        fun onDeletePost(feeditem: FeedsData, position: Int)
    }

}

