package com.example.platformer

import android.util.Log
import androidx.core.math.MathUtils.clamp
import kotlin.math.log

private const val MAX_DELTA = 0.48f
const val GRAVITY = 40f

open class DynamicEntity(sprite: String, x: Float, y: Float): StaticEntity(sprite, x, y) {
    private val TAG: String = "Game"
    var velX = 0f
    var velY = 0f
    var isOnGround = false

    override fun update(dt: Float) {
        if (!isOnGround){
            velY += GRAVITY * dt
        }
        x += clamp(velX * dt, -MAX_DELTA, MAX_DELTA)
        y += clamp(velY * dt, -MAX_DELTA, MAX_DELTA)
        if (top() > engine.worldHeight()){
            setBottom(0f)
        }
        isOnGround = false
    }

    override fun onCollision(that: Entity) {
        getOverlap(this, that, overlap)
        x += overlap.x
        y += overlap.y
        if (overlap.y != 0f){
            velY = 0.0f
                if(overlap.y < 0f){
                    isOnGround = true
                }
        }
    }
}