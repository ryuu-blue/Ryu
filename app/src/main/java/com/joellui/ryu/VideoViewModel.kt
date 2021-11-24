package com.joellui.ryu

import android.webkit.WebView
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.*
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util

class VideoViewModel(application: Application)
    : AndroidViewModel(application)
    , LifecycleObserver, Player.Listener {

    private val _player = MutableLiveData<Player?>()
    private val _error = MutableLiveData<String>()
    private var _playlist = mutableListOf<String>()

    val player: LiveData<Player?> get() = _player
    val error: LiveData<String> get() = _error
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

    fun setPlaylistIfNotSet(playlist: MutableList<String>) {
        if (_playlist.size == 0) {
            setPlaylist(playlist)
        }
    }

    fun setPlaylist(playlist: MutableList<String>) {
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

    fun setEpisode(position: Int) {
        _player.value!!.seekTo(position, 0L)
    }

    fun setUpPlayer() {

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
                for (video in _playlist) {
                    var mediaItem = MediaItem.Builder()
                        .setUri(
                            Uri.parse(video)
                        )
                        .setMimeType(MimeTypes.APPLICATION_M3U8)
                        .build()
                    val dataSourceFactory = DefaultHttpDataSource.Factory()
                    val userAgent = WebView(application).settings.userAgentString
                    dataSourceFactory.setUserAgent(userAgent)
                    val source = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
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