package com.application.onovapplication.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.databinding.ActivityEventsBinding
import com.application.onovapplication.databinding.ActivityFeedDetailBinding
import com.application.onovapplication.model.EventData
import com.application.onovapplication.model.FeedsData
import com.application.onovapplication.model.LikeDataClass
import com.application.onovapplication.repository.BaseUrl
import com.application.onovapplication.viewModels.HomeViewModel
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

import java.lang.Exception


class FeedDetailActivity : BaseAppCompatActivity(),View.OnClickListener {
var feedData: FeedsData? =null
var likeDataClass: LikeDataClass? =null
var eventData: EventData? =null
    private val homeViewModel by lazy { ViewModelProvider(this).get(HomeViewModel::class.java)}
    private lateinit var binding: ActivityFeedDetailBinding

    //  lateinit var eventData: EventData
    var exoPlayer: SimpleExoPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        feedData=intent.getParcelableExtra<FeedsData>("feed") as FeedsData?
        eventData=intent.getParcelableExtra<EventData>("event") as EventData?
        init()

        observeViewModel()
    }

    fun init(){
        if (feedData!=null){
            binding.userName.text = feedData?.Name
            binding.tvLikeCount.text = feedData?.likeCount
            binding.tvDislikeCount.text = feedData?.dislikeCount
            binding.tvCommentC0unt.text = feedData?.commentCount
            binding.feedTitle.text = feedData?.title
            binding.feedType.text = "Feed type: "+feedData?.recordType
            binding.feedDescription.text = feedData?.description
        Glide.with(this).load(BaseUrl.photoUrl + feedData?.profilePic).apply(
            RequestOptions().placeholder(R.drawable.ic_baseline_account_circle_24)).into(binding.ivChatProfile)
        if (feedData?.recordType=="petition"){
            binding.feedTitle.text = feedData?.petitionTitle
            binding.feedDescription.text =feedData?.petitionDiscription
            binding.uprRlyt.setOnClickListener {
                val intent = Intent(this@FeedDetailActivity, PetitionDetailsActivity::class.java)
                intent.putExtra("feed",feedData)
              startActivity(intent)
            }
        }
        when (feedData?.fileType) {
            "document" -> {
                binding.wbFeed.visibility = View.VISIBLE
                binding.vvFeed.visibility = View.GONE
                binding.idExoPlayerVIew.visibility = View.GONE
                binding.ivFeed.visibility = View.GONE
                binding.wbFeed.settings.javaScriptEnabled = true
                binding.wbFeed.settings.javaScriptCanOpenWindowsAutomatically = true
                binding.wbFeed.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
                binding.wbFeed.loadUrl("https://docs.google.com/gview?embedded=true&url=".plus(
                        BaseUrl.photoUrl + feedData?.filePath)
                )
                if (feedData?.recordType=="petition"){
                    binding.wbFeed.loadUrl("https://docs.google.com/gview?embedded=true&url=".plus(
                        BaseUrl.photoUrl + feedData?.petitionMedia)
                    )
                }

            }

            "video" -> {
                binding.wbFeed.visibility = View.GONE
                binding.idExoPlayerVIew.visibility = View.VISIBLE
                binding.ivFeed.visibility = View.GONE
                exoplayer(BaseUrl.photoUrl + feedData?.filePath)
                if (feedData?.recordType=="petition"){
                    exoplayer(BaseUrl.photoUrl + feedData?.petitionMedia)
                }

            }

            "photo" -> {
                binding.wbFeed.visibility = View.GONE
                binding.vvFeed.visibility = View.GONE
                binding.idExoPlayerVIew.visibility = View.GONE
                binding.ivFeed.visibility = View.VISIBLE
                Glide.with(this).load(BaseUrl.photoUrl + feedData?.filePath)
                    .into(binding.ivFeed)
                if (feedData?.recordType=="petition"){
                    Glide.with(this).load(BaseUrl.photoUrl + feedData?.petitionMedia)
                        .into(binding.ivFeed)
                }

            }
        }}
        else{
            binding.userName.visibility = View.GONE
            binding.feedType.visibility = View.GONE
            binding.ivChatProfile.visibility = View.GONE
            binding.feedTitle.text = eventData?.title
            binding.feedDescription.text = eventData?.description
            Glide.with(this).load(BaseUrl.photoUrl + eventData?.cover_image)
                .into(binding.ivFeed)
        }
    }
    private fun observeViewModel() {
        homeViewModel.successfulLike.observe(this, Observer { successful ->
            dismissDialog()
            if (successful != null) {
                if (successful) {
                    if (homeViewModel.status == "success") {
                        if (feedData?.Liked=="1"){
                            binding.iconLike.setColorFilter(getResources().getColor(R.color.red))
                        }
                     else   if (feedData?.Disliked=="1"){
                            binding.iconDislike.setColorFilter(getResources().getColor(R.color.red))
                     }


if (homeViewModel.message=="Disliked successfully"){
likeDataClass?.dislike=true

    binding.iconDislike.setColorFilter(getResources().getColor(R.color.red))
    binding.iconLike.setColorFilter(null)

    if ((binding.tvLikeCount.text).toString().toInt()>0)
        binding.tvLikeCount.text = ((binding.tvLikeCount.text).toString().toInt()-1).toString()
    binding.tvDislikeCount.text = ((feedData?.dislikeCount)!!.toInt()+1).toString()
}else if (homeViewModel.message=="Liked successfully"){
likeDataClass?.like=true
    binding.iconLike.setColorFilter(getResources().getColor(R.color.red))
    binding.iconDislike.setColorFilter(null)
    binding.tvLikeCount.text = ((feedData?.likeCount)!!.toInt()+1).toString()
    if ((binding.tvDislikeCount.text).toString().toInt()>0)
        binding.tvDislikeCount.text = ((binding.tvDislikeCount.text).toString().toInt()-1).toString()

}
                        setError(homeViewModel.message)


                    } else {
                       setError(homeViewModel.message)
                    }

                } else {
                  setError(homeViewModel.message)
                }
            }
        })

    }
    fun exoplayer(videoURL:String){
        try {

            // bandwidthMeter is used for
            // getting default bandwidth
            val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()

            // track selector is used to navigate between
            // video using a default seekbar.
            val trackSelector: TrackSelector =
                DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))

            // we are adding our track selector to exoplayer.
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector)

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
            binding.idExoPlayerVIew.setPlayer(exoPlayer)
            // exoPlayerView.setPlayer(exoPlayer)

            // we are preparing our exoplayer
            // with media source.
            exoPlayer?.prepare(mediaSource)

            // we are setting our exoplayer
            // when it is ready.
            exoPlayer?.setPlayWhenReady(true)
        } catch (e: Exception) {
            // below line is used for
            // handling our errors.
            Log.e("TAG", "Error : $e")
        }
    }

    override fun onClick(v: View?) {
       when(v?.id){
           R.id.likelyt->{
               showDialog()
               if (feedData!=null){
                   if (feedData?.recordType=="post")
                       homeViewModel.likeFeed(this,userPreferences.getuserDetails()?.userRef.toString(),
                           feedData?.recordId.toString(),"post","1")
                   else if (feedData?.recordType=="petition")
                       homeViewModel.likeFeed(this,userPreferences.getuserDetails()?.userRef.toString(),
                           feedData?.recordId.toString(),"petition","1")
                   else if (feedData?.recordType=="donationRequest")
                       homeViewModel.likeFeed(this,userPreferences.getuserDetails()?.userRef.toString(),
                           feedData?.recordId.toString(),"law","1")
                   else if (feedData?.recordType=="donationRequest")
                       homeViewModel.likeFeed(this,userPreferences.getuserDetails()?.userRef.toString(),
                           feedData?.recordId.toString(),"law","1")
               }else{
                   homeViewModel.likeFeed(this,userPreferences.getuserDetails()?.userRef.toString(),
                       eventData?.id.toString(),"event","1")
               }

           }R.id.dislikeLyt->{
           showDialog()
           if (feedData!=null){
               if (feedData?.recordType=="post")
                   homeViewModel.likeFeed(this,userPreferences.getuserDetails()?.userRef.toString(),
                       feedData?.recordId.toString(),"post","0")
               else if (feedData?.recordType=="petition")
                   homeViewModel.likeFeed(this,userPreferences.getuserDetails()?.userRef.toString(),
                       feedData?.recordId.toString(),"petition","0")
               else if (feedData?.recordType=="donationRequest")
                   homeViewModel.likeFeed(this,userPreferences.getuserDetails()?.userRef.toString(),
                       feedData?.recordId.toString(),"donationRequest","0")
               else if (feedData?.recordType=="law")
                   homeViewModel.likeFeed(this,userPreferences.getuserDetails()?.userRef.toString(),
                       feedData?.recordId.toString(),"law","0")
           }else{
               homeViewModel.likeFeed(this,userPreferences.getuserDetails()?.userRef.toString(),
                   eventData?.id.toString(),"event","0")
           }
           }R.id.cmntlyt->{
           if (feedData!=null){
               val intent= Intent(this, CommentActivity::class.java)
               intent.putExtra("feeds",feedData)
               startActivity(intent)
           }else{
               val intent= Intent(this, CommentActivity::class.java)
               intent.putExtra("event",eventData)
               startActivity(intent)
           }
           }R.id.share_btn->{
           if (feedData!=null){
               val intent= Intent(this, UsersActivity::class.java)
               intent.putExtra("feeds",feedData)
               startActivity(intent)
           }else{val intent= Intent(this, UsersActivity::class.java)
               intent.putExtra("event",eventData)
               startActivity(intent)}
           }
       }
    }
}