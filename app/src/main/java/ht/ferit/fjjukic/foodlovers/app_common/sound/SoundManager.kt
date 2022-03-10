package ht.ferit.fjjukic.foodlovers.app_common.sound

import android.content.Context
import android.media.SoundPool

class SoundManager {
    private val soundPool: SoundPool by lazy { SoundPool.Builder().setMaxStreams(10).build() }
    private val soundMap: HashMap<Int, Int> by lazy { HashMap() }
    private var isLoaded: Boolean = false

    init {
        setListener()
    }

    private fun setListener() {
        soundPool.setOnLoadCompleteListener { _, _, _ -> isLoaded = true }
    }

    fun load(context: Context, rawId: Int, priority: Int) {
        soundMap[rawId] = soundPool.load(context, rawId, priority)
    }

    fun play(sound: Int) {
        val soundID: Int = soundMap[sound] ?: 0
        soundPool.play(soundID, 1f, 1f, 1, 0, 1f)
    }
}