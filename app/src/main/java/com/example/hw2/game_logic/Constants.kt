package com.example.hw2.game_logic

class Constants {

    object Life {
        const val END_LIFE: Int = 0
        const val MAX_LIFE: Int = 3
    }
    object Speed {
        const val SLOW_SPEED: Long = 670L
        const val FAST_SPEED: Long = 550L
        const val THRESHOLD: Float = 1.5f
        const val SPAM_DELAY: Long = 400
    }

    object ObstecalsIndexes {
        const val OBSTECALS_MAX_ROW: Int = 4
        const val OBSTECALS_MAX_COL: Int = 4
        const val OBSTECALS_MAT_ROWS: Int = 5
        const val OBSTECALS_MAT_COLS: Int = 5

    } object RandomObstecals {
        const val MIN_RAND: Int = 0
        const val MAX_RAND: Int = 150
    }
    object ScoreMax {
        const val MAX_SCORES: Int = 10

    }
}