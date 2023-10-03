package com.example.platformer

import android.util.SparseArray

internal const val PLAYER = "left1"
internal const val NULLSPRITE = "nullasset"
internal const val NO_TILE = 0

abstract class LevelData {
    var tiles: Array<IntArray> = emptyArray()
    val tileToBitmap = SparseArray<String>()


    fun getRow(y: Int): IntArray{
        return tiles[y]
    }

    fun getTile(x: Int, y: Int): Int {
        return getRow(y)[x]
    }

    fun getSpriteName(tileType: Int): String{
        val filename = tileToBitmap[tileType]
        return filename ?: NULLSPRITE
    }

    fun getHeight(): Int{
        return tiles.size
    }

    fun getWidth(): Int{
        return getRow(0).size
    }

}