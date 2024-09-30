package za.co.varsitycollege.st10204902.purrsonaltrainer.frontend_logic

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

/**
 * Manages the playback of sound effects.
 *
 * @constructor Creates a SoundManager with the specified context and sound resource.
 * @param context The context in which the SoundManager is used.
 * @param soundResource The resource ID of the sound to be played.
 */
class SoundManager(context: Context, soundResource: Int) {
    private var soundPool: SoundPool? = null
    private var soundId: Int = 0

    init {
        // Set up audio attributes for the sound pool
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        // Initialize the sound pool with the specified audio attributes
        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()

        // Load the sound resource into the sound pool
        soundId = soundPool!!.load(context, soundResource, 1)
    }

    /**
     * Plays the loaded sound.
     */
    fun playSound() {
        soundPool?.play(soundId, 1f, 1f, 1, 0, 1f)
    }

    /**
     * Releases the resources associated with the SoundManager.
     */
    fun release() {
        soundPool?.release()
        soundPool = null
    }
}