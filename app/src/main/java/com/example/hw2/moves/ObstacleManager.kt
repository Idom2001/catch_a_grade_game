package com.example.hw2.moves
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import com.example.hw2.game_logic.Constants
import com.example.hw2.R
import com.example.hw2.game_logic.GameManager
import com.example.hw2.utilities.SingleSoundPlayer

class ObstacleManager(private var gameManager: GameManager) {



    private fun showCoin(): Boolean {
        return (Constants.RandomObstecals.MIN_RAND.. Constants.RandomObstecals.MAX_RAND)
            .random() < 5 // 5% chance for coin
    }

    private fun showObstacle(): Boolean {
        return (Constants.RandomObstecals.MIN_RAND.. Constants.RandomObstecals.MAX_RAND)
            .random() < 10 // 10% chance for obstacle
    }

     private fun randObstacles(obstaclesMatrix: Array<Array<AppCompatImageView>>,
                               coinsMatrix: Array<Array<AppCompatImageView>>){//randomize obstacles in the tallest line

        for (col in 0..Constants.ObstecalsIndexes.OBSTECALS_MAX_COL) {
            val obCell = obstaclesMatrix[0][col]
            val coinCell = coinsMatrix[0][col]
            if (showObstacle()) {
                gameManager.increaseDistance()
                obCell.setImageResource(R.drawable.score)
                obCell.visibility = View.VISIBLE
            } else {
                obCell.visibility = View.INVISIBLE
                obCell.setImageResource(0)
                if(showCoin())
                {
                    coinCell.setImageResource(R.drawable.diploma)
                    coinCell.visibility = View.VISIBLE
                }
                else{
                    coinCell.visibility = View.INVISIBLE
                    coinCell.setImageResource(0)
                }
            }
        }
    }

     fun placePlayer(mainIMGPlayerArr: Array<AppCompatImageView>,gameManager: GameManager){
        for (col in 0..Constants.ObstecalsIndexes.OBSTECALS_MAX_COL){
            val cell = mainIMGPlayerArr[col]
            if(col == gameManager.playerCol){//add player here
                cell.setImageResource(R.drawable.student)
                cell.visibility = View.VISIBLE
            }else {
                cell.visibility = View.INVISIBLE
                cell.setImageResource(0)//remove
            }
        }
    }

     fun moveObstaclesDown(
         obstaclesMatrix: Array<Array<AppCompatImageView>>,coinsMatrix: Array<Array<AppCompatImageView>>,
         gameManager: GameManager,
         context: Context) {

        for (row in Constants.ObstecalsIndexes.OBSTECALS_MAX_ROW downTo 1) {
            for (col in 0..Constants.ObstecalsIndexes.OBSTECALS_MAX_COL) {
                val fromAboveObs = obstaclesMatrix[row - 1][col]
                val currentObs = obstaclesMatrix[row][col]
                val fromAboveCoin = coinsMatrix[row - 1][col]
                val currentCoin = coinsMatrix[row][col]

                currentObs.setImageDrawable(fromAboveObs.drawable)
                currentObs.visibility = fromAboveObs.visibility
                fromAboveObs.visibility = View.INVISIBLE
                fromAboveObs.setImageResource(0)

                currentCoin.setImageDrawable(fromAboveCoin.drawable)
                currentCoin.visibility = fromAboveCoin.visibility
                fromAboveCoin.visibility = View.INVISIBLE
                fromAboveCoin.setImageResource(0)
            }
        }
        randObstacles(obstaclesMatrix,coinsMatrix)
        checkCollision(obstaclesMatrix,coinsMatrix,context)
    }
     private fun checkCollision(obstaclesMatrix: Array<Array<AppCompatImageView>>,
                                coinsMatrix: Array<Array<AppCompatImageView>>, context: Context){
        val obstacle = obstaclesMatrix[Constants.ObstecalsIndexes.OBSTECALS_MAX_ROW][gameManager.playerCol]
        val coin = coinsMatrix[Constants.ObstecalsIndexes.OBSTECALS_MAX_ROW][gameManager.playerCol]
        if(obstacle.isVisible) {//lost life
            gameManager.vibrate(context)
            val ssp = SingleSoundPlayer(context)
            ssp.playSound(R.raw.crash_sound)
            Toast.makeText(context, "boom, you lost life💥", Toast.LENGTH_SHORT).show()
            gameManager.lostLife()
        }
         else if(coin.isVisible) {//gain points
            val ssp = SingleSoundPlayer(context)
            ssp.playSound(R.raw.gain_sound)
            Toast.makeText(context, "you gain points 😁", Toast.LENGTH_SHORT).show()
            gameManager.gainPoints()
        }
    }
}