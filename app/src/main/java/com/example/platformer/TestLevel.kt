package com.example.platformer

class TestLevel: LevelData() {

    init {
        tileToBitmap.put(NO_TILE, "no_title")
        tileToBitmap.put(1, PLAYER)
        tileToBitmap.put(2, "yellow_uproundleft")
        tileToBitmap.put(3, "yellow_square")
        tileToBitmap.put(4, "yellow_uproundright")

        tiles = arrayOf(
            intArrayOf(2, 3, 3, 3, 3, 3, 4),
            intArrayOf(2, 0, 0, 0, 0, 0, 4),
            intArrayOf(2, 0, 0, 0, 0, 0, 4),
            intArrayOf(2, 0, 0, 0, 1, 0, 4),
            intArrayOf(2, 0, 0, 0, 0, 0, 4),
            intArrayOf(2, 3, 3, 3, 3, 3, 4),
            intArrayOf(2, 3, 3, 3, 3, 3, 4),
            intArrayOf(2, 0, 0, 0, 0, 0, 4),
            intArrayOf(2, 0, 0, 0, 0, 0, 4),
            intArrayOf(2, 0, 0, 0, 0, 0, 4),
            intArrayOf(2, 0, 0, 0, 0, 0, 4),
            intArrayOf(2, 3, 3, 3, 3, 3, 4),

        )

    }


}