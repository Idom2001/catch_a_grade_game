package com.example.hw2.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hw2.R
import com.example.hw2.game_logic.ScoreManager
import com.example.hw2.utilities.Score
import com.google.android.gms.location.LocationServices
import com.google.android.material.button.MaterialButton

class GameOverActivity : AppCompatActivity() {
    private lateinit var scoreManager: ScoreManager
    private lateinit var startNewGame: MaterialButton
    private lateinit var locationPermissionRequest: ActivityResultLauncher<String>
    private var score:Int = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_over)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        startNewGame = findViewById(R.id.over_startGame)
        startNewGame.setOnClickListener {mainMenu()}
        getLocation()
    }

    private fun mainMenu() {
        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun getLocation() {
        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                getLocationAndSaveScore()
            } else {
                //cant save location
                exitAndSave(0.0, 0.0)
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            getLocationAndSaveScore()
        }
    }


    private fun getLocationAndSaveScore() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            //access permission
            locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                exitAndSave(location.latitude,location.longitude)
            } else {
                //cant save location
                exitAndSave(0.0, 0.0)
            }
        }
    }


    private fun exitAndSave(latitude: Double, longitude: Double) {
        scoreManager = ScoreManager(this)
        score = intent.getIntExtra("score", 0)
        val prefs = getSharedPreferences("game_data", MODE_PRIVATE)
        val name = prefs.getString("player_name", "Unknown").toString()
        scoreManager.tryAddNewScore(Score(name,score,latitude, longitude))
    }
}