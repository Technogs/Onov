package com.application.onovapplication.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.FileProvider
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.databinding.ActivityPetitionDetailsBinding
import com.application.onovapplication.model.FeedsData
import com.application.onovapplication.model.PetitionArray
import com.application.onovapplication.repository.BaseUrl
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayerFactory
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
import java.io.File
import java.io.FileOutputStream

import java.lang.Exception

class PetitionDetailsActivity : BaseAppCompatActivity(), View.OnClickListener {
    var feed:FeedsData?=null
    var pid:String=""
    var past:String=""

   var petitions:PetitionArray?=null
    // creating a variable for exoplayer
    private lateinit var binding: ActivityPetitionDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetitionDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (intent.getParcelableExtra<FeedsData>("feed")!=null)
        feed=intent.getParcelableExtra<FeedsData>("feed") as FeedsData
        else {
            petitions = intent.getParcelableExtra<FeedsData>("petitions") as PetitionArray
            past = intent.getStringExtra("past").toString()


        }
        init()
    }

    fun init(){
   if (feed!=null)     {
       pid=feed?.petitionId.toString()
            binding.petitionTitleDetails.text = feed?.petitionTitle
            binding.senderNameDetails.text = feed?.Name
            binding.petitionDetails.text = feed?.petitionDiscription
            binding.websiteLink.text = feed?.petitionWebsiteLink
            binding.petitionCount.text = feed?.petitionSignCount
            binding.receiveSignCount.text = "Signatures Received: " + feed?.petitionReceiveSignCount
            if (feed?.fileType == "photo") {
                binding.ivFeed.visibility = View.VISIBLE
                binding.idExoPlayerVIew.visibility = View.GONE
                Glide.with(this).load(BaseUrl.photoUrl + feed?.petitionMedia).into(binding.ivFeed)
            } else if (feed?.fileType == "video") {
                binding.idExoPlayerVIew.visibility = View.VISIBLE
                binding.ivFeed.visibility = View.GONE
                exoplayer(BaseUrl.photoUrl + feed?.petitionMedia)
            }
       binding.sharept.setOnClickListener {
           val sendIntent: Intent = Intent().apply {
           action = Intent.ACTION_SEND
           putExtra(Intent.EXTRA_TEXT, ""+feed?.recordType+"\n"+feed?.title+"\n"+feed?.description+"\n"+feed?.fullName+"\n"+BaseUrl.photoUrl+feed?.filePath)
           type = "text/plain"}
           val shareIntent = Intent.createChooser(sendIntent, null)
       startActivity(shareIntent) }
        }else if (petitions!=null){
            pid=petitions?.id.toString()
       binding.petitionTitleDetails.text = petitions?.title
       binding.senderNameDetails.text = petitions?.sentFrom
       binding.petitionDetails.text = petitions?.discription
       binding.websiteLink.text = petitions?.websiteLink
       binding.petitionCount.text = petitions?.signCount
       binding.receiveSignCount.text = "Signatures Received: " + petitions?.getSignCount
       if (petitions?.mediaType == "photo") {
           binding.ivFeed.visibility = View.VISIBLE
           binding.idExoPlayerVIew.visibility = View.GONE
           Glide.with(this).load(BaseUrl.photoUrl + petitions?.petitionMedia).into(binding.ivFeed)
       } else if (petitions?.mediaType == "video") {
           binding.idExoPlayerVIew.visibility = View.VISIBLE
           binding.ivFeed.visibility = View.GONE
           exoplayer(BaseUrl.photoUrl + petitions?.petitionMedia)
       }
       binding.sharept.setOnClickListener {

           val bitmapDrawable = getDrawable(R.drawable.onov_logo) as BitmapDrawable
           val bitmap = bitmapDrawable.bitmap
           shareImageandText(bitmap)
       }
        }

    }

    private fun shareImageandText(bitmap: Bitmap) {
        val uri = getImageToShare(bitmap)
        val intent = Intent(Intent.ACTION_SEND)

        // putting uri of image to be shared
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        // adding text to share
        intent. putExtra(Intent.EXTRA_TEXT, ""+"Petitions"+"\n"+petitions?.title+"\n"+petitions?.discription+"\n"+petitions?.sentFrom+"\n"+BaseUrl.photoUrl+petitions?.petitionMedia)


            // Add subject Here
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")

        // setting type to image
        intent.type = "*/*"

        // calling startactivity() to share
     startActivity(Intent.createChooser(intent, "Share Via"))
    }

    // Retrieving the url to share
    private fun getImageToShare(bitmap: Bitmap): Uri? {
        val imagefolder = File(getCacheDir(), "images")
        var uri: Uri? = null
        try {
            imagefolder.mkdirs()
            val file = File(imagefolder, "shared_image.png")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
            outputStream.flush()
            outputStream.close()
            uri = FileProvider.getUriForFile(this, "com.application.onovapplication.provider", file)
        } catch (e: java.lang.Exception) {
//            Toast.makeText(this, "" + e.message, Toast.LENGTH_LONG).show()
        }
        return uri
    }
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.signPetition -> {
                val intent = Intent(this, SignPetitionActivity::class.java)
                intent.putExtra("pId",pid)
                startActivity(intent)
            } R.id.ViewReceiveSign -> {
                val intent = Intent(this, ViewSignActivity::class.java)
                intent.putExtra("pId",pid)
                startActivity(intent)
            }R.id.sharept -> {

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