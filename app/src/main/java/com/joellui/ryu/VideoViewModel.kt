package com.joellui.ryu

import android.webkit.WebView
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource

import android.app.Application
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.annotation.NonNull
import androidx.lifecycle.*
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.joellui.ryu.model.EpisodeDocument

class VideoViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver,
    Player.Listener {

    private val _player = MutableLiveData<Player?>()
    private val _error = MutableLiveData<String>()
    private val _currentMediaItem = MutableLiveData<MediaItem>()
    private var _playlist: List<EpisodeDocument> = listOf()
    private var _lastPlayedBtn: Button? = null

    val player: LiveData<Player?> get() = _player
    val error: LiveData<String> get() = _error
    val currentMediaItem: LiveData<MediaItem> get() = _currentMediaItem

    private var _currentPart = -1
    private var contentPosition = 0L
    private var playWhenReady = true

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        Log.d("exo", "Error $error ${error.errorCode}")

        this._error.value = "${error.cause!!.message} ${error.message}"
    }

    fun setPlaylist(playlist: List<EpisodeDocument>, partNumber: Int) {
        this._currentPart = partNumber
        this._playlist = playlist
        setUpPlayer()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForegrounded() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackgrounded() {
        releaseExoPlayer()
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            Player.STATE_IDLE -> {
                Log.d("VideoViewModel", "onPlayerStateChanged: STATE_IDLE")
            }
            Player.STATE_BUFFERING -> {
                Log.d("VideoViewModel", "onPlayerStateChanged: STATE_BUFFERING")

                Log.d("VideoViewModel", _player.value?.currentMediaItem.toString())

            }
            Player.STATE_READY -> {
                Log.d("VideoViewModel", "onPlayerStateChanged: STATE_READY")
                Log.d("VideoViewModel", _player.value?.currentMediaItem!!.mediaMetadata.trackNumber.toString())
                this._currentMediaItem.value = _player.value?.currentMediaItem
            }
            Player.STATE_ENDED -> {
                Log.d("VideoViewModel", "onPlayerStateChanged: STATE_ENDED")
            }
        }
    }

    fun setEpisode(stage: View, view: Button, position: Int) {
        _player.value!!.seekTo(position, 0L)

// this is to color the button that is selected
        if (_lastPlayedBtn == null) {
            _lastPlayedBtn = stage.findViewWithTag("episode_btn_1")
        }

        _lastPlayedBtn!!.setTextColor(Color.WHITE)
        _lastPlayedBtn = view

        view.setTextColor(Color.RED)
    }

    private fun setUpPlayer() {
        val application = getApplication<Application>()
        val trackSelector = DefaultTrackSelector(application).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }
        val player = SimpleExoPlayer.Builder(application)
            .setTrackSelector(trackSelector)
            .build()
            .also { exoPlayer ->
                //val demoVideo = "https://api.aniapi.com/v1/proxy/https%3a%2f%2fgogoplay1.com%2fstreaming.php%3fid%3dMzUxOA%3d%3d%26title%3dOne%2bPiece%2bEpisode%2b1/gogoanime/"
                //_playlist.add(demoVideo)

                //Log.v("MSS", "DEMO -> $demoVideo")
                //Log.v("MSS", "Playing -> ${_playlist}")
                //Log.v("MSS", "Video is same as demo ? -> ${demoVideo.equals(_playlist[0])}")
                for (e in _playlist) {
                    var metadata = MediaMetadata.Builder()
                        .setTitle(e.title)
                        .setTrackNumber(e.number)
                        .setDiscNumber(_currentPart)
                        .build()

                    var mediaItem = MediaItem.Builder()
                        .setUri(
                            Uri.parse(e.video)
                        )
                        .setMimeType(MimeTypes.APPLICATION_MP4)
                        .setMediaId(e.id.toString())
                        .setMediaMetadata(metadata)
                        .build()

                    val dataSourceFactory = DefaultHttpDataSource.Factory()
                    val userAgent = WebView(application).settings.userAgentString
                    dataSourceFactory.setUserAgent(userAgent)
                    val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(mediaItem)

                    exoPlayer.addMediaSource(source)
                }

                exoPlayer.addListener(this)
                exoPlayer.prepare()
            }

        player.playWhenReady = playWhenReady
        player.seekTo(contentPosition)

        this._player.value = player
    }

    private fun releaseExoPlayer() {
        val player = _player.value ?: return
        this._player.value = null

        contentPosition = player.contentPosition
        playWhenReady = player.playWhenReady
        player.release()
    }

    override fun onCleared() {
        super.onCleared()
        releaseExoPlayer()
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }
}