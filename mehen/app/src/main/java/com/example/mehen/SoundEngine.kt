package com.example.mehen

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build

class SoundEngine {
    private var soundPool: SoundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_GAME)
            .build()
        SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()
    } else {
        SoundPool(1, AudioManager.STREAM_MUSIC, 0)
    }

    fun load(context: Context, rawId: Int, priority: Int):Int {
        return soundPool.load(context, rawId, priority)
    }

    fun play(soundID: Int, leftVolume: Float, rightVolume: Float, priority: Int, loop: Int, rate: Float) {
        soundPool.play(soundID, leftVolume, rightVolume, priority, loop, rate)
    }
}