package com.example.hw2.game_logic
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.example.hw2.activities.GameOverActivity
import com.example.hw2.moves.ObstacleManager
import com.google.android.material.textview.MaterialTextView

class GameTimer(private var activity: Activity,private var speed: Long,private var mainDistanceText: MaterialTextView,
                private var mainPointsText: MaterialTextView) {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var gameRunnable: Runnable
    var canMove: Boolean = true
        private set

    fun gameLoop(gameManager: GameManager, obstacleManager:
    ObstacleManager, context: Context, obstaclesMatrix: Array<Array<AppCompatImageView>>
                 , mainIMGHearts: Array<AppCompatImageView>,coinsMatrix: Array<Array<AppCompatImageView>>) {

        gameRunnable = object : Runnable {
            override fun run() {
                obstacleManager.moveObstaclesDown(obstaclesMatrix,coinsMatrix,gameManager,context)
                refreshUI(gameManager,mainIMGHearts)
                handler.postDelayed(this,speed)
            }
        }
        handler.post(gameRunnable)
        gameManager.gameRunning()
    }
    fun preventSpam() {
        canMove = false
        handler.postDelayed({
            canMove = true
        }, Constants.Speed.SPAM_DELAY)
    }

    private fun refreshUI(gameManager: GameManager,mainIMGHearts: Array<AppCompatImageView>) {
        mainPointsText.text = gameManager.points.toString()
        mainDistanceText.text = gameManager.distance.toString()
        if(gameManager.currentLostLife== Constants.Life.MAX_LIFE){
            val intent = Intent(activity, GameOverActivity::class.java)
            intent.putExtra("score", mainPointsText.text.toString().toInt())
            activity.startActivity(intent)
            activity.finish()
        }
        else if(gameManager.currentLostLife!= Constants.Life.END_LIFE){
            mainIMGHearts[mainIMGHearts.size-gameManager.currentLostLife].visibility= View.INVISIBLE
        }
    }

    fun destroy() {
        handler.removeCallbacks(gameRunnable)
    }

    fun pause() {
        handler.removeCallbacks(gameRunnable)
    }

    fun resume() {
        handler.post(gameRunnable)
    }
}