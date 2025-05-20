package com.example.hw2.game_logic
import android.content.Context
import com.example.hw2.utilities.Score
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ScoreManager(private val context: Context)  {
    private val prefs = context.getSharedPreferences("game_data", Context.MODE_PRIVATE)
    fun saveScores(scores: List<Score>) {
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(scores)
        editor.putString("scores_list", json)
        editor.apply()
    }
    fun loadScores(): List<Score> {
        val json = prefs.getString("scores_list", null)
        return if (json != null) {
            val type = object : TypeToken<List<Score>>() {}.type
            Gson().fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun tryAddNewScore(newScore: Score) {
        val gson = Gson()
        val scoresJson = prefs.getString("scores_list", null)
        val scoreList: MutableList<Score> = if (scoresJson != null) {
            val type = object : TypeToken<MutableList<Score>>() {}.type
            gson.fromJson(scoresJson, type)
        } else {
            mutableListOf()
        }
        scoreList.add(newScore)
        val sorted = scoreList.sortedByDescending { it.score }
            .take(Constants.ScoreMax.MAX_SCORES)

        //save
        val updatedJson = gson.toJson(sorted)
        prefs.edit().putString("scores_list", updatedJson).apply()
    }


    fun clearScores() {
        prefs.edit().clear().apply()
    }

}