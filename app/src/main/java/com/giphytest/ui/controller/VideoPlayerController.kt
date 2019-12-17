package com.giphytest.ui.controller

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import com.giphytest.App
import com.giphytest.R
import com.giphytest.bean.GiphyImageInfo
import com.giphytest.ui.base.ActionBarProvider
import com.giphytest.ui.base.BaseController
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlaybackControlView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import io.objectbox.Box


class VideoPlayerController(args: Bundle) : BaseController(args), SurfaceHolder.Callback {

    lateinit var mSurfaceView: SurfaceView
    lateinit var txtUpVote: TextView
    lateinit var txtDownVote: TextView
    lateinit var btnShare: LinearLayout
    lateinit var mAspectRatioLayout: AspectRatioFrameLayout
    lateinit var mPlaybackControlView: PlaybackControlView
    lateinit var btnUpVote: LinearLayout
    lateinit var btnDownVote: LinearLayout

    lateinit private var mPlayer: SimpleExoPlayer
    lateinit private var giphyImageInfoBox: Box<GiphyImageInfo>
    lateinit var giphyImageInfo: GiphyImageInfo

    init {
        setHasOptionsMenu(true)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        (activity as ActionBarProvider).supportActionBar().setDisplayHomeAsUpEnabled(true)
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.player_controller, container, false)
    }

    override fun onViewBound(view: View) {
        super.onViewBound(view)
        mSurfaceView = view.findViewById(R.id.surface_view) as SurfaceView
        txtUpVote = view.findViewById(R.id.txtUpVote) as TextView
        txtDownVote = view.findViewById(R.id.txtDownVote) as TextView
        btnShare = view.findViewById(R.id.btnShare) as LinearLayout
        mAspectRatioLayout = view.findViewById(R.id.aspect_ratio_layout) as AspectRatioFrameLayout
        mPlaybackControlView = view.findViewById(R.id.player_view) as PlaybackControlView
        btnUpVote = view.findViewById(R.id.btnUpVote) as LinearLayout
        btnDownVote = view.findViewById(R.id.btnDownVote) as LinearLayout

        giphyImageInfoBox = (applicationContext as App).boxStore.boxFor(GiphyImageInfo::class.java)
        giphyImageInfo = args.getSerializable(ARG) as GiphyImageInfo
        stetUpSurface()

        btnUpVote.setOnClickListener {
            if (giphyImageInfo.totalUpVote != null) {
                giphyImageInfo.totalUpVote = "" + (Integer.parseInt(giphyImageInfo.totalUpVote) + 1)
                txtUpVote.text = giphyImageInfo.totalUpVote
                giphyImageInfoBox.put(giphyImageInfo)
            }
        }

        btnDownVote.setOnClickListener {
            if (giphyImageInfo.totalDownVote != null) {
                giphyImageInfo.totalDownVote = "" + (Integer.parseInt(giphyImageInfo.totalDownVote) + 1)
                txtDownVote.text = giphyImageInfo.totalDownVote
                giphyImageInfoBox.put(giphyImageInfo)
            }
        }
        btnShare.setOnClickListener {
            val share = Intent(android.content.Intent.ACTION_SEND)
            share.type = "text/plain"
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
            share.putExtra(Intent.EXTRA_SUBJECT, "Giphy Video")
            share.putExtra(Intent.EXTRA_TEXT, giphyImageInfo.videoUrl)

            startActivity(Intent.createChooser(share, "Share link!"))
        }
    }


    override val title: String?
        get() = "Player"

    private fun stetUpSurface() {
        txtUpVote.text = giphyImageInfo.totalUpVote
        txtDownVote.text = giphyImageInfo.totalDownVote
        mSurfaceView.holder.addCallback(this)
        val handler = Handler()
        val extractor = DefaultExtractorsFactory()
        val dataSourceFactory = DefaultHttpDataSourceFactory("ExoPlayer Demo")
        mPlayer = ExoPlayerFactory.newSimpleInstance(
                activity!!,
                DefaultTrackSelector(handler),
                DefaultLoadControl()
        )
        mPlaybackControlView.requestFocus()
        mPlaybackControlView.setPlayer(mPlayer)
        // initialize source
        val videoSource = ExtractorMediaSource(
                Uri.parse(giphyImageInfo.videoUrl),
                dataSourceFactory,
                extractor, null, null
        )
        mPlayer.prepare(videoSource)
        mPlayer.playWhenReady = true

    }

    // SurfaceHolder.Callback

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        if (mPlayer != null) {
            mPlayer.setVideoSurfaceHolder(surfaceHolder)
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            router.popController(this)
        }

        return super.onOptionsItemSelected(item)

    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        if (mPlayer != null) {
            mPlayer.setVideoSurfaceHolder(null)
        }
    }

    override fun onActivityPaused(activity: Activity) {
        super.onActivityPaused(activity)
        mPlayer.playWhenReady = false
    }

    override fun onActivityStopped(activity: Activity) {
        super.onActivityStopped(activity)
        if (mPlayer != null) {
            mPlayer.release()
            mPlayer = null!!
        }
    }

    companion object {
        var ARG = "GiphyImageInfo"
    }
}
