package com.application.onovapplication.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.MediaController
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.databinding.ActivityPeopleBinding
import com.application.onovapplication.databinding.ActivityPetitionDetailsBinding
import com.application.onovapplication.model.FeedsData
import com.application.onovapplication.repository.BaseUrl
import com.bumptech.glide.Glide
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

class PetitionDetailsActivity : BaseAppCompatActivity(), View.OnClickListener {
    lateinit var feed:FeedsData
    // creating a variable for exoplayer
    private lateinit var binding: ActivityPetitionDetailsBinding

    var exoPlayer: SimpleExoPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetitionDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        feed=intent.getParcelableExtra<FeedsData>("feed") as FeedsData
     //   if (feed.recordType=="petition"){signPetition.visibility=View.VISIBLE}
        init()
    }

    fun init(){
       binding.petitionTitleDetails.text=feed.petitionTitle
        binding.senderNameDetails.text=feed.Name
        binding.petitionDetails.text=feed.petitionDiscription
        binding.websiteLink.text=feed.petitionWebsiteLink
        binding.petitionCount.text=feed.petitionSignCount
        binding.receiveSignCount.text="Signatures Received: "+feed.petitionReceiveSignCount
        if (feed.fileType=="photo"){
            binding.ivFeed.visibility=View.VISIBLE
            binding.idExoPlayerVIew.visibility=View.GONE
        Glide.with(this).load(BaseUrl.photoUrl +feed.petitionMedia).into( binding.ivFeed)}
        else   if (feed.fileType=="video"){
            binding.idExoPlayerVIew.visibility=View.VISIBLE
            binding.ivFeed.visibility=View.GONE
            exoplayer(BaseUrl.photoUrl + feed.petitionMedia)

//            val mc = MediaController(this)
//            vvFeed.setMediaController(mc)
//           vvFeed.setVideoPath(BaseUrl.photoUrl + feed.filePath)
           }}

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.signPetition -> {
                val intent = Intent(this, SignPetitionActivity::class.java)
                intent.putExtra("pId",feed.petitionId)
                startActivity(intent)
            }
        }
    }


    fun exoplayer(videoURL:String){
        try {

            // bandwisthmeter is used for
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
}