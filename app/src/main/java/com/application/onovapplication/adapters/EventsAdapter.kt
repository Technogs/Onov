
package com.application.onovapplication.adapters

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.*
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.activities.EventDetailsActivity
import com.application.onovapplication.databinding.RvEventsBinding
import com.application.onovapplication.model.EventData
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
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory

class EventsAdapter  (val context: Context,val events:List<EventData>, val onEventClick: OnEventClick) : RecyclerView.Adapter<EventsAdapter.RVHolder>() {
    var exoPlayer: SimpleExoPlayer? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding = RvEventsBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)

        return RVHolder(binding)    }


    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
holder.bind(events[position])
        holder.binding.eventLyt.setOnClickListener {
            val intent = Intent(context , EventDetailsActivity::class.java)
           intent.putExtra("data",events[position])
            context.startActivity(intent)


        }

        holder.binding.eventCmntLyt.setOnClickListener { onEventClick.OnEventCommentClick(events[position]) }
        holder.binding.eventDislikeLyt.setOnClickListener {
            onEventClick.OnEventLikeClick("0",events[position]) }
        holder.binding.eventLikeLyt.setOnClickListener {
            onEventClick.OnEventLikeClick("1",events[position])  }
        holder.binding.eventShareLyt.setOnClickListener {onEventClick.OnEventShareClick(events[position])  }
    }


    inner class RVHolder(val binding: RvEventsBinding) : RecyclerView.ViewHolder(binding.root) {
fun bind(dataItem:EventData?){
    binding.eventTitle.text=dataItem?.title
    binding.tvEventLikeCount.text=dataItem?.likeCount
    binding.tvEventDisLikeCount.text=dataItem?.dislikeCount
    binding.tvEventCommentCount.text=dataItem?.CommentCount
    binding.eventTitle.text=dataItem?.title
    binding.eventTitle.text=dataItem?.title
    binding.eventPic.setOnClickListener { showDialog(dataItem!!) }
    if (dataItem?.cover_image!="") {
        binding.eventPic.visibility=View.VISIBLE
        binding.idExoPlayerVIew.visibility=View.GONE
        Glide.with(context).load(BaseUrl.photoUrl + dataItem?.cover_image)
            .apply(RequestOptions().placeholder(R.drawable.ic_baseline_account_circle_24))
            .into(binding.eventPic)
    } else if (dataItem.ent_video!="")  {
        binding.eventPic.visibility=View.GONE
        binding.idExoPlayerVIew.visibility=View.VISIBLE
        exoplayer(BaseUrl.photoUrl + dataItem.ent_video)
    }
    if (dataItem?.Liked == "1") {
        binding.likeEvt.setColorFilter(context.resources.getColor(R.color.red))
        dataItem.like = true
        dataItem.dislike = false
    } else if (dataItem?.Disliked == "1") {
        binding.dislikeevt.setColorFilter(context.resources.getColor(R.color.red))
        dataItem?.like = false
        dataItem?.dislike = true
    }
    if (dataItem?.like == true) {
        binding.likeEvt.setColorFilter(context.resources.getColor(R.color.red))
    } else {
        binding.likeEvt.colorFilter = null
    }
    if (dataItem?.dislike == true) binding.dislikeevt.setColorFilter(
        context.resources.getColor(R.color.red)
    )
    else binding.dislikeevt.colorFilter = null

}
        private fun showDialog(mdData: EventData) {

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
            cross.setOnClickListener { dialog.dismiss() }
//            when (mdData.fileType) {
//                "document" -> {
//                    wbFeed.visibility = View.VISIBLE
//                    vvFeed.visibility = View.GONE
//                    idExoPlayerVIew.visibility = View.GONE
//
//                    ivFeed.visibility = View.GONE
//                    wbFeed.settings.javaScriptEnabled = true
//
//                    wbFeed.settings.javaScriptCanOpenWindowsAutomatically = true
//                    wbFeed.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
//                    wbFeed.loadUrl(
//                        "https://docs.google.com/gview?embedded=true&url=".plus(
//                            BaseUrl.photoUrl + mdData.filePath
//                        )
//                    )
//                    //     Log.e("PRACHI","https://docs.google.com/gview?embedded=true&url=".plus(BaseUrl.photoUrl + feedData.filePath))
//
//                }
//
//                "video" -> {
////                videoPosition = adapterPosition
////                Log.e("ViewDebate", "posiition=" + videoPosition)
//                    //here position
//                    wbFeed.visibility = View.GONE
//                    //   binding.vvFeed.visibility = View.VISIBLE
//                    idExoPlayerVIew.visibility = View.VISIBLE
//                    ivFeed.visibility = View.GONE
//
//                    exoplayer(BaseUrl.photoUrl + mdData.filePath)
//                }
//
//                "photo" -> {
                    wbFeed.visibility = View.GONE
                    vvFeed.visibility = View.GONE
                    idExoPlayerVIew.visibility = View.GONE
                    ivFeed.visibility = View.VISIBLE
                    Glide.with(context).load(BaseUrl.photoUrl + mdData.cover_image).into(ivFeed)

//                }
//            }
            dialog.setOnKeyListener(DialogInterface.OnKeyListener { dialog, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog?.cancel()
                    return@OnKeyListener true

                }
                false
            })
            dialog.show()

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


   interface OnEventClick{
       fun OnEventLikeClick(type:String,dataItem: EventData?)
       fun OnEventShareClick(dataItem: EventData?)
       fun OnEventCommentClick(dataItem: EventData?)
   }
}

