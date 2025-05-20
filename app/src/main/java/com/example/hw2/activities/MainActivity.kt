package com.example.hw2.activities
import android.annotation.SuppressLint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.example.hw2.R
import com.google.android.material.button.MaterialButton
import com.example.hw2.game_logic.Constants
import com.example.hw2.game_logic.GameManager
import com.example.hw2.game_logic.GameTimer
import com.example.hw2.moves.ObstacleManager
import com.google.android.material.textview.MaterialTextView

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var mainIMGPlayerArr: Array<AppCompatImageView>
    private lateinit var mainIMGHearts: Array<AppCompatImageView>
    private lateinit var mainFABLeft: MaterialButton
    private lateinit var mainFABRight: MaterialButton
    private lateinit var mainDistanceText: MaterialTextView
    private lateinit var mainPointsText: MaterialTextView
    private lateinit var obstaclesMatrix: Array<Array<AppCompatImageView>>
    private lateinit var coinsMatrix: Array<Array<AppCompatImageView>>
    private lateinit var gameManager: GameManager
    private lateinit var obstacleManager: ObstacleManager
    private lateinit var gameTimer: GameTimer
    private lateinit var sensorManager: SensorManager
    private var speed:Long = Constants.Speed.SLOW_SPEED
    private var isTilt: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        speed = intent.getLongExtra("speed", Constants.Speed.SLOW_SPEED)
        isTilt = intent.getBooleanExtra("tilt?", false)
        findViews()
        gameManager = GameManager()
        obstacleManager = ObstacleManager(gameManager)
        gameTimer = GameTimer(this,speed,mainDistanceText, mainPointsText)
        initViews()
        gameTimer.gameLoop(gameManager,obstacleManager
            ,this,obstaclesMatrix
            ,mainIMGHearts,coinsMatrix)
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (!isTilt || event?.sensor?.type != Sensor.TYPE_ACCELEROMETER || !gameTimer.canMove) return

        val x = event.values[0]
        val threshold = Constants.Speed.THRESHOLD

        if (x > threshold) {
            clickLeft()
            gameTimer.preventSpam()
        } else if (x < -threshold) {
            clickRight()
            gameTimer.preventSpam()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    private fun initViews() {
        mainFABLeft.setOnClickListener {clickLeft()}
        mainFABRight.setOnClickListener {clickRight()}
        obstacleManager.placePlayer(mainIMGPlayerArr,gameManager)
    }
    private fun clickLeft() {
        if (gameManager.moveLeft())
            obstacleManager.placePlayer(mainIMGPlayerArr,gameManager)
    }

    private fun clickRight() {
        if(gameManager.moveRight())
            obstacleManager.placePlayer(mainIMGPlayerArr,gameManager)
    }

    @SuppressLint("DiscouragedApi")
    private fun findViews() {
        mainFABLeft = findViewById(R.id.main_FAB_left)
        mainFABRight = findViewById(R.id.main_FAB_right)
        mainDistanceText = findViewById(R.id.main_distance_text)
        mainPointsText = findViewById(R.id.main_points_text)
        if (isTilt) {
            sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
            val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            accelerometer?.also {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
            }
            mainFABLeft.visibility= View.INVISIBLE
            mainFABRight.visibility= View.INVISIBLE
        }
        mainIMGPlayerArr = arrayOf(
            findViewById(R.id.main_IMG_player0),
            findViewById(R.id.main_IMG_player1),
            findViewById(R.id.main_IMG_player2),
            findViewById(R.id.main_IMG_player3),
            findViewById(R.id.main_IMG_player4)
        )
        obstaclesMatrix = Array(Constants.ObstecalsIndexes.OBSTECALS_MAT_ROWS) { row ->
            Array(Constants.ObstecalsIndexes.OBSTECALS_MAT_COLS) { col ->
                val resId = resources.getIdentifier("main_IMG_score_${row}.${col}",
                    "id", packageName)
                findViewById(resId)
            }
        }
        coinsMatrix = Array(Constants.ObstecalsIndexes.OBSTECALS_MAT_ROWS) { row ->
            Array(Constants.ObstecalsIndexes.OBSTECALS_MAT_COLS) { col ->
                val resId = resources.getIdentifier("main_IMG_diploma_${row}.${col}",
                    "id", packageName)
                findViewById(resId)
            }
        }
        mainIMGHearts = arrayOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )
    }

    override fun onPause() {
        super.onPause()
        gameTimer.pause()
        gameManager.gameNotRunning()
        if (isTilt) {
            sensorManager.unregisterListener(this)
        }
    }

    override fun onResume() {
        super.onResume()
        gameTimer.resume()
        gameManager.gameRunning()
        if (isTilt) {
            val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            accelerometer?.also {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        gameTimer.destroy()
        if (isTilt) {
            sensorManager.unregisterListener(this)
        }
    }
}