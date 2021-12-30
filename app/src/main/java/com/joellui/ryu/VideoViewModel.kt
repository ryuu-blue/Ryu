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
    private val _currentEpisode = MutableLiveData<Int>()
    private var _playlist: List<EpisodeDocument> = listOf()

    val player: LiveData<Player?> get() = _player
    val error: LiveData<String> get() = _error
    val currentEpisode: LiveData<Int> get() = _currentEpisode

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

    override fun onTracksChanged(
        trackGroups: TrackGroupArray,
        trackSelections: TrackSelectionArray
    ) {
        val currentIndex = _player.value!!.currentMediaItem!!.mediaMetadata.trackNumber
        this._currentEpisode.value = currentIndex
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