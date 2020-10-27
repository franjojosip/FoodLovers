package ht.ferit.fjjukic.foodlovers.data

import android.content.Context
import android.media.SoundPool

class SoundManager {
    private var soundPool: SoundPool = SoundPool.Builder().setMaxStreams(10).build()
    private var soundMap: HashMap<Int, Int> = HashMap()
    private var isLoaded: Boolean = false

    init {
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