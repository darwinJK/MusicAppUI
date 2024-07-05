package com.example.musicappui

import androidx.annotation.DrawableRes

data class Lib(@DrawableRes val icon : Int, val name : String)
    val libraries = listOf<Lib>(
        Lib(R.drawable.baseline_playlist_play_24,"PlayList"),
        Lib(R.drawable.baseline_mic_24,"Artists"),
        Lib(R.drawable.baseline_album_24,"Albums"),
        Lib(R.drawable.baseline_music_note_24,"Songs"),
        Lib(R.drawable.baseline_queue_music_24,"Genre")

    )

