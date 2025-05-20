package com.example.hw2.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.hw2.R
import com.example.hw2.game_logic.Constants
import com.example.hw2.game_logic.ScoreManager
import com.example.hw2.utilities.Score
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class RecordTableActivity: AppCompatActivity(), OnMapReadyCallback {
    private lateinit var scoreManager: ScoreManager
    private lateinit var recordBackButton: MaterialButton
    private lateinit var scores: List<Score>
    private lateinit var recordScoresText: Array<MaterialTextView>
    private lateinit var recordPlayerText: Array<MaterialTextView>
    private lateinit var recordPlaceText: Array<MaterialTextView>
    private lateinit var recordMap: GoogleMap
    private var currentMarker: Marker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.record_table)
        scores = listOf()
        scoreManager = ScoreManager(this)
        findViews()
        initScoresList()
        recordBackButton.setOnClickListener { goBackToMenu() }
    }

    private fun initScoresList() {
        scores = scoreManager.loadScores()
        showScoresOnScreen()
    }

    private fun showScoresOnScreen() {
        for (i in scores.indices) {
            recordScoresText[i].text = scores[i].score.toString()
            recordPlayerText[i].text = scores[i].name
            recordPlaceText[i].text = (i + 1).toString()

            recordPlayerText[i].setOnClickListener {
                if (::recordMap.isInitialized) {
                    val latLng = LatLng(scores[i].latitude, scores[i].longitude)
                    if (scores[i].latitude != 0.0 || scores[i].longitude != 0.0) {
                        recordMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 25f))
                        currentMarker = recordMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title("${scores[i].name} - ${scores[i].score} points")
                        )
                        currentMarker?.showInfoWindow()

                    } else {
                        Toast.makeText(
                            this,
                            "The player didn't save location",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun findViews() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        recordBackButton = findViewById(R.id.record_back)
        recordScoresText = Array(Constants.ScoreMax.MAX_SCORES) { index ->
            val resId = resources.getIdentifier(
                "table_${index + 1}_place_points", "id", packageName
            )
            findViewById<MaterialTextView>(resId)
        }
        recordPlayerText = Array(Constants.ScoreMax.MAX_SCORES) { index ->
            val resId = resources.getIdentifier(
                "table_${index + 1}_place_name", "id", packageName
            )
            findViewById<MaterialTextView>(resId)
        }
        recordPlaceText = Array(Constants.ScoreMax.MAX_SCORES) { index ->
            val resId = resources.getIdentifier(
                "table_${index + 1}_place", "id", packageName
            )
            findViewById<MaterialTextView>(resId)
        }
    }


    private fun goBackToMenu() {
        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onMapReady(readyMap: GoogleMap) {
        recordMap = readyMap
        if (scores.isEmpty()) return

        for (score in scores) {

            if (score.latitude != 0.0 || score.longitude != 0.0) {
                val latLng = LatLng(score.latitude, score.longitude)
                currentMarker=recordMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title("${score.name} - ${score.score} points")
                )
            }
        }
    }
}