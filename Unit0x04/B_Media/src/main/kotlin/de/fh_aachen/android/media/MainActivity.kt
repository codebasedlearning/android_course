// (C) 2025 A.Voß, a.voss@fh-aachen.de, info@codebasedlearning.dev

package de.fh_aachen.android.media

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import de.fh_aachen.android.media.R.drawable.icon_home
import de.fh_aachen.android.media.R.drawable.icon_movie
import de.fh_aachen.android.media.R.drawable.background_castle
import de.fh_aachen.android.media.R.drawable.background_beats
import de.fh_aachen.android.media.ui.theme.FirstAppTheme
import de.fh_aachen.android.media.ui_lib.LocalNavController
import de.fh_aachen.android.media.ui_lib.NavScreen
import de.fh_aachen.android.media.ui_lib.NavScaffold
import de.fh_aachen.android.media.ui_lib.navScreensOf

enum class Screen { Home, Video }

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstAppTheme {
                NavScaffold(
                    navScreensOf(
                        Screen.Home to NavScreen(icon_home, background_castle) { LoginScreen() },
                        Screen.Video to NavScreen(icon_movie, background_beats) { VideoScreen() },
                    )
                )
            }
        }
    }
}

@Composable
fun LoginScreen() {
    val navController = LocalNavController.current
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = { navController.navigate(Screen.Video.name) }) {
            Text("Media - Login", fontSize = 24.sp, modifier = Modifier.padding(8.dp))
        }
    }
}

// extension property :-)
val ExoPlayer.isAtEnd
    get() = currentPosition==0L || (currentPosition >= duration && duration > 0)

@Composable
fun VideoScreen() {
    val context = LocalContext.current
    // val videoUrl = Uri.Builder().scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
    //    .path(R.raw.sample_5s.toString()).build()
    // or: val videoUri = Uri.parse("android.resource://${context.packageName}/${R.raw.sample_5s}")
    val videoUri = "android.resource://${context.packageName}/${R.raw.bvb_2024_avoss}"
    // or: val videoUrl = "https://www.example.com/sample-video.mp4"
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
            // playWhenReady = true     // starts immediately
        }
    }
    val isPlaying = remember { mutableStateOf(false) }
    val isAtEnd = remember { mutableStateOf(false) }

    /*
     * Because Compose UI functions are NOT lifecycle owners. They can recompose at any time.
     * If you register a listener inside a composable without DisposableEffect, it will:
     *    - register the listener multiple times
     *    - never remove previous listeners
     *    - leak your ExoPlayer or cause duplicated events
     *
     * DisposableEffect may be used to initialize or subscribe to a key and reinitialize
     * when a different key is provided, performing cleanup for the old operation before
     * initializing the new.
     * It runs a block once when the key enters the composition, and executes its
     * onDispose { ... } block when the key leaves the composition.
     *
     * If the key (object identity) changes, that side-effect is considered out-of-date
     * => dispose it + recreate it.
     */
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlayingState: Boolean) {
                isPlaying.value = isPlayingState
                isAtEnd.value = exoPlayer.isAtEnd
            }
            override fun onPlaybackStateChanged(playbackState: Int) {
                isAtEnd.value = exoPlayer.isAtEnd
            }
        }
        exoPlayer.addListener(listener)

        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    //  AndroidView is commonly needed for using Views that are infeasible to be reimplemented
    //  in Compose and there is no corresponding Compose API.
    Column(modifier = Modifier.fillMaxSize()) {
        Text(if (isPlaying.value) "Playing" else "Paused", color = Color.White)
        Text(if (isAtEnd.value) "Start or End ${exoPlayer.contentPosition}" else "In between", color = Color.White)
        AndroidView(
            factory = { PlayerView(context).apply { player = exoPlayer } },
        )
        Row {
            if (isPlaying.value) {
                Button(onClick = { exoPlayer.pause() }) { Text("Pause") }
            } else if (isAtEnd.value){
                Button(onClick = { exoPlayer.seekTo(0); exoPlayer.play() }) { Text("Play") }
            } else {
                Button(onClick = { exoPlayer.play() }) { Text("Continue") }
                Button(onClick = { exoPlayer.seekTo(0); exoPlayer.play() }) { Text("Restart") }
            }
        }
    }
}
