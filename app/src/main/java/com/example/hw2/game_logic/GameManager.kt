package com.example.hw2.game_logic
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class GameManager {
    var currentLostLife: Int = 0
        private set

    var playerCol: Int = 1 //player starting place
        private set

    private var isGameRunning: Boolean = false

    var points: Int = 0
        private set

    var distance: Int = 0
        private set

    fun gameRunning() {
        isGameRunning = true
    }

    fun gameNotRunning() {
        isGameRunning = false
    }

    fun moveLeft(): Boolean {
        if (isGameRunning && playerCol > 0) {
            playerCol--
            return true
        }
        return false
    }

    fun moveRight(): Boolean {
        if (isGameRunning && playerCol < Constants.ObstecalsIndexes.OBSTECALS_MAX_COL) {
            playerCol++
            return true
        }
        return false
    }

    fun lostLife() {
        currentLostLife++
    }

    fun gainPoints() {
        points+=5
    }

    fun increaseDistance() {
        distance++
    }

    @SuppressLint("ObsoleteSdkInt")
    @Suppress("DEPRECATION")
    fun vibrate(context: Context) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    500,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            vibrator.vibrate(500)
        }
    }
}