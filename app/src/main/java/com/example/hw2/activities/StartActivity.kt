package com.example.hw2.activities
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.hw2.R
import com.example.hw2.game_logic.Constants
import com.google.android.material.button.MaterialButton
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.textfield.TextInputEditText

class StartActivity: AppCompatActivity() {
    private lateinit var startPlay: MaterialButton
    private lateinit var startRecordTable: MaterialButton
    private lateinit var startSpeedSwitch: MaterialSwitch
    private lateinit var startModeSwitch: MaterialSwitch
    private lateinit var nameInput: TextInputEditText
    private var speed: Long = 0
    private var isTilt: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_start)
        findViews()
        startPlay.setOnClickListener {playGame()}
        startRecordTable.setOnClickListener {recordTable()}
        startSpeedSwitch.setOnClickListener{speedSwitch()}
        startModeSwitch.setOnClickListener{modeSwitch()}
    }

    private fun speedSwitch() {
        speed = if(startSpeedSwitch.isChecked)
            Constants.Speed.FAST_SPEED//Fast
        else
            Constants.Speed.SLOW_SPEED//Slow
    }

    private fun modeSwitch() {
        isTilt = startModeSwitch.isChecked
    }

    private fun recordTable() {
        val intent = Intent(this, RecordTableActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun findViews() {
        nameInput = findViewById(R.id.game_over_name_input)
        startPlay = findViewById(R.id.start_play)
        startRecordTable = findViewById(R.id.start_recordTable)
        startSpeedSwitch = findViewById(R.id.start_speed_switch)
        startModeSwitch = findViewById(R.id.start_mode_switch)
    }

    private fun playGame() {
        speedSwitch()
        modeSwitch()
        val playerName = nameInput.text.toString()
        val namePrefs = getSharedPreferences("game_data", Context.MODE_PRIVATE)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("speed", speed)
        intent.putExtra("tilt?", isTilt)
        if (playerName.isNotBlank())
            namePrefs.edit().putString("player_name",playerName).apply()
        startActivity(intent)
        finish()
    }
}